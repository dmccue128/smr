/**
 * GPSCoordinates.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Store GPS point and provide various common formats for GPS coordinates.
 */
public class GpsCoordinates {

  /**
   * GPS Coordinates JSON keys for JSON Object representation of coordinates.
   */
  public static final String JSON_KEY_ELEVATION = "altitude";

  /**
   * GPS Coordinates JSON keys for JSON Object representation of coordinates.
   */
  public static final String JSON_KEY_LONGITUDE = "longitude";

  /**
   * GPS Coordinates JSON keys for JSON Object representation of coordinates.
   */
  public static final String JSON_KEY_LATITUDE = "latitude";

  /**
   * Approximate radius of the earth (meters).
   */
  private static final double EARTH_RADIUS = 6371000.0;

  /**
   * Unicode character for degree symbol.
   */
  private static String degreeSymbol = "&deg;";

  private static Latitude parseLat(final String lat) throws NumberFormatException {
    Latitude result;
    try {
      // Try to parse DMS format
      result = Latitude.parseDms(lat);
    } catch (NumberFormatException err) {
      try {
        // Try to parse DDM format
        result = Latitude.parseDdm(lat);
      } catch (NumberFormatException err2) {
        // Try to parse DD format.
        double deg = Double.parseDouble(lat);
        result = new Latitude(Math.toRadians(deg));
      }
    }
    return result;
  }

  private static Longitude parseLon(final String lon) throws NumberFormatException {
    Longitude result;
    try {
      // Try DMS format first
      result = Longitude.parseDms(lon);
    } catch (NumberFormatException err) {
      try {
        // Try DDM format next
        result = Longitude.parseDdm(lon);
      } catch (NumberFormatException err2) {
        // Try DD format
        double deg = Double.parseDouble(lon);
        result = new Longitude(Math.toRadians(deg));
      }
    }
    return result;
  }

  /**
   * Latitude.
   */
  private Latitude latitude;

  /**
   * Longitude.
   */
  private Longitude longitude;

  /**
   * Elevation as ellipsoidal height in meters.
   */
  private double elevation;

  /**
   * Acquire a reference to the application logger.
   */
  protected final Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

  private static final String[] compassPoints = {"north", "north east", "east", "south east",
      "south", "south west", "west", "north west"};

  /**
   * Simplified constructor ignoring elevation.
   *
   * @param lat
   *          the latitude in decimal degrees
   * @param lon
   *          the longitude in decimal degrees
   */
  public GpsCoordinates(final double lat, final double lon) {
    latitude = new Latitude(Math.toRadians(lat));
    longitude = new Longitude(Math.toRadians(lon));
    elevation = 0.0;
  }

  /**
   * Constructor using decimal degrees and meters of elevation, example:
   * 36.989213,-84.231474, 42.0
   *
   * @param lat
   *          the latitude in decimal degrees
   * @param lon
   *          the longitude in decimal degrees
   * @param elev
   *          the elevation (ref. ellipsoid)
   */
  public GpsCoordinates(final double lat, final double lon, final double elev) {
    latitude = new Latitude(Math.toRadians(lat));
    longitude = new Longitude(Math.toRadians(lon));
    elevation = elev;
  }

  /**
   * JSON parsing constructor.
   *
   * @param obj
   *          the JSON Object using degrees as units
   */
  public GpsCoordinates(final JSONObject obj) {
    try {
      double latDegrees = ((Double) obj.get(JSON_KEY_LATITUDE)).doubleValue();
      double lonDegrees = ((Double) obj.get(JSON_KEY_LONGITUDE)).doubleValue();

      latitude = new Latitude(Math.toRadians(latDegrees));
      longitude = new Longitude(Math.toRadians(lonDegrees));
      // If altitude is defined, use it
      Double alt = (Double) obj.get(JSON_KEY_ELEVATION);
      if (alt == null) {
        elevation = 0.0;
      } else {
        elevation = alt.doubleValue();
      }
    } catch (NumberFormatException | NullPointerException err) {
      log.error(err);
      latitude = new Latitude(0.0);
      longitude = new Longitude(0.0);
      elevation = 0.0;
    }
  }

  /**
   * Alternate constructor parses string representations of latitude and
   * longitude in any of three formats: Degrees,Decimal Minutes(DDM) or
   * Degrees,Minutes,Seconds (DMS) or Decimal Degrees (DD).
   *
   * @param lat
   *          the latitude in DMS format (36�59'21.2"N) or DDM format (36
   *          59.35333) or DD format (36.989213)
   * @param lon
   *          the longitude in DMS format(84�13'53.3"W) or DDM format(-84
   *          13.888333) or DD format (-84.231474)
   * @throws NumberFormatException
   *           if the inputs cannot be parsed
   */
  public GpsCoordinates(final String lat, final String lon) throws NumberFormatException {
    latitude = parseLat(lat);
    // If latitude parsed successfully, try to parse longitude
    longitude = parseLon(lon);
    elevation = 0.0;
  }

