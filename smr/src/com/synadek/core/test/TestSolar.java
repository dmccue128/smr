/**
 * TestSolar.java
 * 17 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.core.test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests of solar calculations.
 */
@RunWith(Suite.class)
@SuiteClasses({ TestEquationOfTime.class, TestJulianDay.class, TestSolarAzimuth.class,
  TestSolarDeclination.class, TestSolarElevation.class, TestSolarElevationCorrected.class,
  TestSolarNoon.class, TestSunlight.class, TestSunrise.class, TestSunset.class, })

public class TestSolar {
  /**
   * Define some constants useful in this test suite.
   */
  private static final int year = 2017;
  private static final int month = 12;
  private static final int dayOfMonth = 17;
  private static final int hour = 11;
  private static final int minute = 0;
  private static final int second = 0;
  private static final int nanoOfSecond = 0;
  private static final ZoneOffset offset = ZoneOffset.ofHours(-5);

  public static final OffsetDateTime testDateTime = OffsetDateTime.of(year, month,
      dayOfMonth, hour, minute, second, nanoOfSecond, offset);

  // Use GPS coordinates for Times Square in NYC
  public static final double testLatitude = 40.758896;
  public static final double testLongitude = -73.985130;

  // The body of this class remains empty.
  // It is used only as a holder for the above declarations and annotations.
}
