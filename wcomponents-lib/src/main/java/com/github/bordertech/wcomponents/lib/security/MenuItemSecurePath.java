package com.github.bordertech.wcomponents.lib.security;

import com.github.bordertech.didums.Didums;
import com.github.bordertech.wcomponents.lib.common.WLibMenuItem;

/**
 * Menu item that links to a secure path.
 *
 * @author jonathan
 */
public class MenuItemSecurePath extends WLibMenuItem {

	private static final AppSecurityManager MANAGER = Didums.getService(AppSecurityManager.class);

	private final AppPath appPath;

	/**
	 * @param text the menu item text
	 * @param path the secure path
	 */
	public MenuItemSecurePath(final String text, final String path) {
		this(text, new DefaultAppPath(path));
	}

	/**
	 *
	 * @param text the menu item text
	 * @param path the application path
	 */
	public MenuItemSecurePath(final String text, final AppPath path) {
		super(text, path.getPath());
		this.appPath = path;
	}

	/**
	 *
	 * @return the liked application path
	 */
	public AppPath getAppPath() {
		return appPath;
	}

	/**
	 * @param secureMode true if menu item is secured
	 */
	public void setSecureMode(final boolean secureMode) {
		getOrCreateComponentModel().secureMode = secureMode;
	}

	/**
	 * @return true if the menu item is secured
	 */
	public boolean isSecureMode() {
		return getComponentModel().secureMode;
	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && checkAccess();
	}

	/**
	 * @return true if the user has access to this menu item
	 */
	protected boolean checkAccess() {
		if (!isSecureMode()) {
			return true;
		}
		return MANAGER.userAccessToPath(appPath);
	}

	@Override
	protected SecureMenuItemModel newComponentModel() {
		return new SecureMenuItemModel();
	}

	@Override
	protected SecureMenuItemModel getOrCreateComponentModel() {
		return (SecureMenuItemModel) super.getOrCreateComponentModel();
	}

	@Override
	protected SecureMenuItemModel getComponentModel() {
		return (SecureMenuItemModel) super.getComponentModel();
	}

	/**
	 * This model holds the state information.
	 */
	public static final class SecureMenuItemModel extends MenuItemModel {

		/**
		 * Set true if using security.
		 */
		private boolean secureMode;

	}

}
