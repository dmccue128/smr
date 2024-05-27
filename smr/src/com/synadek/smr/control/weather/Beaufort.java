/**
 * WindSpeed.java
 * 16 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import java.util.Locale;

import com.synadek.core.ResourceFormatter;

/**
 * Beaufort Descriptors for wind speed.
 */
public final class Beaufort {

	/**
	 * The maximum level on the Beaufort scale is 12.
	 */
	private static final int BEAUFORT_MAX = 12;

	/**
	 * Top wind speed for each level of the Beaufort scale.
	 */
	private static final double[] beaufortLimits = { 0.2, 1.5, 3.3, 5.4, 7.9, 10.7, 13.8, 17.1, 20.7, 24.4, 28.4,
			32.6 };

	/**
	 * Standard names for each level of the Beaufort scale.
	 */
	private static final String[] beaufortLabels = { "label.calm", "label.lightair", "label.lightbreeze",
			"label.gentlebreeze", "label.moderatebreeze", "label.freshbreeze", "label.strongbreeze", "label.neargale",
			"label.gale", "label.severegale", "label.storm", "label.violentstorm", "label.hurricane", };

	/**
	 * Expanded descriptions of each level of the Beaufort scale.
	 */
	private static final String[] beaufortDescriptions = { "msg.water.calm", "msg.water.lightair",
			"msg.water.lightbreeze", "msg.water.gentlebreeze", "msg.water.moderatebreeze", "msg.water.freshbreeze",
			"msg.water.strongbreeze", "msg.water.neargale", "msg.water.gale", "msg.water.severegale", "msg.water.storm",
			"msg.water.violentstorm", "msg.water.hurricane", };

	/**
	 * Note: Beaufort levels 6-7 warrant one pennant flag. Levels 8-9, two pennants.
	 * Levels 10-11, one storm flag. Level 12 two storm flags.
	 */
	/**
	 * Return the number of pennant flags to be flown for a given Beaufort level.
	 *
	 * @param bf is the Beaufort number
	 */
	public int pennantFlags(int bf) {
		if (bf < 6 || 9 < bf) {
			return 0;
		}
		if (bf < 8) {
			return 1;
		}
		return 2;
	}

	/**
	 * Return the number of storm flags to be flown for a given Beaufort level.
	 *
	 * @param bf is the Beaufort number
	 */
	public int stormFlags(int bf) {
		if (bf < 10) {
			return 0;
		}
		if (bf < 12) {
			return 1;
		}
		return 2;
	}

	/**
	 * Return speed in Beaufort scale (0-12). Note: negative speeds are invalid and
	 * always return zero.
	 * 
	 * @param speed in meters per second
	 * @return speed in Beaufort scale.
	 */
	public static final int index(final double windSpeed) {
		for (int i = 0; i < beaufortLimits.length; i++) {
			if (windSpeed < beaufortLimits[i]) {
				return i;
			}
		}
		// Default to last entry in the table
		return BEAUFORT_MAX;
	}

	/**
	 * Return Beaufort label for wind speed.
	 * 
	 * @param bf     is the Beaufort number
	 * @param locale is the locale in which to translate the message
	 * @return the label or null if an invalid input is supplied
	 */
	public static final String label(final int bf, final Locale locale) {
		if (bf < 0 || bf >= beaufortLabels.length) {
			return null;
		}
		return ResourceFormatter.getMessage(beaufortLabels[bf], locale);
	}

	/**
	 * Return Beaufort description for wind speed.
	 * 
	 * @param bf     is the Beaufort number
	 * @param locale is the locale in which to translate the message
	 * @return the description or null if an invalid input is supplied
	 */
	public static final String description(final int bf, final Locale locale) {
		if (bf < 0 || bf >= beaufortLabels.length) {
			return null;
		}
		return ResourceFormatter.getMessage(beaufortDescriptions[bf], locale);
	}
}
