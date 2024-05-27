/**
 * TestSunlight.java
 *
 * @author Daniel
 */

package com.synadek.env.test;

import static org.junit.Assert.assertEquals;

import com.synadek.env.InvalidValueException;
import com.synadek.env.Solar;

import java.time.Duration;

import org.junit.Test;

/**
 * Test calculation of duration of sunlight (minutes) in a location on a specific date.
 */
public class TestSunlight {

  /**
   * Test basic calculation of solar noon from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {
    final Duration sunlightDuration =
        Solar.getSunlightDuration(TestSolar.testLatitude, TestSolar.testDateTime);
    // Result should be nine hours, fifteen minutes and twenty seconds (9:15:20)
    final long expectedHours = 9;
    final long expectedMinutes = 15;
    final long expectedSeconds = 20;
    final long expectedTotalSeconds =
        (expectedHours * 3600) + (expectedMinutes * 60) + expectedSeconds;

    assertEquals(Long.valueOf(expectedTotalSeconds),
        Long.valueOf(sunlightDuration.getSeconds()));
  }

  /**
   * Test boundary condition in which an invalid (negative) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLatitude() throws InvalidValueException {
    final Duration sunlightDuration =
        Solar.getSunlightDuration(-360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunlight duration, but got: "
        + (sunlightDuration.getSeconds() / 60.0));
  }

  /**
   * Test boundary condition in which an invalid (excessively large) latitude is provided.
   * 
   * @throws InvalidValueException
   *           if latitude, longitude, or date/time is invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadLatitude() throws InvalidValueException {
    final Duration sunlightDuration =
        Solar.getSunlightDuration(360.0, TestSolar.testDateTime);
    System.out.println("Did not expect a value for sunlight duration, but got: "
        + (sunlightDuration.getSeconds() / 60.0));
  }

  /**
   * Test boundary condition in which a null date/time parameter is provided.
   * 
   * @throws InvalidValueException
   *           if input is null
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    final Duration sunlightDuration =
        Solar.getSunlightDuration(TestSolar.testLatitude, null);
    System.out.println("Did not expect a value for sunlight duration, but got: "
        + (sunlightDuration.getSeconds() / 60.0));
  }

}
