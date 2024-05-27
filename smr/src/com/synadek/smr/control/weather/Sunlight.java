/**
 * Sunlight.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import java.time.Duration;

/**
 * Report the amount of sunlight reaching the vessel.
 */
public interface Sunlight {

	/**
	 * Return the amount of light reaching the vessel.
	 * 
	 * @return the light level in watts per square meter (W/m2)
	 */
	float solarPower();

	/**
	 * Return the accumulated light energy reaching the vessel over a recent period
	 * of time.
	 * 
	 * @param interval the time period in minutes (max 1,440 equivalent to 24
	 *                 hours).
	 * @return the energy in watt-seconds (joules) per square meter (Ws/m2)
	 */
	float solarEnergy(Duration duration);

	/**
	 * Get the sunrise time for a specified location.
	 *
	 * @param lon           longitude
	 * @param lat           latitude
	 * @param zone          time zone
	 * @param julianCentury julian century
	 * @return the sunrise time
	 */
	double getSunrise(double lon, double lat, double zone, double julianCentury);

	/**
	 * Get the sunset time for a specified location.
	 *
	 * @param lon           longitude
	 * @param lat           latitude
	 * @param zone          time zone
	 * @param julianCentury julian century
	 * @return the sunset time
	 */
	double getSunset(double lon, double lat, double zone, double julianCentury);

	/**
	 * Get the sunlight duration for a specified location.
	 *
	 * @param lat           latitude
	 * @param zone          time zone
	 * @param julianCentury julian century
	 * @return the sunlight duration
	 */
	double getSunlightDuration(double lat, double julianCentury);
}
