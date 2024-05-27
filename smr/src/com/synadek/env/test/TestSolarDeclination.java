/**
 * TestSolarDeclination.java
 *
 * @author Daniel
 */

package com.synadek.env.test;

import static org.junit.Assert.assertEquals;

import com.synadek.env.InvalidValueException;
import com.synadek.env.Solar;

import org.junit.Test;

/**
 * Test calculation of solar declination.
 */
public class TestSolarDeclination {

  /**
   * Test basic calculation of solar declination from a fixed, valid input.
   * 
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {

    double declination = Solar.getSolarDeclination(TestSolar.testDateTime);
    double expectedValue = -23.3716773;

    assertEquals(Double.valueOf(expectedValue), Double.valueOf(declination));
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   * 
   * @throws InvalidValueException
   *           if input is null
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    double declination = Solar.getSolarDeclination(null);
    System.out.println("Did not expect a value for declination, but got: " + declination);
  }
}
