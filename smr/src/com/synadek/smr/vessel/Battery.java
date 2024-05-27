/**
 * Battery.java
 * 1 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Electrical battery subsystem.
 */
public interface Battery extends VesselComponent {

  /**
   * @return voltage level of battery subsystem.
   */
  float getVoltage();
}
