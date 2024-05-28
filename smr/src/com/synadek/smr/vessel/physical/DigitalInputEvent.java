/**
 * DigitalInputEvent.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PhysicalDeviceType;

/**
 * Event to be signaled to registered event handlers for a digital input.
 */
public class DigitalInputEvent {

  /**
   * Device signaling the event.
   */
  private final PhysicalDeviceType device;

  /**
   * New value of this device.
   */
  private final boolean newState;

  /**
   * Default constructor.
   *
   * @param dev
   *          the physical device (See VesselPhysicalModel)
   * @param val
   *          the new state of the device
   */
  public DigitalInputEvent(final PhysicalDeviceType dev, final boolean val) {
    device = dev;
    newState = val;
  }

  /**
   * Get the physical device type.
   *
   * @return the device
   */
  public final PhysicalDeviceType getDevice() {
    return device;
  }

  /**
   * Get the new value.
   *
   * @return the newValue
   */
  public final boolean getNewState() {
    return newState;
  }

}
