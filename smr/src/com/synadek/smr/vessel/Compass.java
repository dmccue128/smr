/**
 * Compass.java
 * 1 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Compass reports the compass heading of the vessel.
 */
public interface Compass extends VesselComponent {

  /**
   * @return the direction (compass heading in degrees 0-359) that the vessel is headed.
   */
  int getDirection();
}
