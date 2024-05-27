/**
 * AnalogEvent.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PhysicalDeviceType;

/**
 * Event to be signaled to registered event handlers for an analog update.
 */
public class AnalogEvent {

  /**
   * Device signaling the event.
   */
  private final PhysicalDeviceType device;

  /**
   * New value of this device.
   */
  private final double newValue;

  /**
   * Default constructor.
   * 
   * @param dev
   *          the device model
   * @param val
   *          the value of the event
   */
  public AnalogEvent(final PhysicalDeviceType dev, final double val) {
    device = dev;
    newValue = val;
  }

  /**
   * @return the device
   */
  public final PhysicalDeviceType getDevice() {
    return device;
  }

  /**
   * @return the newValue
   */
  public final double getNewValue() {
    return newValue;
  }

}
