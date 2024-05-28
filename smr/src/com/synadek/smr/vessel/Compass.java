/**
 * Compass.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Compass reports the compass heading of the vessel.
 */
public interface Compass extends VesselComponent {

  /**
   * Get the compass direction.
   *
   * @return the direction (compass heading in degrees 0-359) that the vessel is
   *         headed.
   */
  int getDirection();
}
