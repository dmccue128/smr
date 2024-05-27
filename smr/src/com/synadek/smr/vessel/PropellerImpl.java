/**
 * PropellerImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Propeller speed and direction.
 */
public class PropellerImpl extends VesselComponentImpl implements Propeller {

	/**
	 * Default constructor.
	 */
	public PropellerImpl() {
		super(VesselComponentType.VESSEL_PROPELLER, "propeller");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public PropellerImpl(final String name) {
		super(VesselComponentType.VESSEL_PROPELLER, name);
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
	 * @see com.synadek.smr.vessel.Propeller#getSpeed()
	 */
	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Propeller#setSpeed(int)
	 */
	@Override
	public void setSpeed(int newRpm) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Propeller#getDirection()
	 */
	@Override
	public propellerDirection getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Propeller#setDirection(com.synadek.smr.vessel.
	 * Propeller.propellerDirection)
	 */
	@Override
	public void setDirection(propellerDirection newDirection) {
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
