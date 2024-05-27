/**
 * Rudder.java
 * 1 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Rudder management.
 */
public interface Rudder extends VesselComponent {

  /**
   * Get the direction (degrees to starboard) of the rudder. That is, a "straight" rudder returns
   * zero. A rudder angled ten degrees starboard returns ten. A rudder angled ten degrees to port
   * returns -10.
   * 
   * @return the direction
   */
  int getDirection();

  /**
   * Set rudder direction using the encoding described above for getRudderDirection.
   * 
   * @param degrees
   *          is the number of degrees to angle the rudder to port or starboard.
   */
  void setDirection(int degrees);
}
