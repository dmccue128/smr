/**
 * Conversions.java
 * 3 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.core;

/**
 * Common unit conversions used by modules of smr.
 */
public class Conversions {

  /**
   * knotsToKph - convert knots to kilometers per hour.
   * 
   * @param knots
   *          is the input speed
   * @return the equivalent speed in kilometers per hour
   */
  public static final double knotsToKph(final double knots) {
    // One knot equals exactly 1.852 kilometers per hour.
    return knots * 1.852d;
  }

  /**
   * knotsToMph - convert knots to miles per hour.
   * 
   * @param knots
   *          is the input speed
   * @return the equivalent speed in miles per hour
   */
  public static final double knotsToMph(final double knots) {
    // One knot equals 1.15077945 miles per hour.
    return knots * 1.15077945d;
  }

  /**
   * knotsToMps - convert knots to meters per second.
   * 
   * @param knots
   *          is the input speed
   * @return the equivalent speed in meters per second
   */
  public static final double knotsToMps(final double knots) {
    // One knot equals approximately 0.514 meters per second.
    return knots * 0.51444444d;
  }

  /**
   * mpsToKnots - convert meters per second to knots.
   * 
   * @param mps
   *          is the input speed
   * @return the equivalent speed in knots
   */
  public static final double mpsToKnots(final double mps) {
    // One meter per second equals 1.94384449 knots.
    return mps * 1.94384449d;
  }

  /**
   * mpsToKph - convert meters per second to kilometers per hour.
   * 
   * @param mps
   *          is the input speed
   * @return the equivalent speed in kilometers per hour
   */
  public static final double mpsToKph(final double mps) {
    // One meter per second equals 3.6 kilometers per hour.
    return mps * 3.6d;
  }

  /**
   * mpsToMph - convert meters per second to miles per hour.
   * 
   * @param mps
   *          is the input speed
   * @return the equivalent speed in miles per hour
   */
  public static final double mpsToMph(final double mps) {
    // One meter per second equals 2.23693629 miles per hour.
    return mps * 2.23693629d;
  }

  /**
   * Salinity may be measured using an electrical current. The potential of a solution to pass an
   * electric current is called electrical conductivity (EC) and it is usually measured in
   * microSiemens per centimetre (µS/cm). This is often expressed simply as an 'EC Unit'. Other
   * units for measuring salinity are: deciSiemens per metre (dS/m) and Parts Per Million in water
   * (mg/litre).
   * 
   * @param ecUnits
   *          electrical current in microSiemens per centimeter
   * @return salinity in parts per million
   */
  public static final double ecToPpm(final double ecUnits) {
    return ecUnits / 1000.0d * 640.0d;
  }

  /**
   * One kilowatt hour is 3 600 000 watt seconds.
   * 
   * @param kwh
   *          energy in kilowatt hours
   * @return energy in watt-seconds aka joules
   */
  public static final double kwhToWs(final double kwh) {
    return kwh * 3600000.0d;
  }

  /**
   * One kilowatt hour is 3 600 000 watt seconds.
   * 
   * @param ws
   *          energy in watt-seconds (aka joules)
   * @return energy in kilowatt hours
   */
  public static final double wSToKwh(final double ws) {
    return ws / 3600000.0d;
  }
}
