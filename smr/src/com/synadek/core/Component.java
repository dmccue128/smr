/**
 * Component.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

import java.util.Locale;
import org.json.simple.JSONObject;

/**
 * Logical or Physical device component.
 */
public interface Component {

  /**
   * Summary health of a component is represented by a status code.
   */
  public enum Status {
    /**
     * ACTIVE status indicates the component is fully functional and active.
     */
    ACTIVE,
    /**
     * IMPAIRED status indicates the component has some limitation of
     * functionality but is operable at some level.
     */
    IMPAIRED,
    /**
     * INACTIVE status indicates the component is not active, not operational at
     * this time. It does not distinguish betwee a component that is simply
     * "switched-off" vs. a component that has failed. It only indicates that
     * the component is not presently operating.
     */
    INACTIVE
  }

  /**
   * Connect initializes the device and prepares it for use. Connect is
   * idempotent. Invoking connect on a device that is already connected is a
   * no-op.
   *
   * @return true if successful
   * @throws ComponentException
   *           if an error occurs accessing the device.
   */
  boolean connect() throws ComponentException;

  /**
   * Connect initializes the device and prepares it for use. Connect is
   * idempotent. Invoking connect on a device that is already connected is a
   * no-op.
   *
   * @param sim
   *          true if connecting to a simulation of the component
   * @return true if successful
   * @throws ComponentException
   *           if an error occurs accessing the device.
   */
  boolean connect(boolean sim) throws ComponentException;

  /**
   * Disconnect shuts down the device. Disconnect is idempotent. Invoking
   * disonnect on a device that is already disconnected is a no-op.
   *
   * @throws ComponentException
   *           if an error occurs accessing the device.
   */
  void disconnect() throws ComponentException;

  /**
   * Every component instance has a unique name established when the component
   * is created. Component names do not change.
   *
   * @return the name
   */
  String getName();

  /**
   * Return the current status of the component.
   *
   * @return the status
   */
  Component.Status getStatusCode();

  /**
   * Return a localized message describing the status of the component.
   *
   * @param locale
   *          is the locale in which the message should be generated. A null
   *          locale parameter will return a message in the default locale.
   * @return the message
   */
  String getStatusMessage(Locale locale);

  /**
   * isConnected returns true if the device is connected and available for use.
   *
   * @return true if the device is connected and available for use.
   */
  boolean isConnected();

  /**
   * isSimulated returns true if the device or one of its subcomponents is
   * simulated.
   *
   * @return true if some or all of this component is simulated
   */
  boolean isSimulated();

  /**
   * Get configuration associated with this component.
   *
   * @return the configuration
   */
  Configuration getConfiguration();

  /**
   * Get the schema for the configuration of this component.
   *
   * @return a JSON Schema.
   */
  JSONObject getConfigurationSchema();

  /**
   * Reset the configuration of this component.
   */
  void resetConfiguration();
}
