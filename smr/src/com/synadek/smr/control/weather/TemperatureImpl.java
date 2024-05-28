/**
 * TemperatureImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import com.synadek.core.AbstractComponent;
import com.synadek.core.ComponentException;

/**
 * Reports air and water temperature around the vessel.
 */
public class TemperatureImpl extends AbstractComponent implements Temperature {

  private static final float DEFAULT_AIR_TEMPERATURE = 35.0f;
  private static final float DEFAULT_SURFACE_TEMPERATURE = 10.0f;

  // When component is simulated, use these values
  private float simulatedAirTemperature;
  private float simulatedSurfaceTemperature;

  /**
   * Default constructor.
   */
  public TemperatureImpl() {
    super("temperature");
    simulatedAirTemperature = DEFAULT_AIR_TEMPERATURE;
    simulatedSurfaceTemperature = DEFAULT_SURFACE_TEMPERATURE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.weather.Temperature#airTemperature()
   */
  @Override
  public float airTemperature() {

    if (!this.connected) {
      return 0.0f;
    }

    if (this.simulated) {
      return this.simulatedAirTemperature;
    }

    // TODO implement air temperature sensing
    return 0.0f;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#connect(boolean)
   */
  @Override
  public boolean connect(boolean sim) throws ComponentException {
    if (sim) {
      this.simulated = true;
      this.connected = true;
      return true;
    }

    // Confirm availability of HW to implement sensing
    this.connected = true;
    return true;
  }

  /**
   * For simulated temperature sensors, set the simulated air temperature.
   *
   * @param degreesCelsius
   *          the simulated air temperature
   */
  public void setAirTemperature(float degreesCelsius) {
    if (this.connected && this.simulated) {
      this.simulatedAirTemperature = degreesCelsius;
    }
  }

  /**
   * For simulated temperature sensors, set the simulated water surface
   * temperature.
   *
   * @param degreesCelsius
   *          the temperature
   */
  public void setSurfaceTemperature(float degreesCelsius) {
    if (this.connected && this.simulated) {
      this.simulatedSurfaceTemperature = degreesCelsius;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.weather.Temperature#surfaceTemperature()
   */
  @Override
  public float surfaceTemperature() {

    if (!this.connected) {
      return 0.0f;
    }

    if (this.simulated) {
      return this.simulatedSurfaceTemperature;
    }

    // TODO implement surface temperature sensing
    return 0.0f;
  }
}
