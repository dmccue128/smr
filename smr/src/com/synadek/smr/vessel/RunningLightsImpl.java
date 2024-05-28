/**
 * RunningLightsImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Control of running lights and safety beacons.
 */
public class RunningLightsImpl extends VesselComponentImpl implements RunningLights {

  private LightMode myMode;

  /**
   * Default constructor.
   */
  public RunningLightsImpl() {
    super(VesselComponentType.VESSEL_RUNNING_LIGHTS, "running lights");
    resetConfiguration();
  }

  /**
   * Named constructor.
   *
   * @param name
   *          a name for this component
   */
  public RunningLightsImpl(final String name) {
    super(VesselComponentType.VESSEL_RUNNING_LIGHTS, name);
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

    this.myMode = LightMode.LIGHTS_OFF;

    if (sim) {
      this.simulated = true;
      this.connected = true;
      return true;
    }

    // TODO confirm that running light HW is available
    this.simulated = false;
    this.connected = true;
    return true;
  }

  /**
   * Disconnect shuts down the device. Disconnect is idempotent. Invoking
   * disonnect on a device that is already disconnected is a no-op.
   *
   * @throws ComponentException
   *           if an error occurs accessing the device.
   */
  @Override
  public void disconnect() throws ComponentException {
    if (this.connected) {
      setRunningLights(LightMode.LIGHTS_OFF);
      this.connected = false;
    }
    this.simulated = false;
    this.myMode = LightMode.LIGHTS_OFF;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.RunningLights#getRunningLights()
   */
  @Override
  public LightMode getRunningLights() {
    return myMode;
  }

  /**
   * Switch running lights to indicate a stationary vessel (Single all-around
   * white light).
   */
  private void setLightsStationary() {
    // TODO implement stationary running lights
  }

  /**
   * Switch running lights to indicate a vessel underway (Red (port), Green
   * (starboard) and white (stern) lights).
   */
  private void setLightsUnderway() {
    // TODO implement running lights for vessel underway
  }

  private void setLightsOff() {
    // TODO implement running lights off
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.RunningLights#setRunningLights(com.synadek.smr.
   * vessel.RunningLights.LightMode)
   */
  @Override
  public void setRunningLights(LightMode mode) {

    this.myMode = mode;

    if (!this.connected) {
      log.warn("Failed to set running lights for disconnected component");
    } else if (this.simulated) {
      log.info("Simulated running lights set to {}", mode);
    } else {
      switch (mode) {
        case LIGHTS_OFF:
          setLightsOff();
          break;
        case LIGHTS_STATIONARY:
          setLightsStationary();
          break;
        case LIGHTS_UNDERWAY:
          setLightsUnderway();
          break;
        default:
          break;
      }
    }
  }

}
