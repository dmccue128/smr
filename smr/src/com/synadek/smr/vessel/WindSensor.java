/**
 * WindDirection.java
 * 1 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * WindSensor detects and reports wind information such as wind speed or direction.
 */
public interface WindSensor extends VesselComponent {

  /**
   * @return wind speed in km/hour relative to the vessel.
   */
  float getVesselRelativeWindSpeed();

  /**
   * @return wind direction in degrees (0-359) relative to the bow of the vessel.
   */
  int getVesselRelativeWindDirection();

}
