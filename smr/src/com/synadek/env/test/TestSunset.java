/**
 * TestSunset.java
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
 * Test calculation of sunset at a location and day.
 */
public class TestSunset {
  /**
   * Test basic calculation of sunset from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {
    final LocalTime sunset = Solar.getSunset(TestSolar.testLatitude,
        TestSolar.testLongitude, TestSolar.testDateTime);
    int expectedHour = 16;
    int expectedMinute = 29;
    int expectedSecond = 54;

    assertEquals(Integer.valueOf(expectedHour), Integer.valueOf(sunset.getHour()));
    assertEquals(Integer.valueOf(expectedMinute), Integer.valueOf(sunset.getMinute()));
    assertEquals(Integer.valueOf(expectedSecond), Integer.valueOf(sunset.getSecond()));
  }

  /**
   * Test boundary condition in which an invalid (negative) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLatitude() throws InvalidValueException {
    final LocalTime sunset =
        Solar.getSunset(-360.0, TestSolar.testLongitude, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunset, but got: " + sunset);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLatitude() throws InvalidValueException {
    final LocalTime sunset =
        Solar.getSunset(360.0, TestSolar.testLongitude, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunset, but got: " + sunset);
  }

  /**
   * Test boundary condition in which an invalid (negative) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLongitude() throws InvalidValueException {
    final LocalTime sunset =
        Solar.getSunset(TestSolar.testLatitude, -360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunset, but got: " + sunset);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLongitude() throws InvalidValueException {
    final LocalTime sunset =
        Solar.getSunset(TestSolar.testLatitude, 360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunset, but got: " + sunset);
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    final LocalTime sunset =
        Solar.getSunset(TestSolar.testLatitude, TestSolar.testLongitude, null);
    System.out.println("Did not expect a value for sunset, but got: " + sunset);
  }
}
