/**
 * GnssReceiverImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;
import com.synadek.core.GpsCoordinates;

/**
 * GPS/GNSS receiver implementation.
 */
public class GnssReceiverImpl extends VesselComponentImpl implements GnssReceiver {

  /**
   * Default constructor.
   */
  public GnssReceiverImpl() {
    super(VesselComponentType.VESSEL_GNSS_RECEIVER, "GNSS Receiver");
    resetConfiguration();
  }

  /**
   * Named constructor.
   *
   * @param name
   *          a name for this component
   */
  public GnssReceiverImpl(final String name) {
    super(VesselComponentType.VESSEL_GNSS_RECEIVER, name);
    resetConfiguration();
  }

  /**
   * Connect parameter indicates whether to connect to a physical or simulated
   * component.
   *
   * @param sim
   *          true if the connection is to a simulation of the component
   * @throws ComponentException
   *           if an error occurs
   */
  @Override
  public boolean connect(final boolean sim) throws ComponentException {

    // Simulation is not yet supported for this component
    if (sim) {
      log.error(ERR_SIM_NOT_AVAIL);
      return false;
    }

    this.simulated = false;
    this.connected = true;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#resetConfiguration()
   */
  @Override
  public void resetConfiguration() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.GnssReceiver#getLocation()
   */
  @Override
  public GpsCoordinates getLocation() {
    // TODO Auto-generated method stub
    return null;
  }

}
