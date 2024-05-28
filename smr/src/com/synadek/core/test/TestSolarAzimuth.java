/**
 * TestSolarAzimuth.java
 *
 * @author Daniel
 */

package com.synadek.core.test;

import static org.junit.Assert.assertEquals;
import com.synadek.core.Solar;
import org.junit.Test;

/**
 * Test calculation of Solar Azimuth.
 */
public class TestSolarAzimuth {

  /**
   * Test basic calculation of solar azimuth from a fixed, valid input.
   */
  @Test
  public void testHappyPath() {

    double solarAzimuth = Solar.getSolarAzimuth(TestSolar.testLatitude, TestSolar.testLongitude,
        TestSolar.testDateTime);
    double expectedValue = 166.79904024565553;

    assertEquals(Double.valueOf(expectedValue), Double.valueOf(solarAzimuth));
  }

  /**
   * Test boundary condition in which an invalid (negative) latitude is
   * provided.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLatitude() {
    double azimuth = Solar.getSolarAzimuth(-360.0, TestSolar.testLongitude,
        TestSolar.testDateTime);
    System.out.println("Did not expect a value for Solar Azimuth, but got: " + azimuth);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) latitude is
   * provided.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLatitude() {
    double azimuth = Solar.getSolarAzimuth(360.0, TestSolar.testLongitude, TestSolar.testDateTime);
    System.out.println("Did not expect a value for Solar Azimuth, but got: " + azimuth);
  }

  /**
   * Test boundary condition in which an invalid (negative) longitude is
   * provided.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLongitude() {
    double azimuth = Solar.getSolarAzimuth(TestSolar.testLatitude, -360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for Solar Azimuth, but got: " + azimuth);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) longitude
   * is provided.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLongitude() {
    double azimuth = Solar.getSolarAzimuth(TestSolar.testLatitude, 360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for Solar Azimuth, but got: " + azimuth);
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    double azimuth = Solar.getSolarAzimuth(TestSolar.testLatitude, TestSolar.testLongitude, null);
    System.out.println("Did not expect a value for Solar Azimuth, but got: " + azimuth);
  }

}
