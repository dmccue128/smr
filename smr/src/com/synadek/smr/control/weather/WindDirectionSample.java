/**
 * WindDirectionSample.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

/**
 * Each Wind Direction sample contains data necessary to continuously compute
 * average direction and standard deviation.
 */
class WindDirectionSample {

  /**
   * Wind direction in radians.
   */
  private final double headingRadians;

  /**
   * Sin of the sample heading.
   */
  private final double sin;
  /**
   * Cosine of the sample heading.
   */
  private final double cos;

  /**
   * Timestamp when the sample was recorded.
   */
  private final long timestamp;

  /**
   * Default constructor.
   *
   * @param heading
   *          is the heading in degrees
   */
  public WindDirectionSample(final double heading) {
    headingRadians = Math.toRadians(heading);
    sin = Math.sin(headingRadians);
    cos = Math.cos(headingRadians);
    timestamp = System.currentTimeMillis();
  }

  /**
   * The heading.
   *
   * @return the heading in radians
   */
  public double getHeading() {
    return headingRadians;
  }

  /**
   * The cosine of the heading.
   *
   * @return the cos
   */
  public double getCos() {
    return cos;
  }

  /**
   * The sin of the heading.
   *
   * @return the sin
   */
  public double getSin() {
    return sin;
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
