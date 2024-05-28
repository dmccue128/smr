/**
 * AnchorImpl.java
 * 2 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import java.util.Locale;

import org.json.simple.JSONObject;

import com.synadek.core.ComponentException;
import com.synadek.core.ControlledActuator;
import com.synadek.core.JsonSchema;
import com.synadek.core.PidController;

/**
 * Controller for the vessel's anchor.
 */
public class AnchorImpl extends VesselComponentImpl implements Anchor, ControlledActuator {

	/**
	 * 
	 */
	private static final String JSON_KEY_MAX_SPEED = "maxSpeed";

	/**
	 * 
	 */
	public static final String JSON_KEY_MAX_DEPTH = "maxDepth";

	/**
	 * Motor Controller timing interval (ms).
	 */
	private static final long INTERVAL = 1000L;

	/**
	 * Default maximum depth of anchor i.e., length of anchor chain in meters.
	 */
	private static final float MAX_DEPTH = 5.0f;

	/**
	 * Default maximum desired anchor movement in meters per second.
	 */
	private static final float MAX_SPEED = 0.2f;

	/**
	 * Maximum negative value for anchor movement in a single step.
	 */
	private static final float MIN_MOVEMENT = -(MAX_SPEED * (INTERVAL / 1000));

	/**
	 * Maximum positive value for anchor movement in a single step.
	 */
	private static final float MAX_MOVEMENT = -MIN_MOVEMENT;

	/**
	 * Margin of error for anchor position (meters).
	 */
	private static final float ERROR_MARGIN = 0.1f;

	/**
	 * Maximum number of iterations of PID algorithm to attempt to move anchor to
	 * desired depth.
	 */
	protected static final int MAX_ITERATIONS = 100;

	/**
	 * Track the extent of anchor chain (meters) that is out.
	 */
	private float anchorDepth;

	/**
	 * Motor controller value.
	 */
	private float motorValue;

