/**
 * ControlledActuator.java 
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

/**
 * Interface for an actuator that can be controlled by a PID controller.
 */
public interface ControlledActuator {

  /**
   * Get the current value of the actuator.
   *
   * @return the value
   */
  float getInputValue();

  /**
   * Set the desired output value of the actuator.
   *
   * @param newValue
   *          the desired value
   */
  void setOutputValue(float newValue);

}
