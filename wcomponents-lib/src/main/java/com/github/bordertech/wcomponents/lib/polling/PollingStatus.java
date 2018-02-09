package com.github.bordertech.wcomponents.lib.polling;

/**
 * Status of the polling panel.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public enum PollingStatus {
	/**
	 * Not started yet.
	 */
	STOPPED,
	/**
	 * Service action started.
	 */
	PROCESSING,
	/**
	 * Polling timed out.
	 */
	TIMEOUT
}
