/**
 * WindDirection.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * WindSensor detects and reports wind information such as wind speed or
 * direction.
 */
public interface WindSensor extends VesselComponent {

  /**
   * Get the speed of the wind relative to the vessel.
   *
   * @return wind speed in km/hour relative to the vessel.
   */
  float getVesselRelativeWindSpeed();

  /**
   * Get the direction of the wind relative to the vessel.
   *
   * @return wind direction in degrees (0-359) relative to the bow of the
   *         vessel.
   */
  int getVesselRelativeWindDirection();

}
