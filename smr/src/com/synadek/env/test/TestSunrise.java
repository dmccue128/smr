/**
 * TestSunrise.java
 *
 * @author Daniel
 */

package com.synadek.env.test;

import static org.junit.Assert.assertEquals;

import com.synadek.env.InvalidValueException;
import com.synadek.env.Solar;

import java.time.LocalTime;

import org.junit.Test;

/**
 * Test calculation of sunrise at a location and time.
 */
public class TestSunrise {
  /**
   * Test basic calculation of sunrise from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {
    final LocalTime sunrise = Solar.getSunrise(TestSolar.testLatitude,
        TestSolar.testLongitude, TestSolar.testDateTime);
    int expectedHour = 7;
    int expectedMinute = 14;
    int expectedSecond = 34;

    assertEquals(Integer.valueOf(expectedHour), Integer.valueOf(sunrise.getHour()));
    assertEquals(Integer.valueOf(expectedMinute), Integer.valueOf(sunrise.getMinute()));
    assertEquals(Integer.valueOf(expectedSecond), Integer.valueOf(sunrise.getSecond()));
  }

  /**
   * Test boundary condition in which an invalid (negative) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLatitude() throws InvalidValueException {
    final LocalTime sunrise =
        Solar.getSunrise(-360.0, TestSolar.testLongitude, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunrise, but got: " + sunrise);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLatitude() throws InvalidValueException {
    final LocalTime sunrise =
        Solar.getSunrise(360.0, TestSolar.testLongitude, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunrise, but got: " + sunrise);
  }

  /**
   * Test boundary condition in which an invalid (negative) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLongitude() throws InvalidValueException {
    final LocalTime sunrise =
        Solar.getSunrise(TestSolar.testLatitude, -360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunrise, but got: " + sunrise);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLongitude() throws InvalidValueException {
    final LocalTime sunrise =
        Solar.getSunrise(TestSolar.testLatitude, 360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunrise, but got: " + sunrise);
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    final LocalTime sunrise =
        Solar.getSunrise(TestSolar.testLatitude, TestSolar.testLongitude, null);
    System.out.println("Did not expect a value for sunrise, but got: " + sunrise);
  }

}
