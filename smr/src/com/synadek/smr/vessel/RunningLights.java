/**
 * RunningLights.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Control of running lights and safety beacons.
 */
public interface RunningLights extends VesselComponent {

  /**
   * Vessel marker lights are either turned off, on for stationary vessel, or on
   * for vessel underway.
   */
  enum LightMode {
    /**
     * Lights off.
     */
    LIGHTS_OFF,
    /**
     * Single all-around white light indicates stationary vessel.
     */
    LIGHTS_STATIONARY,
    /**
     * Red (port), Green (starboard) and white (stern) lights indicate a vessel
     * underway.
     */
    LIGHTS_UNDERWAY
  }

  /**
   * Get the status of vessel running lights.
   *
   * @return the mode setting for running lights
   */
  LightMode getRunningLights();

  /**
   * Turn on/off standard running lights for travel or stationary setting.
   *
   * @param mode
   *          indicates desired mode
   */
  void setRunningLights(LightMode mode);

}
