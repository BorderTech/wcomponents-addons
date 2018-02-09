package com.github.bordertech.wcomponents.lib.security;

import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WComponent;

/**
 * Secure WComponent (usually a card in a card manager) that is assigned to an application path.
 *
 * @author jonathan
 */
public interface SecureCard extends WComponent {

	/**
	 *
	 * @return the card application path and roles
	 */
	AppPath getAppPath();

	/**
	 * Called when the Card is navigated to.
	 *
	 * @param request the request being processed
	 */
	void handleShowCardRequest(final Request request);

	/**
	 * Check if the card has not changed but the parameters have.
	 *
	 * @param request the request being processed
	 */
	void handleCheckCardRequest(final Request request);

}
