define(['wclib/js/lib/masonry.pkgd-4.2.0', 'wc/dom/initialise'], function (Masonry, initialise) {

	function resizeGrid(gridItemId) {
		return function () {
			console.log('Resize grid ' + gridItemId);
			var grid = document.getElementById(gridItemId);
			if (grid) {
				console.log('Found grid ' + grid.id);
				var msnry = Masonry.data(grid);
				if (msnry) {
					console.log('Refresh layout');
					msnry.layout();
				}
			}
		}
	}

	return function (gridItemId) {
		initialise.register({ postInit: function () {
				resizeGrid(gridItemId);
			} });
	};
});
