/**
 * VesselPhysicalModel.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.core.Component;
import com.synadek.core.ComponentException;
import com.synadek.core.InvalidValueException;

/**
 * Abstract interface for a vessel to physical I/O hardware.
 */
public interface VesselPhysicalModel extends Component {

  /**
   * Physical devices defined in this model.
   */
  public enum PhysicalDeviceType {
    /**
     * Air temperature sensor.
     */
    PHY_AIR_TEMPERATURE_SENSOR,
    /**
     * Anchor motor direction control.
     */
    PHY_ANCHOR_MOTOR_DIRECTION,
    /**
     * Anchor motor speed control.
     */
    PHY_ANCHOR_MOTOR_SPEED,
    /**
     * Anchor sensor - down. True when anchor is deployed to the full extent of the anchor chain.
     */
    PHY_ANCHOR_SENSOR_DOWN,
    /**
     * Anchor sensor - up. True when the anchor is fully retracted into the ship.
     */
    PHY_ANCHOR_SENSOR_UP,
    /**
     * System battery voltage sensor.
     */
    PHY_BATTERY_VOLTAGE_SENSOR,
    /**
     * Compass.
     */
    PHY_COMPASS_SENSOR,
    /**
     * GNSS Receiver.
     */
    PHY_GNSS_RECEIVER,
    /**
     * Master power relay. True means turn the relay on.
     */
    PHY_MASTER_RELAY_CONTROL,
    /**
     * Propeller motor direction control.
     */
    PHY_PROPELLER_MOTOR_DIRECTION,
    /**
     * Propeller motor speed control.
     */
    PHY_PROPELLER_MOTOR_SPEED,
    /**
     * Rudder motor direction control.
     */
    PHY_RUDDER_MOTOR_DIRECTION,
    /**
     * Rudder motor speed control.
     */
    PHY_RUDDER_MOTOR_SPEED,
    /**
     * Running lights control.
     */
    PHY_RUNNING_LIGHTS_RELAY,
    /**
     * Water temperature sensor.
     */
    PHY_WATER_TEMPERATURE_SENSOR,
    /**
     * Wind direction sensor.
     */
    PHY_WIND_DIRECTION_SENSOR,
    /**
     * Wind speed sensor.
     */
    PHY_WIND_SPEED_SENSOR,
  }

  /**
   * Define possible modes (uses for) a device. "SYSTEM_TYPE" means unusable for software e.g.,
   * power and ground pins.
   */
  public enum PinType {
    /**
     * Digital input device.
     */
    DIGITAL_INPUT_TYPE,
    /**
     * Digital output device.
     */
    DIGITAL_OUTPUT_TYPE,
    /**
     * Analog input device.
     */
    ANALOG_INPUT_TYPE,
    /**
     * Analog output device.
     */
    ANALOG_OUTPUT_TYPE,
    /**
     * Reserved pin e.g., GND or VCC
     */
    SYSTEM_TYPE,
    /**
     * Unknown entry.
     */
    UNKNOWN_TYPE,
  }

  /**
   * Get the type of a physical device.
   * 
   * @param dev
   *          the device
   * @return the type
   */
  PinType getType(PhysicalDeviceType dev);

  /**
   * Get the state of a digital input.
   * 
   * @param deviceId
   *          the id of the digital input
   * @return the state of the digital input
   * @throws ComponentException
   *           if underlying device is not connected
   */
  boolean getDigitalInputState(PhysicalDeviceType deviceId) throws ComponentException;

  /**
   * Set the state of a simulated digital input. This method is only valid when the interface is
   * being simulated.
   * 
   * @param deviceId
   *          the id of the digital input
   * @param newVal
   *          the new value
   * @throws ComponentException
   *           if underlying device is not connected or not simulated
   */
  void setDigitalInputState(PhysicalDeviceType deviceId, boolean newVal)
      throws ComponentException;

  /**
   * Get the state of a digital output.
   * 
   * @param deviceId
   *          the id of the digital output
   * @return the state of the digital output
   * @throws ComponentException
   *           if underlying device is not connected
   */
  boolean getDigitalOutputState(PhysicalDeviceType deviceId) throws ComponentException;

