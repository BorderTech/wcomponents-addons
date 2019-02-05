package com.github.bordertech.wcomponents.addons.common;

import com.github.bordertech.wcomponents.WText;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Display formatted timestamp.
 * <p>
 * Expects to be bound to a {@link Date}.
 * </p>
 */
public class TimestampWText extends WText {

	@Override
	public String getText() {
		// Calc each time as may change
		Object data = super.getData();
		if (data instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			return sdf.format((Date) data);
		} else {
			return null;
		}
	}

}
