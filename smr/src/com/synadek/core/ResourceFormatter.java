/**
 * ResourceFormatter.java
 * 17 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilities for accessing and formatting resources.
 */
public class ResourceFormatter {

	/**
	 * base name for resource bundle.
	 */
	public static final String RESOURCE_BUNDLE = "com.smr.resources.MyResources";

	/**
	 * Error string returned for failed resource lookups.
	 */
	public static final String RESOURCE_ERROR = "Resource error";

	/**
	 * Acquire a reference to the application logger.
	 */
	private static Logger log = LogManager.getLogger(ResourceFormatter.class.getPackage().getName());

	/**
	 * Default constructor.
	 */
	ResourceFormatter() {
		/*
		 * No initialization necessary.
		 */
	}

	/**
	 * Format a message based on a pattern and the preferred user locale.
	 * 
	 * @param args        is the array of arguments to apply to the pattern
	 * @param resourceKey is the key to find the pattern to use for the formatting
	 * @param locale      is the locale for which the resources are sought
	 * @return the localized message
	 */
	public static String formatMessage(final Object[] args, final String resourceKey, final Locale locale) {
		String result;
		final String pattern = getMessage(resourceKey, locale);
		if (pattern == null || pattern.equals(RESOURCE_ERROR)) {
			return RESOURCE_ERROR;
		}

		try {
			final MessageFormat formatter = new MessageFormat(pattern, locale);
			result = formatter.format(args);
			if (result == null) {
				log.error("Formatting of resourceKey, {}, with pattern, {}, returned null", resourceKey, pattern);
				result = RESOURCE_ERROR;
			}
		} catch (NullPointerException n) {
			log.error("Null pointer exception formatting message: {}", formatDebugMsg(args, resourceKey, pattern));
			result = RESOURCE_ERROR;
		} catch (IllegalArgumentException e) {
			log.error("Illegal message format request using pattern, {}: {}", pattern, e.getMessage());
			// pattern should always be valid -- print a stack trace to assist debugging
			e.printStackTrace();
			result = RESOURCE_ERROR;
		}

		return result;
	}

	/**
	 * Return a message based on preferred user locale.
	 * 
	 * @param resourceKey is the key to find the pattern to use for the formatting
	 * @param locale      is the locale for which the resources are sought
	 * @return localized message or RESOURCE_ERROR if resource not found
	 */
	public static String getMessage(final String resourceKey, final Locale locale) {
		String result;
		try {
			result = getSessionResources(locale).getString(resourceKey);
			if (result == null) {
				log.error("Lookup of resourceKey, {}, returned null", resourceKey);
				result = RESOURCE_ERROR;
			}
		} catch (NullPointerException n) {
			log.error("Failed attempt to look up a null resource key: {}", n.getMessage());
			// key should never be null -- print a stack trace to assist debugging
			n.printStackTrace();
			result = RESOURCE_ERROR;
		} catch (MissingResourceException m) {
			log.error("Failed attempt to look up resource key, {}: {}", resourceKey, m.getMessage());
			result = RESOURCE_ERROR;
		} catch (ClassCastException c) {
			log.error("Non-string result from look up of resource key, {}: {}", resourceKey, c.getMessage());
			// result should always be a string -- print a stack trace to assist
			// in debugging
			c.printStackTrace();
			result = RESOURCE_ERROR;
		}
		return result;
	}

	/**
	 * format error message for debugging purposes.
	 * 
	 * @param args        is the array of arguments to apply to the pattern
	 * @param resourceKey is the key to find the pattern to use for the formatting
	 * @param pattern     is the retrieved message pattern from the resource file
	 * @return a debug message string displaying information about the error
	 */
	private static String formatDebugMsg(final Object[] args, final String resourceKey, final String pattern) {
		final StringBuffer sb = new StringBuffer();
		if (args != null) {
			sb.append(args.length);
			sb.append(" arguments.");

			int idx = 0;
			for (Object o : args) {
				if (o == null) {
					sb.append(" Argument ");
					sb.append(idx);
					sb.append(" is null, ");
				}
				idx++;
			}
		} else {
			sb.append("Null argument array");
		}

		if (resourceKey == null) {
			sb.append("Resource key is null");
		} else {
			sb.append(" resource key ");
			sb.append(resourceKey);
			sb.append(" returned pattern ");
			sb.append(pattern);
		}

		return sb.toString();
	}

	/**
	 * getSessionResources locates the most appropriate resource bundle in the pool
	 * to use for localizing messages for the user interface.
	 * 
	 * @param locale is the locale for which the resources are sought
	 * @return a resource bundle
	 */
	private static ResourceBundle getSessionResources(final Locale locale) {

		// If undefined, return default
		if (locale == null) {
			return ResourceBundle.getBundle(RESOURCE_BUNDLE);
		}

		return ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
	}
}
