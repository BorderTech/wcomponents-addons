package com.github.bordertech.wcomponents.lib.polling;

import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WProgressBar;
import com.github.bordertech.wcomponents.lib.common.WDiv;
import java.util.List;

/**
 * Component capable of polling.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public interface Pollable extends WComponent {

	/**
	 * Default polling timeout of 120 seconds.
	 */
	int DEFAULT_POLLING_TIMEOUT = 120;

	/**
	 * @return the root container that holds the polling components
	 */
	WDiv getContentHolder();

	/**
	 * The AJAX targets that will be refreshed when the polling is complete.
	 *
	 * @return the extra AJAX targets
	 */
	List<AjaxTarget> getAjaxTargets();

	/**
	 * Add an AJAX target to be refreshed when the polling is complete.
	 *
	 * @param target the extra AJAX target to add
	 */
	void addAjaxTarget(final AjaxTarget target);

	/**
	 * Start AJAX polling manually.
	 */
	void doManualStart();

	/**
	 * Put the panel in a retry mode.
	 */
	void doShowRetry();

	/**
	 * Retry the polling action.
	 */
	void doRetry();

	/**
	 * Reset to start polling again.
	 */
	void doRefreshContent();

	/**
	 * @return the AJAX polling interval in milli seconds
	 */
	int getPollingInterval();

	/**
	 * @return the service status
	 */
	PollingStatus getPollingStatus();

	/**
	 * @return the text displayed while polling
	 */
	String getPollingText();

	/**
	 *
	 * @param interval the AJAX polling interval in milli seconds
	 */
	void setPollingInterval(final int interval);

	/**
	 * @param pollingStatus the panel status
	 */
	void setPollingStatus(final PollingStatus pollingStatus);

	/**
	 * @param text the text displayed while polling
	 */
	void setPollingText(final String text);

	/**
	 * @return the retry button.
	 */
	WButton getRetryButton();

	/**
	 * @return the start button
	 */
	WButton getStartButton();

	/**
	 * @return the start type
	 */
	PollingStartType getStartType();

	/**
	 *
	 * @param startType the start type
	 */
	void setStartType(final PollingStartType startType);

	/**
	 *
	 * @return the polling timeout interval (in seconds) or 0 for no timeout.
	 */
	int getPollingTimeout();

	/**
	 *
	 * @param pollingTimeout the polling timeout in seconds or 0 for no timeout
	 */
	void setPollingTimeout(final int pollingTimeout);

	/**
	 *
	 * @param useRetryOnError true if display retry button on error
	 */
	void setUseRetryOnError(final boolean useRetryOnError);

	/**
	 *
	 * @return true if display retry button on error
	 */
	boolean isUseRetryOnError();

	/**
	 * @return the progress bar used while polling
	 */
	WProgressBar getProgressBar();

}
