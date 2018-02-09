package com.github.bordertech.wcomponents.lib.common;

import com.github.bordertech.wcomponents.AjaxHelper;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WDecoratedLabel;
import com.github.bordertech.wcomponents.WTab;
import com.github.bordertech.wcomponents.WTabSet;

/**
 * Keep Lazy tabs that are open as visible during AJAX operations.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class WLibTab extends WTab {

	/**
	 * Creates a new tab.
	 *
	 * @param content the tab content
	 * @param tabName the tab label
	 * @param mode the {@link TabMode | tab mode}
	 */
	public WLibTab(final WComponent content, final String tabName, final WTabSet.TabMode mode) {
		super(content, tabName, mode);
	}

	/**
	 * Creates a new tab.
	 *
	 * @param content the tab content
	 * @param tabName the tab label
	 * @param mode the {@link TabMode | tab mode}.
	 * @param accessKey the access key used to activate this tab
	 */
	public WLibTab(final WComponent content, final String tabName, final WTabSet.TabMode mode, final char accessKey) {
		super(content, tabName, mode, accessKey);
	}

	/**
	 * Creates a new tab.
	 *
	 * @param content the tab content
	 * @param label the tab label, which may contain rich content.
	 * @param mode the {@link TabMode | tab mode}.
	 */
	public WLibTab(final WComponent content, final WDecoratedLabel label, final WTabSet.TabMode mode) {
		super(content, label, mode);
	}

	/**
	 * Creates a new tab.
	 *
	 * @param content the tab content
	 * @param label the tab label, which may contain rich content.
	 * @param mode the {@link TabMode | tab mode}.
	 * @param accessKey the access key used to activate this tab
	 */
	public WLibTab(final WComponent content, final WDecoratedLabel label, final WTabSet.TabMode mode, final char accessKey) {
		super(content, label, mode, accessKey);
	}

	@Override
	protected void preparePaintComponent(final Request request) {
		if (isLazy()) {
			if (AjaxHelper.getCurrentOperation() == null) {
				setRendered(null);
			}
			super.preparePaintComponent(request);
			if (isOpen()) {
				setRendered(Boolean.TRUE);
			}
			// Keep Lazy tabs that are open as visible during AJAX operations.
			getContent().setVisible(isOpen() || isRendered());
		} else {
			super.preparePaintComponent(request);
		}
	}

	/**
	 * @return true if lazy tab is currently open and been rendered to the client
	 */
	protected boolean isRendered() {
		Boolean rendered = (Boolean) getAttribute("wc-rendered");
		return rendered != null && rendered;
	}

	/**
	 * @param rendered true if tab is currently rendered
	 */
	protected void setRendered(final Boolean rendered) {
		setAttribute("wc-rendered", rendered);
	}

	/**
	 * @return true if tab is lazy mode
	 */
	protected boolean isLazy() {
		return getMode() == WTabSet.TabMode.LAZY;
	}

}
