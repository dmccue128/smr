/**
 * Battery.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Electrical battery subsystem.
 */
public interface Battery extends VesselComponent {

  /**
   * Get the battery voltage.
   *
   * @return voltage level of battery subsystem.
   */
  float getVoltage();
}
