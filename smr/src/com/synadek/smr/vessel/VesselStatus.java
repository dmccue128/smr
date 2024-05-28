/**
 * VesselStatus.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import org.json.simple.JSONObject;

/**
 * VesselStatus status.
 */
public class VesselStatus {

  // propulsion
  private double speed;
  private double heading;

  // position
  private double roll;
  private double pitch;
  private double yaw;
  private double surge;
  private double heave;
  private double sway;

  // Power
  private double batteryVoltage;
  // percent battery remaining?

  // Communications packets sent since last powered-up or rebooted
  private int packetCount;

  // Other
  private long upTime; // time since last power-up or reboot (milliseconds)
  private double temperature; // degrees celsius

  /**
   * Default constructor.
   */
  public VesselStatus() {

  }

  /**
   * JSON parsing constructor.
   *
   * @param obj
   *          to be parsed
   */
  public VesselStatus(final JSONObject obj) {
    speed = ((Double) obj.get("speed")).doubleValue();
    heading = ((Double) obj.get("heading")).doubleValue();
    roll = ((Double) obj.get("roll")).doubleValue();
    pitch = ((Double) obj.get("pitch")).doubleValue();
    yaw = ((Double) obj.get("yaw")).doubleValue();
    surge = ((Double) obj.get("surge")).doubleValue();
    heave = ((Double) obj.get("heave")).doubleValue();
    sway = ((Double) obj.get("sway")).doubleValue();
    batteryVoltage = ((Double) obj.get("batteryVoltage")).doubleValue();
    packetCount = ((Integer) obj.get("packetCount")).intValue();
    upTime = ((Long) obj.get("upTime")).longValue();
    temperature = ((Double) obj.get("temperature")).doubleValue();
  }

  /**
   * Get the speed.
   *
   * @return the speed
   */
  public final double getSpeed() {
    return speed;
  }

  /**
   * Set the speed.
   *
   * @param speed
   *          the speed to set
   */
  public final void setSpeed(double speed) {
    this.speed = speed;
  }

  /**
   * Get heading.
   *
   * @return the heading
   */
  public final double getHeading() {
    return heading;
  }

  /**
   * Set heading.
   *
   * @param heading
   *          the heading to set
   */
  public final void setHeading(double heading) {
    this.heading = heading;
  }

  /**
   * Get roll.
   *
   * @return the roll
   */
  public final double getRoll() {
    return roll;
  }

  /**
   * Set roll.
   *
   * @param roll
   *          the roll to set
   */
  public final void setRoll(double roll) {
    this.roll = roll;
  }

  /**
   * Get pitch.
   *
   * @return the pitch
   */
  public final double getPitch() {
    return pitch;
  }

  /**
   * Set pitch.
   *
   * @param pitch
   *          the pitch to set
   */
  public final void setPitch(double pitch) {
    this.pitch = pitch;
  }

  /**
   * Get yaw.
   *
   * @return the yaw
   */
  public final double getYaw() {
    return yaw;
  }

  /**
   * Set yaw.
   *
   * @param yaw
   *          the yaw to set
   */
  public final void setYaw(double yaw) {
    this.yaw = yaw;
  }

  /**
   * Get surge.
   *
   * @return the surge
   */
  public final double getSurge() {
    return surge;
  }

  /**
   * Set surge.
   *
   * @param surge
   *          the surge to set
   */
  public final void setSurge(double surge) {
    this.surge = surge;
  }

  /**
   * Get heave.
   *
   * @return the heave
   */
  public final double getHeave() {
    return heave;
  }

  /**
   * Set heave.
   *
   * @param heave
   *          the heave to set
   */
  public final void setHeave(double heave) {
    this.heave = heave;
  }

  /**
   * Get sway.
   *
   * @return the sway
   */
  public final double getSway() {
    return sway;
  }

  /**
   * Set sway.
   *
   * @param sway
   *          the sway to set
   */
  public final void setSway(double sway) {
    this.sway = sway;
  }

  /**
   * Get battery voltage.
   *
   * @return the batteryVoltage
   */
  public final double getBatteryVoltage() {
    return batteryVoltage;
  }

  /**
   * Set batteryVoltage.
   *
   * @param batteryVoltage
   *          the batteryVoltage to set
   */
  public final void setBatteryVoltage(double batteryVoltage) {
    this.batteryVoltage = batteryVoltage;
  }

  /**
   * Get packet count.
   *
   * @return the packetCount
   */
  public final int getPacketCount() {
    return packetCount;
  }

  /**
   * Set packetCount.
   *
   * @param packetCount
   *          the packetCount to set
   */
  public final void setPacketCount(int packetCount) {
    this.packetCount = packetCount;
  }

  /**
   * Get uptime.
   *
   * @return the upTime
   */
  public final long getUpTime() {
    return upTime;
  }

  /**
   * Set uptime.
   *
   * @param upTime
   *          the upTime to set
   */
  public final void setUpTime(long upTime) {
    this.upTime = upTime;
  }

  /**
   * Get temperature.
   *
   * @return the temperature
   */
  public final double getTemperature() {
    return temperature;
  }

  /**
   * Set temperature.
   *
   * @param temperature
   *          the temperature to set
   */
  public final void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  /**
   * Create a JSON object representation of the vessel status.
   *
   * @return the status as a JSON object.
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJson() {
    final JSONObject result = new JSONObject();
    result.put("speed", Double.valueOf(speed));
    result.put("heading", Double.valueOf(heading));
    result.put("roll", Double.valueOf(roll));
    result.put("pitch", Double.valueOf(pitch));
    result.put("yaw", Double.valueOf(yaw));
    result.put("surge", Double.valueOf(surge));
    result.put("heave", Double.valueOf(heave));
    result.put("sway", Double.valueOf(sway));
    result.put("batteryVoltage", Double.valueOf(batteryVoltage));
    result.put("packetCount", Integer.valueOf(packetCount));
    result.put("upTime", Long.valueOf(upTime));
    result.put("temperature", Double.valueOf(temperature));
    return result;
  }

}
