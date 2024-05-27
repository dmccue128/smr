/**
 * TestJulianDay.java
 *
 * @author Daniel
 */

package com.synadek.core.test;

import static org.junit.Assert.assertEquals;

import com.synadek.core.InvalidValueException;
import com.synadek.core.Solar;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

/**
 * Test solar calculations.
 */
public class TestJulianDay {

  /**
   * Test basic calculation of Julian day from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {

    double julianDay = Solar.computeJulianDay(TestSolar.testDateTime);

    double expectedValue = 2458105.1666666665;
    assertEquals(Double.valueOf(expectedValue), Double.valueOf(julianDay));
  }

  /**
   * Test basic calculation of Julian day from ZoneOffset zero.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testZoneZero() throws InvalidValueException {

    // Create a new date/time similar to testDateTime but with offset zero
    final OffsetDateTime zeroDateTime = OffsetDateTime
        .of(TestSolar.testDateTime.toLocalDateTime(), ZoneOffset.ofHours(0));

    double julianDay = Solar.computeJulianDay(zeroDateTime);

    double expectedValue = 2458104.95833333335;
    assertEquals(Double.valueOf(expectedValue), Double.valueOf(julianDay));
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   * 
   * @throws InvalidValueException
   *           if input is null
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    double julianDay = Solar.computeJulianDay(null);
    System.out.println("Did not expect a value for julianDay, but got: " + julianDay);
  }

}
