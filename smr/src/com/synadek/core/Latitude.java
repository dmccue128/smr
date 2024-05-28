/**
 * Latitude.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A geographic coordinate that specifies the north–south position of a point on
 * the Earth's surface. Latitude is an angle which ranges from 0° at the Equator
 * to 90° (North or South) at the poles.
 */
public class Latitude {

  /**
   * Unicode character for degree symbol.
   */
  private static String DEGREE_SYMBOL = "&deg;";

  /**
   * double,Minutes,Seconds (DMS) format for latitude. Example: 36�59'21.2"N
   */
  private static Pattern DMS_LATITUDE_PATTERN = Pattern
      .compile("([0-9]+)\u00B0 *([0-9]+)' *([0-9]+)\" *([NnSs])");

  /**
   * double, Decimal Minutes (DDM) format for latitude. Example: 36 59.35333
   */
  private static Pattern DDM_LATITUDE_PATTERN = Pattern.compile("([-+]*[0-9]+) +([0-9]+.[0-9]+)");

  public static double LATITUDE_ARCTIC_CIRCLE = 66.57;
  public static double LATITUDE_TROPIC_OF_CANCER = 23.43;
  public static double LATITUDE_EQUATOR = 0.0;
  public static double LATITUDE_TROPIC_OF_CAPRICORN = -23.43;
  public static double LATITUDE_ANTARCTIC_CIRCLE = -66.57;

  /**
   * Parse a latitude in double, Decimal Minutes (DDM) format.
   *
   * @param lat
   *          the latitude in DDM format, e.g., 36 59.35333
   * @return the latitude
   * @throws NumberFormatException
   *           if the string cannot be parsed
   */
  public static Latitude parseDdm(final String lat) throws NumberFormatException {

    double result = 0.0;

    // Attempt to match the string against the DMS latitude pattern
    final Matcher ddmLatMatch = DDM_LATITUDE_PATTERN.matcher(lat);

    if (ddmLatMatch.matches()) {
      final double degrees = Double.parseDouble(ddmLatMatch.group(1));
      final double minutes = Double.parseDouble(ddmLatMatch.group(2));
      result = degrees + (minutes * 60);
      return Latitude.fromDegrees(result);
    }

    throw new NumberFormatException(lat);
  }

  /**
   * Parse a latitude in double,Minutes,Seconds (DMS) format.
   *
   * @param lat
   *          the latitude in DMS format, e.g., 36x59'21.2"N
   * @return the latitude
   * @throws NumberFormatException
   *           if the string cannot be parse
   */
  public static Latitude parseDms(final String lat) throws NumberFormatException {

    double result = 0.0;

    // Attempt to match the string against the DMS latitude pattern
    final Matcher dmsLatMatch = DMS_LATITUDE_PATTERN.matcher(lat);

    if (dmsLatMatch.matches()) {
      final double degrees = Double.parseDouble(dmsLatMatch.group(1));
      final double minutes = Double.parseDouble(dmsLatMatch.group(2));
      final double seconds = Double.parseDouble(dmsLatMatch.group(3));
      final String direction = dmsLatMatch.group(4);
      result = degrees + (minutes * 60 + seconds) / 3600;
      if (direction.equalsIgnoreCase("S")) {
        result = -result;
      }
      return Latitude.fromDegrees(result);
    }

    throw new NumberFormatException(lat);
  }

  /**
   * Value of this latitude object (radians).
   */
  double radianValue;

  /**
   * Default constructor.
   *
   * @param lat
   *          the latitude in degrees
   */
  public static Latitude fromDegrees(final double lat) {
    return new Latitude(Math.toRadians(lat));
  }

  /**
   * Default constructor.
   *
   * @param lat
   *          the latitude in radians
   */
  public Latitude(final double lat) {
    radianValue = lat;
  }

  /**
   * Get the latitude in double, Decimal Minutes format. Example: 36 59.35333
   *
   * @return the latitude in double, Decimal Minutes format
   */
  public final String getDdm() {
    final double degrees = this.radianValue;
    final double minutes = degrees % 1;
    return String.format("%g %8.5g", Double.valueOf(Math.floor(degrees)), Double.valueOf(minutes));
  }

  /**
   * Get latitude in double, Minutes, Seconds (DMS) format. Example:
   * 36�59'21.2"N
   *
   * @return the latitude in DMS format
   */
  public final String getDms() {
    final double degrees = this.degrees();
    final double allSeconds = degrees % 1;
    final double minutes = Math.floor(allSeconds / 60.0);
    final double seconds = Math.round(allSeconds % 60);
    final char ns = (degrees < 0.0) ? 'S' : 'N';
    return String.format("%g%s%g'%g\"%c", Double.valueOf(Math.floor(degrees)), DEGREE_SYMBOL,
        Double.valueOf(minutes), Double.valueOf(seconds), Character.valueOf(ns));
  }

  public final double degrees() {
    return Math.toDegrees(this.radianValue);
  }

  public final double radians() {
    return this.radianValue;
  }
}
