package com.github.bordertech.lde.tomcat;

/**
 * Tomcat Launcher Proxy to help IDEs find a main method and launch Tomcat.
 */
public final class TomcatLauncherProxy {

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
	private TomcatLauncherProxy() {
	}

}
