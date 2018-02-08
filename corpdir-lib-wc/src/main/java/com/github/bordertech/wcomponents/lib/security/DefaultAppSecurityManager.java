package com.github.bordertech.wcomponents.lib.security;

import java.util.Collections;
import java.util.Set;

/**
 * Default security manager with no roles and paths defined.
 *
 * @author jonathan
 */
public class DefaultAppSecurityManager implements AppSecurityManager {

	@Override
	public Set<AppRole> getUserRoles() {
		return Collections.EMPTY_SET;
	}

	@Override
	public boolean userAccessToRole(final AppRole role) {
		return true;
	}

	@Override
	public boolean userAccessToPath(final AppPath role) {
		return true;
	}

	@Override
	public Set<AppPath> getAppPaths() {
		return Collections.EMPTY_SET;
	}

	@Override
	public Set<AppRole> getAppRoles() {
		return Collections.EMPTY_SET;
	}

	@Override
	public AppPath findAppPath(final String path) {
		return null;
	}

}
