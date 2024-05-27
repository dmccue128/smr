/**
 * Solar.java
 * 10 Dec 2017
 * @author Daniel McCue
 * 
 * Copyright 2017 Daniel McCue
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.synadek.core;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.Validate;

/**
 * Using the equations provided by NOAA, compute various attributes of sunlight
 * at a point on the earth at a point in time.
 */
public class Solar {

  /**
   * Useful constant for calculations.
   */
  private static final double MINUTES_PER_DAY = 1440;

  /**
   * All methods are static so there is no need to expose a constructor.
   */
  private Solar() {

  }

  /**
   * Utility method to round results to a fixed number of decimal places.
   */
  private static double round(final double val) {
    return BigDecimal.valueOf(val).doubleValue();
  }

  /**
   * Utility method to assess whether a value is in the valid range of a latitude.
   * Latitude here is in decimal degrees from 90.0 (the North Pole) to -90.0 (the
   * South Pole). The equator is at latitude 0.0.
   * 
   * @param val
   *          the value
   * @throws IllegalArgumentException
   *           if the value could not represent a valid latitude (-90...+90)
   */
  private static void validateLatitude(final double val) {
    Validate.inclusiveBetween(-90.0, 90.0, val);
  }

  /**
   * Utility method to assess whether a value is in the valid range of a
   * longitude. Longitude here is in decimal degrees from -180.0 westward from the
   * Prime Meridian to +180.0 eastward. The Prime Meridian (passing through
   * Greenwich, England) is at Longitude 0.0
   * 
   * @param val
   *          the value
   * @throws IllegalArgumentException
   *           if the value could not represent a valid longitude (-180..+180)
   */
  private static void validateLongitude(final double val) {
    Validate.inclusiveBetween(-180, 180, val);
  }

  /**
   * Utility method to assess whether a value is in the valid range of date/time
   * supported by this library.
   * 
   * @param val
   *          the value
   * @throws NullPointerException
   *           if the value is null
   * @throws IllegalArgumentException
   *           if the value is a date/time out of range of this library
   */
  private static void validateDateTime(final OffsetDateTime val) {
    Validate.notNull(val);
    Validate.isTrue(val.getYear() >= 1900,
        "Dates prior to January 1, 1900 are not supported by this library. The date %s is not supported",
        val.format(DateTimeFormatter.RFC_1123_DATE_TIME));
  }

  /**
   * Convert a date and time to a Julian date.
   * 
   * @param datetime
   *          the date and time with offset from UTC
   * @return the Julian date rounded to six decimal places
   * @throws IllegalArgumentException
   *           if date/time represents a date not supported by this library i.e.
   *           before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double computeJulianDay(final OffsetDateTime datetime) {

    // Validate the input parameter for date/time
    validateDateTime(datetime);

    double val = datetime.toEpochSecond() / 86400.0 + 2440587.5;
    return round(val);
  }

  /**
   * Convert a Julian day to Julian century.
   * 
   * @param julianDay
   *          the day
   * @return the same date as a Julian century
   */
  private static double getJulianCentury(final double julianDay) {
    return (julianDay - 2451545.0) / 36525.0;
  }

  /**
   * Compute the geocentric mean longitude of the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the geometric mean (degrees)
   */
  private static double getGeoMeanLongSun(final double julianCentury) {
    double geoMeanLongSun =
        (280.46646 + julianCentury * (36000.76983 + julianCentury * 0.0003032)) % 360.0;
    return geoMeanLongSun;
  }

  /**
   * Compute the geocentric mean anomaly of the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the mean anomaly (degrees)
   */
  private static double getGeoMeanAnomSun(final double julianCentury) {
    double geoMeanAnomSun = 357.52911 + julianCentury * (35999.05029 - 0.0001537 * julianCentury);
    return geoMeanAnomSun;
  }

  /**
   * Compute the eccentricity of earth's orbit.
   * 
   * @param julianCentury
   *          the date
   * @return the eccentricity of earth's orbit
   */
  private static double getEccentEarthOrbit(final double julianCentury) {
    double eccentEarthOrbit =
        0.016708634 - julianCentury * (0.000042037 + 0.0000001267 * julianCentury);
    return eccentEarthOrbit;
  }

