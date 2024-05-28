/**
 * PowerImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;
import java.util.Locale;

/**
 * Power management implementation.
 */
public class PowerManagementImpl extends VesselComponentImpl implements PowerManagement {

  /**
   * Default constructor.
   */
  public PowerManagementImpl() {
    super(VesselComponentType.VESSEL_POWER_MANAGEMENT, "power management");
    resetConfiguration();
  }

  /**
   * Named constructor.
   *
   * @param name
   *          a name for this component
   */
  public PowerManagementImpl(final String name) {
    super(VesselComponentType.VESSEL_POWER_MANAGEMENT, name);
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
   * @see com.synadek.core.Component#getStatusCode()
   */
  @Override
  public Status getStatusCode() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#getStatusMessage(java.util.Locale)
   */
  @Override
  public String getStatusMessage(Locale locale) {
    // TODO Auto-generated method stub
    return null;
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
   * @see com.synadek.smr.vessel.Power#getStatus()
   */
  @Override
  public PowerStatus getStatus() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Power#powerUp()
   */
  @Override
  public void powerUp() {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Power#powerDown()
   */
  @Override
  public void powerDown() {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Power#sleep()
   */
  @Override
  public void sleep() {
    // TODO Auto-generated method stub

  }

}
