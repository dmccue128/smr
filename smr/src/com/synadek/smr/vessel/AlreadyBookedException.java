/**
 * AlreadyBookedException.java
 * 4 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import java.util.Date;

/**
 * Thrown when an attempt is made to book a vessel that is already booked.
 */
public class AlreadyBookedException extends Exception {

  /**
   * Serial version Id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Date of the previous booking.
   */
  final Date previousDate;

  /**
   * Name of the mission that previously booked the vessel on this date.
   */
  final String previousMission;

  /**
   * Default constructor.
   * 
   * @param date
   *          the date on which the booking occurs
   * @param previousBooking
   *          is the name of the mission that previously booked this vessel on the date
   */
  public AlreadyBookedException(final Date date, final String previousBooking) {
    previousDate = date;
    previousMission = previousBooking;
  }

  /**
   * @return the previousDate
   */
  public final Date getPreviousDate() {
    return previousDate;
  }

  /**
   * @return the previousMission
   */
  public final String getPreviousMission() {
    return previousMission;
  }
}
