/**
 * BatteryImpl.java
 * 2 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import java.util.Locale;

import org.json.simple.JSONObject;

import com.synadek.core.ComponentException;
import com.synadek.core.InvalidValueException;

/**
 * Battery subsystem.
 */
public class BatteryImpl extends VesselComponentImpl implements Battery {

	/**
	 * Default value for simulated battery voltage in volts.
	 */
	private final static float DEFAULT_VOLTAGE = 12.0f;

	/**
	 * Maximum allowable simulated voltage.
	 */
	private final static float MAX_VOLTAGE = 25.0f;

	/**
	 * Battery voltage.
	 */
	private float voltage;

	/**
	 * Default constructor.
	 */
	public BatteryImpl() {
		super(VesselComponentType.VESSEL_BATTERY, "battery");
		this.voltage = DEFAULT_VOLTAGE;
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public BatteryImpl(final String name) {
		super(VesselComponentType.VESSEL_BATTERY, name);
		this.voltage = DEFAULT_VOLTAGE;
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
	 * @see com.smr.vessel.Battery#getVoltage()
	 */
	@Override
	public float getVoltage() {
		return this.voltage;
	}

	/**
	 * Simulation allows simulated battery level to be adjusted.
	 * 
	 * @param newVoltage is the new simulated battery voltage in volts.
	 * @throws InvalidValueException if voltage is negative or exceeds MAX_VOLTAGE
	 */
	public void setVoltage(final float newVoltage) throws InvalidValueException {

		// Reject voltage if out of range
		if (newVoltage < 0 || newVoltage > MAX_VOLTAGE) {
			throw new InvalidValueException(String.valueOf(newVoltage));
		}

		// Update simulated voltage.
		this.voltage = newVoltage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.smr.resources.AbstractComponent#componentStatus(java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getStatus(Locale locale) {
		final JSONObject result = new JSONObject();
		result.put("voltage", Double.valueOf(this.voltage));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.resources.Component#isSimulated()
	 */
	@Override
	public boolean isSimulated() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.core.Component#resetConfiguration()
	 */
	@Override
	public void resetConfiguration() {

	}

}
