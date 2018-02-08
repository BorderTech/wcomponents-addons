package com.github.bordertech.wcomponents.lib.security;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Default application path and roles implementation.
 *
 * @author Jonathan Austin
 */
public class DefaultAppPath implements AppPath {

	private final String path;

	/**
	 *
	 * @param path the application path.
	 */
	public DefaultAppPath(final String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Set<AppRole> getAppRoles() {
		return Collections.EMPTY_SET;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + Objects.hashCode(this.path);
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DefaultAppPath other = (DefaultAppPath) obj;
		if (!Objects.equals(this.path, other.path)) {
			return false;
		}
		return true;
	}

}
