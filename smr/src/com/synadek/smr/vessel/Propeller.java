/**
 * Propeller.java
 * 1 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Propeller speed and direction.
 */
public interface Propeller extends VesselComponent {

  /**
   * Define propeller rotational directions.
   */
  enum propellerDirection {
    /**
     * Propelling the vessel forward.
     */
    FORWARD,
    /**
     * Moving backward.
     */
    REVERSE,
    /**
     * No propulsion -- no (intentional) movement.
     */
    STOPPED
  }

  /**
   * Get propeller speed in revolutions per minute (rpm).
   * 
   * @return the propeller speed
   */
  int getSpeed();

  /**
   * Set propeller speed.
   * 
   * @param newRpm
   *          is the new speed setting in revolutions per minute. Note: setting speed to zero causes
   *          propeller direction to be set to STOPPED.
   */
  void setSpeed(int newRpm);

  /**
   * Get propeller direction (forward, reverse, or stopped).
   * 
   * @return direction
   */
  propellerDirection getDirection();

  /**
   * Set propeller direction.
   * 
   * @param newDirection
   *          is the new direction for the propeller. Note that setting propeller direction to
   *          STOPPED causes propeller speed to go to zero.
   */
  void setDirection(propellerDirection newDirection);
}
