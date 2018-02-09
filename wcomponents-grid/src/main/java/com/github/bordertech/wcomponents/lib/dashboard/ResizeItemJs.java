package com.github.bordertech.wcomponents.lib.dashboard;

import com.github.bordertech.wcomponents.RenderContext;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WText;
import com.github.bordertech.wcomponents.WebUtilities;

/**
 *
 * @author exitxl
 */
public class ResizeItemJs extends WText {

	public ResizeItemJs() {
		setEncodeText(false);
	}

	@Override
	protected void preparePaintComponent(final Request request) {
		if (!isInitialised()) {
			// Find grid Item
			DashboardItem item = WebUtilities.getAncestorOfClass(DashboardItem.class, this);
			if (item != null) {
				setText("<script type='text/javascript'>require(['wclib/js/wc-dashboard-fit-item.js'], function(fit){fit('"
						+ item.getId() + "');});</script>");
			}
			setInitialised(true);
		}
	}

	public void resizeGridItem() {
		setVisible(true);
	}

	@Override
	protected void afterPaint(final RenderContext renderContext) {
		setVisible(false);
	}

}
