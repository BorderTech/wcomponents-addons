package com.github.bordertech.wcomponents.lib.security;

import java.io.Serializable;
import java.util.Set;

/**
 * This application manager is responsible for determining a user's access.
 *
 * @author Jonathan Austin
 */
public interface AppSecurityManager extends Serializable {

	/**
	 * @return the current user roles
	 */
	Set<AppRole> getUserRoles();

	/**
	 * @param role the role to check access
	 * @return true if the current user has access to this role
	 */
	boolean userAccessToRole(final AppRole role);

	/**
	 * @param role the role to check access
	 * @return true if the current user has access to this role's path
	 */
	boolean userAccessToPath(final AppPath role);

	/**
	 * @return the application paths the current user has access to
	 */
	Set<AppPath> getAppPaths();

	/**
	 * @return the application roles the current user has access to
	 */
	Set<AppRole> getAppRoles();

	/**
	 * @param path the URL path to match
	 * @return the matching application path to the URL path, or null if no match
	 */
	AppPath findAppPath(final String path);

}
