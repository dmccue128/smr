/**
 * InvalidBookingDateException.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import java.util.Date;

/**
 * Indicates an attempt to book a date too far in the future.
 */
public class InvalidBookingDateException extends Exception {

  /**
   * Serial version Id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The invalid date.
   */
  final Date invalidDate;

  /**
   * Default constructor.
   *
   * @param date
   *          the invalid date
   */
  public InvalidBookingDateException(final Date date) {
    invalidDate = date;
  }

  /**
   * Get the invalid date.
   *
   * @return the invalidDate
   */
  public final Date getInvalidDate() {
    return invalidDate;
  }

}