  /**
   * Alternate constructor parses string representations of latitude and
   * longitude in any of three formats: Degrees,Decimal Minutes(DDM) or
   * Degrees,Minutes,Seconds (DMS) or Decimal Degrees (DD).
   *
   * @param lat
   *          the latitude in DMS format (36�59'21.2"N) or DDM format (36
   *          59.35333) or DD format (36.989213)
   * @param lon
   *          the longitude in DMS format(84�13'53.3"W) or DDM format(-84
   *          13.888333) or DD format (-84.231474)
   * @param elev
   *          the elevation in meters (from ellipsoid)
   * @throws NumberFormatException
   *           if the inputs cannot be parsed
   */
  public GpsCoordinates(final String lat, final String lon, final String elev)
      throws NumberFormatException {
    latitude = parseLat(lat);
    // If latitude parsed successfully, try to parse longitude
    longitude = parseLon(lon);
    // Try to parse the elevation value (meters)
    elevation = Double.parseDouble(elev);
  }

  /**
   * Get the elevation represented in this GPS coordinate object.
   *
   * @return the elevation relative to ellipsoid
   */
  public double getElevation() {
    return elevation;
  }

  /**
   * Compute the bearing from this GPS coordinate to some other GPS coordinate.
   *
   * @param toLocation
   *          the other GPS coordinates
   * @return the bearing in degrees
   */
  public double getBearing(final GpsCoordinates toLocation) {
    double deltaLongitude = toLocation.getLongitude().radians() - longitude.radians();
    double lat1 = this.latitude.radians();
    double lat2 = toLocation.getLatitude().radians();
    double y = Math.sin(deltaLongitude) * Math.cos(lat2);
    double x = Math.cos(lat1) * Math.sin(lat2)
        - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLongitude);

    double bearing = Math.atan2(y, x) * 180 / Math.PI;
    if (bearing < 0) {
      bearing = bearing + 360;
    }

