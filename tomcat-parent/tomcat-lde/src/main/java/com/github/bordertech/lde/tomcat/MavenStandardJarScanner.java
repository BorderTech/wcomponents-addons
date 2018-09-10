package com.github.bordertech.lde.tomcat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;

/**
 * Include the maven target classes when scanning the class path.
 * <p>
 * This is needed to pick up the classes on the dependant maven project modules as they are above the "webapp" class
 * loader and their annotations are not scanned.
 * </p>
 */
public class MavenStandardJarScanner extends StandardJarScanner {

	@Override
	@SuppressFBWarnings(value = "DMI_COLLECTION_OF_URLS", justification = "The set of URLs is from TOMCAT method")
	protected void doScanClassPath(final JarScanType scanType, final ServletContext context, final JarScannerCallback callback,
			final Set<URL> processedURLs) {
		// Process maven class paths as WebApp
		processURLs(scanType, callback, processedURLs, true, getMavenClassPaths());
		// Process classpath
		super.doScanClassPath(scanType, context, callback, processedURLs);
	}

	/**
	 * @return the maven class paths
	 */
	protected Deque<URL> getMavenClassPaths() {

		Deque<URL> urls = new LinkedList<>();

		String classPath = System.getProperty("java.class.path");
		if (classPath == null || classPath.length() == 0) {
			return urls;
		}

		String[] classPathEntries = classPath.split(File.pathSeparator);
		for (String classPathEntry : classPathEntries) {
			// Check is a maven target/class
			if (!classPathEntry.endsWith("classes")) {
				continue;
			}
			// Convert to URL
			File f = new File(classPathEntry);
			try {
				urls.add(f.toURI().toURL());
			} catch (MalformedURLException e) {
				System.out.println("Bad classpath entry: " + classPathEntry);
			}
		}
		return urls;
	}

}
