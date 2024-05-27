/**
 * WindSpeedSample.java
 * 17 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

/**
 * Each Wind Speed sample contains data necessary to continuously compute average speed and standard
 * deviation.
 */
public class WindSpeedSample {

  /**
   * Speed in meters per second.
   */
  private final double speed;

  /**
   * Timestamp when the sample was recorded.
   */
  private final long timestamp;

  /**
   * Default constructor.
   * 
   * @param speedMps
   *          is the speed in meters per second.
   */
  public WindSpeedSample(final double speedMps) {
    speed = speedMps;
    timestamp = System.currentTimeMillis();
  }

  /**
   * Return the speed in meters per second.
   * 
   * @return the speed in meters per second
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * Timestamp when the sample was recorded.
   * 
   * @return the timestamp in millis since the epoch
   */
  public long getTimestamp() {
    return timestamp;
  }
}
