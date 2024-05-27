/**
 * BookingCalendar.java
 * 3 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.fleet;

import com.synadek.smr.vessel.Vessel;

import java.util.HashMap;
import java.util.Map;

/**
 * A booking calendar for the fleet indicating when each vessel is booked/available.
 */
public class BookingCalendar {

  private final Vessel[] fleet = { GoldenMoon.getInstance(), SilverMoon.getInstance() };

  private final Map<String, BookingParameter> bookingData = new HashMap<>();

  private static BookingCalendar instance;

  /**
   * Default constructor.
   */
  private BookingCalendar() {

    // Construct the booking data by querying each vessel in the fleet.
    for (Vessel vessel : fleet) {
      bookingData.put(vessel.getName(), getData(vessel));
    }
  }

  /**
   * Get a reference to the booking calendar.
   */
  public static BookingCalendar getInstance() {
    if (instance == null) {
      instance = new BookingCalendar();
      return instance;
    }
    return instance;
  }

  /**
   * Discard the booking calendar instance.
   */
  public static void discardInstance() {
    instance = null;
  }

  /**
   * Get booking data from a vessel
   * 
   * @param vessel
   *          the vessel
   * @return the booking data
   */
  private BookingParameter getData(final Vessel vessel) {
    final BookingParameter result = new BookingParameter();

    result.setBookedDates(vessel.getBookedDates());
    result.setCargoDepth(vessel.getCargoDepth());
    result.setCargoHeight(vessel.getCargoHeight());
    result.setCargoWidth(vessel.getCargoWidth());
    result.setCruisingSpeed(vessel.getCruisingSpeed());
    result.setMaximumCargoWeight(vessel.getMaximumCargoWeight());
    result.setCurrentLocation(vessel.getLocation());

    return result;
  }
}
