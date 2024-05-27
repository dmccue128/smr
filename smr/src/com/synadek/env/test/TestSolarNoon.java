/**
 * TestSolarNoon.java
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
 * Test calculation of Solar Noon.
 */
public class TestSolarNoon {

  /**
   * Test basic calculation of solar noon from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {
    final LocalTime solarNoon =
        Solar.getSolarNoon(TestSolar.testLongitude, TestSolar.testDateTime);
    int expectedHour = 11;
    int expectedMinute = 52;
    int expectedSecond = 14;

    assertEquals(Integer.valueOf(expectedHour), Integer.valueOf(solarNoon.getHour()));
    assertEquals(Integer.valueOf(expectedMinute), Integer.valueOf(solarNoon.getMinute()));
    assertEquals(Integer.valueOf(expectedSecond), Integer.valueOf(solarNoon.getSecond()));
  }

  /**
   * Test boundary condition in which an invalid (negative) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLongitude() throws InvalidValueException {
    final LocalTime solarNoon = Solar.getSolarNoon(-360, TestSolar.testDateTime);
    System.out.println("Did not expect a value for solar noon, but got: " + solarNoon);
  }

  /**
   * Test boundary condition in which an invalid (excessively large) longitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLongitude() throws InvalidValueException {
    final LocalTime solarNoon = Solar.getSolarNoon(360, TestSolar.testDateTime);
    System.out.println("Did not expect a value for solar noon, but got: " + solarNoon);
  }

  /**
   * Test boundary condition in which a null date/time parameter is provided.
   * 
   * @throws InvalidValueException
   *           if input is null
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    final LocalTime solarNoon = Solar.getSolarNoon(TestSolar.testLongitude, null);
    System.out.println("Did not expect a value for solar noon, but got: " + solarNoon);
  }
}
