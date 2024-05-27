/**
 * RudderImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Rudder Management.
 */
public class RudderImpl extends VesselComponentImpl implements Rudder {

	/**
	 * Default constructor.
	 */
	public RudderImpl() {
		super(VesselComponentType.VESSEL_RUDDER, "rudder");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public RudderImpl(final String name) {
		super(VesselComponentType.VESSEL_RUDDER, name);
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
	 * @see com.synadek.smr.vessel.Rudder#getDirection()
	 */
	@Override
	public int getDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Rudder#setDirection(int)
	 */
	@Override
	public void setDirection(int degrees) {
		// TODO Auto-generated method stub

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
