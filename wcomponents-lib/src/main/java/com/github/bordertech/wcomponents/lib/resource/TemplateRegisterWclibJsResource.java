package com.github.bordertech.wcomponents.lib.resource;

import com.github.bordertech.wcomponents.template.TemplateRendererFactory;

/**
 * Setups the template resource to register the wclib path for require js.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class TemplateRegisterWclibJsResource extends TemplateResource {

	/**
	 * Default to "wclib" path.
	 */
	public TemplateRegisterWclibJsResource() {
		this("/wclib");
	}

	/**
	 * @param path the path wclib will be mapped to
	 */
	public TemplateRegisterWclibJsResource(final String path) {
		super(getBuilder("wclib", path));
	}

	/**
	 * @param pathFrom the from path
	 * @param pathTo the to path
	 * @return the template builder for the require js config template
	 */
	public static Builder getBuilder(final String pathFrom, final String pathTo) {
		TemplateResource.Builder builder = new TemplateResource.Builder("/wclib/hbs/util/requireConfigPath.hbs");
		builder.setEngineName(TemplateRendererFactory.TemplateEngine.HANDLEBARS);
		builder.addParameter("path-from", pathFrom);
		builder.addParameter("path-to", pathTo);
		builder.setDescription("Register wclib path");
		builder.setMimeType("text/javascript");
		return builder;
	}

}
