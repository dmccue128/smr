/**
 * Temperature.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

/**
 * Reports air and water temperature around the vessel.
 */
public interface Temperature {

  /**
   * Report the temperature of the air near the vessel.
   *
   * @return the temperature in degrees Celsius
   */
  float airTemperature();

  /**
   * Report the water temperature near the surface around the vessel.
   *
   * @return the temperature in degrees Celsius
   */
  float surfaceTemperature();

}
