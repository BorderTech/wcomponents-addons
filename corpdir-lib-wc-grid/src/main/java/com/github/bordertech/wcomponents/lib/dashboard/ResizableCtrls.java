package com.github.bordertech.wcomponents.lib.dashboard;

import com.github.bordertech.wcomponents.WText;

/**
 *
 * @author exitxl
 */
public class ResizableCtrls extends WText {

	public ResizableCtrls() {
		StringBuilder txt = new StringBuilder();
		txt.append(buildCtrl("nw"));
		txt.append(buildCtrl("n"));
		txt.append(buildCtrl("ne"));
		txt.append(buildCtrl("w"));
		txt.append(buildCtrl("e"));
		txt.append(buildCtrl("sw"));
		txt.append(buildCtrl("s"));
		txt.append(buildCtrl("se"));
		setText(txt.toString());
		setEncodeText(false);
	}

	private String buildCtrl(final String location) {
		return "<div class='ui-resizable-handle ui-resizable-" + location + "'/>";
	}

}
