/**
 * IntershipCommunicationsImpl.java
 * 19 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import org.json.simple.JSONObject;

import com.synadek.core.ComponentException;

/**
 * Communications from this vessel to other vessels in communications range.
 */
public class IntershipCommunicationsImpl extends VesselComponentImpl implements IntershipCommunications {

	/**
	 * Default constructor.
	 */
	public IntershipCommunicationsImpl() {
		super(VesselComponentType.VESSEL_SHIP_TO_SHIP, "ship-to-ship communications");
		resetConfiguration();
	}

	/**
	 * Named constructor.
	 * 
	 * @param name a name for this component
	 */
	public IntershipCommunicationsImpl(final String name) {
		super(VesselComponentType.VESSEL_SHIP_TO_SHIP, name);
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
	 * @see com.synadek.smr.vessel.IntershipCommunications#sendMessage(java.lang.
	 * String, org.json.simple.JSONObject)
	 */
	@Override
	public void sendMessage(String shipName, JSONObject message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.IntershipCommunications#sendMessage(java.lang.
	 * String, org.json.simple.JSONObject, java.lang.String)
	 */
	@Override
	public void sendMessage(String shipName, JSONObject metadata, String imagePath) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synadek.smr.vessel.IntershipCommunications#addListener(com.synadek.smr.
	 * vessel.CommunicationsListener)
	 */
	@Override
	public void addListener(CommunicationsListener comm) {
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