	/**
	 * Default constructor.
	 */
	public AnchorImpl() {
		super(VesselComponentType.VESSEL_ANCHOR, "anchor", "Controller for the vessel's anchor.");
		this.anchorDepth = 0.0f;
		this.motorValue = 0.0f;
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public AnchorImpl(final String name) {
		super(VesselComponentType.VESSEL_ANCHOR, name);
		this.anchorDepth = 0.0f;
		this.motorValue = 0.0f;
		resetConfiguration();
	}

	/**
	 * Connect parameter indicates whether to connect to a physical or simulated
	 * component.
	 *
	 * @param sim true if the connection is to a simulation of the component
	 * @throws ComponentException if an error occurs
	 */
	@Override
	public boolean connect(final boolean sim) throws ComponentException {

		// Simulation is not yet supported for this component
		if (sim) {
			log.error(ERR_SIM_NOT_AVAIL);
			return false;
		}

		this.simulated = false;
		this.connected = true;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.vessel.Anchor#anchorDepth()
	 */
	@Override
	public float anchorDepth() {
		return this.anchorDepth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.vessel.Anchor#raiseAnchor()
	 */
	@Override
	public void raiseAnchor() {
		drawAnchor(0.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.vessel.Anchor#lowerAnchor(float)
	 */
	@Override
	public void lowerAnchor(float desiredDepth) {
		drawAnchor(desiredDepth);
	}

	/**
	 * drawAnchor is the utility method that moves the anchor to a desired depth by
	 * driving the anchor motor. Note: depth really only indicates the length of
	 * anchor chain extended from the vessel. In shallow water, the anchor may not
	 * achieve this vertical depth in the water and the vessel may drift
	 * horizontally to the extent of the chain.
	 * 
	 * @param desiredDepth is the new depth to which the anchor should be set. Depth
	 *                     of zero means anchor fully raised.
	 */
	private void drawAnchor(final float desiredDepth) {

		// Compute acceptable results bounds
		final float minRange = desiredDepth - ERROR_MARGIN;
		final float maxRange = desiredDepth + ERROR_MARGIN;

		// Create a PID controller for the motor
		final PidController controller = new PidController(this, MIN_MOVEMENT, MAX_MOVEMENT, INTERVAL);

		// Set the target depth for the controller
		controller.setTarget(desiredDepth);
		log.info("Making anchor depth from {} to {}", anchorDepth, desiredDepth);

		// Stop trying after MAX_ITERATIONS regardless of anchor position
		int failsafe = MAX_ITERATIONS;

		// Move anchor until result is achieved or failsafe is triggered
		while (failsafe-- > 0 && (anchorDepth < minRange || anchorDepth > maxRange)) {
			log.info("Invoking PID to move anchor from {} to {}<x<<{}", anchorDepth, minRange, maxRange);
			controller.Compute();
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}

		// Check result
		if (failsafe <= 0 && (anchorDepth < minRange || anchorDepth > maxRange)) {
			log.warn("SimAnchor failed to reach desired depth of {} after {} iterations.", desiredDepth,
					MAX_ITERATIONS);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.sim.ControlledActuator#getInputValue()
	 */
	@Override
	public float getInputValue() {
		return this.motorValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.sim.ControlledActuator#setOutputValue(float)
	 */
	@Override
	public void setOutputValue(float newValue) {

		log.debug("PID Controller requests anchor setting to {}", Float.valueOf(newValue));

		// Compute the number of turns of the anchor spool to achieve the new depth
		// setting or move the desired amount

		// Question: Is newValue the amount of chain to move e.g., 0.2 meters or the
		// depth to move to?
		// If it is depth, we must compute the current depth (input value) and the
		// desired depth (newValue) and
		// take the difference between the two as the amount of chain to move. Then
		// calculate the amount of motor energy to apply (or, for a stepper motor,
		// the number of steps to take) to achieve that much chain movement.
		final float metersToMove = (anchorDepth - newValue);

		// If anchor chain is spooled on a spool with radius 5 cm, circumference is
		// 31.4 cm or .314 meters.
		final float anchorSpoolCircumference = .314f;

		// Assume Stepper motor that controls the anchor chain moves spool 1/8 turn
		// per step.
		final float turnsPerStep = 1.0f / 8.0f;

		// Calculate the amount of chain moved per step of the stepper motor
		final float metersPerStep = turnsPerStep * anchorSpoolCircumference;

		// calculate number of steps required to move the desired amount of anchor
		// chain
		final float stepsRequired = metersToMove / metersPerStep;

		// Power the stepper motor to the desired number of steps.
		// insert motor control here
		log.debug("Stepping anchor motor {} steps to move anchor chain {} meters.", Float.valueOf(stepsRequired),
				Float.valueOf(metersToMove));

		// Adjust motor controller setting
		this.motorValue = newValue;

		// Anchor moves by some amount proportional to motor controller setting.
		// For now, just make the proportion 1:1.
		this.anchorDepth -= newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.resources.AbstractComponent#componentStatus(java.util.Locale)
	 */
	@Override
	public JSONObject getStatus(Locale locale) {
		final JSONObject result = new JSONObject();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.resources.AbstractComponent#disconnect()
	 */
	@Override
	public void disconnect() {
		log.debug("Disconnecting component, {}", this.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.resources.AbstractComponent#resetConfiguration()
	 */
	@Override
	public void resetConfiguration() {
		log.debug("Resetting configuration for {}", this.getName());

		// Define configuration properties
		myConfiguration.setProperty(JSON_KEY_MAX_DEPTH, MAX_DEPTH);
		myConfiguration.setProperty(JSON_KEY_MAX_SPEED, MAX_SPEED);

		// Declare the schema for these properties
		final JSONObject depthProp = JsonSchema
				.schemaNumber("Maximum depth of anchor i.e., length of anchor chain in meters.", 0.0, true);
		final JSONObject speedProp = JsonSchema.schemaNumber("Maximum speed of anchor movement in meters per second.",
				0.0, true);
		JsonSchema mySchema = this.myConfiguration.getConfigurationSchema();
		mySchema.setProperty(JSON_KEY_MAX_DEPTH, depthProp, true);
		mySchema.setProperty(JSON_KEY_MAX_SPEED, speedProp, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.vessel.Anchor#anchorChainLength()
	 */
	@Override
	public float anchorChainLength() {
		return MAX_DEPTH;
	}
}
