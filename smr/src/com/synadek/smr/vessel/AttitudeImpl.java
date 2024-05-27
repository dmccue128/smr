/**
 * AttitdueImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;

/**
 * Report linear and rotational movement of the vessel
 * (heave,sway,surge,roll,pitch,yaw).
 */
public class AttitudeImpl extends VesselComponentImpl implements Attitude {

	/**
	 * Default constructor.
	 */
	public AttitudeImpl() {
		super(VesselComponentType.VESSEL_ATTITUDE, "attitude");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public AttitudeImpl(final String name) {
		super(VesselComponentType.VESSEL_ATTITUDE, name);
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
	 * @see com.synadek.smr.vessel.Attitude#getHeave()
	 */
	@Override
	public int getHeave() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Attitude#getSway()
	 */
	@Override
	public int getSway() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Attitude#getSurge()
	 */
	@Override
	public int getSurge() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Attitude#getPitch()
	 */
	@Override
	public int getPitch() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Attitude#getRoll()
	 */
	@Override
	public int getRoll() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Attitude#getYaw()
	 */
	@Override
	public int getYaw() {
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
