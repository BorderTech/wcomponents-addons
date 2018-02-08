define(['wclib/js/lib/GridInteract-1.0.0'], function (GridInteract) {

	function resizeGrid(gridItemId) {
		return function () {
			console.log('Resizing item ' + gridItemId);
			var gridItem = document.getElementById(gridItemId);
			if (gridItem) {
				console.log('Found item ' + gridItemId);
				GridInteract.getGrid().layout();
			}
		}
	}

	return function (gridItemId) {
		window.setTimeout(resizeGrid(gridItemId), 1200);
	};
});
