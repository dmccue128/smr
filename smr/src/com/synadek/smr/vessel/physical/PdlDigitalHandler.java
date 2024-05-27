/**
 * PDLDigitalHandler.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PhysicalDeviceType;

/**
 * Defines an interface for handlers that will be invoked when a digital IO of a physical device
 * input changes state.
 */
public interface PdlDigitalHandler extends PdlEventHandler {

  /**
   * When a digital input device changes state, this method will be invoked.
   * 
   * @param idx
   *          is the index of the digital input that changed state
   * @param val
   *          is the new value of the digital input
   */
  public void physicalDeviceStateChange(final PhysicalDeviceType idx, final boolean val);

}
