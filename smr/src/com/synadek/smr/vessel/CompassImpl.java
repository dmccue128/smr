/**
 * CompassImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Compass provides the compass heading of the vessel.
 */
public class CompassImpl extends VesselComponentImpl implements Compass {

  /**
   * Default constructor.
   */
  public CompassImpl() {
    super(VesselComponentType.VESSEL_COMPASS, "compass");
    resetConfiguration();
  }

  /**
   * Named constructor.
   *
   * @param name
   *          a name for this component
   */
  public CompassImpl(final String name) {
    super(VesselComponentType.VESSEL_COMPASS, name);
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
   * @see com.synadek.core.Component#disconnect()
   */
  @Override
  public void disconnect() throws ComponentException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#isConnected()
   */
  @Override
  public boolean isConnected() {
    // TODO Auto-generated method stub
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#isSimulated()
   */
  @Override
  public boolean isSimulated() {
    // TODO Auto-generated method stub
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Compass#getDirection()
   */
  @Override
  public int getDirection() {
    // For now, we're always heading north
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#resetConfiguration()
   */
  @Override
  public void resetConfiguration() {

  }

}
