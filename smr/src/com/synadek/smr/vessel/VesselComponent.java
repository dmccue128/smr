/**
 * VesselComponent.java
 * 18 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * A component of a vessel.
 */
public interface VesselComponent {

  /**
   * Types of components that may be present on a vessel. Note: Not all vessels implement all
   * components. Some vessels may implement more than one instance of a component type e.g., two
   * propellers
   */
  public enum VesselComponentType {
    /**
     * Anchor.
     */
    VESSEL_ANCHOR,
    /**
     * Attitude (roll, pitch, yaw, surge, sway, heave)
     */
    VESSEL_ATTITUDE,
    /**
     * Battery.
     */
    VESSEL_BATTERY,
    /**
     * Compass.
     */
    VESSEL_COMPASS,
    /**
     * GNSS receiver.
     */
    VESSEL_GNSS_RECEIVER,
    /**
     * Power management.
     */
    VESSEL_POWER_MANAGEMENT,
    /**
     * Propeller.
     */
    VESSEL_PROPELLER,
    /**
     * Rudder.
     */
    VESSEL_RUDDER,
    /**
     * Running lights.
     */
    VESSEL_RUNNING_LIGHTS,
    /**
     * Ship to ship communications.
     */
    VESSEL_SHIP_TO_SHIP,
    /**
     * ShiptoShore communications.
     */
    VESSEL_SHIP_TO_SHORE,
    /**
     * Water sensor.
     */
    VESSEL_WATER_SENSOR,
    /**
     * Wind sensor.
     */
    VESSEL_WIND_SENSOR,
  }

  public VesselComponentType getComponentType();

}
