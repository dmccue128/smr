/**
 * AbstractVesselImpl.java
 * 14 Nov 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.synadek.core.AbstractComponent;
import com.synadek.core.ComponentException;
import com.synadek.smr.vessel.VesselComponent.VesselComponentType;
import com.synadek.smr.vessel.physical.VesselPhysicalModel;

/**
 * Implement methods common to vessels.
 */
public abstract class AbstractVesselImpl extends AbstractComponent implements Vessel {

	/**
	 * Physical device model for the vessel.
	 */
	private final VesselPhysicalModel myPhysicalModel;

	/**
	 * Overall status of vessel.
	 */
	private VesselStatus myStatus = VesselStatus.VESSEL_NOT_READY;

	/**
	 * List of components of this vessel.
	 */
	private final List<VesselComponent> myComponents = new LinkedList<VesselComponent>();

	/**
	 * Serial number of the vessel.
	 */
	private final String mySerialNumber;

	/**
	 * Booked dates for this vessel.
	 */
	private final Map<Date, String> bookingList = new HashMap<>();

	/**
	 * Default constructor. physicalModel the physical device model used by this
	 * vessel name the name of the vessel serialNumber the serial number of the
	 * vessel
	 */
	protected AbstractVesselImpl(final VesselPhysicalModel physicalModel, final String name,
			final String serialNumber) {
		super(name);
		myPhysicalModel = physicalModel;
		mySerialNumber = serialNumber;

		// TODO Read booking info from database
		bookingList.put(new Date(), "some mission");
	}

	/**
	 * Connect parameter indicates whether to connect to a physical or simulated
	 * component.
	 *
	 * @param sim true if the connection is to a simulation of the component
	 * @throws ComponentException if an error occurs
	 */
	@Override
	abstract public boolean connect(final boolean sim) throws ComponentException;

	/**
	 * Get the status of the vessel.
	 * 
	 * @return the status
	 */
	@Override
	public VesselStatus getStatus() {
		return myStatus;
	}

	/**
	 * Set the overall status of the vessel.
	 */
	protected void setStatus(final VesselStatus sts) {
		myStatus = sts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Vessel#getSerialNumber()
	 */
	@Override
	public String getSerialNumber() {
		return mySerialNumber;

	}

	/**
	 * For diagnostic purposes, provide access to the underlying physical vessel
	 * model.
	 * 
	 * @return the physical model
	 */
	public VesselPhysicalModel getPhysicalModel() {
		return myPhysicalModel;
	}

	/**
	 * Add a component to the list of known components for this vessel.
	 * 
	 * @param component the new component
	 */
	protected void addComponent(final VesselComponent component) {
		myComponents.add(component);
	}

	/**
	 * Remove a component from the list of known components for this vessel.
	 * 
	 * @param component the component to be removed
	 */
	protected void removeComponent(final VesselComponent component) {
		myComponents.remove(component);
	}

	/**
	 * Get a list of the vessel components of a specific type.
	 * 
	 * @param componentType the type of component
	 * @return a list (possibly empty) of the vessel components of that type
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getComponent(final VesselComponentType componentType) {

		final List<T> result = new LinkedList<>();

		for (VesselComponent component : myComponents) {
			if (component.getComponentType() == componentType) {
				result.add((T) component);
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Vessel#getBookedDates()
	 */
	@Override
	public Set<Date> getBookedDates() {
		return bookingList.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Vessel#bookDates(java.lang.String,
	 * java.util.Date[])
	 */
	@Override
	public void bookDates(String missionId, Set<Date> dates)
			throws InvalidBookingDateException, AlreadyBookedException {

		// Do not allow bookings beyond a year in advance
		final Calendar now = Calendar.getInstance();
		final Calendar furthestBookingDate = Calendar.getInstance();
		furthestBookingDate.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);

		// Validate input
		for (Date date : dates) {
			if (date.after(furthestBookingDate.getTime())) {
				throw new InvalidBookingDateException(date);
			}
			if (this.getBooking(date) != null) {
				throw new AlreadyBookedException(date, getBooking(date));
			}
		}

		// OK to book
		for (Date date : dates) {
			this.bookDate(date, missionId);
		}
	}

	/**
	 * getBooking returns the name of the mission for which this vessel is booked on
	 * a particular date.
	 * 
	 * @param date the date in question
	 * @return the name of the mission or null if the vessel is not booked on this
	 *         date
	 */
	public String getBooking(final Date date) {
		return bookingList.get(date);
	}

	/**
	 * Book a date for a mission.
	 * 
	 * @param date    the date
	 * @param mission the mission name
	 * @throws AlreadyBookedException if the vessel is already booked on the
	 *                                specified date
	 */
	public void bookDate(final Date date, final String mission) throws AlreadyBookedException {
		final String previousBooking = bookingList.get(date);
		if (previousBooking != null) {
			throw new AlreadyBookedException(date, previousBooking);
		}
		bookingList.put(date, mission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synadek.smr.vessel.Vessel#cancelDates(java.util.Date[])
	 */
	@Override
	public void cancelDates(Set<Date> dates) {
		for (Date date : dates) {
			bookingList.remove(date);
		}
	}
}