  /**
   * Compute the equation of the center of the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the equation of the center
   */
  private static double getSunEqOfCtr(final double julianCentury) {
    double geoMeanAnomSun = getGeoMeanAnomSun(julianCentury);
    double sunEqOfCtr = Math.sin(Math.toRadians(geoMeanAnomSun))
        * (1.914602 - julianCentury * (0.004817 + 0.000014 * julianCentury))
        + Math.sin(Math.toRadians(2 * geoMeanAnomSun)) * (0.019993 - 0.000101 * julianCentury)
        + Math.sin(Math.toRadians(3 * geoMeanAnomSun)) * 0.000289;
    return sunEqOfCtr;
  }

  /**
   * Compute the true longitude of the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the longitude (degrees)
   */
  private static double getSunTrueLong(final double julianCentury) {
    return getGeoMeanLongSun(julianCentury) + getSunEqOfCtr(julianCentury);
  }

  /**
   * Compute the true anomoly of the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the anomaly (degrees)
   */
  // Not actually used in any of these calculations, this method commented out
  // private static double getSunTrueAnom(final double julianCentury) {
  // return getGeoMeanAnomSun(julianCentury) + getSunEqOfCtr(julianCentury);
  // }

  /**
   * Compute the apparent longitude of the sun, accounting for atmospheric
   * refraction.
   * 
   * @param julianCentury
   *          the date
   * @return the apparent longitude (degrees)
   */
  private static double getSunAppLong(final double julianCentury) {
    double sunAppLong = getSunTrueLong(julianCentury) - 0.00569
        - 0.00478 * Math.sin(Math.toRadians(125.04 - 1934.136 * julianCentury));
    return sunAppLong;
  }

  /**
   * Compute mean Oblique Ecliptic.
   * 
   * @param julianCentury
   *          time as julian century
   * @return the oblique ecliptic (degrees)
   */
  private static double getMeanObliqueEcliptic(final double julianCentury) {
    double meanObliqueEcliptic = 23.0 + (26.0 + ((21.448
        - julianCentury * (46.815 + julianCentury * (0.00059 - julianCentury * 0.001813)))) / 60.0)
        / 60.0;
    return meanObliqueEcliptic;
  }

  /**
   * Compute the oblique correction for the sun (degrees).
   * 
   * @param julianCentury
   *          time as julian century
   * @return oblique correction (degrees)
   */
  private static double getObliqueCorr(final double julianCentury) {
    double obliqueCorr = getMeanObliqueEcliptic(julianCentury)
        + 0.00256 * Math.cos(Math.toRadians(125.04 - 1934.136 * julianCentury));
    return obliqueCorr;
  }

  /**
   * Compute the variance.
   * 
   * @param julianCentury
   *          the date
   * @return the variance
   */
  private static double getVarY(final double julianCentury) {
    double obliqueCorr = getObliqueCorr(julianCentury);
    double halfCorrRadians = Math.toRadians(obliqueCorr) / 2.0;
    double tanCorr = Math.tan(halfCorrRadians);
    return tanCorr * tanCorr;
  }

  /**
   * Compute the equation of time for the sun.
   * 
   * @param julianCentury
   *          the date
   * @return the equation of time (minutes)
   */
  private static double getEquationOfTime(final double julianCentury) {
    double varY = getVarY(julianCentury);
    double eccentEarthOrbit = getEccentEarthOrbit(julianCentury);
    double longSunRadians = Math.toRadians(getGeoMeanLongSun(julianCentury));
    double anomSunRadians = Math.toRadians(getGeoMeanAnomSun(julianCentury));
    // double longSunRadians = Math.toRadians(getSunTrueLong(julianCentury));
    // double anomSunRadians = Math.toRadians(getSunTrueAnom(julianCentury));

    double eqOfTime = 4.0 * Math.toDegrees(varY * Math.sin(2.0 * longSunRadians)
        - 2.0 * eccentEarthOrbit * Math.sin(anomSunRadians)
        + 4.0 * eccentEarthOrbit * varY * Math.sin(anomSunRadians) * Math.cos(2.0 * longSunRadians)
        - 0.5 * varY * varY * Math.sin(4.0 * longSunRadians)
        - 1.25 * eccentEarthOrbit * eccentEarthOrbit * Math.sin(2.0 * anomSunRadians));
    return eqOfTime;
  }

