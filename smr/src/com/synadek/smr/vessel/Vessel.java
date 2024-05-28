/**
 * Vessel.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.Component;
import com.synadek.core.GpsCoordinates;
import com.synadek.smr.vessel.VesselComponent.VesselComponentType;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface to vessel functions.
 */
public interface Vessel extends Component {

  /**
   * Overall status of a vessel.
   */
  public enum VesselStatus {
    /**
     * Ready to sail.
     */
    VESSEL_READY,
    /**
     * Disabled in some way.
     */
    VESSEL_NOT_READY,
  }

  /**
   * Get the status of the vessel.
   *
   * @return the status
   */
  public VesselStatus getStatus();

  /**
   * Get the serial number of the vessel.
   *
   * @return the serial number
   */
  public String getSerialNumber();

  /**
   * Get current location of the vessel.
   *
   * @return location in GPSCoordinates
   */
  public GpsCoordinates getLocation();

  /**
   * Get cruising speed (knots) of the vessel. One knot is 1.852 kilometres per
   * hour.
   */
  public double getCruisingSpeed();

  /**
   * Get the width of the cargo bay in meters.
   */
  public int getCargoWidth();

  /**
   * Get the height of the cargo bay in meters.
   */
  public int getCargoHeight();

  /**
   * Get the depth of the cargo bay in meters.
   */
  public int getCargoDepth();

  /**
   * Get the maximum cargo weight (kg).
   */
  public double getMaximumCargoWeight();

  /**
   * Get a list of vessel components of a given type.
   *
   * @param componentType
   *          the type of component
   * @return A list (possibly empty) of available components of that type
   */
  public <T> List<T> getComponent(final VesselComponentType componentType);

  /**
   * Get booked dates i.e., days on which the vessel is already committed.
   */
  public Set<Date> getBookedDates();

  /**
   * Book the vessel for specific dates.
   *
   * @throws InvalidBookingDateException
   *           when attempting to book a date too far in the future
   * @throws AlreadyBookedException
   *           when attempting to book a date that is already booked
   */
  public void bookDates(final String missionId, final Set<Date> dates)
      throws InvalidBookingDateException, AlreadyBookedException;

  /**
   * Cancel booking dates.
   */
  public void cancelDates(final Set<Date> dates);

  /**
   * Secure the vessel to a stationary object e.g., dock or buoy, or anchor,
   * lock-up and batten-down the hatches. e.g., furl sails (if any), retract
   * antennae, blow/reset ballast, power off or low-power, etc.
   */
  public void secureVessel();

  /**
   * Offload vessel-common provisions e.g., uploading data or removing
   * equipment. Note: payload management is a separate operation. This method
   * offloads provisions required for the operation of the vessel.
   */
  public void offloadVessel();
}
