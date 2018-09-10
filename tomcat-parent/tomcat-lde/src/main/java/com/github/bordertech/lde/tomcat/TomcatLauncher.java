package com.github.bordertech.lde.tomcat;

import com.github.bordertech.didums.Didums;

/**
 * Tomcat Launcher Helper Class.
 */
public final class TomcatLauncher {

	private static final TomcatLauncherProvider LAUNCHER;

	static {
		// Check if implementation defined
		if (Didums.hasService(TomcatLauncherProvider.class)) {
			LAUNCHER = Didums.getService(TomcatLauncherProvider.class);
		} else {
			// Default implementation
			LAUNCHER = new TomcatLauncherProvider();
		}
	}

	/**
	 * Private constructor.
	 */
	private TomcatLauncher() {
	}

	/**
	 * Launch Tomcat.
	 */
	public static void launchTomcat() {
		LAUNCHER.launchTomcat();
	}

	/**
	 * Stop Tomcat.
	 */
	public static void stopTomcat() {
		LAUNCHER.stopTomcat();
	}

	/**
	 * @return the TOMCAT launcher provider
	 */
	public static TomcatLauncherProvider getLauncher() {
		return LAUNCHER;
	}

}
