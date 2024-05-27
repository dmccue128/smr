/**
 * VesselSimulation.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.synadek.core.ComponentException;
import com.synadek.core.InvalidValueException;

/**
 * Simulation of a physical vessel model.
 */
public class VesselSimulation extends AbstractVesselModel {

	private final Map<PhysicalDeviceType, PinType> pintypeMap = new HashMap<>();
	private final Map<PhysicalDeviceType, Boolean> digitalValueMap = new HashMap<>();
	private final Map<PhysicalDeviceType, Double> analogValueMap = new HashMap<>();
	private final Map<PhysicalDeviceType, Double> analogChangeTrigger = new HashMap<>();
	private final Map<PhysicalDeviceType, String> nameMap = new HashMap<>();

	public VesselSimulation() {

		// Initialize pin types
		pintypeMap.put(PhysicalDeviceType.PHY_AIR_TEMPERATURE_SENSOR, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_ANCHOR_MOTOR_DIRECTION, PinType.DIGITAL_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_ANCHOR_MOTOR_SPEED, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_DOWN, PinType.DIGITAL_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_UP, PinType.DIGITAL_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_BATTERY_VOLTAGE_SENSOR, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_COMPASS_SENSOR, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_GNSS_RECEIVER, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_MASTER_RELAY_CONTROL, PinType.DIGITAL_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_PROPELLER_MOTOR_DIRECTION, PinType.DIGITAL_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_PROPELLER_MOTOR_SPEED, PinType.ANALOG_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_RUDDER_MOTOR_DIRECTION, PinType.DIGITAL_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_RUDDER_MOTOR_SPEED, PinType.ANALOG_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_RUNNING_LIGHTS_RELAY, PinType.DIGITAL_OUTPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_WATER_TEMPERATURE_SENSOR, PinType.DIGITAL_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_WIND_DIRECTION_SENSOR, PinType.ANALOG_INPUT_TYPE);
		pintypeMap.put(PhysicalDeviceType.PHY_WIND_SPEED_SENSOR, PinType.ANALOG_INPUT_TYPE);

		// Initialize names
		nameMap.put(PhysicalDeviceType.PHY_AIR_TEMPERATURE_SENSOR, "air temperature sensor");
		nameMap.put(PhysicalDeviceType.PHY_ANCHOR_MOTOR_DIRECTION, "anchor motor direction");
		nameMap.put(PhysicalDeviceType.PHY_ANCHOR_MOTOR_SPEED, "anchor motor speed");
		nameMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_DOWN, "anchor down");
		nameMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_UP, "anchor up");
		nameMap.put(PhysicalDeviceType.PHY_BATTERY_VOLTAGE_SENSOR, "battery voltage");
		nameMap.put(PhysicalDeviceType.PHY_COMPASS_SENSOR, "compass direction");
		nameMap.put(PhysicalDeviceType.PHY_GNSS_RECEIVER, "GNSS");
		nameMap.put(PhysicalDeviceType.PHY_MASTER_RELAY_CONTROL, "master power relay");
		nameMap.put(PhysicalDeviceType.PHY_PROPELLER_MOTOR_DIRECTION, "propeller direction");
		nameMap.put(PhysicalDeviceType.PHY_PROPELLER_MOTOR_SPEED, "propeller speed");
		nameMap.put(PhysicalDeviceType.PHY_RUDDER_MOTOR_DIRECTION, "rudder direction");
		nameMap.put(PhysicalDeviceType.PHY_RUDDER_MOTOR_SPEED, "rudder speed");
		nameMap.put(PhysicalDeviceType.PHY_RUNNING_LIGHTS_RELAY, "running lights");
		nameMap.put(PhysicalDeviceType.PHY_WATER_TEMPERATURE_SENSOR, "water temperature");
		nameMap.put(PhysicalDeviceType.PHY_WIND_DIRECTION_SENSOR, "wind direction");
		nameMap.put(PhysicalDeviceType.PHY_WIND_SPEED_SENSOR, "wind speed");
		// Initialize values
		for (PhysicalDeviceType key : PhysicalDeviceType.values()) {
			digitalValueMap.put(key, Boolean.FALSE);
			analogValueMap.put(key, Double.valueOf(0.0));
			analogChangeTrigger.put(key, Double.valueOf(1.0));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#getType(com.synadek.smr.
	 * vessel.physical. VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public PinType getType(PhysicalDeviceType dev) {
		return pintypeMap.get(dev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#getDigitalInputState(com.
	 * synadek.smr.vessel .physical.VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public boolean getDigitalInputState(PhysicalDeviceType deviceId) throws ComponentException {
		return digitalValueMap.get(deviceId).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#setDigitalInputState(com.
	 * synadek.smr.vessel .physical.VesselPhysicalModel.PhysicalDeviceType, boolean)
	 */
	@Override
	public void setDigitalInputState(PhysicalDeviceType deviceId, boolean newVal) throws ComponentException {
		digitalValueMap.put(deviceId, Boolean.valueOf(newVal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#getDigitalOutputState(com
	 * .synadek.smr. vessel.physical.VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public boolean getDigitalOutputState(PhysicalDeviceType deviceId) throws ComponentException {
		return digitalValueMap.get(deviceId).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#setDigitalOutputState(com
	 * .synadek.smr. vessel.physical.VesselPhysicalModel.PhysicalDeviceType,
	 * boolean)
	 */
	@Override
	public void setDigitalOutputState(PhysicalDeviceType deviceId, boolean newVal) throws ComponentException {
		digitalValueMap.put(deviceId, Boolean.valueOf(newVal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#getAnalogInputValue(com.
	 * synadek.smr.vessel. physical.VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public double getAnalogInputValue(PhysicalDeviceType deviceId) throws ComponentException {
		return analogValueMap.get(deviceId).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#getAnalogChangeTrigger(
	 * com.synadek.smr. vessel.physical.VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public double getAnalogChangeTrigger(PhysicalDeviceType deviceId) throws ComponentException {
		return analogChangeTrigger.get(deviceId).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.physical.VesselPhysicalModel#setAnalogChangeTrigger(
	 * com.synadek.smr. vessel.physical.VesselPhysicalModel.PhysicalDeviceType,
	 * double)
	 */
	@Override
	public void setAnalogChangeTrigger(PhysicalDeviceType deviceId, double newVal) throws ComponentException {
		analogChangeTrigger.put(deviceId, Double.valueOf(newVal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.physical.VesselPhysicalModel#getNameResource(com.
	 * synadek.smr.vessel. physical.VesselPhysicalModel.PhysicalDeviceType)
	 */
	@Override
	public String getNameResource(PhysicalDeviceType deviceId) throws ComponentException, InvalidValueException {
		return nameMap.get(deviceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.core.Component#getConfigurationSchema()
	 */
	@Override
	public JSONObject getConfigurationSchema() {

		return new JSONObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.core.Component#resetConfiguration()
	 */
	@Override
	public void resetConfiguration() {

	}

	@Override
	public boolean connect(boolean sim) throws ComponentException {
		if (!sim) {
			log.error("VesselSimulation must connect using sim=true");
			return false;
		}
		return true;
	}

}
