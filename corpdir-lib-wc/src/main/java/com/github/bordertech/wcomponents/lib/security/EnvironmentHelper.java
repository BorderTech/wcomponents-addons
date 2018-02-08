package com.github.bordertech.wcomponents.lib.security;

import com.github.bordertech.wcomponents.Environment;
import com.github.bordertech.wcomponents.util.Config;

/**
 * Environment helper to make the environment details available on the thread.
 * <p>
 * Allows relative base URLs to be used so the URLs can be used in different contexts without changing.
 * </p>
 *
 * @author jonathan
 */
public final class EnvironmentHelper {

	private static final String SECURE_SERVLET_PATH = Config.getInstance().getString("wclib.secure.cardmgr.servlet.path", "/");

	private static final ThreadLocal<Environment> THREAD_LOCAL_ENVIRONMENT = new ThreadLocal<>();

	/**
	 * Private constructor.
	 */
	private EnvironmentHelper() {
	}

	/**
	 * Clear the details of the current Environment on the thread.
	 */
	public static void clearDetails() {
		THREAD_LOCAL_ENVIRONMENT.remove();
	}

	/**
	 * @param env the current environment details
	 */
	public static void setEnvironment(final Environment env) {
		THREAD_LOCAL_ENVIRONMENT.set(env);
	}

	/**
	 * Get the base URL at which this web application is hosted.
	 * <p>
	 * Implementations ensure that this method returns a URL WITHOUT a trailing slash, as in above example.
	 * </p>
	 *
	 * @return the base url at which this web application is hosted.
	 */
	public static String getBaseUrl() {
		Environment env = THREAD_LOCAL_ENVIRONMENT.get();
		return env == null ? "" : env.getBaseUrl();
	}

	/**
	 * Helper method to append the base URL.
	 *
	 * @param relativeUrl the relative URL
	 * @return the URL with the base URL appended
	 */
	public static String prefixBaseUrl(final String relativeUrl) {
		return prefixUrl(getBaseUrl(), relativeUrl);
	}

	/**
	 * Get the path of the secure servlet.
	 * <p>
	 * Implementations ensure that this method returns a URL WITH a trailing slash.
	 * </p>
	 *
	 * @return the secure servlet path at which this web application is hosted.
	 */
	public static String getSecureServletPath() {
		return SECURE_SERVLET_PATH;
	}

	/**
	 * Helper method to append the secure servlet path.
	 *
	 * @param relativeUrl the relative URL to prefix with the servlet path
	 * @return the relative URL with the sevlet path
	 */
	public static String prefixSecureServletPath(final String relativeUrl) {
		return prefixUrl(getSecureServletPath(), relativeUrl);
	}

	/**
	 *
	 * @param prefix the URL prefix
	 * @param relativeUrl the relative URL to append
	 * @return a URL with the prefix attached
	 */
	private static String prefixUrl(final String prefix, final String relativeUrl) {
		if (prefix == null) {
			throw new IllegalArgumentException("Prefix cannot be null");
		}
		if (relativeUrl == null) {
			throw new IllegalArgumentException("Relative URL cannot be null");
		}

		boolean prefixHas = prefix.endsWith("/");
		boolean relativeHas = relativeUrl.startsWith("/");

		StringBuilder result = new StringBuilder();
		result.append(prefix);
		// Both have a dash
		if (prefixHas && relativeHas) {
			// Remove dash
			result.append(relativeUrl.substring(1));
		} else if (!prefixHas && !relativeHas) {
			// Add a dash
			result.append("/");
			result.append(relativeUrl);
		} else {
			result.append(relativeUrl);
		}
		return result.toString();
	}

}
