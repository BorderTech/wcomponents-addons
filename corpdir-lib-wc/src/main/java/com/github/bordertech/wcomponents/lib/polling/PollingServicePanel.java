package com.github.bordertech.wcomponents.lib.polling;

import com.github.bordertech.didums.Didums;
import com.github.bordertech.taskmanager.service.ResultHolder;
import com.github.bordertech.taskmanager.service.ServiceAction;
import com.github.bordertech.taskmanager.service.ServiceException;
import com.github.bordertech.taskmanager.service.ServiceHelper;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.lib.common.WDiv;
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
 * Note - If {@link #isHoldCachedResult()} is true, then a generated cache key is used each time and the cache cleared
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
	public void setServiceCacheKey(final String key) {
		getOrCreateComponentModel().cacheKey = key;
	}

	@Override
	public boolean isHoldCachedResult() {
		return getComponentModel().holdCachedResult;
	}

	@Override
	public void setHoldCachedResult(final boolean holdCachedResult) {
		getOrCreateComponentModel().holdCachedResult = holdCachedResult;
	}

	@Override
	public ResultHolder<S, T> getServiceResult() {
		return getComponentModel().serviceResult;
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
		handleClearResults();
		super.doRefreshContent();
	}

	@Override
	public void doManuallyLoadResult(final S criteria, final T result) {
		if (result == null || criteria == null) {
			return;
		}
		getContentHolder().reset();
		handleClearResults();
		getStartButton().setVisible(false);
		setServiceCriteria(criteria);
		handleResult(new ResultHolder(criteria, result));
	}

	@Override
	protected void handleStoppedPolling() {
		String key = getServiceCacheKey();
		ResultHolder result = getServiceCache().get(key);
		if (result == null) {
			result = new ResultHolder(key, new IllegalStateException("Result has expired so service result not available"));
		}
		handleResult(result);
	}

	@Override
	protected boolean checkForStopPolling() {
		String key = getServiceCacheKey();
		try {
			if (SERVICE_HELPER.checkASyncResult(getServiceCache(), key) != null) {
				setPollingStatus(PollingStatus.STOPPED);
			}
		} catch (ServiceException e) {
			// Put Exception in the CACHE so it can be retrieved after it stops polling (Will be cleared straight away).
			ResultHolder result = new ResultHolder(key, e);
			getServiceCache().put(key, result);
			setPollingStatus(PollingStatus.STOPPED);
		}
		return super.checkForStopPolling();
	}

	/**
	 * @param serviceResult the service result
	 */
	protected void setServiceResult(final ResultHolder<S, T> serviceResult) {
		getOrCreateComponentModel().serviceResult = serviceResult;
	}

	/**
	 * Start the async service call.
	 *
	 * @return the result if already cached
	 */
	protected ResultHolder<S, T> handleASyncServiceCall() {

		// Clear result
		handleClearResults();

		// Check we have a service action
		ServiceAction action = getServiceAction();
		if (action == null) {
			throw new IllegalStateException("No service action provided for polling.");
		}

		// Generate a cache key (if needed)
		if (!isHoldCachedResult()) {
			setServiceCacheKey(generateCacheKey());
		}

		// Check we have a cache key
		String key = getServiceCacheKey();
		if (key == null) {
			throw new IllegalStateException("No cache key provided for polling.");
		}

		// Start Service action (will return result if already cached)
		return SERVICE_HELPER.handleAsyncServiceCall(getServiceCache(), key, getServiceCriteria(), action);
	}

	/**
	 * Clear previous results.
	 */
	protected void handleClearResults() {
		setServiceResult(null);
		if (!isHoldCachedResult() && getServiceCacheKey() != null) {
			String key = getServiceCacheKey();
			setServiceCacheKey(null);
			getServiceCache().remove(key);
		}
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
		// Save the result
		setServiceResult(resultHolder);
		// Check if we need to clear the cache
		if (!isHoldCachedResult()) {
			getServiceCache().remove(getServiceCacheKey());
		}
		if (resultHolder.isResult()) {
			// Successful Result
			T result = resultHolder.getResult();
			handleSuccessfulResult(result);
		} else {
			// Exception message
			Exception excp = resultHolder.getException();
			handleExceptionResult(excp);
			LOG.error("Error loading data. " + excp.getMessage());
		}
	}

	/**
	 * Handle an exception occurred.
	 *
	 * @param excp the exception that occurred
	 */
	protected void handleExceptionResult(final Exception excp) {
		handleErrorMessage(excp.getMessage());
		doShowRetry();
	}

	/**
	 * Handle the successful result. Default behaviour is to set the result as the bean for the content.
	 *
	 * @param result the polling action result
	 */
	protected void handleSuccessfulResult(final T result) {
		// Set the result as the bean
		getContentHolder().setBean(result);
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

		private boolean holdCachedResult = true;

		private ServiceAction<S, T> serviceAction;

		private ResultHolder<S, T> serviceResult;
	}

}
