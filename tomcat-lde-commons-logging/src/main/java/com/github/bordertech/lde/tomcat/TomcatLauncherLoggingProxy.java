package com.github.bordertech.lde.tomcat;

/**
 * Tomcat Launcher Proxy to help IDEs find the main method and Launch Tomcat.
 */
public final class TomcatLauncherLoggingProxy {

	/**
	 * Launch Tomcat.
	 *
	 * @param args main arguments
	 */
	public static void main(final String[] args) {
		TomcatLauncher.launchTomcat();
	}

	/**
	 * Private constructor.
	 */
	private TomcatLauncherLoggingProxy() {
	}

}
