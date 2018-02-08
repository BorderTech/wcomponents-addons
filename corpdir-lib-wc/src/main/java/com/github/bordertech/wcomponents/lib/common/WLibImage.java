package com.github.bordertech.wcomponents.lib.common;

import com.github.bordertech.wcomponents.ImageResource;
import com.github.bordertech.wcomponents.WImage;
import com.github.bordertech.wcomponents.lib.security.EnvironmentHelper;

/**
 * Allows a relative base URL to be used so the URL can be used in different contexts without changing.
 * <p>
 * This will be put into WComponents core.
 * </p>
 *
 * @author jonathan
 */
public class WLibImage extends WImage implements RelativeUrlBaseable {

	/**
	 * Creates a WImage with no content.
	 */
	public WLibImage() {
	}

	/**
	 * <p>
	 * Creates a WImage with the given static content. This is provided as a convenience method for when the image is
	 * included as static content in the class path rather than in the web application's resources.
	 * </p>
	 * <p>
	 * The mime type for the image is looked up from the "mimeType.*" mapping configuration parameters using the
	 * resource's file extension.
	 * </p>
	 *
	 * @param imageResource the resource path to the image file.
	 * @param description the image description.
	 */
	public WLibImage(final String imageResource, final String description) {
		super(imageResource, description);
	}

	/**
	 * Creates a WImage with the given image resource.
	 *
	 * @param image the image resource
	 */
	public WLibImage(final ImageResource image) {
		super(image);
	}

	/**
	 * @param imageUrl the image URL
	 * @param relativeBaseUrl true if use relative to base URL
	 */
	public void setImageUrl(final String imageUrl, final boolean relativeBaseUrl) {
		super.setImageUrl(imageUrl);
		setRelativeBaseUrl(relativeBaseUrl);
	}

	@Override
	public String getImageUrl() {
		String url = super.getImageUrl();
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
