/**
 * WaterSensorImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Report conditions of the water surrounding the vessel and interactions of
 * water with the vessel.
 */
public class WaterSensorImpl extends VesselComponentImpl implements WaterSensor {

	/**
	 * Default constructor.
	 */
	public WaterSensorImpl() {
		super(VesselComponentType.VESSEL_WATER_SENSOR, "water sensor");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public WaterSensorImpl(final String name) {
		super(VesselComponentType.VESSEL_WATER_SENSOR, name);
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
	 * @see com.synadek.smr.vessel.WaterSensor#isVesselInWater()
	 */
	@Override
	public boolean isVesselInWater() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.WaterSensor#getWaterTemperature()
	 */
	@Override
	public float getWaterTemperature() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.WaterSensor#getWaterSalinity()
	 */
	@Override
	public float getWaterSalinity() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.WaterSensor#getVesselRelativeWaterSpeed()
	 */
	@Override
	public float getVesselRelativeWaterSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.WaterSensor#getDepth()
	 */
	@Override
	public float getDepth() {
		// TODO Auto-generated method stub
		return 0;
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
