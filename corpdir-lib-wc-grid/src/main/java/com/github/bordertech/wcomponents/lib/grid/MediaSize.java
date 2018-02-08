package com.github.bordertech.wcomponents.lib.grid;

/**
 *
 *
 * @author jonathan
 */
public enum MediaSize {
	/**
	 * Extra small devices (portrait phones, less than 576px).
	 */
	XS,
	/**
	 * Small devices (landscape phones, 576px and up).
	 */
	SM,
	/**
	 * Medium devices (tablets, 768px and up).
	 */
	MD,
	/**
	 * Large devices (desktops, 992px and up).
	 */
	LG,
	/**
	 * Extra large devices (large desktops, 1200px and up).
	 */
	XL;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
