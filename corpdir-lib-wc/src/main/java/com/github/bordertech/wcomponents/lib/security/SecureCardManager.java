package com.github.bordertech.wcomponents.lib.security;

import com.github.bordertech.wcomponents.WComponent;
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
public interface SecureCardManager<T extends SecureCard> extends WComponent {

	/**
	 * @param card the card to add
	 */
	void addSecureCard(final T card);

	/**
	 * @param card the card to remove
	 */
	void removeSecureCard(final T card);

	/**
	 * @return the list of available cards
	 */
	List<T> getSecureCards();

	/**
	 * @param screenPath the screen path
	 * @return the matching card
	 */
	T getSecureCard(final String screenPath);

	/**
	 * @param secureMode true if use security
	 */
	void setSecureMode(final boolean secureMode);

	/**
	 * @return true if using security
	 */
	boolean isSecureMode();

	/**
	 * @return the current card being displayed
	 */
	T getCurrentCard();

	/**
	 * @param card the card to set as being displayed
	 */
	void setCurrentCard(final T card);

	/**
	 * @param card the card to check if being displayed
	 * @return true if this card is currently being displayed
	 */
	boolean isCurrentCard(final T card);

}
