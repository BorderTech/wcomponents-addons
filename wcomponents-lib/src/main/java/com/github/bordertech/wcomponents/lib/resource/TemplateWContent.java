package com.github.bordertech.wcomponents.lib.resource;

import com.github.bordertech.wcomponents.WContent;
import com.github.bordertech.wcomponents.util.SystemException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * A {@link WContent} with a {@link TemplateResource}.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class TemplateWContent extends WContent {

	/**
	 * @param resource the template resource
	 */
	public TemplateWContent(final TemplateResource resource) {
		this(resource, null);
	}

	/**
	 * @param resource the template resource
	 * @param cacheKey the cache key
	 */
	public TemplateWContent(final TemplateResource resource, final String cacheKey) {
		setContentAccess(resource);
		if (cacheKey != null) {
			setCacheKey(cacheKey + "-" + getTemplateHash(resource));
		}
	}

	/**
	 * Calculate a HASH for the template. This will make sure the template is cache busted when it changes.
	 *
	 * @param resource the template resource
	 * @return the template hash
	 */
	protected String getTemplateHash(final TemplateResource resource) {
		final int bufferSize = 1024;
		try (InputStream stream = resource.getStream()) {
			// Compute CRC-32 checksum
			// TODO Is a 1 in 2^32 chance of a cache bust fail good enough?
			// Checksum checksumEngine = new Adler32();
			Checksum checksumEngine = new CRC32();
			byte[] buf = new byte[bufferSize];

			for (int read = stream.read(buf); read != -1; read = stream.read(buf)) {
				checksumEngine.update(buf, 0, read);
			}

			return Long.toHexString(checksumEngine.getValue());
		} catch (Exception e) {
			throw new SystemException("Error calculating resource hash", e);
		}
	}

}
