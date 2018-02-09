package com.github.bordertech.wcomponents.addons.common;

import com.github.bordertech.wcomponents.WApplication;
import com.github.bordertech.wcomponents.addons.common.relative.EnvironmentHelper;
import com.github.bordertech.wcomponents.addons.common.resource.ApplicationResourceWContent;
import com.github.bordertech.wcomponents.addons.common.resource.TemplateRegisterWclibJsResource;
import com.github.bordertech.wcomponents.addons.common.resource.TemplateWContent;

/**
 * Helper class for Applications to configure the library resources.
 *
 * @author jonathan
 */
public final class LibHelperUtil {

	/**
	 * Private constructor.
	 */
	private LibHelperUtil() {
	}

	/**
	 * Configure an application to use wc library.
	 *
	 * @param app the application to configure
	 */
	public static void configApplication(final WApplication app) {

		// CSS
		String url = EnvironmentHelper.prefixBaseUrl("wclib/css/lib/cssgrid@0.0.4.css");
		app.addCssUrl(url);

		// Javascript - Allow requireJS to load wclib js libraries
		TemplateWContent registerWclib = new TemplateWContent(new TemplateRegisterWclibJsResource(), "reg");
		app.add(registerWclib);
		app.addJsResource(new ApplicationResourceWContent(registerWclib, "regkey"));
	}

}
