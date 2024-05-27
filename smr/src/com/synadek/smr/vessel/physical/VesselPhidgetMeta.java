/**
 * VesselPhidgetMeta.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PinType;

/**
 * Descriptor of a pin (digital output, digital input, or analog input) of a Phidget interface kit.
 */
public class VesselPhidgetMeta {
  /**
   * Physical device name (suitable for diagnostics).
   */
  private final String name;
  /**
   * Type of pin.
   */
  private final PinType type;
  /**
   * pin number (relative to type). Note that digital input pin 1 is not the same as digital output
   * pin 1.
   */
  private final int index;

  /**
   * Default constructor.
   * 
   * @param n
   *          a name for the pin suitable for diagnostic display
   * @param t
   *          the type of the pin (see VesselPhysicalModel.PinType)
   * @param idx
   *          the physical pin number (relative to type) e.g., zero could be A0, DI0, DO0
   */
  public VesselPhidgetMeta(final String n, final PinType t, final int idx) {
    this.name = n;
    this.type = t;
    this.index = idx;
  }

  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**
   * @return the type
   */
  public final PinType getType() {
    return type;
  }

  /**
   * @return the index
   */
  public final int getIndex() {
    return index;
  }
}
