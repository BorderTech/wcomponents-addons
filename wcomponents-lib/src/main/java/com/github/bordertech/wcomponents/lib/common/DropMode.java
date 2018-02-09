package com.github.bordertech.wcomponents.lib.common;

/**
 * Drop modes for dropzone components.
 *
 * @author jonathan
 */
public enum DropMode {
	/**
	 * COPY dragged item when dropped.
	 */
	COPY,
	/**
	 * MOVE dragged item when dropped.
	 */
	MOVE,
	/**
	 * LINK dragged item when dropped.
	 */
	LINK;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
