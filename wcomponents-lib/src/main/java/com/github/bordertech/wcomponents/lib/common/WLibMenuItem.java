package com.github.bordertech.wcomponents.lib.common;

import com.github.bordertech.wcomponents.Action;
import com.github.bordertech.wcomponents.WDecoratedLabel;
import com.github.bordertech.wcomponents.WMenuItem;
import com.github.bordertech.wcomponents.lib.security.EnvironmentHelper;

/**
 * Allows a relative base URL to be used so the URL can be used in different contexts without changing.
 * <p>
 * This will be put into WComponents core.
 * </p>
 *
 * @author jonathan
 */
public class WLibMenuItem extends WMenuItem implements RelativeUrlBaseable {

	/**
	 * Creates a WMenuItem with the given label.
	 *
	 * @param label the menu item label.
	 */
	public WLibMenuItem(final WDecoratedLabel label) {
		super(label);
	}

	/**
	 * Creates a WMenuItem with the given label and url.
	 *
	 * @param label the menu item label.
	 * @param url the URL to navigate to when the menu item is invoked.
	 */
	public WLibMenuItem(final WDecoratedLabel label, final String url) {
		super(label, url);
	}

	/**
	 * Creates a WMenuItem with the given label and action.
	 *
	 * @param label the menu item label.
	 * @param action the action to execute when the menu item is invoked.
	 */
	public WLibMenuItem(final WDecoratedLabel label, final Action action) {
		super(label, action);
	}

	/**
	 * Creates a WMenuItem with the given text and url.
	 *
	 * @param text the menu item text.
	 * @param url the URL to navigate to when the menu item is invoked.
	 */
	public WLibMenuItem(final String text, final String url) {
		super(text, url);
	}

	/**
	 * Creates a WMenuItem with the given label and action.
	 *
	 * @param text the menu item text.
	 * @param action the action to execute when the menu item is invoked.
	 */
	public WLibMenuItem(final String text, final Action action) {
		super(text, action);
	}

	/**
	 * Creates a new WMenuItem with the specified text.
	 *
	 * @param text the menu item's text.
	 */
	public WLibMenuItem(final String text) {
		super(text);
	}

	/**
	 * Creates a new WMenuItem with the specified text and accessKey.
	 *
	 * @param text the menu item's text.
	 * @param accessKey the menu item's access key.
	 */
	public WLibMenuItem(final String text, final char accessKey) {
		super(text, accessKey);
	}

	/**
	 * Creates a new WMenuItem with the specified text, accessKey and action.
	 *
	 * @param text the menu item's text.
	 * @param accessKey the menu item's access key.
	 * @param action the action to execute when the menu item is invoked.
	 */
	public WLibMenuItem(final String text, final char accessKey, final Action action) {
		super(text, accessKey, action);
	}

	/**
	 * @param url the URL to navigate to
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
