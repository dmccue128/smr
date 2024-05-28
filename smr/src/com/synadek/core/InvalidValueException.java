/**
 * InvalidValueException.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

/**
 * Raised when an attempt is made to update an object with an invalid value.
 */
public class InvalidValueException extends Exception {

  /**
   * Default serial version ID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   *
   * @param message
   *          an error message describing the nature of the exception
   */
  public InvalidValueException(final String message) {
    super(message);
  }

}
