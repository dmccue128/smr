/**
 * TestSolarElevationCorrected.java
 *
 * @author Daniel McCue
 */

package com.synadek.core.test;

import static org.junit.Assert.assertEquals;
import com.synadek.core.InvalidValueException;
import com.synadek.core.Solar;
import org.junit.Test;

/**
 * Test calculation of Solar Elevation Corrected for Atmospheric Refraction.
 */
public class TestSolarElevationCorrected {

  /**
   * Test basic calculation of solar elevation from a fixed, valid input.
   *
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(TestSolar.testLatitude,
        TestSolar.testLongitude, TestSolar.testDateTime);
    double expectedValue = 24.764535784028794;

    assertEquals(Double.valueOf(expectedValue), Double.valueOf(elevation));
  }

  /**
   * Test boundary condition in which an invalid (negative) latitude is
   * provided.
   *
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLatitude() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(-360, TestSolar.testLongitude,
        TestSolar.testDateTime);
    System.out.println("Did not expect a value for elevation, but got: " + elevation);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) latitude is
   * provided.
   *
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLatitude() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(360, TestSolar.testLongitude,
        TestSolar.testDateTime);
    System.out.println("Did not expect a value for elevation, but got: " + elevation);
  }

  /**
   * Test boundary condition in which an invalid (negative) longitude is
   * provided.
   *
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLongitude() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(TestSolar.testLatitude, -360,
        TestSolar.testDateTime);
    System.out.println("Did not expect a value for elevation, but got: " + elevation);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) longitude
   * is provided.
   *
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLongitude() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(TestSolar.testLatitude, 360,
        TestSolar.testDateTime);
    System.out.println("Did not expect a value for elevation, but got: " + elevation);
  }

  /**
   * Test boundary condition in which a null date/time parameter is provided.
   *
   * @throws InvalidValueException
   *           if input is null
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    double elevation = Solar.getSolarElevationCorrected(TestSolar.testLatitude,
        TestSolar.testLongitude, null);
    System.out.println("Did not expect a value for elevation, but got: " + elevation);
  }

}