    return bearing;
  }

  /**
   * Get compass bearing to another set of GPS coordinates.
   *
   * @param toLocation
   *          the destination location
   * @return the approximate compass bearing as a String, one of "N", "S", "E",
   *         "W", "NE", "SE", "SW", "NW"
   */
  public String getCompassBearing(final GpsCoordinates toLocation) {
    int bearing = (int) Math.round(getBearing(toLocation));
    bearing += 22.5;
    bearing = bearing % 360;
    // Reduce to an integer from 0 to 7
    bearing = bearing / 45;
    return compassPoints[bearing];
  }

  /**
   * Get GPS coordinates in Decimal Degrees format. Example:
   * 36.989213,-84.231474
   *
   * @return the coordinates
   */
  public final String getCoords() {
    return String.valueOf(latitude) + "," + String.valueOf(longitude);
  }

  /**
   * Get GPS coordinates in Degrees, Decimal Minutes format. Example: 36
   * 59.35333 -84 13.888333
   *
   * @return the coordinates
   */
  public final String getCoordsDdm() {
    return getLatDdm() + ' ' + getLonDdm();
  }

  /**
   * Get GPS coordinates in Degrees, Minutes, Seconds (DMS) format. Example:
   * 36�59'21.2"N 84�13'53.3"W
   *
   * @return the coordinates
   */
  public final String getCoordsDms() {
    return getLatitudeDms() + ' ' + getLongitudeDms();
  }

  /**
   * Get the distance in meters to another GPS location.
   *
   * @param toLocation
   *          the other GPS coordinates
   * @return the distance in meters
   */
  public double getDistance(final GpsCoordinates toLocation) {

    double lat1 = this.latitude.radians();
    double lat2 = toLocation.getLatitude().radians();
    double deltaLatitude = lat2 - lat1;
    double deltaLongitude = toLocation.getLongitude().radians() - this.getLongitude().radians();
    double halfDeltaLat = deltaLatitude / 2.0;
    double halfDeltaLon = deltaLongitude / 2.0;

    // Haversine formula
    double a = Math.sin(halfDeltaLat) * Math.sin(halfDeltaLat)
        + Math.cos(lat1) * Math.cos(lat2) * Math.sin(halfDeltaLon) * Math.sin(halfDeltaLon);
    double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    double distance = EARTH_RADIUS * c;

    return distance;
  }

  /**
   * Compute the endpoint coordinates, given a bearing and distance from this
   * location.
   *
   * @param bearing
   *          compass bearing in degrees
   * @param distance
   *          distance in meters
   * @return coordinates of end point
   */
  public GpsCoordinates getEndLocation(final int bearing, final long distance) {

    double dr = distance / EARTH_RADIUS;

    double endLatitude = Math.asin(Math.sin(this.getLatitude().radians()) * Math.cos(dr)
        + Math.cos(this.getLatitude().radians()) * Math.sin(dr) * Math.cos(bearing));

    double endLongitude = this.getLongitude().radians()
        + Math.atan2(Math.sin(bearing) * Math.sin(dr) * Math.cos(this.getLatitude().radians()),
            Math.cos(dr) - Math.sin(this.getLatitude().radians()) * Math.sin(endLatitude));

    // The longitude can be normalised to −180…+180 using (lon+540)%360-180
    return new GpsCoordinates(endLatitude, endLongitude);
  }

  /**
   * Final bearing.
   *
   * @param toLocation
   *          the other GPS coordinates
   * @return the final bearing (degrees)
   */
  public int getFinalBearing(final GpsCoordinates toLocation) {

    double deltaLongitude = toLocation.getLongitude().radians() - this.getLongitude().radians();

    double y = Math.sin(deltaLongitude) * Math.cos(toLocation.getLatitude().radians());

    double x = Math.cos(this.getLatitude().radians())
        * Math.sin(toLocation.getLatitude().radians())
        - Math.sin(this.getLatitude().radians()) * Math.cos(toLocation.getLatitude().radians())
            * Math.cos(deltaLongitude);

    // compute bearing in range -180..180
    int bearing = (int) Math.round(Math.toDegrees(Math.atan2(y, x)));

    // normalize to a compass bearing
    return (bearing + 360) % 360;
  }

  /**
   * Get the latitude in Degrees, Decimal Minutes format. Example: 36 59.35333
   *
   * @return the latitude
   */
  public final String getLatDdm() {
    final double degrees = latitude.degrees();
    final double minutes = degrees % 1;
    return String.format("%g %8.5g", Double.valueOf(Math.floor(degrees)), Double.valueOf(minutes));
  }

  /**
   * Get the latitude in Decimal Degrees format. Example: 36.989213
   *
   * @return the latitude
   */
  public final Latitude getLatitude() {
    return latitude;
  }

  /**
   * Get latitude in Degrees, Minutes, Seconds (DMS) format. Example:
   * 36�59'21.2"N
   *
   * @return the latitude
   */
  public final String getLatitudeDms() {
    final double degrees = latitude.degrees();
    final double allSeconds = degrees % 1;
    final double minutes = Math.floor(allSeconds / 60.0);
    final double seconds = Math.round(allSeconds % 60);
    final char ns = (degrees < 0.0) ? 'S' : 'N';
    return String.format("%g%s%g'%g\"%c", Double.valueOf(degrees), degreeSymbol,
        Double.valueOf(minutes), Double.valueOf(seconds), Character.valueOf(ns));
  }

  /**
   * Get the longitude in Degrees, Decimal Minutes format. Example: -84
   * 13.888333
   *
   * @return the longitude
   */
  public final String getLonDdm() {
    final double degrees = longitude.degrees();
    final double minutes = degrees % 1;
    return String.format("%g %8.5g", Double.valueOf(degrees), Double.valueOf(minutes));
  }

  /**
   * Get the longitude in Decimal Degrees format. Example: -84.231474
   *
   * @return the longitude
   */
  public final Longitude getLongitude() {
    return longitude;
  }

  /**
   * Get longitude in Degrees, Minutes, Seconds (DMS) format. Example:
   * 84�13'53.3"W
   *
   * @return the longitude
   */
  public final String getLongitudeDms() {
    final double degrees = longitude.degrees();
    final double allSeconds = degrees % 1;
    final double minutes = Math.floor(allSeconds / 60.0);
    final double seconds = allSeconds % 60;
    final char ew = (degrees < 0.0) ? 'W' : 'E';
    return String.format("%g%s%g'%4.1g\"%c", Double.valueOf(degrees), degreeSymbol,
        Double.valueOf(minutes), Double.valueOf(seconds), Character.valueOf(ew));
  }

  /**
   * Midpoint between this coordinate and another location (on great circle
   * route).
   *
   * @param toLocation
   *          the other GPS coordinates
   * @return the GPS coordinates of the midpoint
   */
  public GpsCoordinates getMidpoint(final GpsCoordinates toLocation) {

    double deltaLongitude = toLocation.getLongitude().radians() - this.getLongitude().radians();

    double bxValue = Math.cos(toLocation.getLatitude().radians()) * Math.cos(deltaLongitude);
    double byValue = Math.cos(toLocation.getLatitude().radians()) * Math.sin(deltaLongitude);
    double midLatitude = Math.atan2(
        Math.sin(this.getLatitude().radians()) + Math.sin(toLocation.getLatitude().radians()),
        Math.sqrt((Math.cos(this.getLatitude().radians()) + bxValue)
            * (Math.cos(this.getLatitude().radians()) + bxValue) + byValue * byValue));
    double midLongitude = this.getLongitude().radians()
        + Math.atan2(byValue, Math.cos(this.getLatitude().radians()) + bxValue);

    return new GpsCoordinates(midLatitude, midLongitude);
  }

  /**
   * Get the GPS coordinates in JSON object format.
   *
   * @return the JSON object
   */
  @SuppressWarnings("unchecked")
  public final JSONObject toJson() {
    final JSONObject result = new JSONObject();
    result.put(JSON_KEY_LATITUDE, Double.valueOf(latitude.degrees()));
    result.put(JSON_KEY_LONGITUDE, Double.valueOf(longitude.degrees()));
    result.put(JSON_KEY_ELEVATION, Double.valueOf(elevation));
    return result;
  }
}
