package com.github.bordertech.wcomponents.lib.resource;

import com.github.bordertech.wcomponents.ContentStreamAccess;
import com.github.bordertech.wcomponents.template.TemplateRenderer;
import com.github.bordertech.wcomponents.template.TemplateRendererFactory;
import com.github.bordertech.wcomponents.util.ConfigurationProperties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provide a Template as the Content Stream.
 *
 * @author jonathan
 */
public class TemplateResource implements ContentStreamAccess {

	private static final String DEFAULT_ENGINE = ConfigurationProperties.getDefaultRenderingEngine();

	private final boolean inline;
	private final String template;
	private final String engineName;
	private final String description;
	private final String mimeType;
	private final Map<String, Object> parameters;
	private final Map<String, Object> engineOptions;

	/**
	 * @param builder the resource builder
	 */
	public TemplateResource(final Builder builder) {
		this.inline = builder.isInline();
		this.template = builder.getTemplate();
		this.engineName = builder.getEngineName() == null ? DEFAULT_ENGINE : builder.getEngineName();
		this.description = builder.getDescription() == null ? "template" : builder.getDescription();
		this.mimeType = builder.getMimeType() == null ? "text/plain" : builder.getMimeType();
		this.parameters = Collections.unmodifiableMap(builder.getParameters());
		this.engineOptions = Collections.unmodifiableMap(builder.getEngineOptions());
	}

	/**
	 * @return true if stream inline resource
	 */
	public boolean isInline() {
		return inline;
	}

	/**
	 * @return the template resource name
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @return the template engine name
	 */
	public String getEngineName() {
		return engineName;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @return the template parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @return the template engine options
	 */
	public Map<String, Object> getEngineOptions() {
		return engineOptions;
	}

	@Override
	public InputStream getStream() throws IOException {
		return new ByteArrayInputStream(processTemplate());

	}

	@Override
	public byte[] getBytes() {
		return processTemplate();
	}

	/**
	 * @return the template as a byte array
	 */
	protected byte[] processTemplate() {
		StringWriter output = new StringWriter();
		PrintWriter writer = new PrintWriter(output);
		TemplateRenderer templateRenderer = TemplateRendererFactory.newInstance(getEngineName());
		if (isInline()) {
			templateRenderer.renderInline(getTemplate(), getParameters(), Collections.EMPTY_MAP, writer, getEngineOptions());
		} else {
			templateRenderer.renderTemplate(getTemplate(), getParameters(), Collections.EMPTY_MAP, writer, getEngineOptions());
		}
		return output.toString().getBytes();
	}

	/**
	 * Option Builder for template resources.
	 */
	public static class Builder {

		private final String template;
		private final boolean inline;
		private String engineName;
		private String description;
		private String mimeType;
		private final Map<String, Object> parameters = new HashMap<>();
		private final Map<String, Object> engineOptions = new HashMap<>();

		/**
		 * @param template the template resource name
		 */
		public Builder(final String template) {
			this(template, false);
		}

		/**
		 * @param template the template resource name
		 * @param inline true if stream inline
		 */
		public Builder(final String template, final boolean inline) {
			this.template = template;
			this.inline = inline;
		}

		/**
		 * @return true if stream inline
		 */
		protected boolean isInline() {
			return inline;
		}

		/**
		 * @return the template resource name
		 */
		protected String getTemplate() {
			return template;
		}

		/**
		 * @return the template engine name
		 */
		protected String getEngineName() {
			return engineName;
		}

		/**
		 * @return the resource description
		 */
		protected String getDescription() {
			return description;
		}

		/**
		 * @return the resource mime type
		 */
		protected String getMimeType() {
			return mimeType;
		}

		/**
		 * @return the template parameters
		 */
		protected Map<String, Object> getParameters() {
			return parameters;
		}

		/**
		 * @return the template engine options
		 */
		protected Map<String, Object> getEngineOptions() {
			return engineOptions;
		}

		/**
		 * @param engine the template engine
		 * @return the builder
		 */
		public Builder setEngineName(final TemplateRendererFactory.TemplateEngine engine) {
			this.engineName = engine.getEngineName();
			return this;
		}

		/**
		 * @param name the template engine name
		 * @return the builder
		 */
		public Builder setEngineName(final String name) {
			this.engineName = name;
			return this;
		}

		/**
		 * @param desc the resource description
		 * @return the builder
		 */
		public Builder setDescription(final String desc) {
			this.description = desc;
			return this;
		}

		/**
		 * @param type the resource mime type
		 * @return the builder
		 */
		public Builder setMimeType(final String type) {
			this.mimeType = type;
			return this;
		}

		/**
		 * @param key the template parameter key
		 * @param value the template parameter value
		 * @return the builder
		 */
		public Builder addParameter(final String key, final Object value) {
			parameters.put(key, value);
			return this;
		}

		/**
		 * @param key the engine option key
		 * @param value the engine option value
		 * @return the builder
		 */
		public Builder addEngineOption(final String key, final Object value) {
			engineOptions.put(key, value);
			return this;
		}

		/**
		 * @return build the template resource
		 */
		public TemplateResource build() {
			return new TemplateResource(this);
		}

	}

}
