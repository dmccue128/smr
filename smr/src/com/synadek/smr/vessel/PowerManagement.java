/**
 * Power.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Manage power including vessel power-up, power-down, sleep mode, etc.
 */
public interface PowerManagement extends VesselComponent {

  /**
   * Define potential power states for this vessel.
   */
  enum PowerStatus {
    /**
     * Normal operation - powered-up.
     */
    POWERED_UP,
    /**
     * Powered down completely.
     */
    POWERED_DOWN,
    /**
     * Sleeping -- paused and drawing minimal power, but not completely shut
     * down.
     */
    SLEEPING
  }

  /**
   * Get power status.
   *
   * @return the status
   */
  PowerStatus getStatus();

  /**
   * Power up all vessel subsystems.
   */
  void powerUp();

  /**
   * Power down all vessel subsystems.
   */
  void powerDown();

  /**
   * Maintain essential vessel systems while minimizing power consumption.
   */
  void sleep();

}
