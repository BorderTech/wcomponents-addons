define(['wc/dom/initialise', 'wclib/js/wc-grid-resize', 'wclib/js/wc-grid-drag'], function (initialise, resize, drag) {

	function setupGrid(gridId, resizeOptions) {
		resize(gridId, resizeOptions);
		drag(gridId);
	}

	return function (gridId, resizeOptions) {
		initialise.register({ postInit: function () {
				setupGrid(gridId, resizeOptions);
			} });
	};

});
