define(['interact'], function (interact) {

	function handleResizeStart(event) {
		console.log("Resize Start:" + event.target.id);
		console.log(event);
		event.target.classList.remove('grid-column-2');
	}

	function handleResizeMove(event) {
		console.log("Resize Move:" + event.target.id);
		var target = event.target;
		// update the element's style
		target.style.width = event.rect.width + 'px';
		target.style.height = event.rect.height + 'px';
	}

	function handleResizeEnd(event) {
		console.log("Resize End:" + event.target.id);
	}

	function setupResizable(gridId, options) {

		var select, cols, interItem;

		// Default options (if none provided)
		if (options === undefined) {
			options = { restrict: { elementRect: { left: 1, right: 1, top: 1, bottom: 1 } }, edges: { left: true, right: true, bottom: true, top: false } };
		}

		// Build selector
		select = '#' + gridId + ' .resize';

		// Resize
		var cols = document.querySelectorAll(select);
		[].forEach.call(cols, function (col) {
			interItem = interact(col);
			// Resize
			interItem.resizable(options)
					.on('resizestart', handleResizeStart)
					.on('resizemove', handleResizeMove)
					.on('resizeend', handleResizeEnd);
		});
	}

	return function (gridId, options) {
		setupResizable(gridId, options);
	};

});
