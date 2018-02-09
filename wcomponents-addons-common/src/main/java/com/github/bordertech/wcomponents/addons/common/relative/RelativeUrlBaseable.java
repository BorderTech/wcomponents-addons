package com.github.bordertech.wcomponents.addons.common.relative;

import com.github.bordertech.wcomponents.WComponent;

/**
 * Allow a URL that is relative to the base URL to be used so it works in different application contexts without having
 * to be changed.
 *
 * @author jonathan
 */
public interface RelativeUrlBaseable extends WComponent {

	/**
	 * Component model attribute key.
	 */
	String RELATIVE_FLAG_ATTR = "wc_relative";

	/**
	 *
	 * @param relativeBaseUrl true if URL is relative to base URL
	 */
	void setRelativeBaseUrl(final boolean relativeBaseUrl);

	/**
	 *
	 * @return true if URL is relative to base URL
	 */
	boolean isRelativeBaseUrl();

}
