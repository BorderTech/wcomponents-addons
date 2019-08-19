package com.github.bordertech.wcomponents.addons.polling;

import com.github.bordertech.taskmaster.TaskFuture;
import com.github.bordertech.taskmaster.service.ResultHolder;
import com.github.bordertech.taskmaster.service.ServiceAction;
import com.github.bordertech.taskmaster.service.ServiceHelper;
import com.github.bordertech.taskmaster.service.exception.RejectedServiceException;
import com.github.bordertech.taskmaster.service.exception.ServiceException;
import com.github.bordertech.taskmaster.service.impl.ResultHolderDefault;
import com.github.bordertech.taskmaster.service.util.ServiceCacheUtil;
import com.github.bordertech.wcomponents.BeanProvider;
import com.github.bordertech.wcomponents.BeanProviderBound;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.addons.common.WDiv;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import javax.cache.Cache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This panel is used to load data via a threaded service action and polling AJAX.
 * <p>
 * Expects the following to be set before polling:-
 * </p>
 * <ul>
 * <li>{@link #setServiceCriteria(java.io.Serializable)} to provide the service criteria</li>
 * <li>{@link #setServiceCacheKey(java.lang.String) (key)} to provide the cache key</li>
 * <li>{@link #setServiceAction(com.github.bordertech.taskmaster.service.ServiceAction)} to provide the service
 * action</li>
 * </ul>
 * <p>
 * The successful polling result will be set as the bean available to the panel. The content of the panel will only be
 * displayed if the polling action was successful. If the polling action fails, then the error message will be displayed
 * along with a retry button.
 * </p>
 * <p>
 * Methods commonly overridden:-
 * </p>
 * <ul>
 * <li>{@link #getServiceCacheKey()} - provides the cache key used for the service result.</li>
 * <li>{@link #handleInitResultContent(com.github.bordertech.wcomponents.Request)} - init the result content on
 * successful service call.</li>
 * <li>{@link #handleInitPollingPanel(com.github.bordertech.wcomponents.Request) } - init the polling panel.</li>
 * </ul>
 *
 * @param <S> the polling criteria type
 * @param <T> the polling result type
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class PollingServicePanel<S extends Serializable, T extends Serializable> extends PollingPanel {

	private static final Log LOG = LogFactory.getLog(PollingServicePanel.class);

	private final WDiv contentResultHolder = new WDiv() {
		@Override
		protected void preparePaintComponent(final Request request) {
			super.preparePaintComponent(request);
			if (!isInitialised()) {
				handleInitResultContent(request);
				setInitialised(true);
			}
		}
	};

	/**
	 * Default constructor.
	 */
	public PollingServicePanel() {
		this(174);
	}

	/**
	 * Construct polling panel.
	 *
	 * @param delay the AJAX polling delay
	 */
	public PollingServicePanel(final int delay) {
		super(delay);
		getContentHolder().add(contentResultHolder);
		contentResultHolder.setVisible(false);

		// The content holder will use the "result" from the result holder
		getContentHolder().setBeanProperty("result");

		// Set a BEAN Provider on the content that retrieves the service result
		getContentHolder().setBeanProvider(new BeanProvider() {
			@Override
			public Object getBean(final BeanProviderBound bound) {
				return getServiceResult();
			}
		});
	}

	/**
	 * @return the container holding the result components
	 */
	public final WDiv getContentResultHolder() {
		return contentResultHolder;
	}

	/**
	 * @return the service criteria to use in the service call
	 */
	public S getServiceCriteria() {
		return getComponentModel().criteria;
	}

	/**
	 * @param criteria the service criteria to use in the service call
	 */
	public void setServiceCriteria(final S criteria) {
		getOrCreateComponentModel().criteria = criteria;
	}

	/**
	 * @return the service action to use for polling
	 */
	public ServiceAction<S, T> getServiceAction() {
		return getComponentModel().serviceAction;
	}

	/**
	 * @param serviceAction the service action to use for polling
	 */
	public void setServiceAction(final ServiceAction<S, T> serviceAction) {
		getOrCreateComponentModel().serviceAction = serviceAction;
	}

	/**
	 * @return the service cache key
	 */
	public String getServiceCacheKey() {
		return getComponentModel().cacheKey;
	}

	/**
	 * @param cacheKey the service cache key
	 */
	public void setServiceCacheKey(final String cacheKey) {
		getOrCreateComponentModel().cacheKey = cacheKey;
	}

	/**
	 * @param threadPool the service thread pool or null for default
	 */
	public void setServiceThreadPool(final String threadPool) {
		getOrCreateComponentModel().threadPool = threadPool;
	}

	/**
	 * @return the service thread pool, or null for default
	 */
	public String getServiceThreadPool() {
		return getComponentModel().threadPool;
	}

	/**
	 * @return true if use the cache to hold the service result
	 */
	public boolean isUseCachedResult() {
		return getComponentModel().useCachedResult;
	}

	/**
	 * @param useCachedResult true if use the cache to hold the service result
	 */
	public void setUseCachedResult(final boolean useCachedResult) {
		getOrCreateComponentModel().useCachedResult = useCachedResult;
	}

	/**
	 * @return the service result, or null if still processing.
	 */
	public ResultHolder<S, T> getServiceResult() {
		if (isUseCachedResult()) {
			ResultHolder resultHolder = getServiceCache().get(getServiceCacheKey());
			if (resultHolder == null && getContentResultHolder().isVisible()) {
				return handleCacheExpired();
			}
			return resultHolder;
		} else {
			return getComponentModel().serviceResult;
		}
	}

	/**
	 * @param serviceResult the service result
	 */
	public void setServiceResult(final ResultHolder<S, T> serviceResult) {
		String key = getServiceCacheKey();
		if (isUseCachedResult()) {
			if (key == null) {
				throw new IllegalStateException("A cache key must be provided for a cached service result");
			}
			if (serviceResult == null) {
				getServiceCache().remove(key);
			} else {
				getServiceCache().put(getServiceCacheKey(), serviceResult);
			}
		} else {
			getOrCreateComponentModel().serviceResult = serviceResult;
		}
	}

	/**
	 *
	 * @return the result for an expired cache item
	 */
	protected ResultHolder<S, T> handleCacheExpired() {
		// TODO Maybe try and reload if expired
		throw new IllegalStateException("Cache entry has expired");
	}

	@Override
	public void doStartPolling() {
		// Check not started
		if (getPollingStatus() == PollingStatus.PROCESSING) {
			return;
		}
		// Start the service call
		ResultHolder<S, T> result = handleASyncServiceCall();
		if (result == null) {
			super.doStartPolling();
		} else {
			handleResult(result);
		}
	}

	@Override
	public void doRefreshContent() {
		handleClearServiceCache();
		// Clear the result
		setServiceResult(null);
		super.doRefreshContent();
	}

	/**
	 * Manually set the criteria and the result.
	 *
	 * @param criteria the criteria
	 * @param result the result
	 */
	public void doManuallyLoadResult(final S criteria, final T result) {
		// Check we have a cache key if using a cached service response
		if (isUseCachedResult() && getServiceCacheKey() == null) {
			throw new IllegalStateException("No cache key provided for the result to be held against.");
		}
		getContentHolder().reset();
		getStartButton().setVisible(false);
		setServiceCriteria(criteria);
		ResultHolder resultHolder = new ResultHolderDefault(criteria, result);
		handleSaveServiceResult(resultHolder);
		handleResult(resultHolder);
	}

	@Override
	protected boolean checkForStopPolling() {
		ResultHolder result;
		if (isServiceRunning()) {
			// Check if Service Finished
			result = handleAsyncCheckProcess();
		} else {
			// Try and start service (usually means no threads were available)
			result = handleASyncServiceCall();
			if (isServiceRunning()) {
				// Started service successfully.
				LOG.info("Successfully started service on ajax poll in pool [" + getServiceThreadPool() + "].");
			}
		}
		// If have result, stop polling
		if (result != null) {
			setPollingStatus(PollingStatus.STOPPED);
			handleSaveServiceResult(result);
		}
		return super.checkForStopPolling();
	}

	@Override
	protected void handleStoppedPolling() {
		super.handleStoppedPolling();
		// Make sure the task is cleared
		clearTaskFuture();
		// Process result
		ResultHolder result = getServiceResult();
		if (result == null) {
			// This state should not happen
			throw new IllegalStateException("Service result is not available.");
		}
		handleResult(result);
	}

	@Override
	protected void handleTimeoutPolling() {
		super.handleTimeoutPolling();
		// Make sure the task is cleared
		clearTaskFuture();
	}

	/**
	 * Save the service result locally or keep it in the cache.
	 *
	 * @param resultHolder the service result
	 */
	protected void handleSaveServiceResult(final ResultHolder<S, T> resultHolder) {
		setServiceResult(resultHolder);
	}

	/**
	 * Start the async service call.
	 *
	 * @return the result if already cached, or null
	 */
	protected ResultHolder<S, T> handleASyncServiceCall() {

		// Check we have a service action
		ServiceAction action = getServiceAction();
		if (action == null) {
			throw new IllegalStateException("No service action provided for polling.");
		}

		// Check we have a cache key
		if (isUseCachedResult() && getServiceCacheKey() == null) {
			throw new IllegalStateException("No cache key provided for polling.");
		}

		// Clear previous task (if any)
		clearTaskFuture();

		// Start Service action.
		try {
			TaskFuture<ResultHolder<S, T>> future;
			if (isUseCachedResult()) {
				// Cached service call (and cache exceptions)
				future = ServiceHelper.submitAsync(getServiceCriteria(), getServiceAction(), getServiceCache(), getServiceCacheKey(), getServiceThreadPool(), true);
			} else {
				// Clear current result
				setServiceResult(null);
				// Service call with no caching
				future = ServiceHelper.submitAsync(getServiceCriteria(), getServiceAction(), getServiceThreadPool());
			}
			if (future.isDone()) {
				// Result might have been cached so return it immediately
				return extractResultFromTask(future);
			}
			setTaskFuture(future);
		} catch (ServiceException e) {
			clearTaskFuture();
			return new ResultHolderDefault(e);
		} catch (RejectedServiceException e) {
			// Could not start service (usually no threads available). Try and start on the next poll.
			LOG.info("Could not start service in pool [" + getServiceThreadPool() + "]. Will try next poll.", e);
		}
		return null;
	}

	/**
	 * Check if the service has finished.
	 *
	 * @return the result or null if still running
	 */
	protected ResultHolder<S, T> handleAsyncCheckProcess() {
		TaskFuture<ResultHolder<S, T>> future = getTaskFuture();
		if (future == null) {
			throw new IllegalStateException("No future set for async processing");
		}
		if (future.isDone()) {
			// Clear the task
			clearTaskFuture();
			// Extract the result form the future
			try {
				return extractResultFromTask(future);
			} catch (ServiceException e) {
				return new ResultHolderDefault(e);
			}
		}
		return null;
	}

	/**
	 * Extract the result form the future.
	 *
	 * @param future the task future
	 * @return the result holder
	 * @throws ServiceException exception in processing
	 */
	protected ResultHolder<S, T> extractResultFromTask(final TaskFuture<ResultHolder<S, T>> future) throws ServiceException {
		if (future.isDone()) {
			try {
				return future.get();
			} catch (ExecutionException | InterruptedException e) {
				// FIXME Handle interruption correctly
				throw new ServiceException("Error geting result from future", e);
			}
		}
		return null;
	}

	/**
	 * @return true if service is running
	 */
	protected boolean isServiceRunning() {
		return getTaskFuture() != null;
	}

	/**
	 * @return the task future running the process or null
	 */
	protected TaskFuture<ResultHolder<S, T>> getTaskFuture() {
		return getComponentModel().taskFuture;
	}

	/**
	 * @param future the task future running the service call
	 */
	protected void setTaskFuture(final TaskFuture<ResultHolder<S, T>> future) {
		getOrCreateComponentModel().taskFuture = future;
	}

	/**
	 * Clear the current task.
	 */
	protected void clearTaskFuture() {
		TaskFuture current = getTaskFuture();
		// No task to clear
		if (current == null) {
			return;
		}
		// Check if task can be cancelled
		if (!current.isDone()) {
			current.cancel(true);
		}
		setTaskFuture(null);
	}

	/**
	 * Initialise the result content.
	 *
	 * @param request the request being processed
	 */
	protected void handleInitResultContent(final Request request) {
		// Do Nothing
	}

	/**
	 * Clear the result cache if necessary (eg Service Layer).
	 */
	protected void handleClearServiceCache() {
		// Do nothing
	}

	/**
	 * Handle the result from the polling action.
	 *
	 * @param resultHolder the polling action result
	 */
	protected void handleResult(final ResultHolder<S, T> resultHolder) {
		if (resultHolder.isResult()) {
			// Successful Result
			handleResultSuccessful(resultHolder.getResult());
		} else {
			// Exception message
			Exception excp = resultHolder.getException();
			handleResultException(excp);
			LOG.error("Error loading data. " + excp.getMessage());
		}
	}

	/**
	 * Handle the service returned an exception.
	 *
	 * @param excp the exception that occurred
	 */
	protected void handleResultException(final Exception excp) {
		addErrorMessage(excp.getMessage());
		doShowRetry();
	}

	/**
	 * Handle the service had a successful result.
	 *
	 * @param result the service result
	 */
	protected void handleResultSuccessful(final T result) {
		getContentResultHolder().setVisible(true);
	}

	/**
	 * @return the service cache instance
	 */
	protected Cache<String, ResultHolder> getServiceCache() {
		return ServiceCacheUtil.getDefaultResultHolderCache();
	}

	@Override
	protected PollingServiceModel<S, T> newComponentModel() {
		return new PollingServiceModel();
	}

	@Override
	protected PollingServiceModel<S, T> getOrCreateComponentModel() {
		return (PollingServiceModel) super.getOrCreateComponentModel();
	}

	@Override
	protected PollingServiceModel<S, T> getComponentModel() {
		return (PollingServiceModel) super.getComponentModel();
	}

	/**
	 * This model holds the state information.
	 *
	 * @param <S> the criteria type
	 * @param <T> the service action
	 */
	public static class PollingServiceModel<S extends Serializable, T extends Serializable> extends PollingModel {

		private S criteria;

		private String cacheKey;

		private String threadPool;

		private boolean useCachedResult = true;

		private TaskFuture<ResultHolder<S, T>> taskFuture;

		private ServiceAction<S, T> serviceAction;

		private ResultHolder<S, T> serviceResult;
	}

}
