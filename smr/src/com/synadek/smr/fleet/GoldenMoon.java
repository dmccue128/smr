/**
 * GoldenMoon.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.fleet;

import com.synadek.core.ComponentException;
import com.synadek.core.GpsCoordinates;
import com.synadek.smr.vessel.AbstractVesselImpl;
import com.synadek.smr.vessel.Anchor;
import com.synadek.smr.vessel.AnchorImpl;
import com.synadek.smr.vessel.Battery;
import com.synadek.smr.vessel.BatteryImpl;
import com.synadek.smr.vessel.Compass;
import com.synadek.smr.vessel.CompassImpl;
import com.synadek.smr.vessel.GnssReceiver;
import com.synadek.smr.vessel.GnssReceiverImpl;
import com.synadek.smr.vessel.PowerManagement;
import com.synadek.smr.vessel.PowerManagementImpl;
import com.synadek.smr.vessel.Propeller;
import com.synadek.smr.vessel.PropellerImpl;
import com.synadek.smr.vessel.Rudder;
import com.synadek.smr.vessel.RudderImpl;
import com.synadek.smr.vessel.RunningLights;
import com.synadek.smr.vessel.RunningLightsImpl;
import com.synadek.smr.vessel.Vessel;
import com.synadek.smr.vessel.WindSensor;
import com.synadek.smr.vessel.WindSensorImpl;
import com.synadek.smr.vessel.physical.VesselSimulation;

/**
 * Simulated Vessel, Golden Moon.
 */
public class GoldenMoon extends AbstractVesselImpl {

  /**
   * The singleton instance of this class.
   */
  private static final GoldenMoon myInstance = null;

  /**
   * The serial number assigned to this vessel.
   */
  private static final String GOLDEN_MOON_SERIAL_NUMBER = "000";

  /**
   * Cruising speed of this vessel (knots).
   */
  private static final double GOLDEN_MOON_CRUISING_SPEED = 8.0;

  /**
   * Get a reference to the singleton instance of this vessel.
   * 
   */
  public static Vessel getInstance() {
    if (myInstance == null) {
      return new GoldenMoon();
    }
    return myInstance;
  }

  /**
   * Anchor.
   */
  private Anchor anchor;

  /**
   * Battery.
   */
  private Battery battery;

  /**
   * Compass.
   */
  private Compass compass;

  /**
   * GNSS receiver.
   */
  private GnssReceiver gnssReceiver;

  /**
   * Power manager.
   */
  private PowerManagement powerManagement;

  /**
   * Propeller.
   */
  private Propeller propeller;

  /**
   * Rudder.
   */
  private Rudder rudder;

  /**
   * Running lights.
   */
  private RunningLights runningLights;

  /**
   * Wind sensor.
   */
  private WindSensor windSensor;

  /**
   * Default constructor.
   */
  private GoldenMoon() {
    super(new VesselSimulation(), "Golden Moon", GOLDEN_MOON_SERIAL_NUMBER);

    // Connect the components of this vessel
    try {

      // Connect the physical model for this vessel
      this.getPhysicalModel().connect();

      anchor = new AnchorImpl();
      addComponent(anchor);

      battery = new BatteryImpl();
      addComponent(battery);

      compass = new CompassImpl();
      addComponent(compass);

      gnssReceiver = new GnssReceiverImpl("GNSS");
      addComponent(gnssReceiver);

      powerManagement = new PowerManagementImpl();
      addComponent(powerManagement);

      propeller = new PropellerImpl();
      addComponent(propeller);

      rudder = new RudderImpl();
      addComponent(rudder);

      runningLights = new RunningLightsImpl();
      addComponent(runningLights);

      windSensor = new WindSensorImpl();
      addComponent(windSensor);

      setStatus(VesselStatus.VESSEL_READY);

    } catch (ComponentException err) {
      setStatus(VesselStatus.VESSEL_NOT_READY);
      log.error(err);
    }
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
   * @see com.synadek.smr.vessel.Vessel#getCargoDepth()
   */
  @Override
  public int getCargoDepth() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#getCargoHeight()
   */
  @Override
  public int getCargoHeight() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#getCargoWidth()
   */
  @Override
  public int getCargoWidth() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#getCruisingSpeed()
   */
  @Override
  public double getCruisingSpeed() {
    return GOLDEN_MOON_CRUISING_SPEED;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#getLocation()
   */
  @Override
  public GpsCoordinates getLocation() {
    return gnssReceiver.getLocation();
  }

  /**
   * Get the maximum cargo weight (kg).
   */
  @Override
  public double getMaximumCargoWeight() {
    return 0.5;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#offloadVessel()
   */
  @Override
  public void offloadVessel() {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.Vessel#secureVessel()
   */
  @Override
  public void secureVessel() {
    // TODO Auto-generated method stub

  }
}
