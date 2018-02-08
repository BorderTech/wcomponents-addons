package com.github.bordertech.wcomponents.lib.common;

import com.github.bordertech.wcomponents.WLink;
import com.github.bordertech.wcomponents.lib.security.EnvironmentHelper;

/**
 * Allows a relative base URL to be used so the URL can be used in different contexts without changing.
 * <p>
 * This will be put into WComponents core.
 * </p>
 *
 * @author jonathan
 */
public class WLibLink extends WLink implements RelativeUrlBaseable {

	/**
	 * Creates a WLink.
	 */
	public WLibLink() {
	}

	/**
	 * Creates a WLink with the given text and url.
	 *
	 * @param text the link test to display.
	 * @param url the link url.
	 */
	public WLibLink(final String text, final String url) {
		super(text, url);
	}

	/**
	 * @param url the URL link
	 * @param relativeBaseUrl true if use relative to base URL
	 */
	public void setUrl(final String url, final boolean relativeBaseUrl) {
		super.setUrl(url);
		setRelativeBaseUrl(relativeBaseUrl);
	}

	@Override
	public String getUrl() {
		String url = super.getUrl();
		if (url != null && isRelativeBaseUrl()) {
			return EnvironmentHelper.prefixBaseUrl(url);
		}
		return url;
	}

	/**
	 * @param imageUrl the image URL
	 * @param relativeBaseUrl true if use relative to base URL
	 */
	public void setImageUrl(final String imageUrl, final boolean relativeBaseUrl) {
		super.setImageUrl(imageUrl);
		setRelativeBaseUrl(relativeBaseUrl);
	}

	@Override
	public String getImageUrl() {
		String url = super.getImageUrl();
		if (url != null && isImageRelativeBaseUrl()) {
			return EnvironmentHelper.prefixBaseUrl(url);
		}
		return url;
	}

	@Override
	public void setRelativeBaseUrl(final boolean relativeBaseUrl) {
		setAttribute(RelativeUrlBaseable.RELATIVE_FLAG_ATTR, relativeBaseUrl);
	}

	@Override
	public boolean isRelativeBaseUrl() {
		Boolean flag = (Boolean) getAttribute(RelativeUrlBaseable.RELATIVE_FLAG_ATTR);
		return flag != null && flag;
	}

	/**
	 * @param relativeBaseUrl true if Image URL is relative to base URL
	 */
	public void setImageRelativeBaseUrl(final boolean relativeBaseUrl) {
		setAttribute("wc_relative_img", relativeBaseUrl);
	}

	/**
	 * @return true if Image URL is relative to base URL
	 */
	public boolean isImageRelativeBaseUrl() {
		Boolean flag = (Boolean) getAttribute("wc_relative_img");
		return flag != null && flag;
	}

}
