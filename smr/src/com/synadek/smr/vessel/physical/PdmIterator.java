/**
 * PdmIterator.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.smr.vessel.physical.VesselPhysicalModel.PhysicalDeviceType;
import com.synadek.smr.vessel.physical.VesselPhysicalModel.PinType;

import java.util.Iterator;

/**
 * Iterate over a class of devices in the physical device model.
 */
public class PdmIterator implements Iterator<PhysicalDeviceType> {

  /**
   * Reference to the Pio model supporting this device.
   */
  private final VesselPhysicalModel model;
  /**
   * Snapshot of devices supported by this pdm.
   */
  private final PhysicalDeviceType[] devices;
  /**
   * Selected pin type to use as a filter on the set of devices.
   */
  private final PinType type;
  /**
   * Position of the iterator in the array of PhysicalDeviceType devices.
   */
  private int position;

  /**
   * Default constructor.
   * 
   * @param mdl
   *          the Pio model supporting this device
   * @param filter
   *          the type of pin to be used as a filter
   */
  public PdmIterator(final VesselPhysicalModel mdl, final PinType filter) {
    this.model = mdl;
    this.devices = PhysicalDeviceType.values();
    this.type = filter;
    this.position = 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#hasNext()
   */
  @Override
  public boolean hasNext() {
    for (int i = position; i < devices.length; i++) {
      if (this.type.equals(model.getType(devices[i]))) {
        return true;
      }
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#next()
   */
  @Override
  public PhysicalDeviceType next() {
    PhysicalDeviceType result = null;
    for (; position < devices.length; position++) {
      if (this.type.equals(model.getType(devices[position]))) {
        result = devices[position++];
        break;
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#remove()
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
