define(function () {

	function handleDragStart(ev) {
		ev.target.style.opacity = '0.4';
		ev.dataTransfer.setData("dragId", ev.target.id);
		ev.dataTransfer.effectAllowed = 'move';
	}

	function handleDragOver(ev) {
		ev.preventDefault(); // Necessary. Allows us to drop.
		ev.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.
		return false;
	}

	function handleDragEnter(ev) {
		ev.target.classList.add('over');
	}

	function handleDragLeave(ev) {
		ev.target.classList.remove('over');  // this / e.target is previous target element.
	}

	function handleDrop(ev) {
		ev.preventDefault();
		ev.stopPropagation();
		var dragId = ev.dataTransfer.getData("dragId");
		var dragged = document.getElementById(dragId);
		var dropped = ev.currentTarget;
		if (dragged.id !== dropped.id) {
			swapNodes(dragged, dropped);
		}
		dragged.style.opacity = '1';
	}

	function handleDragEnd(ev) {
		ev.target.classList.remove('over');  // this / e.target is previous target element.
		ev.srcElement.style.opacity = '1';
	}

	function swapNodes(n1, n2) {

		var p1 = n1.parentNode;
		var p2 = n2.parentNode;
		var i1, i2;

		if (!p1 || !p2 || p1.isEqualNode(n2) || p2.isEqualNode(n1))
			return;

		for (var i = 0; i < p1.children.length; i++) {
			if (p1.children[i].isEqualNode(n1)) {
				i1 = i;
			}
		}
		for (var i = 0; i < p2.children.length; i++) {
			if (p2.children[i].isEqualNode(n2)) {
				i2 = i;
			}
		}

		if (p1.isEqualNode(p2) && i1 < i2) {
			i2++;
		}
		p1.insertBefore(n2, p1.children[i1]);
		p2.insertBefore(n1, p2.children[i2]);
	}

	function setupDraggable(gridId) {

		var select, cols;

		// Build selector
		select = '#' + gridId + ' .drag';

		cols = document.querySelectorAll(select);
		[].forEach.call(cols, function (col) {
			col.addEventListener('dragstart', handleDragStart, false);
			col.addEventListener('dragover', handleDragOver, false);
			col.addEventListener('dragenter', handleDragEnter, false);
			col.addEventListener('dragleave', handleDragLeave, false);
			col.addEventListener('drop', handleDrop, false);
			col.addEventListener('dragend', handleDragEnd, false);
		});
	}

	return function (gridId) {
		setupDraggable(gridId);
	};

});


