/**
 * VesselStatus.java
 * 18 Apr 2016
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

  // Communications
  private int packetCount; // number of packets sent since last powered-up or rebooted

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
   * @param JSONObject
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
   * @return the speed
   */
  public final double getSpeed() {
    return speed;
  }

  /**
   * @param speed
   *          the speed to set
   */
  public final void setSpeed(double speed) {
    this.speed = speed;
  }

  /**
   * @return the heading
   */
  public final double getHeading() {
    return heading;
  }

  /**
   * @param heading
   *          the heading to set
   */
  public final void setHeading(double heading) {
    this.heading = heading;
  }

  /**
   * @return the roll
   */
  public final double getRoll() {
    return roll;
  }

  /**
   * @param roll
   *          the roll to set
   */
  public final void setRoll(double roll) {
    this.roll = roll;
  }

  /**
   * @return the pitch
   */
  public final double getPitch() {
    return pitch;
  }

  /**
   * @param pitch
   *          the pitch to set
   */
  public final void setPitch(double pitch) {
    this.pitch = pitch;
  }

  /**
   * @return the yaw
   */
  public final double getYaw() {
    return yaw;
  }

  /**
   * @param yaw
   *          the yaw to set
   */
  public final void setYaw(double yaw) {
    this.yaw = yaw;
  }

  /**
   * @return the surge
   */
  public final double getSurge() {
    return surge;
  }

  /**
   * @param surge
   *          the surge to set
   */
  public final void setSurge(double surge) {
    this.surge = surge;
  }

  /**
   * @return the heave
   */
  public final double getHeave() {
    return heave;
  }

  /**
   * @param heave
   *          the heave to set
   */
  public final void setHeave(double heave) {
    this.heave = heave;
  }

  /**
   * @return the sway
   */
  public final double getSway() {
    return sway;
  }

  /**
   * @param sway
   *          the sway to set
   */
  public final void setSway(double sway) {
    this.sway = sway;
  }

  /**
   * @return the batteryVoltage
   */
  public final double getBatteryVoltage() {
    return batteryVoltage;
  }

  /**
   * @param batteryVoltage
   *          the batteryVoltage to set
   */
  public final void setBatteryVoltage(double batteryVoltage) {
    this.batteryVoltage = batteryVoltage;
  }

  /**
   * @return the packetCount
   */
  public final int getPacketCount() {
    return packetCount;
  }

  /**
   * @param packetCount
   *          the packetCount to set
   */
  public final void setPacketCount(int packetCount) {
    this.packetCount = packetCount;
  }

  /**
   * @return the upTime
   */
  public final long getUpTime() {
    return upTime;
  }

  /**
   * @param upTime
   *          the upTime to set
   */
  public final void setUpTime(long upTime) {
    this.upTime = upTime;
  }

  /**
   * @return the temperature
   */
  public final double getTemperature() {
    return temperature;
  }

  /**
   * @param temperature
   *          the temperature to set
   */
  public final void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  @SuppressWarnings("unchecked")
  public JSONObject toJSON() {
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
