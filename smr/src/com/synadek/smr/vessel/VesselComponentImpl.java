/**
 * VesselComponentImpl.java
 * 18 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.AbstractComponent;

/**
 * A component of a vessel.
 */
public abstract class VesselComponentImpl extends AbstractComponent implements VesselComponent {

	private final VesselComponentType myType;

	/**
	 * Simple constructor.
	 * 
	 * @param name the name of this component
	 */
	public VesselComponentImpl(final VesselComponentType type, final String name) {
		super(name);
		myType = type;
	}

	/**
	 * Default constructor.
	 * 
	 * @param name the name of this component
	 * @param desc a description of the component
	 */
	public VesselComponentImpl(final VesselComponentType type, final String name, final String desc) {
		super(name, desc);
		myType = type;
	}

	/**
	 * Get the type of this component.
	 * 
	 * @return the vessel component type
	 */
	@Override
	public VesselComponentType getComponentType() {
		return myType;
	}

}
