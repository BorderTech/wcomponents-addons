package com.github.bordertech.wcomponents.addons.polling;

import com.github.bordertech.didums.Didums;
import com.github.bordertech.taskmaster.RejectedTaskException;
import com.github.bordertech.taskmaster.service.ResultHolder;
import com.github.bordertech.taskmaster.service.ServiceAction;
import com.github.bordertech.taskmaster.service.ServiceHelper;
import com.github.bordertech.wcomponents.BeanProvider;
import com.github.bordertech.wcomponents.BeanProviderBound;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.addons.common.WDiv;
import java.io.Serializable;
import java.util.UUID;
import javax.cache.Cache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This panel is used to load data via a threaded service action and polling AJAX.
 * <p>
 * Expects the following to be set before polling:-
 * </p>
 * <ul>
 * <li>{@link #setServiceCriteria(criteria)} to provide the service criteria</li>
 * <li>{@link #setServiceCacheKey(key)} to provide the cache key</li>
 * <li>{@link #setServiceAction(action)} to provide the service action</li>
 * </ul>
 * <p>
 * Note - If {@link #isUseCachedResult()} is true, then a generated cache key is used each time and the cache cleared
 * after the result is processed.
 * </p>
 * <p>
 * The successful polling result will be set as the bean available to the panel. The content of the panel will only be
 * displayed if the polling action was successful. If the polling action fails, then the error message will be displayed
 * along with a retry button.
 * </p>
 * <p>
 *
 * </p>
 * <p>
 * Methods commonly overridden:-
 * </p>
 * <ul>
 * <li>{@link #getServiceCacheKey()} - provides the cache key used for the service result.</li>
 * <li>{@link #handleInitResultContent(Request)} - init the result content on successful service call.</li>
 * <li>{@link #handleInitPollingPanel(Request) } - init the polling panel.</li>
 * </ul>
 *
 * @param <S> the polling criteria type
 * @param <T> the polling result type
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class PollingServicePanel<S extends Serializable, T extends Serializable> extends PollingPanel implements PollableService<S, T> {

	private static final Log LOG = LogFactory.getLog(PollingServicePanel.class);

	private static final ServiceHelper SERVICE_HELPER = Didums.getService(ServiceHelper.class);

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

	@Override
	public final WDiv getContentResultHolder() {
		return contentResultHolder;
	}

	@Override
	public S getServiceCriteria() {
		return getComponentModel().criteria;
	}

	@Override
	public void setServiceCriteria(final S criteria) {
		getOrCreateComponentModel().criteria = criteria;
	}

	@Override
	public ServiceAction<S, T> getServiceAction() {
		return getComponentModel().serviceAction;
	}

	@Override
	public void setServiceAction(final ServiceAction<S, T> serviceAction) {
		getOrCreateComponentModel().serviceAction = serviceAction;
	}

	@Override
	public String getServiceCacheKey() {
		return getComponentModel().cacheKey;
	}

	@Override
	public void setServiceCacheKey(final String cacheKey) {
		getOrCreateComponentModel().cacheKey = cacheKey;
	}

	@Override
	public void setServiceThreadPool(final String threadPool) {
		getOrCreateComponentModel().threadPool = threadPool;
	}

	@Override
	public String getServiceThreadPool() {
		return getComponentModel().threadPool;
	}

	@Override
	public boolean isUseCachedResult() {
		return getComponentModel().useCachedResult;
	}

	@Override
	public void setUseCachedResult(final boolean useCachedResult) {
		getOrCreateComponentModel().useCachedResult = useCachedResult;
	}

	@Override
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
	@Override
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
			if (key != null) {
				getServiceCache().remove(key);
			}
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
		// Flag service as not running yet
		setServiceRunning(false);
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

	@Override
	public void doManuallyLoadResult(final S criteria, final T result) {
		// Check we have a cache key if using a cached service response
		if (isUseCachedResult() && getServiceCacheKey() == null) {
			throw new IllegalStateException("No cache key provided for the result to be held against.");
		}
		getContentHolder().reset();
		getStartButton().setVisible(false);
		setServiceCriteria(criteria);
		ResultHolder resultHolder = new ResultHolder(criteria, result);
		handleSaveServiceResult(resultHolder);
		handleResult(resultHolder);
	}

	@Override
	protected boolean checkForStopPolling() {
		String key = getServiceCacheKey();
		ResultHolder result;
		if (isServiceRunning()) {
			// Service was started so check for result
			try {
				result = SERVICE_HELPER.checkASyncResult(getServiceCache(), key);
			} catch (Exception e) {
				result = new ResultHolder(key, e);
			}
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
		// Reset service flag
		setServiceRunning(false);
		// Process result
		ResultHolder result = getServiceResult();
		if (result == null) {
			throw new IllegalStateException("Service result is not available.");
		}
		handleResult(result);
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
	 * @return the result if already cached
	 */
	protected ResultHolder<S, T> handleASyncServiceCall() {

		// Check we have a service action
		ServiceAction action = getServiceAction();
		if (action == null) {
			throw new IllegalStateException("No service action provided for polling.");
		}

		// Generate a cache key so the polling result can be held
		if (!isUseCachedResult()) {
			//Generate a key
			setServiceCacheKey(generateCacheKey());
			// Clear previous result
			setServiceResult(null);
		}

		// Check we have a cache key
		String key = getServiceCacheKey();
		if (key == null) {
			throw new IllegalStateException("No cache key provided for polling.");
		}

		// Start Service action (will return result if already cached)
		try {
			ResultHolder result = SERVICE_HELPER.handleAsyncServiceCall(getServiceCache(), key, getServiceCriteria(), action);
			setServiceRunning(true);
			return result;
		} catch (RejectedTaskException e) {
			// Could not start service (usually no threads available). Try and start on the next poll.
			LOG.info("Could not start service in pool [" + getServiceThreadPool() + "]. Will try next poll.");
			setServiceRunning(false);
			return null;
		}
	}

	protected boolean isServiceRunning() {
		return getComponentModel().serviceRunning;
	}

	protected void setServiceRunning(final boolean serviceRunning) {
		getOrCreateComponentModel().serviceRunning = serviceRunning;
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
	 * Handle an exception occurred.
	 *
	 * @param excp the exception that occurred
	 */
	protected void handleResultException(final Exception excp) {
		handleErrorMessage(excp.getMessage());
		doShowRetry();
	}

	/**
	 * Handle the successful result.
	 *
	 * @param result the service result
	 */
	protected void handleResultSuccessful(final T result) {
		getContentResultHolder().setVisible(true);
	}

	/**
	 * @return a key to uniquely identify a service request
	 */
	protected String generateCacheKey() {
		String key = "polling=" + UUID.randomUUID().toString();
		return key;
	}

	/**
	 * @return the service cache instance
	 */
	protected Cache<String, ResultHolder> getServiceCache() {
		return SERVICE_HELPER.getDefaultResultHolderCache();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PollingServiceModel<S, T> newComponentModel() {
		return new PollingServiceModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PollingServiceModel<S, T> getOrCreateComponentModel() {
		return (PollingServiceModel) super.getOrCreateComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
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
	public static class PollingServiceModel<S, T> extends PollingModel {

		private S criteria;

		private String cacheKey;

		private String threadPool;

		private boolean useCachedResult = true;

		private boolean serviceRunning;

		private ServiceAction<S, T> serviceAction;

		private ResultHolder<S, T> serviceResult;
	}

}
