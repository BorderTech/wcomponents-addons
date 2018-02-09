package com.github.bordertech.wcomponents.lib.resource;

import com.github.bordertech.wcomponents.WApplication;
import com.github.bordertech.wcomponents.WContent;

/**
 * Allows a {@link WContent} URL to be used as an {@link WApplication.ApplicationResource}.
 *
 * @author jonathan
 */
public class ApplicationResourceWContent extends WApplication.ApplicationResource {

	private final WContent content;

	/**
	 * @param content the content that provides the URL
	 * @param urlKey the key used by WApplication
	 */
	public ApplicationResourceWContent(final WContent content, final String urlKey) {
		super(urlKey);
		this.content = content;
	}

	@Override
	public String getTargetUrl() {
		return content.getUrl();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

}
