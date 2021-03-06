package com.github.bordertech.wcomponents.addons.cardpath;

import java.io.Serializable;

/**
 * Application roles that can be assigned to users and linked to servlet paths.
 *
 * @author Jonathan Austin
 */
public interface AppRole extends Serializable {

	/**
	 * @return the role name
	 */
	String getRoleName();

}
