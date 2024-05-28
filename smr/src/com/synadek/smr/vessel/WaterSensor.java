/**
 * WaterSpeed.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Provide sensor data for the water surrounding the vessel (if any).
 */
public interface WaterSensor extends VesselComponent {

  /**
   * When depth cannot be determined, report DEPTH_UNKNOWN.
   */
  public static final int DEPTH_UNKNOWN = -1;

  /**
   * Return true if the vessel is in water.
   *
   * @return true if the vessel hull is in water.
   */
  boolean isVesselInWater();

  /**
   * Return the water temperature.
   *
   * @return water temperature in degrees Celsius.
   */
  float getWaterTemperature();

  /**
   * Ocean salinity is defined as the salt concentration (e.g., Sodium and
   * Chlorine) in sea water.  It is measured PSU (Practical Salinity Unit),
   * which is a unit based on the properties of sea water conductivity. It is
   * equivalent to per thousand or (o/00) or to g/kg. The averaged salinity in
   * the global ocean is 35.5 PSU, varying from less than 15 PSU at the mouth of
   * the rivers to more than 40 PSU in the Dead Sea.
   *
   * @return water salinity in PSU.
   */
  float getWaterSalinity();

  /**
   * Return the relative speed.
   *
   * @return speed in knots/hour of water passing the hull of the vessel.
   */
  float getVesselRelativeWaterSpeed();

  /**
   * Return the water depth.
   *
   * @return depth of water (meters) beneath the vessel. Note: a value of
   *         DEPTH_UNKNOWN indicates the water depth is unknown or not
   *         measurable.
   */
  float getDepth();

}
