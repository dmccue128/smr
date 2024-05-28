/**
 * PidController.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

/**
 * Proportional Integral Differential (PID) controller. Thanks to Brett
 * Beauregard for the code and excellent explanation at
 * http://brettbeauregard.com/blog/2011/04/improving-the-beginners-pid-
 * introduction.
 * <p>
 * Example of use:
 * </p>
 * <p>
 * PIDController myPID(myActuator, minOut, maxOut, samplingMillis);
 * </p>
 * <p>
 * void loop() { myPID.setTarget(desiredValue); * myPID.Compute(); }
 * </p>
 */
public class PidController {

  /**
   * Manual mode for the controller.
   */
  public static final int MANUAL_MODE = 0;
  /**
   * Automatic mode for the controller.
   */
  public static final int AUTOMATIC_MODE = 1;

  /**
   * Timestamp of last update to PID controls.
   */
  private long lastTime;

  /**
   * Input value.
   */
  private float inputValue;
  /**
   * Output value.
   */
  private float outputValue;
  /**
   * Setpoint value.
   */
  private float setpointValue;

  /**
   * The actuator that is being controlled by the PIDController.
   */
  private final ControlledActuator actuator;

  /**
   * Integration term.
   */
  private float integrationTerm;
  /**
   * Last input value.
   */
  private float lastInput;
  /**
   * Proportional coefficient.
   */
  private float kp;
  /**
   * Integral coefficient.
   */
  private float ki;
  /**
   * Derivative coefficient.
   */
  private float kd;
  /**
   * Sample interval default set to 1 second.
   */
  private long sampleTimeMillis = 1000; // 1 sec
  /**
   * Minimum output.
   */
  private float outMin;
  /**
   * Maximum output.
   */
  private float outMax;
  /**
   * Auto-compute the in.
   */
  private boolean inAuto = false;

  /**
   * The controller.
   *
   * @param actor
   *          the actuator being controlled
   * @param minOutputValue
   *          minimum output value
   * @param maxOutputValue
   *          maximum output value
   * @param sampleIntervalMillis
   *          sample interval in milliseconds
   */
  public PidController(final ControlledActuator actor, final float minOutputValue,
      final float maxOutputValue, final long sampleIntervalMillis) {

    this.outMin = minOutputValue;
    this.outMax = maxOutputValue;
    this.sampleTimeMillis = sampleIntervalMillis;

    // Assign the actuator
    this.actuator = actor;

    // Initialize key parameters of the controller
    initialize();
  }

  /**
   * Initialize the controller.
   */
  private void initialize() {

    // Capture the current input value as the 'last input'
    this.lastInput = this.actuator.getInputValue();

    // Initialize the integration term to the (range-bounded) current output
    // value
    if (outputValue > outMax) {
      integrationTerm = outMax;
    } else if (outputValue < outMin) {
      integrationTerm = outMin;
    } else {
      integrationTerm = outputValue;
    }
  }

  /**
   * Set the target level for the controller.
   *
   * @param target
   *          the new level
   */
  public void setTarget(final float target) {
    if (target > this.outMax) {
      this.setpointValue = this.outMax;
    } else if (target < this.outMin) {
      this.setpointValue = this.outMin;
    } else {
      this.setpointValue = target;
    }
  }

  /**
   * Compute the next value and driver the actuator to that level.
   */
  public void compute() {

    if (!inAuto) {
      return;
    }

    // Record the current time
    final long now = System.currentTimeMillis();

    // Record the elapsed time (ms) since the last invocation of the
    // controller
    final long timeChange = (now - lastTime);

    // If at least sampleTime milliseconds have passed, update the
    // controls.
    if (timeChange >= sampleTimeMillis) {

      // Compute the error between current value and setpoint value
      final float error = setpointValue - actuator.getInputValue();

      // Compute the integral term and be sure it is in range
      integrationTerm += (ki * error);
      if (integrationTerm > outMax) {
        integrationTerm = outMax;
      } else if (integrationTerm < outMin) {
        integrationTerm = outMin;
      }

      // Compute the derivative term
      float derivativeInput = (inputValue - lastInput);

      // Compute PID Output
      outputValue = kp * error + integrationTerm - kd * derivativeInput;

      // Remember some variables for next time
      this.lastInput = inputValue;
      this.lastTime = now;
    }

    // Force output value to be in range
    if (outputValue > outMax) {
      outputValue = outMax;
    } else if (outputValue < outMin) {
      outputValue = outMin;
    }

    // Drive the new output value to the actuator
    this.actuator.setOutputValue(outputValue);
  }

  /**
   * Update tuning parameters for PID.
   *
   * @param kpCoefficient
   *          - Proportional coefficient
   * @param kiCoefficient
   *          - Integral coefficient
   * @param kdCoefficient
   *          - Derivative coefficient
   */
  public void setTunings(float kpCoefficient, float kiCoefficient, float kdCoefficient) {
    final float sampleTimeInSec = sampleTimeMillis / 1000.0f;
    kp = kpCoefficient;
    ki = kiCoefficient * sampleTimeInSec;
    kd = kdCoefficient / sampleTimeInSec;
  }

  /**
   * Set/update the sample time interval.
   *
   * @param newSampleTime
   *          the new sample interval in milliseconds
   */
  public void setSampleTime(int newSampleTime) {
    if (newSampleTime > 0) {
      float ratio = newSampleTime / sampleTimeMillis;
      ki *= ratio;
      kd /= ratio;
      sampleTimeMillis = newSampleTime;
    }
  }

  /**
   * Update the minimum and maximum acceptable values for output.
   *
   * @param minOutput
   *          is the new minimum value
   * @param maxOutput
   *          is the new maximum value
   */
  void setOutputLimits(float minOutput, float maxOutput) {

    // Basic integrity check
    if (minOutput > maxOutput) {
      return;
    }

    // Update the min and max parameters for this PID Controller
    outMin = minOutput;
    outMax = maxOutput;

    // If current output value is out of range of new min/max, bring it back
    // in range
    if (outputValue > outMax) {
      outputValue = outMax;
    } else if (outputValue < outMin) {
      outputValue = outMin;
    }

    // If current iTerm is out of range of the new min/max, bring it back in
    // range
    if (integrationTerm > outMax) {
      integrationTerm = outMax;
    } else if (integrationTerm < outMin) {
      integrationTerm = outMin;
    }
  }

  /**
   * Switch from manual to automatic mode or vice versa.
   *
   * @param newMode
   *          is the new mode to set
   */
  public void setMode(int newMode) {
    boolean newAuto = (newMode == AUTOMATIC_MODE);
    if (newAuto && !inAuto) { /* we just went from manual to auto */
      initialize();
    }
    inAuto = newAuto;
  }

}
