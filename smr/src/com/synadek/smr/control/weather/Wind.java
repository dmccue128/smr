/**
 * Wind.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import java.util.Locale;

/**
 * Reports wind conditions surrounding the vessel.
 */
public interface Wind {

  /**
   * Average wind direction.
   *
   * @return average wind direction as compass direction (0-359).
   */
  double getWindDirectionAvg();

  /**
   * Standard deviation on wind direction samples.
   *
   * @return the standard deviation
   */
  double getWindDirectionStdDev();

  /**
   * Average wind speed.
   *
   * @return wind speed in m/s.
   */
  double getWindSpeedAvg();

  /**
   * Standard deviation of wind speed samples.
   *
   * @return the standard deviation
   */
  double getWindSpeedStdDev();

  /**
   * Wind speed range according to Beaufort scale.
   *
   * @return wind speed characterization according to Beaufort scale.
   */
  int getWindSpeedBeaufort();

  /**
   * Wind speed label according to Beaufort scale.
   *
   * @param locale
   *          is the locale in which to translate the label
   * @return wind speed label
   */
  String getWindSpeedLabel(Locale locale);

  /**
   * Wind speed description according to Beaufort scale.
   *
   * @param locale
   *          is the locale in which to translate the label
   * @return wind speed description
   */
  String getWindSpeedDescription(Locale locale);

  /**
   * Get time period over which wind measurements are averaged.
   *
   * @return the time period in seconds
   */
  int getWindSampleTime();

  /**
   * Set time period over which to average wind measurements.
   *
   * @param samplePeriodSeconds
   *          is the number of seconds of data over which to compute averages
   */
  void setWindSampleTime(int samplePeriodSeconds);
}