  /**
   * Compute the equation of time for the sun.
   * 
   * @param datetime
   *          the date and time with offset from UTC
   * @return the equation of time (minutes)
   * @throws IllegalArgumentException
   *           if date/time represents a date not supported by this library i.e.
   *           before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double getEquationOfTime(final OffsetDateTime datetime) {

    // Validate the input parameter for date/time
    validateDateTime(datetime);

    double julianDay = computeJulianDay(datetime);
    double julianCentury = getJulianCentury(julianDay);
    return round(getEquationOfTime(julianCentury));
  }

  /**
   * Compute the hour angle at sunrise.
   * 
   * @param lat
   *          the latitude (degrees)
   * @param julianCentury
   *          the date
   * @return the hour angle at sunrise (degrees)
   */
  private static double getHaSunrise(final double lat, final double julianCentury) {
    double sunDeclinRadians = Math.toRadians(getSolarDeclination(julianCentury));
    double latRadians = Math.toRadians(lat);
    double haSunrise = Math.toDegrees(Math
        .acos(Math.cos(Math.toRadians(90.833)) / (Math.cos(latRadians) * Math.cos(sunDeclinRadians))
            - Math.tan(latRadians) * Math.tan(sunDeclinRadians)));
    return haSunrise;
  }

  /**
   * Compute solar noon given a longitude and time.
   * 
   * @param lon
   *          the longitude
   * @param zone
   *          the zone offset
   * @param julianCentury
   *          the date
   * @return solar noon (LST)
   */
  private static double getSolarNoon(final double lon, final double zone,
      final double julianCentury) {
    return (720 - (4.0 * lon) - getEquationOfTime(julianCentury) + (zone * 60.0)) / MINUTES_PER_DAY;
  }

  /**
   * Compute solar noon given a longitude and time.
   * 
   * @param lon
   *          the longitude
   * @param dateTime
   *          the date and time with offset from UTC
   * @return solar noon (LST)
   * @throws IllegalArgumentException
   *           if longitude is invalid or date/time represents a date not
   *           supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static LocalTime getSolarNoon(final double lon, final OffsetDateTime dateTime) {

    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    double julianDay = computeJulianDay(dateTime);
    double julianCentury = getJulianCentury(julianDay);
    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    double pctOfDay = getSolarNoon(lon, zone, julianCentury);
    return LocalTime.ofSecondOfDay(Math.round(pctOfDay * 24.0 * 60.0 * 60.0));

  }

  /**
   * Compute the time of sunrise from a location and time.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param datetime
   *          the date and time with offset from UTC the date
   * @return the local time of sunrise
   * @throws IllegalArgumentException
   *           if longitude is invalid or date/time represents a date not
   *           supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static LocalTime getSunrise(final double lat, final double lon,
      final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    double solarNoon = getSolarNoon(lon, zone, julianCentury);
    double haSunrise = getHaSunrise(lat, julianCentury);
    double hours = (solarNoon - haSunrise * 4.0 / MINUTES_PER_DAY) * 24;
    return LocalTime.ofSecondOfDay(Math.round(hours * 60.0 * 60.0));
  }

  /**
   * Compute the time of sunset from a location and time.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param datetime
   *          the date and time with offset from UTC
   * @return the local time of sunset
   * @throws IllegalArgumentException
   *           if longitude is invalid or date/time represents a date not
   *           supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static LocalTime getSunset(final double lat, final double lon,
      final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    double solarNoon = getSolarNoon(lon, zone, julianCentury);
    double haSunrise = getHaSunrise(lat, julianCentury);
    double hours = (solarNoon + haSunrise * 4.0 / MINUTES_PER_DAY) * 24;

    return LocalTime.ofSecondOfDay(Math.round(hours * 60.0 * 60.0));
  }

  /**
   * Compute the duration of sunlight given a latitude and a date.
   * 
   * @param lat
   *          the latitude
   * @param datetime
   *          the date and time with offset from UTC
   * @return the duration of sunlight (minutes)
   * @throws IllegalArgumentException
   *           if latitude is invalid or date/time represents a date not supported
   *           by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static Duration getSunlightDuration(final double lat, final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    double minutes = 8.0 * getHaSunrise(lat, julianCentury);
    long seconds = Math.round(minutes * 60.0);
    return Duration.ofSeconds(seconds);
  }

  /**
   * Compute the true solar time.
   * 
   * @param lon
   *          the longitude
   * @param zone
   *          the zone offset
   * @param minutesPastMidnight
   *          the time of day
   * @param julianCentury
   *          the date
   * @return the true solar time (minutes)
   */
  private static double getTrueSolarTime(final double lon, final double zone,
      final int minutesPastMidnight, final double julianCentury) {
    double trueSolarTimeSubtotal =
        (minutesPastMidnight + getEquationOfTime(julianCentury) + (4.0 * lon) - (60.0 * zone));
    return (trueSolarTimeSubtotal + MINUTES_PER_DAY) % MINUTES_PER_DAY;
  }

