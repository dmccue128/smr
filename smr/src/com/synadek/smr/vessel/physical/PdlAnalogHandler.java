/**
 * PDLAnalogHandler.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PhysicalDeviceType;

/**
 * Defines an interface for handlers that will be invoked when an analog IO of a physical device
 * input changes state.
 */
public interface PdlAnalogHandler extends PdlEventHandler {
  /**
   * When an analog input device changes state, this method will be invoked.
   * 
   * @param idx
   *          is the index of the analog input that changed state
   * @param val
   *          is the new value of the analog input
   */
  public void physicalDeviceStateChange(final PhysicalDeviceType idx, final double val);
}
