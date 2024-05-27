/**
 * ComponentException.java
 * 2 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.core;

/**
 * Exception thrown from Component management subsystem.
 */
public class ComponentException extends Exception {
  /**
   * Default serial version.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   * 
   * @param msg
   *          is the error message associated with this exception
   */
  public ComponentException(final String msg) {
    super(msg);
  }
}
