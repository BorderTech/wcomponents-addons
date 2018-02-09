package com.github.bordertech.wcomponents.lib.security;

import com.github.bordertech.didums.Didums;
import com.github.bordertech.wcomponents.Environment;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.UIContext;
import com.github.bordertech.wcomponents.UIContextHolder;
import com.github.bordertech.wcomponents.WCardManager;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.util.SystemException;
import com.github.bordertech.wcomponents.util.Util;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Secure Card Manager and Path.
 * <p>
 * Use {@link WLibServlet} to setup the {@link EnvironmentHelper}.
 * </p>
 *
 * @author Jonathan Austin
 * @param <T> the card type
 */
public class SecureCardManagerImpl<T extends SecureCard> extends WCardManager implements SecureCardManager<T> {

	private static final AppSecurityManager MANAGER = Didums.getService(AppSecurityManager.class);

	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);

		if ("GET".equals(request.getMethod())) {
			checkUrlPath(request);
		}
		checkAccess();

		// Get environment details
		UIContext uic = UIContextHolder.getCurrent();
		uic = UIContextHolder.getPrimaryUIContext(uic);
		final Environment env = uic.getEnvironment();

		// Add an action to run at the end of all the actions to set the post path to reflect the active card.
		// We need to run this last incase any other actions change the active card.
		uic.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Check path
				String currentScreenPath = getCurrentScreenPath(env.getPostPath());
				AppPath destinationPath = getCurrentCard().getAppPath();
				if (destinationPath != null
						&& destinationPath.getPath() != null
						&& !Util.equals(currentScreenPath, destinationPath.getPath())) {
					// Path has changed
					String url = getRedirectUrl(destinationPath);
					forward(url);
				}
			}
		});
	}

	@Override
	public void setSecureMode(final boolean secureMode) {
		getOrCreateComponentModel().secureMode = secureMode;
	}

	@Override
	public boolean isSecureMode() {
		return getComponentModel().secureMode;
	}

	@Override
	public void add(final WComponent component) {
		if (component instanceof SecureCard) {
			super.add(component);
		}
	}

	@Override
	public void add(final WComponent component, final String tag) {
		if (component instanceof SecureCard) {
			super.add(component, tag);
		}
	}

	@Override
	public void remove(final WComponent component) {
		if (component instanceof SecureCard) {
			super.remove(component);
		}
	}

	@Override
	public void makeVisible(final WComponent component) {
		if ((component instanceof SecureCard) && getSecureCards().contains((T) component)) {
			super.makeVisible(component);
			checkAccess();
		}
	}

	@Override
	public void addSecureCard(final T card) {
		add(card);
	}

	@Override
	public void removeSecureCard(final T card) {
		remove(card);
	}

	@Override
	public T getSecureCard(final String screenPath) {
		for (T card : getSecureCards()) {
			if (Objects.equal(card.getAppPath().getPath(), screenPath)) {
				return card;
			}
		}
		return null;
	}

	@Override
	public List<T> getSecureCards() {
		List<T> list = new ArrayList<>();
		for (WComponent child : getCardContainer().getChildren()) {
			list.add((T) child);
		}
		return list;
	}

	@Override
	public T getCurrentCard() {
		return (T) getVisible();
	}

	@Override
	public void setCurrentCard(final T card) {
		makeVisible(card);
	}

	@Override
	public boolean isCurrentCard(final T card) {
		return card == getCurrentCard();
	}

	/**
	 * Check the visible card and the URL path match.
	 *
	 * @param request the request being processed
	 */
	protected void checkUrlPath(final Request request) {
		// Get environment details
		UIContext uic = UIContextHolder.getCurrent();
		uic = UIContextHolder.getPrimaryUIContext(uic);
		final Environment env = uic.getEnvironment();

		// Make sure that the correct card is visible based on the URL.
		String currentScreenPath = getCurrentScreenPath(env.getPostPath());
		// Match Path to Card
		T screen = getSecureCard(currentScreenPath);
		if (screen != null) {
			if (getVisible() == screen) {
				// Check if request changed
				screen.handleCheckCardRequest(request);
			} else {
				// Switch to this card
				setCurrentCard(screen);
				// Call Setup
				screen.handleShowCardRequest(request);
			}
		}
	}

	/**
	 * Check if the user is allowed to access this card.
	 */
	protected void checkAccess() {
		getVisible().setVisible(true);
		if (isSecureMode()) {
			AppPath path = getCurrentCard().getAppPath();
			boolean allowed = MANAGER.userAccessToPath(path);
			if (!allowed) {
				getVisible().setVisible(false);
				handleAccessError();
			}
		}
	}

	/**
	 * Handle a user does not have access.
	 */
	protected void handleAccessError() {
		throw new SystemException("You don't have access to this system.");
	}

	/**
	 * @param postPath the post path
	 * @return the current screen path
	 */
	protected String getCurrentScreenPath(final String postPath) {
		String servletPath = EnvironmentHelper.getSecureServletPath();
		int idx = postPath.lastIndexOf(servletPath) + servletPath.length();
		String currentScreenPath = postPath.substring(idx);
		return currentScreenPath;
	}

	/**
	 * @param path the application path
	 * @return the redirect URL
	 */
	protected String getRedirectUrl(final AppPath path) {
		String servletPath = EnvironmentHelper.getSecureServletPath();
		return EnvironmentHelper.prefixBaseUrl(servletPath + path.getPath());
	}

	@Override
	protected SecureCardManagerModel newComponentModel() {
		return new SecureCardManagerModel();
	}

	@Override
	protected SecureCardManagerModel getOrCreateComponentModel() {
		return (SecureCardManagerModel) super.getOrCreateComponentModel();
	}

	@Override
	protected SecureCardManagerModel getComponentModel() {
		return (SecureCardManagerModel) super.getComponentModel();
	}

	/**
	 * This model holds the state information.
	 */
	public static final class SecureCardManagerModel extends CardManagerModel {

		/**
		 * Set true if using security.
		 */
		private boolean secureMode;
	}

}