  /**
   * Compute the hour angle of the sun.
   * 
   * @param lon
   *          the longitude
   * @param zone
   *          the zone offset
   * @param minutesPastMidnight
   *          the time of day
   * @param julianCentury
   *          the date
   * @return the hour angle of the sun (degrees)
   */
  private static double getHourAngle(final double lon, final double zone,
      final int minutesPastMidnight, final double julianCentury) {
    double quarterTime = getTrueSolarTime(lon, zone, minutesPastMidnight, julianCentury) / 4.0;
    return quarterTime < 0.0 ? quarterTime + 180.0 : quarterTime - 180.0;
  }

  /**
   * Compute the declination of the sun.
   * 
   * @param julianCentury
   *          time as julian century
   * @return the declination (degrees)
   */
  private static double getSolarDeclination(final double julianCentury) {
    double sunDeclin =
        Math.toDegrees(Math.asin(Math.sin(Math.toRadians(getObliqueCorr(julianCentury)))
            * Math.sin(Math.toRadians(getSunAppLong(julianCentury)))));
    return sunDeclin;
  }

  /**
   * Compute the declination of the sun.
   * 
   * @param dateTime
   *          the date and time with offset from UTC
   * @return the declination (degrees)
   * @throws IllegalArgumentException
   *           if date/time represents a date not supported by this library i.e.
   *           before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double getSolarDeclination(final OffsetDateTime dateTime) {

    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    double julianDay = computeJulianDay(dateTime);
    double julianCentury = getJulianCentury(julianDay);
    return round(getSolarDeclination(julianCentury));
  }

  /**
   * Compute the solar zenith angle (degrees).
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param zone
   *          the time zone offset
   * @param minutesPastMidnight
   *          number of minutes past midnight in the day
   * @param julianCentury
   *          the date
   * @return the solar zenith angle (degrees)
   */
  private static double getSolarZenithAngle(final double lat, final double lon, final double zone,
      final int minutesPastMidnight, final double julianCentury) {
    double sunDeclinRadians = Math.toRadians(getSolarDeclination(julianCentury));
    double latRadians = Math.toRadians(lat);
    double hourAngleRadians =
        Math.toRadians(getHourAngle(lon, zone, minutesPastMidnight, julianCentury));
    double solarZenithAngle =
        Math.toDegrees(Math.acos(Math.sin(latRadians) * Math.sin(sunDeclinRadians)
            + Math.cos(latRadians) * Math.cos(sunDeclinRadians) * Math.cos(hourAngleRadians)));
    return solarZenithAngle;
  }

  /**
   * Compute the solar elevation angle.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param zone
   *          the time zone offset
   * @param minutesPastMidnight
   *          number of minutes past midnight in the day
   * @param julianCentury
   *          the date
   * @return the solar zenith angle (degrees)
   */
  private static double getSolarElevation(final double lat, final double lon, final double zone,
      final int minutesPastMidnight, final double julianCentury) {
    return 90.0 - getSolarZenithAngle(lat, lon, zone, minutesPastMidnight, julianCentury);
  }

  /**
   * Compute the solar elevation angle.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param dateTime
   *          the date and time with offset from UTC
   * @return the solar zenith angle (degrees)
   * @throws IllegalArgumentException
   *           if longitude is invalid or date/time represents a date not
   *           supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double getSolarElevation(final double lat, final double lon,
      final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    final int minutesPastMidnight = dateTime.getHour() * 60 + dateTime.getMinute();
    return round(getSolarElevation(lat, lon, zone, minutesPastMidnight, julianCentury));
  }

  /**
   * Compute approximate atmospheric refraction.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param zone
   *          the time zone offset
   * @param minutesPastMidnight
   *          number of minutes past midnight in the day
   * @param julianCentury
   *          the date
   * @return the approximate atmospheric refraction (degrees)
   */
  private static double getApproxAtmosphericRefraction(final double lat, final double lon,
      final double zone, final int minutesPastMidnight, final double julianCentury) {

    double solarElevationAngle =
        getSolarElevation(lat, lon, zone, minutesPastMidnight, julianCentury);
    double approxAtmosphericRefraction = 0.0;

    if (solarElevationAngle > 85.0) {
      approxAtmosphericRefraction = 0.0;
    } else if (solarElevationAngle > 5.0) {
      approxAtmosphericRefraction = 58.1 / Math.tan(Math.toRadians(solarElevationAngle))
          - 0.07 / Math.pow(Math.tan(Math.toRadians(solarElevationAngle)), 3.0)
          + 0.000086 / Math.pow(Math.tan(Math.toRadians(solarElevationAngle)), 5.0);
    } else if (solarElevationAngle > -0.575) {
      approxAtmosphericRefraction = 1735.0 + solarElevationAngle * (-518.2 + solarElevationAngle
          * (103.4 + solarElevationAngle * (-12.79 + solarElevationAngle * 0.711)));
    } else {
      approxAtmosphericRefraction = -20.772 / Math.tan(Math.toRadians(solarElevationAngle));
    }

    approxAtmosphericRefraction = approxAtmosphericRefraction / 3600.0;

    return approxAtmosphericRefraction;
  }

