define(['wclib/js/lib/GridListCtrl-1.0.0', 'interact'],
		function (GridListCtrl, interact) {

			function GridInteract(element, opts) {

				// Create Grid Ctrl
				var grid = new GridListCtrl(element, opts);

				this.getGrid = function () {
					return grid;
				};

				function _setupInteract() {
					var dragOpt = grid.getDragOptions();
					var resizeOpt = grid.getResizeOptions();

//            // Make the GRID the restriction
//            dragOpt.restrict.restriction = element;
//            resizeOpt.restrict.restriction = element;
//            dragOpt.restrict.elementRect = { left: 0, right: 1, top: 0, bottom: 1};
//            resizeOpt.restrict.elementRect = { left: 0, right: 1, top: 0, bottom: 1};

					var gridItems = grid.getItemElements();
					gridItems.forEach(function (gridItem) {
						_setupGridItem(gridItem, dragOpt, resizeOpt);
					});
				}
				;

				function _setupGridItem(gridItem, dragOpt, resizeOpt) {
					var interItem = interact(gridItem);


					// Resize
					if (grid.isResize()) {
						interItem.resizable(resizeOpt)
								.on('resizestart', _onResizeStart)
								.on('resizemove', _onResizeMove)
								.on('resizeend', _onResizeEnd);
					}
					// Drag
					if (grid.isDrag()) {
						interItem.draggable(dragOpt)
								.on('dragstart', _onDragStart)
								.on('dragmove', _onDragMove)
								.on('dragend', _onDragEnd);
					}
				}
				;

				function _onResizeStart(event) {
					console.log("Resize Start:" + event.target.id);
					console.log(event);
					grid.resizeStart(event.target);
				}
				;
				function _onResizeMove(event) {
					console.log("Resize Move:" + event.target.id);
					var rect = {
						width: event.rect.width,
						height: event.rect.height,
						top: event.rect.top,
						left: event.rect.left
					};
					grid.resizeMove(rect, event.edges);
				}
				;
				function _onResizeEnd(event) {
					console.log("Resize End:" + event.target.id);
					grid.resizeEnd();
				}
				;

				function _onDragStart(event) {
					console.log("Drag start:" + event.target.id);
					grid.dragStart(event.target);
				}
				;

				function _onDragMove(event) {
					console.log("Drag move:" + event.target.id);
					var dx = event.clientX - event.clientX0;
					var dy = event.clientY - event.clientY0;
					grid.dragMove(dx, dy);
				}
				;
				function _onDragEnd(event) {
					console.log("Drag end:" + event.target.id);
					grid.dragEnd();
				}
				;

				if (grid.isResize() || grid.isDrag()) {
					_setupInteract();
				}

			}

			return GridInteract;

		});
