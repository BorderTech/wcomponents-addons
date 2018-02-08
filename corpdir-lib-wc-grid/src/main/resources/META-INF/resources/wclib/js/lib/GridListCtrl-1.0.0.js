define(['wclib/js/lib/gridlist-0.4.1-wc'], function (GridList) {

	/**
	 * Creates a new
	 * @param {Element} containerElement
	 * @param {Object} opts Configuration object.
	 * @constructor
	 */
	function GridListCtrl(containerElement, opts) {

		containerElement.style.position = 'relative';

		var DEFAULT_GRID_OPTIONS = {
			lanes: 6,
			selector: '.grid-item',
			resize: true,
			drag: true,
			direction: 'vertical',
			init: true,
			dragOptions: null,
			resizeOptions: null
		};
		var DEFAULT_DRAG_OPTIONS = { restrict: { } };
		var DEFAULT_RESIZE_OPTIONS = { restrict: { }, edges: { left: true, right: true, bottom: true, top: false } };

		var _gridList;
		var _cellHeightPx, _cellWidthPx;
		var _itemsSnapshot;
		var _actionDetails = null;
		var _containerId = containerElement.id;
		var _options = _appendObject(DEFAULT_GRID_OPTIONS, opts);
		_options.dragOptions = _appendObject(DEFAULT_DRAG_OPTIONS, _options.dragOptions);
		_options.resizeOptions = _appendObject(DEFAULT_RESIZE_OPTIONS, _options.resizeOptions);

		this.getItems = function () {
			return _gridList.items;
		};

		this.getItemElements = function () {
			var elements = [];
			var items = _gridList.items;
			if (items) {
				items.forEach(function (item) {
					var element = _getItemElement(item);
					elements.push(element);
				});
			}
			return elements;
		};

		this.isResize = function () {
			return _options.resize;
		};

		this.isDrag = function () {
			return _options.drag;
		};

		this.getResizeOptions = function () {
			return _options.resizeOptions;
		};

		this.getDragOptions = function () {
			return _options.dragOptions;
		};

		this.refresh = function () {
			this.loadGrid();
		};

		this.loadGrid = function () {
			var items = _getGridItemsFromDOM(_getContainerElement(), _options.selector);
			_gridList = new GridList(items, {
				lanes: _options.lanes,
				direction: _options.direction
			});
			_gridList.resizeGrid(_options.lanes);
			this.reflow();
		};

		this.resize = function (lanes) {
			if (lanes) {
				_options.lanes = lanes;
			}
			_createGridSnapshot();
			_resizeGrid();
			_updateGridSnapshot();
			this.reflow();
		};

		this.reflow = function () {
			_calculateCellSize();
			this.render();
		};

		/**
		 * Resize an item.
		 *
		 * @param {Object} size
		 * @param {Number} [size.w]
		 * @param {Number} [size.h}
		 */
		this.resizeItem = function (element, size) {
			var item = _getElementDetails(element);
			if (item) {
				_createGridSnapshot();
				_gridList.resizeItem(item, size);
				_updateGridSnapshot();
			}
			this.render();
		};

		this.appendItem = function (element, position) {
			var item = _getElementDetails(element);
			if (position) {
				_appendObject(item, position);
			}
			_gridList.items.push(item);
			this.resize();
		};

		this.removeItem = function (element) {
			var idx, item = _getItemByElement(element);
			if (item) {
				idx = _gridList.items.indexOf(item);
				_gridList.items.splice(idx);
				this.resize();
			}
		};

		this.layout = function () {
			this.render();
		};

		this.render = function () {
			_applySizeToItems();
			_applyPositionToItems();
		};

		this.dragStart = function (element) {
			_setupPlaceHolder(element);
			element.classList.add('dragging');
		};
		this.dragMove = function (dx, dy) {
			_handleDragMove(dx, dy);
		};
		this.dragEnd = function () {
			_clearPlaceHolder();
		};
		this.resizeStart = function (element) {
			_setupPlaceHolder(element);
			element.classList.add('resizing');
		};
		this.resizeMove = function (rect, edges) {
			_handleResizeMove(rect, edges);
		};
		this.resizeEnd = function () {
			_clearPlaceHolder();
		};

		function _setupPlaceHolder(element) {
			var item = _getItemByElement(element);
			var pos = { x: item.x, y: item.y };
			var size = { w: item.w, h: item.h };
			var itemRect = element.getBoundingClientRect();
			var gridRect = _getContainerElement().getBoundingClientRect();
			_createGridSnapshot();
			_actionDetails = {
				element: element,
				item: { h: item.h, w: item.w, id: item.id },
				prevSize: size,
				prevPosition: pos,
				itemRect: { left: itemRect.left,
					top: itemRect.top,
					width: itemRect.width },
				gridRect: { left: gridRect.left,
					top: gridRect.top,
					right: gridRect.left + (_cellHeightPx * _options.lanes) }
			};
			_highlightPositionForItem(item);
		}

		function _clearPlaceHolder() {
			var target = _actionDetails.element;
			target.classList.remove('dragging');
			target.classList.remove('resizing');
			_removePositionHighlight();
			_actionDetails = null;
			_updateGridSnapshot();
			_applySizeToItems();
			_applyPositionToItems();
		}

		/**
		 * @param {type} dx the distance mouse has moved in x direction
		 * @param {type} dy the distance the mouse has moved in y direction
		 * @returns {undefined}
		 */
		function _handleDragMove(dx, dy) {
			var item;

			var gridRect = _actionDetails.gridRect;
			var itemRect = _actionDetails.itemRect;

			// Check side walls
			var leftPos = itemRect.left + dx;
			if (leftPos < gridRect.left) {
				leftPos = gridRect.left;
			} else if (leftPos + itemRect.width > gridRect.right) {
				leftPos = gridRect.right - itemRect.width;
			}
			// Check top wall
			var topPos = itemRect.top + dy;
			if (topPos < gridRect.top) {
				topPos = gridRect.top;
			}
			var offsetLeft = leftPos - gridRect.left;
			var offsetTop = topPos - gridRect.top;

			// Move the grid item (relative to the grid)
			var target = _actionDetails.element;
			target.style.left = offsetLeft + 'px';
			target.style.top = offsetTop + 'px';

			// Check if changed position in the grid
			var newPosition = _snapPlaceHolderToGridPosition(offsetLeft, offsetTop);

			if (_dragPositionChanged(newPosition)) {
				_regenerateGridSnapshot();
				// Since the items list is a deep copy, we need to fetch the item
				// corresponding to this drag action again
				item = _getItemByElement(_actionDetails.element);
				console.log("Move to:" + newPosition);
				_gridList.moveItemToPosition(item, [newPosition.x, newPosition.y]);
				_resizeGrid();
				_applyPositionToItems();
			}
		}

		function _handleResizeMove(rect, edges) {
			var changedPosition, changedSize, item;
			var target = _actionDetails.element;
			var gridRect = _actionDetails.gridRect;

			var leftPos = rect.left;
			var topPos = rect.top;
			var width = rect.width;
			var height = rect.height;
			var overhang;
			var newPosition, newSize;

			// Check sides
			if (edges.left && leftPos < gridRect.left) {
				overhang = gridRect.left - leftPos;
				leftPos = gridRect.left;
				width = width - overhang;
			}
			if (edges.right && (leftPos + width > gridRect.right)) {
				width = gridRect.right - leftPos;
			}

			// Check top
			if (edges.top && topPos < gridRect.top) {
				topPos = gridRect.top;
			}

			var offsetLeft = leftPos - gridRect.left;
			var offsetTop = topPos - gridRect.top;

			// translate the element
			if (edges.left) {
				target.style.right = gridRect.right - (offsetLeft + width) + 'px';
				target.style.left = '';
			} else {
				target.style.left = offsetLeft + 'px';
				target.style.right = '';
			}
			target.style.top = offsetTop + 'px';
			target.style.width = width + "px";
			target.style.height = height + "px";

			// Get new size
			var newSize = _snapPlaceHolderToCellSize(width, rect.height);

			// If dragging left and changed size then will have to change position
			if (edges.left) {
				var diff = newSize.w - _actionDetails.prevSize.w;
				if (diff !== 0) {
					changedPosition = true;
					newPosition = { x: _actionDetails.prevPosition.x - diff, y: _actionDetails.prevPosition.y };
					_actionDetails.prevPosition = newPosition;
				}
			}

			changedSize = _dragSizeChanged(newSize);
			// Check size (snap to grid)
			if (changedPosition || changedSize) {
				_regenerateGridSnapshot();
				// Since the items list is a deep copy, we need to fetch the item
				// corresponding to this drag action again
				item = _getItemByElement(_actionDetails.element);
				// If chaning position, should only be to the left
				if (changedPosition) {
					console.log("Move to:" + newPosition);
					_gridList.moveItemToPosition(item, [newPosition.x, newPosition.y]);
					item = _getItemByElement(_actionDetails.element);
				}
				if (changedSize) {
					console.log("Size to:" + newSize);
					_gridList.resizeItem(item, newSize);
					_resizeGrid();
				}
				_applyPositionToItems();
				_applySizeToItems();
			}
		}

		function _calculateCellSize() {
			var rect = _getContainerElement().getBoundingClientRect();
			if (_isGrowHorizontal()) {
				_cellHeightPx = Math.floor(rect.height / _options.lanes);
				_cellWidthPx = _cellHeightPx;
			} else {
				_cellWidthPx = Math.floor(rect.width / _options.lanes);
				_cellHeightPx = _cellWidthPx;
			}
		}

		function _applySizeToItems() {
			var items = _gridList.items;
			items.forEach(function (item) {
				var element;
				if (_actionDetails && item.id === _actionDetails.item.id) {
					element = _placeholder;
				} else {
					element = _getItemElement(item);
				}
				element.style.position = 'absolute';
				element.style.width = _getItemWidth(item) + "px";
				element.style.height = _getItemHeight(item) + "px";
			});
		}

		function _applyPositionToItems() {
			var items = _gridList.items;
			items.forEach(function (item) {
				var element;
				if (_actionDetails && item.id === _actionDetails.item.id) {
					element = _placeholder;
				} else {
					element = _getItemElement(item);
				}
				element.style.left = item.x * _cellWidthPx + "px";
				element.style.top = item.y * _cellHeightPx + "px";
			});
		}


		function _getItemWidth(item) {
			return item.w * _cellWidthPx;
		}

		function _getItemHeight(item) {
			return item.h * _cellHeightPx;
		}

		function _getItemElement(item) {
			var element = document.getElementById(item.id);
			return element;
		}

		function _getItemByElement(element) {
			var i, next;
			var items = _gridList.items;
			for (i = 0; i < items.length; ++i) {
				next = items[i];
				if (next.id === element.id) {
					return next;
				}
			}
			return null;
		}

		function _getGridItemsFromDOM(container, selector) {
			var gridItems = [], gridItem;
			var elements = container.querySelectorAll(selector);

			elements.forEach(function (element) {
				gridItem = _getElementDetails(element);
				gridItems.push(gridItem);
			});

			return gridItems;
		}

		function _getElementDetails(element) {
			// X
			var gridItem = { id: element.id };
			var val = element.getAttribute('data-x');
			if (val) {
				gridItem.x = Number(val);
			}
			// Y
			val = element.getAttribute('data-y');
			if (val) {
				gridItem.y = Number(val);
			}
			// W
			val = element.getAttribute('data-w');
			if (!val) {
				val = "1";
			}
			gridItem.w = Number(val);
			// H
			val = element.getAttribute('data-h');
			if (!val) {
				val = "1";
			}
			gridItem.h = Number(val);
			return gridItem;
		}

		function _triggerOnChange() {
			if (typeof (_options.onChange) !== 'function') {
				return;
			}
			_options.onChange.call(_gridList.getChangedItems(_itemsSnapshot, 'element'));
		}

		function _createGridSnapshot() {
			_itemsSnapshot = GridList.cloneItems(_gridList.items);
		}

		function _updateGridSnapshot() {
			_triggerOnChange();
			GridList.cloneItems(_gridList.items, _itemsSnapshot);
		}

		function _regenerateGridSnapshot() {
			// Regenerate the grid with the positions from when the drag started
			GridList.cloneItems(_itemsSnapshot, _gridList.items);
			_gridList.generateGrid();
		}

		function _dragPositionChanged(newPosition) {
			if (newPosition.x !== _actionDetails.prevPosition.x ||
					newPosition.y !== _actionDetails.prevPosition.y) {
				_actionDetails.prevPosition = newPosition;
				return true;
			}
			return false;
		}

		function _dragSizeChanged(newSize) {
			if (newSize.w !== _actionDetails.prevSize.w ||
					newSize.h !== _actionDetails.prevSize.h) {
				_actionDetails.prevSize = newSize;
				return true;
			}
			return false;
		}

		function _snapPlaceHolderToGridPosition(leftPos, topPos) {
			// Keep item position within the grid and don't let the item create more
			// than one extra column
			var col = Math.round(leftPos / _cellWidthPx);
			var row = Math.round(topPos / _cellHeightPx);
			var maxCols = _getMaxCols();

			col = Math.max(col, 0);
			row = Math.max(row, 0);

			if (_isGrowHorizontal()) {
				col = Math.min(col, maxCols);
				row = Math.min(row, _options.lanes - _actionDetails.item.h);
			} else {
				col = Math.min(col, _options.lanes - _actionDetails.item.w);
				row = Math.min(row, maxCols);
			}

			return { x: col, y: row };
		}

		function _snapPlaceHolderToCellSize(width, height) {
			var w = Math.round(width / _cellWidthPx);
			var h = Math.round(height / _cellHeightPx);
			w = Math.max(w, 1);
			h = Math.max(h, 1);
			if (_isGrowHorizontal()) {
				w = Math.min(w, _options.lanes);
			} else {
				h = Math.min(h, _options.lanes);
			}
			return { w: w, h: h };
		}

		function _getMaxCols() {
			if (_isGrowHorizontal()) {
				return _gridList.grid.length;
			} else {
				return _options.lanes;
			}
		}

		function _isGrowHorizontal() {
			return _options.direction === "horizontal";
		}

		function _resizeGrid() {
			_gridList.resizeGrid(_options.lanes);
		}

		function _highlightPositionForItem(item) {
			_placeholder = _getContainerElement().querySelector('.placeholder');
			if (_placeholder) {
				_placeholder.style.position = 'absolute';
				_placeholder.style.width = _getItemWidth(item) + "px";
				_placeholder.style.height = _getItemHeight(item) + "px";
				_placeholder.style.left = item.x * _cellWidthPx + "px";
				_placeholder.style.top = item.y * _cellHeightPx + "px";
				_placeholder.style.display = 'inline';
			}
		}

		function _removePositionHighlight() {
			if (_placeholder) {
				_placeholder.style.display = 'none';
				_placeholder = null;
			}
		}

		function _getContainerElement() {
			return document.getElementById(_containerId);
		}

		function _appendObject(toObj, fromObj) {
			if (fromObj) {
				Object.keys(fromObj).forEach(function (key) {
					toObj[key] = fromObj[key];
				});
			}
			return toObj;
		}

		// Load grid
		if (_options.init) {
			this.loadGrid();
		}
	}

	return GridListCtrl;

});