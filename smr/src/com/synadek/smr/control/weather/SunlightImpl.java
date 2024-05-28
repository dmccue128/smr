/**
 * SunlightImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import com.synadek.core.AbstractComponent;
import com.synadek.core.ComponentException;
import java.time.Duration;

/**
 * Report the amount of sunlight reaching the vessel.
 */
public class SunlightImpl extends AbstractComponent implements Sunlight {

  private static final double MINUTES_PER_DAY = 1440;

  /**
   * Default constructor.
   */
  public SunlightImpl() {
    super("Sunlight");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.weather.Sunlight#solarPower()
   */
  @Override
  public float solarPower() {
    // TODO Implement sunlight sensing
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.weather.Sunlight#solarEnergy(int)
   */
  @Override
  public float solarEnergy(Duration duration) {
    // TODO implement reporting of aggregate sunlight received over a period of
    // time
    return 0.0f;
  }

  public double getJulianCentury(double julianDay) {
    return (julianDay - 2451545.0) / 36525.0;
  }

  private double getGeoMeanLongSun(double julianCentury) {
    double geoMeanLongSun = (280.46646 + julianCentury * (36000.76983 + julianCentury * 0.0003032))
        % 360.0;
    return geoMeanLongSun;
  }

  public double getGeoMeanAnomSun(double julianCentury) {
    double geoMeanAnomSun = 357.52911 + julianCentury * (35999.05029 - 0.0001537 * julianCentury);
    return geoMeanAnomSun;
  }

  private double getEccentEarthOrbit(double julianCentury) {
    double eccentEarthOrbit = 0.016708634
        - julianCentury * (0.000042037 + 0.0000001267 * julianCentury);
    return eccentEarthOrbit;
  }

  private double getSunEqOfCtr(double julianCentury) {
    double geoMeanAnomSun = getGeoMeanAnomSun(julianCentury);
    double sunEqOfCtr = Math.sin(Math.toRadians(geoMeanAnomSun))
        * (1.914602 - julianCentury * (0.004817 + 0.000014 * julianCentury))
        + Math.sin(Math.toRadians(2 * geoMeanAnomSun)) * (0.019993 - 0.000101 * julianCentury)
        + Math.sin(Math.toRadians(3 * geoMeanAnomSun)) * 0.000289;
    return sunEqOfCtr;
  }

  public double getSunTrueLong(double julianCentury) {
    return getGeoMeanLongSun(julianCentury) + getSunEqOfCtr(julianCentury);
  }

  private double getSunAppLong(double julianCentury) {
    double sunAppLong = getSunTrueLong(julianCentury) - 0.00569
        - 0.00478 * Math.sin(Math.toRadians(125.04 - 1934.136 * julianCentury));
    return sunAppLong;
  }

  /**
   * get mean Oblique Ecliptic (degrees).
   *
   * @param julianCentury
   *          time as julian century
   * @return the oblique ecliptic
   */
  public double getMeanObliqueEcliptic(double julianCentury) {
    double meanObliqueEcliptic = 23.0 + (26.0 + ((21.448
        - julianCentury * (46.815 + julianCentury * (0.00059 - julianCentury * 0.001813)))) / 60.0)
        / 60.0;
    return meanObliqueEcliptic;
  }

  /**
   * get Oblique Corr (degrees).
   *
   * @param julianCentury
   *          time as julian century
   * @return oblique corr
   */
  public double getObliqueCorr(double julianCentury) {
    double obliqueCorr = getMeanObliqueEcliptic(julianCentury)
        + 0.00256 * Math.cos(Math.toRadians(125.04 - 1934.136 * julianCentury));
    return obliqueCorr;
  }

  private double getVarY(double julianCentury) {
    double obliqueCorr = getObliqueCorr(julianCentury);
    double halfCorrRadians = Math.toRadians(obliqueCorr) / 2.0;
    double tanCorr = Math.tan(halfCorrRadians);
    return tanCorr * tanCorr;
  }

  private double getEqOfTime(double julianCentury) {

    double varY = getVarY(julianCentury);
    double eccentEarthOrbit = getEccentEarthOrbit(julianCentury);
    double geoMeanLongSunRadians = Math.toRadians(getGeoMeanLongSun(julianCentury));
    double geoMeanAnomSunRadians = Math.toRadians(getGeoMeanAnomSun(julianCentury));

    double eqOfTime = 4.0 * Math.toDegrees(varY * Math.sin(2 * geoMeanLongSunRadians)
        - 2 * eccentEarthOrbit * Math.sin(geoMeanAnomSunRadians)
        + 4 * eccentEarthOrbit * varY * Math.sin(geoMeanLongSunRadians)
            * Math.cos(2 * geoMeanLongSunRadians)
        - 0.5 * varY * varY * Math.sin(4 * geoMeanLongSunRadians)
        - 1.25 * eccentEarthOrbit * eccentEarthOrbit * Math.sin(2 * geoMeanAnomSunRadians));
    return eqOfTime;
  }

  private double getHaSunrise(double lat, double julianCentury) {
    double sunDeclinRadians = Math.toRadians(getSunDeclin(julianCentury));
    double haSunrise = Math.toDegrees(Math.acos(Math.cos(Math.toRadians(90.833))
        / (Math.cos(Math.toRadians(lat)) * Math.cos(sunDeclinRadians))
        - Math.tan(Math.toRadians(lat)) * Math.tan(sunDeclinRadians)));
    return haSunrise;
  }

  public double getSolarNoon(double lon, double zone, double julianCentury) {
    return (720 - (4 * lon) - getEqOfTime(julianCentury) + (zone * 60)) / MINUTES_PER_DAY;
  }

  @Override
  public double getSunrise(double lon, double lat, double zone, double julianCentury) {
    double solarNoon = getSolarNoon(lon, zone, julianCentury);
    double haSunrise = getHaSunrise(lat, julianCentury);
    return solarNoon - haSunrise * 4.0 / MINUTES_PER_DAY;
  }

  @Override
  public double getSunset(double lon, double lat, double zone, double julianCentury) {
    double solarNoon = getSolarNoon(lon, zone, julianCentury);
    double haSunrise = getHaSunrise(lat, julianCentury);
    return solarNoon + haSunrise * 4.0 / MINUTES_PER_DAY;
  }

  @Override
  public double getSunlightDuration(double lat, double julianCentury) {
    return 8.0 * getHaSunrise(lat, julianCentury);
  }

  private double getTrueSolarTime(double lon, double zone, double pctOfDay, double julianCentury) {
    double minutesPastMidnight = pctOfDay * MINUTES_PER_DAY;
    double trueSolarTimeSubtotal = (minutesPastMidnight + getEqOfTime(julianCentury) + (4.0 * lon)
        - (60.0 * zone));
    return (trueSolarTimeSubtotal + MINUTES_PER_DAY) % MINUTES_PER_DAY;
  }

  public double getHourAngle(double lon, double zone, double pctOfDay, double julianCentury) {
    double quarterTime = getTrueSolarTime(lon, zone, pctOfDay, julianCentury) / 4.0;
    return quarterTime < 0 ? quarterTime + 180 : quarterTime - 180;
  }

  /**
   * Get the sun Declin (degrees).
   *
   * @param julianCentury
   *          time as julian century
   */
  public double getSunDeclin(double julianCentury) {
    double sunDeclin = Math
        .toDegrees(Math.asin(Math.sin(Math.toRadians(getObliqueCorr(julianCentury)))
            * Math.sin(Math.toRadians(getSunAppLong(julianCentury)))));
    return sunDeclin;
  }

  /**
   * Get the solar zenith angle (degrees).
   *
   * @param lat
   *          the latitude
   * @return the solar zenith angle
   */
  public double getSolarZenithAngle(double lon, double lat, double zone, double pctOfDay,
      double julianCentury) {
    double sunDeclinRadians = Math.toRadians(getSunDeclin(julianCentury));
    double latRadians = Math.toRadians(lat);
    double hourAngleRadians = Math.toRadians(getHourAngle(lon, zone, pctOfDay, julianCentury));
    double solarZenithAngle = Math
        .toDegrees(Math.acos(Math.sin(latRadians) * Math.sin(sunDeclinRadians)
            + Math.cos(latRadians) * Math.cos(sunDeclinRadians) * Math.cos(hourAngleRadians)));
    return solarZenithAngle;
  }

  /**
   * Get the solar azimuth angle (degrees).
   *
   * @param lat
   *          the latitude
   * @return the solar zenith angle
   */
  public double getSolarAzimuthAngle(double lon, double lat, double zone, double pctOfDay,
      double julianCentury) {

    double solarAzimuthAngle;

    if (getHourAngle(lon, zone, pctOfDay, julianCentury) > 0) {
      solarAzimuthAngle = (Math
          .toDegrees(Math.acos(((Math.sin(Math.toRadians(lat)) * Math
              .cos(Math.toRadians(getSolarZenithAngle(lon, lat, zone, pctOfDay, julianCentury))))
              - Math.sin(Math.toRadians(getSunDeclin(julianCentury))))
              / (Math.cos(Math.toRadians(lat)) * Math.sin(
                  Math.toRadians(getSolarZenithAngle(lon, lat, zone, pctOfDay, julianCentury))))))
          + 180.0) % 360;
    } else {
      solarAzimuthAngle = (540.0 - Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(lat))
          * Math.cos(Math.toRadians(getSolarZenithAngle(lon, lat, zone, pctOfDay, julianCentury))))
          - Math.sin(Math.toRadians(getSunDeclin(julianCentury))))
          / (Math.cos(Math.toRadians(lat)) * Math.sin(
              Math.toRadians(getSolarZenithAngle(lon, lat, zone, pctOfDay, julianCentury)))))))
          % 360;
    }

    return solarAzimuthAngle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#connect(boolean)
   */
  @Override
  public boolean connect(boolean sim) throws ComponentException {
    if (sim) {
      this.simulated = true;
      this.connected = true;
      return true;
    }
    // TODO Confirm the availability of sunlight sensing HW
    this.simulated = false;
    this.connected = true;
    return true;
  }

}