  /**
   * Compute solar elevation corrected for atmospheric refraction.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param zone
   *          the time zone offset
   * @param minutesPastMidnight
   *          number of minutes past midnight in the day
   * @param julianCentury
   *          the date
   * @return the corrected solar elevation (degrees)
   */
  private static double getSolarElevationCorrected(final double lat, final double lon,
      final double zone, final int minutesPastMidnight, final double julianCentury) {
    return getSolarElevation(lat, lon, zone, minutesPastMidnight, julianCentury)
        + getApproxAtmosphericRefraction(lat, lon, zone, minutesPastMidnight, julianCentury);
  }

  /**
   * Compute the solar elevation angle.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param dateTime
   *          the date and time with offset from UTC
   * @return the solar zenith angle (degrees)
   * @throws IllegalArgumentException
   *           if latitude or longitude is invalid or datetime represents a date
   *           not supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double getSolarElevationCorrected(final double lat, final double lon,
      final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    final int minutesPastMidnight = dateTime.getHour() * 60 + dateTime.getMinute();
    return round(getSolarElevationCorrected(lat, lon, zone, minutesPastMidnight, julianCentury));
  }

  /**
   * Compute the solar azimuth angle (degrees).
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param zone
   *          the time zone offset
   * @param minutesPastMidnight
   *          number of minutes past midnight in the day
   * @param julianCentury
   *          the date
   * @return the solar zenith angle (degrees CW from N)
   */
  private static double getSolarAzimuth(final double lat, final double lon, final double zone,
      final int minutesPastMidnight, final double julianCentury) {

    double solarZenithAngleDegrees =
        getSolarZenithAngle(lat, lon, zone, minutesPastMidnight, julianCentury);
    double solarZenithAngleRadians = Math.toRadians(solarZenithAngleDegrees);

    double latRadians = Math.toRadians(lat);
    double declinRadians = Math.toRadians(getSolarDeclination(julianCentury));
    double hourAngle = getHourAngle(lon, zone, minutesPastMidnight, julianCentury);

    double solarAzimuthAngle;
    if (hourAngle > 0.0) {
      solarAzimuthAngle = (Math.toDegrees(Math.acos(
          ((Math.sin(latRadians) * Math.cos(solarZenithAngleRadians)) - Math.sin(declinRadians))
              / (Math.cos(latRadians) * Math.sin(solarZenithAngleRadians))))
          + 180.0) % 360.0;
    } else {
      solarAzimuthAngle = (540.0 - Math.toDegrees(Math.acos(
          ((Math.sin(latRadians) * Math.cos(solarZenithAngleRadians)) - Math.sin(declinRadians))
              / (Math.cos(latRadians) * Math.sin(solarZenithAngleRadians)))))
          % 360.0;
    }

    return solarAzimuthAngle;
  }

  /**
   * Get solar azimuth.
   * 
   * @param lat
   *          the latitude
   * @param lon
   *          the longitude
   * @param dateTime
   *          the date and time with offset from UTC
   * @throws IllegalArgumentException
   *           if latitude or longitude is invalid or date/time represents a date
   *           not supported by this library i.e. before 1900
   * @throws NullPointerException
   *           if date/time is null
   */
  public static double getSolarAzimuth(final double lat, final double lon,
      final OffsetDateTime dateTime) {

    // Validate the input parameter for latitude
    validateLatitude(lat);
    // Validate the input parameter for longitude
    validateLongitude(lon);
    // Validate the input parameter for date/time
    validateDateTime(dateTime);

    final double zone = dateTime.getOffset().getTotalSeconds() / 3600.0;
    final double julianCentury = getJulianCentury(computeJulianDay(dateTime));
    final int minutesPastMidnight = dateTime.getHour() * 60 + dateTime.getMinute();
    return round(getSolarAzimuth(lat, lon, zone, minutesPastMidnight, julianCentury));
  }

}
