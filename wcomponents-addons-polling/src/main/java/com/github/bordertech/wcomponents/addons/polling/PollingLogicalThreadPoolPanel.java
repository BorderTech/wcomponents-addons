package com.github.bordertech.wcomponents.addons.polling;

import com.github.bordertech.taskmaster.logical.LogicalThreadPoolController;
import com.github.bordertech.taskmaster.service.ResultHolder;
import com.github.bordertech.wcomponents.WebUtilities;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Polling panel that can also use a logical thread pool.
 * <p>
 * Is used in conjunction with {@link LogicalThreadPoolController}.
 * </p>
 *
 * @param <S> the polling criteria type
 * @param <T> the polling result type
 */
public class PollingLogicalThreadPoolPanel<S extends Serializable, T extends Serializable> extends PollingServicePanel<S, T> {

	private static final Log LOG = LogFactory.getLog(PollingLogicalThreadPoolPanel.class);

	private final boolean useLogicalThreadPool;

	/**
	 * Default constructor.
	 */
	public PollingLogicalThreadPoolPanel() {
		this(174);
	}

	/**
	 * @param delay the AJAX polling delay
	 */
	public PollingLogicalThreadPoolPanel(final int delay) {
		this(delay, false);
	}

	/**
	 * @param delay the AJAX polling delay
	 * @param useLogicalThreadPool true if use thread pool to control invoking the service
	 */
	public PollingLogicalThreadPoolPanel(final int delay, final boolean useLogicalThreadPool) {
		super(delay);
		this.useLogicalThreadPool = useLogicalThreadPool;
	}

	/**
	 * @return true if use logical thread pool to control invoking the service
	 */
	public final boolean isUseLogicalThreadPool() {
		return useLogicalThreadPool;
	}

	@Override
	protected ResultHolder<S, T> handleASyncServiceCall() {
		if (isUseLogicalThreadPool()) {
			return handleASyncLogicalThreadPoolCall();
		} else {
			return super.handleASyncServiceCall();
		}
	}

	/**
	 * Handle invoking the service with a logical thread pool.
	 *
	 * @return the result if already cached
	 */
	protected ResultHolder<S, T> handleASyncLogicalThreadPoolCall() {
		LogicalThreadPoolController ctrl = getThreadController();
		// Check thread available
		if (ctrl.acquireThread()) {
			ResultHolder<S, T> result = null;
			try {
				result = super.handleASyncServiceCall();
			} finally {
				if (isServiceRunning()) {
					// If we have a result and are not currently polling (no need to hold the thread)
					if (result != null && getPollingStatus() != PollingStatus.PROCESSING) {
						ctrl.releaseThread();
					}
				} else {
					// Service did not start for some reason
					ctrl.releaseThread();
				}
			}
			return result;
		} else {
			// No thread available
			LOG.info("Could not start service in logical pool. Will try next poll.");
			return null;
		}
	}

	@Override
	protected void handleStoppedPolling() {
		if (isServiceRunning() && isUseLogicalThreadPool()) {
			getThreadController().releaseThread();
		}
		super.handleStoppedPolling();
	}

	@Override
	protected void handleTimeoutPolling() {
		if (isServiceRunning() && isUseLogicalThreadPool()) {
			// Even though the Thread could still be running. We will treat it as finished.
			getThreadController().releaseThread();
		}
		super.handleTimeoutPolling();
	}

	/**
	 * @return the logical thread pool controller
	 */
	protected LogicalThreadPoolController getThreadController() {
		LogicalThreadPoolController ctrl = WebUtilities.getAncestorOfClass(LogicalThreadPoolController.class, this);
		if (ctrl == null) {
			throw new IllegalStateException("No logical thread pool controller found.");
		}
		return ctrl;
	}

}