  /**
   * Set the state of a digital output.
   * 
   * @param deviceId
   *          the id of the digital output
   * @param newVal
   *          the new value
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void setDigitalOutputState(PhysicalDeviceType deviceId, boolean newVal)
      throws ComponentException;

  /**
   * Get the value of an analog input.
   * 
   * @param deviceId
   *          the id of the analog input
   * @return the value of the analog input
   * @throws ComponentException
   *           if underlying device is not connected
   */
  double getAnalogInputValue(PhysicalDeviceType deviceId) throws ComponentException;

  /**
   * Add a digital input change listener. The input change handler method will be called when an
   * input on this Interface Kit has changed. There is no limit on the number of input change
   * handlers that can be registered for a particular VesselPhysicalModel.
   * 
   * @param deviceId
   *          the id of the digital input
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void addDigitalInputListener(PhysicalDeviceType deviceId, PdlDigitalHandler l)
      throws ComponentException;

  /**
   * Remove a digital input change listener from all GPIO pins.
   * 
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void removeDigitalInputListener(PdlDigitalHandler l) throws ComponentException;

  /**
   * Add a digital output change listener for digital outputs on this device. The output change
   * handler method will be called when an input on this Interface Kit has changed. There is no
   * limit on the number of output change handlers that can be registered for a particular
   * VesselPhysicalModel.
   * 
   * @param deviceId
   *          the id of the digital output
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void addDigitalOutputListener(PhysicalDeviceType deviceId, PdlDigitalHandler l)
      throws ComponentException;

  /**
   * Remove a digital output change listener from all GPIO pins.
   * 
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void removeDigitalOutputListener(PdlDigitalHandler l) throws ComponentException;

  /**
   * Add an analog sensor change listener. The sensor change handler method will be called when a
   * sensor on this Interface Kit has changed by at least the trigger amount (see
   * SetAnalogChangeTrigger) that has been set for this sensor. There is no limit on the number of
   * sensor change handlers that can be registered for a particular VesselPhysicalModel.
   * 
   * @param deviceId
   *          id of the analog input
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void addAnalogInputListener(PhysicalDeviceType deviceId, PdlAnalogHandler l)
      throws ComponentException;

  /**
   * Remove an analog sensor change listener from all GPIO pins.
   * 
   * @param l
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  void removeAnalogInputListener(PdlAnalogHandler l) throws ComponentException;

  /**
   * Get the change trigger for an analog input. This is the amount that an analog input must change
   * between successive SensorChangeEvents. This is based on the 0-1000 range provided by
   * getSensorValue. This value is by default set to 10 for most Interface Kits with analog inputs.
   * 
   * @param deviceId
   *          the id of the analog input
   * @return the value of the change trigger
   * @throws ComponentException
   *           if underlying device is not connected or does not support the operation
   */
  double getAnalogChangeTrigger(PhysicalDeviceType deviceId) throws ComponentException;

  /**
   * Set the change trigger for an analog input.
   * 
   * @param deviceId
   *          the id of the analog input
   * @param newVal
   *          the trigger amount
   * @throws ComponentException
   *           if underlying device is not connected or does not support the operation
   */
  void setAnalogChangeTrigger(PhysicalDeviceType deviceId, double newVal)
      throws ComponentException;

  /**
   * Get a resource key for the name of a physical device.
   * 
   * @param deviceId
   *          the id of the physical device
   * @return a resource key suitable for accessing the name of this device
   * @throws ComponentException
   *           if the component is not connected
   * @throws InvalidValueException
   *           if the id is invalid
   */
  String getNameResource(PhysicalDeviceType deviceId)
      throws ComponentException, InvalidValueException;

  /**
   * Iterate over the Pdms of the specified type.
   * 
   * @param type
   *          the subset of PDMs of interest
   * @return an iterator that yields all the PDMs of that type
   */
  PdmIterator getDeviceIterator(PinType type);

  /**
   * Invoke registered handlers for analog input events.
   * 
   * @param ae
   *          the event to dispatch
   */
  void invokeAnalogHandlers(AnalogEvent ae);

  /**
   * Invoke registered handlers for digital input events.
   * 
   * @param evt
   *          the event to dispatch
   */
  void invokeDigitalInputHandlers(DigitalInputEvent evt);
}
