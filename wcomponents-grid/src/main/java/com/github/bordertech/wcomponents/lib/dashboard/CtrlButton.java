package com.github.bordertech.wcomponents.lib.dashboard;

import com.github.bordertech.wcomponents.WText;

/**
 *
 * @author exitxl
 */
public class CtrlButton extends WText {

	public CtrlButton(final String classes, final String tooltip) {
		super("<button type='button' title='" + tooltip + "' class='wc-linkbutton wc-nobutton wc-icon grid-ctrl " + classes + "'></button>");
		setEncodeText(false);
	}

}
