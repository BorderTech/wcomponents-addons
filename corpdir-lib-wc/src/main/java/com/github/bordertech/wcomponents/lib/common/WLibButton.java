package com.github.bordertech.wcomponents.lib.common;

import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.lib.security.EnvironmentHelper;

/**
 * Allows a relative base URL to be used so the URL can be used in different contexts without changing.
 * <p>
 * This will be put into WComponents core.
 * </p>
 *
 * @author jonathan
 */
public class WLibButton extends WButton implements RelativeUrlBaseable {

	/**
	 * Creates a WButton with no text or image. The button text must be set after construction.
	 */
	public WLibButton() {
	}

	/**
	 * Creates a WButton with the specified text.
	 *
	 * @param text the button text, using {@link MessageFormat} syntax.
	 */
	public WLibButton(final String text) {
		super(text);
	}

	/**
	 * Constructor. Set the button text and access key. Access keys are not case sensitive.
	 *
	 * @param text The button text.
	 * @param accessKey The shortcut key that activates the button.
	 */
	public WLibButton(final String text, final char accessKey) {
		super(text, accessKey);
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
		if (url != null && isRelativeBaseUrl()) {
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

}
