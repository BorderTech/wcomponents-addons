package com.github.bordertech.wcomponents.lib.security;

import java.io.Serializable;
import java.util.Set;

/**
 * Application servlet paths.
 *
 * @author Jonathan Austin
 */
public interface AppPath extends Serializable {

	/**
	 * @return the application path
	 */
	String getPath();

	/**
	 * @return the roles that have access to this path
	 */
	Set<AppRole> getAppRoles();

}
