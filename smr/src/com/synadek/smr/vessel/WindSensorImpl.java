/**
 * WindSensorImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 *
 */
public class WindSensorImpl extends VesselComponentImpl implements WindSensor {

	/**
	 * Default constructor.
	 */
	public WindSensorImpl() {
		super(VesselComponentType.VESSEL_WIND_SENSOR, "wind sensor");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public WindSensorImpl(final String name) {
		super(VesselComponentType.VESSEL_WIND_SENSOR, name);
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
	 * @see com.synadek.smr.vessel.WindSensor#getVesselRelativeWindSpeed()
	 */
	@Override
	public float getVesselRelativeWindSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.WindSensor#getVesselRelativeWindDirection()
	 */
	@Override
	public int getVesselRelativeWindDirection() {
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
		// TODO Auto-generated method stub

	}
}
