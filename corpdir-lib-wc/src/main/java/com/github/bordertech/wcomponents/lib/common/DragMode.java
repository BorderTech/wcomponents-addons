package com.github.bordertech.wcomponents.lib.common;

/**
 * Drag modes for draggable components.
 *
 * @author jonathan
 */
public enum DragMode {
	/**
	 * DRAG Mode enabled.
	 */
	TRUE,
	/**
	 * DRAG Mode disabled.
	 */
	FALSE,
	/**
	 * Use DRAG Mode if available.
	 */
	AUTO;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
