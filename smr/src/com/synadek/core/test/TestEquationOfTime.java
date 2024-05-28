/**
 * TestEquationOfTime.java
 *
 * @author Daniel
 */

package com.synadek.core.test;

import static org.junit.Assert.assertEquals;
import com.synadek.core.InvalidValueException;
import com.synadek.core.Solar;
import org.junit.Test;

/**
 * Test calculation of Solar Equation of Time.
 */
public class TestEquationOfTime {

  /**
   * Test basic Solar Equation of Time calculation from a fixed, valid input.
   *
   * @throws InvalidValueException
   *           if date/time is invalid
   */
  @Test
  public void testHappyPath() throws InvalidValueException {

    double eqOfTime = Solar.getEquationOfTime(TestSolar.testDateTime);

    // Expected value
    double expectedValue = 3.7022028325305647;

    assertEquals(Double.valueOf(expectedValue), Double.valueOf(eqOfTime));
  }

  /**
   * Test boundary condition in which a null input parameter is provided.
   *
   * @throws IllegalArgumentException
   *           if a null input parameter is provided
   */
  @Test(expected = NullPointerException.class)
  public void testNullInput() throws InvalidValueException {
    double eqOfTime = Solar.getEquationOfTime(null);
    System.out.println("Did not expect a value for Equation Of Time, but got: " + eqOfTime);
  }

}
