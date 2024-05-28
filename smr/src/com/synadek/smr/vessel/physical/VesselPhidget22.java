/**
 * VesselPhidget22.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.phidget22.AttachEvent;
import com.phidget22.AttachListener;
import com.phidget22.DetachEvent;
import com.phidget22.DetachListener;
import com.phidget22.DeviceClass;
import com.phidget22.DigitalInput;
import com.phidget22.DigitalInputStateChangeEvent;
import com.phidget22.DigitalInputStateChangeListener;
import com.phidget22.DigitalOutput;
import com.phidget22.ErrorEvent;
import com.phidget22.ErrorListener;
import com.phidget22.Phidget;
import com.phidget22.PhidgetException;
import com.phidget22.VoltageInput;
import com.phidget22.VoltageInputSensorChangeEvent;
import com.phidget22.VoltageInputSensorChangeListener;
import com.phidget22.VoltageInputVoltageChangeEvent;
import com.phidget22.VoltageInputVoltageChangeListener;
import com.synadek.core.ComponentException;
import com.synadek.core.InvalidValueException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Pio using Phidget22 library.
 */
public class VesselPhidget22 extends AbstractVesselModel
    implements
      DigitalInputStateChangeListener,
      VoltageInputSensorChangeListener,
      VoltageInputVoltageChangeListener {

  /**
   * Map Physical Device Model device ids to Phidget meta data.
   */
  private static Map<PhysicalDeviceType, VesselPhidgetMeta> GpioMap = createPhysicaltoGpioMap();

  /**
   * Reverse map from Phidget physical digital input indexes (0-7) to PDM IDs.
   */
  private static final Map<Integer, PhysicalDeviceType> digitalInputMap = new HashMap<>();

  /**
   * Reverse map from Phidget physical digital output indexes (0-7) to PDM IDs.
   */
  private static final Map<Integer, PhysicalDeviceType> digitalOutputMap = new HashMap<>();
  /**
   * Reverse map from Phidget physical analog input indexes (0-7) to PDM IDs.
   */
  private static final Map<Integer, PhysicalDeviceType> analogInputMap = new HashMap<>();
  /**
   * Channel map for digital inputs.
   */
  private static final Map<Integer, DigitalInput> digitalInputChannelMap = new HashMap<>();
  /**
   * Channel map for digital outputs.
   */
  private static final Map<Integer, DigitalOutput> digitalOutputChannelMap = new HashMap<>();
  /**
   * Channel map for analog inputs.
   */
  private static final Map<Integer, VoltageInput> analogInputChannelMap = new HashMap<>();

  private static Map<PhysicalDeviceType, VesselPhidgetMeta> createPhysicaltoGpioMap() {
    final Map<VesselPhysicalModel.PhysicalDeviceType, VesselPhidgetMeta> myMap = new HashMap<>();

    // Analog inputs
    myMap.put(PhysicalDeviceType.PHY_AIR_TEMPERATURE_SENSOR,
        new VesselPhidgetMeta("air temperature sensor", PinType.ANALOG_INPUT_TYPE, 0));
    myMap.put(PhysicalDeviceType.PHY_BATTERY_VOLTAGE_SENSOR, new VesselPhidgetMeta(
        "battery voltage sensor", VesselPhysicalModel.PinType.ANALOG_INPUT_TYPE, 1));
    myMap.put(PhysicalDeviceType.PHY_WATER_TEMPERATURE_SENSOR,
        new VesselPhidgetMeta("water temperature sensor", PinType.ANALOG_INPUT_TYPE, 2));
    myMap.put(PhysicalDeviceType.PHY_WIND_DIRECTION_SENSOR,
        new VesselPhidgetMeta("wind direction sensor", PinType.ANALOG_INPUT_TYPE, 3));
    myMap.put(PhysicalDeviceType.PHY_WIND_SPEED_SENSOR,
        new VesselPhidgetMeta("wind speed sensor", PinType.ANALOG_INPUT_TYPE, 4));

    // Digital inputs
    myMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_DOWN,
        new VesselPhidgetMeta("Anchor down", PinType.DIGITAL_INPUT_TYPE, 0));
    myMap.put(PhysicalDeviceType.PHY_ANCHOR_SENSOR_UP,
        new VesselPhidgetMeta("Anchor up", PinType.DIGITAL_INPUT_TYPE, 1));

    // Digital outputs
    myMap.put(PhysicalDeviceType.PHY_ANCHOR_MOTOR_DIRECTION,
        new VesselPhidgetMeta("anchor motor direction", PinType.DIGITAL_OUTPUT_TYPE, 0));
    myMap.put(PhysicalDeviceType.PHY_MASTER_RELAY_CONTROL,
        new VesselPhidgetMeta("master power relay", PinType.DIGITAL_OUTPUT_TYPE, 1));
    myMap.put(PhysicalDeviceType.PHY_PROPELLER_MOTOR_DIRECTION,
        new VesselPhidgetMeta("Propeller motor direction", PinType.DIGITAL_OUTPUT_TYPE, 2));
    myMap.put(PhysicalDeviceType.PHY_RUDDER_MOTOR_DIRECTION,
        new VesselPhidgetMeta("Rudder motor direction", PinType.DIGITAL_OUTPUT_TYPE, 3));
    myMap.put(PhysicalDeviceType.PHY_RUNNING_LIGHTS_RELAY,
        new VesselPhidgetMeta("Running lights", PinType.DIGITAL_OUTPUT_TYPE, 4));

    return myMap;
  }

  /**
   * Default constructor.
   */
  public VesselPhidget22() {
    super("PhidgetInterfaceKit");

    // Set debugging level for phidget library
    // LogLevel is one of: CRITICAL, ERROR, WARNING, INFO, VERBOSE
    // try {
    // com.phidget22.Log.enable(com.phidget22.LogLevel.DEBUG, null);
    // } catch (PhidgetException err) {
    // log.error(err);
    // }

    // Map each device in the physical device model to a Phidget channel
    for (Map.Entry<PhysicalDeviceType, VesselPhidgetMeta> devEntry : GpioMap.entrySet()) {
      final PhysicalDeviceType dev = devEntry.getKey();
      final VesselPhidgetMeta pin = devEntry.getValue();

      final Integer index = Integer.valueOf(pin.getIndex());
      final PinType type = pin.getType();

      switch (pin.getType()) {
        case DIGITAL_INPUT_TYPE:
          digitalInputMap.put(index, dev);
          break;
        case DIGITAL_OUTPUT_TYPE:
          digitalOutputMap.put(index, dev);
          break;
        case ANALOG_INPUT_TYPE:
          analogInputMap.put(index, dev);
          break;
        default:
          log.error("Unsupported pin type, " + type.toString());
      }
    }
  }

  /**
   * connect is the initializer for the physical device model.
   *
   * @throws ComponentException
   *           if an error occurs
   */
  @Override
  public final boolean connect(boolean sim) throws ComponentException {

    if (sim) {
      log.error(ERR_SIM_NOT_AVAIL);
      return false;
    }

    // Ignore repeated connect requests
    if (this.isConnected()) {
      log.error("Ignoring repeated attempt to connect to " + this.getName());
      return false;
    }

    super.connect();

    log.info("Connecting PioPhidget...");

    try {
      // Map each device in the physical device model to a Phidget channel
      for (Map.Entry<PhysicalDeviceType, VesselPhidgetMeta> devEntry : GpioMap.entrySet()) {
        final PhysicalDeviceType dev = devEntry.getKey();
        final VesselPhidgetMeta pin = devEntry.getValue();

        final int index = pin.getIndex();
        final PinType type = pin.getType();

        switch (pin.getType()) {

          case DIGITAL_INPUT_TYPE:
            // Connect to the phidget channel
            final DigitalInput din = openDigitalInputChannel(dev, pin.getIndex());
            // Map the physical pin number to the channel
            digitalInputChannelMap.put(Integer.valueOf(index), din);
            log.info("Registered " + dev.toString() + " as digital input " + index);
            break;

          case DIGITAL_OUTPUT_TYPE:
            // Connect to the phidget channel
            final DigitalOutput dout = openDigitalOutputChannel(dev, pin.getIndex());
            // Map the physical pin number to the channel
            digitalOutputChannelMap.put(Integer.valueOf(index), dout);
            log.info("Registered " + dev.toString() + " as digital output " + index);
            break;

          case ANALOG_INPUT_TYPE:
            // Connect to the phidget channel
            final VoltageInput ain = openVoltageInputChannel(dev, pin.getIndex());
            // Map the physical pin number to the channel
            analogInputChannelMap.put(Integer.valueOf(index), ain);
            log.info("Registered " + dev.toString() + " as analog input " + index);
            break;

          default:
            log.error("Unsupported pin type, " + type.toString());
        }
      }

    } catch (Exception err) {
      log.error("Exception while connecting Phidget devices", err);
    } finally {
      log.info("PioPhidget connections made.  Launching event dispatchers");
    }

    // Launch event dispatch threads
    this.startEventDispatchers();
    return true;
  }

  /**
   * Disconnect this phidget device.
   *
   * @throws ComponentException
   *           if an error occurs
   */
  @Override
  public void disconnect() throws ComponentException {

    // Quietly ignore repeated attempts to disconnect
    if (!this.isConnected()) {
      log.info("Ignoring repeated attempt to disconnect from " + this.getName());
      return;
    }

    super.disconnect();

    // Close and discard all the channels
    for (DigitalInput din : digitalInputChannelMap.values()) {
      if (din != null) {
        try {
          din.close();
          log.info("Closed digital input channel " + din.getDeviceName());
        } catch (PhidgetException err) {
          log.error(err);
        }
      }
    }
    digitalInputChannelMap.clear();

    for (DigitalOutput dout : digitalOutputChannelMap.values()) {
      if (dout != null) {
        try {
          dout.close();
          log.info("Closed digital output channel " + dout.getDeviceName());
        } catch (PhidgetException err) {
          log.error(err);
        }
      }
    }
    digitalOutputChannelMap.clear();

    for (VoltageInput ain : analogInputChannelMap.values()) {
      if (ain != null) {
        try {
          ain.close();
          log.info("Closed analog input channel " + ain.getDeviceName());
        } catch (PhidgetException err) {
          log.error(err);
        }
      }
    }
    analogInputChannelMap.clear();

    // Close the phidget
    try {

      // Per Phidget22 API documentation: flags Reserved for future use. Pass 0.
      final int flags = 0;

      Phidget.finalize(flags);

      log.info("PioPhidget " + this.getName() + " disconnected");

    } catch (NullPointerException err) {
      log.warn("Null pointer exception attempting to close PioPhidget");
    } catch (PhidgetException err) {
      log.error(err);
    }

    // Stop event dispatch threads
    this.stopEventDispatchers();

    log.info("PioPhidget disconnected");

  }

  /**
   * Return the value of an analog input directly from the Phidget device.
   *
   * @param dev
   *          the physical analog input device to read
   * @return the value
   * @throws ComponentException
   *           if underlying device is not connected
   */
  private final double getAiValue(final PhysicalDeviceType dev) throws ComponentException {
    double val = 0;
    try {

      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get analog input state for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      if (ain == null) {
        log.error("Analog input " + pin.getIndex() + " is not mapped?");
        throw new ComponentException("Analog input for device " + dev + " is not mapped");
      }

      // Get the value of the analog input from the channel
      val = ain.getVoltage();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }

    return val;
  }

  /*
   * (non-Javadoc)
   * 
   * @see us.steriliz.device.phidget.InterfaceKit#getSensorChangeTrigger(int)
   * 
   * @throws ComponentException if underlying device is not connected
   */
  @Override
  public double getAnalogChangeTrigger(final PhysicalDeviceType dev) throws ComponentException {

    double val = 0;
    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get change trigger value for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      val = ain.getSensorValueChangeTrigger();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
    return val;
  }

  /**
   * Get the analog data rate setting for this analog input.
   *
   * @param dev
   *          the PIO device ID
   * @return the data rate
   * @throws ComponentException
   *           if an error occurs
   */
  public int getAnalogDataRate(final PhysicalDeviceType dev) throws ComponentException {

    int val = 0;
    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get data rate for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      val = ain.getDataInterval();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
    return val;
  }

  /**
   * Get the max analog data rate.
   *
   * @param dev
   *          the PIO device ID
   * @return the maximum data rate
   * @throws ComponentException
   *           if an error occurs
   */
  public int getAnalogDataRateMax(final PhysicalDeviceType dev) throws ComponentException {

    int val = 0;
    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get data rate maximum for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      val = ain.getMaxDataInterval();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
    return val;
  }

  /**
   * Get the minimum supported data rate for this analog input device.
   *
   * @param dev
   *          the PIO Device ID
   * @return the minimum data rate
   * @throws ComponentException
   *           if an error occurs
   */
  public int getAnalogDataRateMin(final PhysicalDeviceType dev) throws ComponentException {

    int val = 0;
    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get data rate minimum for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      val = ain.getMinDataInterval();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
    return val;
  }

  /**
   * Return the value of an analog input by pin.
   *
   * @param pinNumber
   *          the pin number
   * @return the value
   * @throws ComponentException
   *           if underlying device is not connected or pin is not mapped
   */
  public final double getAnalogInputValue(final int pinNumber) throws ComponentException {

    final PhysicalDeviceType dev = analogInputMap.get(Integer.valueOf(pinNumber));

    if (dev == null) {
      throw new ComponentException("Pin not mapped");
    }

    // Go to the device to get the value
    return getAiValue(dev);
  }

  /**
   * Return the value of an analog input.
   *
   * @param dev
   *          the physical analog input device to read
   * @return the value
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public final double getAnalogInputValue(final PhysicalDeviceType dev) throws ComponentException {

    // Go to the device to get the value
    return getAiValue(dev);
  }

  /**
   * Return the value of the digital input by pin.
   *
   * @param pinNumber
   *          the pin number
   * @return the state
   * @throws ComponentException
   *           if underlying device is not connected or pin is not mapped
   */
  public final boolean getDigitalInputState(final int pinNumber) throws ComponentException {

    final PhysicalDeviceType dev = digitalInputMap.get(Integer.valueOf(pinNumber));

    if (dev == null) {
      throw new ComponentException("Pin not mapped");
    }

    // Go to the device to get the value
    return getDiState(dev);
  }

  /**
   * Return the value of the digital input.
   *
   * @param dev
   *          the physical digital input device to read
   * @return the value.
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public final boolean getDigitalInputState(final PhysicalDeviceType dev)
      throws ComponentException {

    // Go to the device to get the value
    return getDiState(dev);
  }

  /**
   * Return the value of a digital output by pin.
   *
   * @param pinNumber
   *          the pin number
   * @return the state
   * @throws ComponentException
   *           if underlying device is not connected or pin is not mapped
   */
  public final boolean getDigitalOutputState(final int pinNumber) throws ComponentException {

    final PhysicalDeviceType dev = digitalOutputMap.get(Integer.valueOf(pinNumber));

    if (dev == null) {
      throw new ComponentException("Pin not mapped");
    }

    // Go to the device to get the value
    return getDoState(dev);
  }

  /*
   * (non-Javadoc)
   * 
   * @see us.steriliz.device.phidget.InterfaceKit#getOutputState(int)
   * 
   * @throws ComponentException if underlying device is not connected
   */
  @Override
  public boolean getDigitalOutputState(final PhysicalDeviceType dev) throws ComponentException {

    // Go directly to the Phidget device to get the value
    return getDoState(dev);
  }

  /**
   * Get digital input state directly from phidget device.
   *
   * @param dev
   *          the physical digital input device to read
   * @return the value.
   * @throws ComponentException
   *           if underlying device is not connected
   */
  public final boolean getDiState(final PhysicalDeviceType dev) throws ComponentException {
    boolean val = false;
    try {

      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.DIGITAL_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get digital input state for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final DigitalInput din = digitalInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      if (din == null) {
        log.error("Digital input " + pin.getIndex() + " is not mapped?");
        throw new ComponentException("Digital input for device " + dev + " is not mapped");
      }

      // Get the state of the pin
      val = din.getState();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }

    return val;
  }

  /**
   * Get a digital output state directly from the Phidget device.
   *
   * @param dev
   *          the device for which the value is sought
   * @return the state
   */
  private boolean getDoState(final PhysicalDeviceType dev) throws ComponentException {
    boolean val = false;
    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.DIGITAL_OUTPUT_TYPE) {
        throw new ComponentException(
            "Attempt to get digital output state for a pin of type " + pin.getType());
      }

      final DigitalOutput dout = digitalOutputChannelMap.get(Integer.valueOf(pin.getIndex()));

      val = dout.getState();

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
    return val;
  }

  /*
   * (non-Javadoc)
   * 
   * @see us.steriliz.qd.pdl.PioModel#getNameResource(int)
   */
  @Override
  public String getNameResource(PhysicalDeviceType deviceId)
      throws ComponentException, InvalidValueException {

    final VesselPhidgetMeta desc = GpioMap.get(deviceId);

    if (desc != null) {
      return desc.getName();
    }

    return null;
  }

  /**
   * Return the physical pin number associated with a device.
   *
   * @param dev
   *          the device
   * @return the pin number or -1 if the device is not mapped
   */
  public int getPinNumber(final PhysicalDeviceType dev) {

    // Validate input parameter
    if (dev == null) {
      return -1;
    }

    // Map the VesselPhysicalModel device to a descriptor for a physical pin on
    // the
    // Phidget
    final VesselPhidgetMeta pin = GpioMap.get(dev);
    if (pin != null) {
      return pin.getIndex();
    }

    return -1;
  }

  /**
   * Get the type of a physical device.
   *
   * @param dev
   *          the device
   * @return the type
   */
  @Override
  public PinType getType(final PhysicalDeviceType dev) {
    final VesselPhidgetMeta pinDesc = GpioMap.get(dev);
    if (pinDesc == null) {
      return PinType.UNKNOWN_TYPE;
    }
    return pinDesc.getType();
  }

  /**
   * Assume the device is connected if any channel is connected.
   *
   * @return true if the component is connected
   */
  @Override
  public boolean isConnected() {

    // Check all channels to see if any is connected.
    for (DigitalInput din : digitalInputChannelMap.values()) {
      try {
        if (din != null && din.getIsChannel()) {
          return true;
        }
      } catch (PhidgetException err) {
        log.error(err);
      }
    }
    for (DigitalOutput dout : digitalOutputChannelMap.values()) {
      try {
        if (dout != null && dout.getIsChannel()) {
          return true;
        }
      } catch (PhidgetException err) {
        log.error(err);
      }
    }
    for (VoltageInput ain : analogInputChannelMap.values()) {
      try {
        if (ain != null && ain.getIsChannel()) {
          return true;
        }
      } catch (PhidgetException err) {
        log.error(err);
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.phidget22.VoltageInputSensorChangeListener#onSensorChange(com.
   * phidget22. VoltageInputSensorChangeEvent)
   */
  @Override
  public void onSensorChange(VoltageInputSensorChangeEvent arg0) {

    log.info("Sensor changed: " + arg0.getSensorValue());

    // Determine which analog input I/O event (pin) has triggered
    final VoltageInput source = arg0.getSource();

    int physicalPinNumber;
    try {
      physicalPinNumber = source.getChannel();
    } catch (PhidgetException err) {
      log.error(err);
      return;
    }

    // Map the Phidget pin number to a physical device in the
    // VesselPhysicalModel
    final PhysicalDeviceType dev = analogInputMap.get(Integer.valueOf(physicalPinNumber));

    if (dev == null) {
      log.error(
          "Received event on pin " + physicalPinNumber + " but no device is mapped to this pin");
      return;
    }

    dispatchEvent(new AnalogEvent(dev, arg0.getSensorValue()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.phidget22.DigitalInputStateChangeListener#onStateChange(com.phidget22.
   * DigitalInputStateChangeEvent)
   */
  @Override
  public void onStateChange(DigitalInputStateChangeEvent arg0) {

    // Validate input parameters
    if (arg0 == null) {
      return;
    }

    final boolean newVal = arg0.getState();

    // Determine which analog input I/O event (pin) has triggered
    final DigitalInput source = arg0.getSource();

    int physicalPinNumber;
    try {
      physicalPinNumber = source.getChannel();
    } catch (PhidgetException err) {
      log.error(err);
      return;
    }

    // Map the Phidget pin number to a physical device in the
    // VesselPhysicalModel
    final PhysicalDeviceType dev = digitalInputMap.get(Integer.valueOf(physicalPinNumber));

    if (dev == null) {
      log.error(
          "Received event on pin " + physicalPinNumber + " but no device is mapped to this pin");
      return;
    }

    dispatchEvent(new DigitalInputEvent(dev, newVal));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.phidget22.VoltageInputVoltageChangeListener#onVoltageChange(com.
   * phidget22. VoltageInputVoltageChangeEvent)
   */
  @Override
  public void onVoltageChange(VoltageInputVoltageChangeEvent arg0) {

    // Validate input parameters
    if (arg0 == null) {
      return;
    }

    // Determine which analog input I/O event (pin) has triggered
    final VoltageInput source = arg0.getSource();

    int physicalPinNumber;
    try {
      physicalPinNumber = source.getChannel();
    } catch (NullPointerException | PhidgetException err) {
      log.error(err);
      return;
    }

    // Map the Phidget pin number to a physical device in the
    // VesselPhysicalModel
    final PhysicalDeviceType dev = analogInputMap.get(Integer.valueOf(physicalPinNumber));

    if (dev == null) {
      log.error(
          "Received event on pin " + physicalPinNumber + " but no device is mapped to this pin");
      return;
    }

    // Dispatch the event to registered event listeners
    dispatchEvent(new AnalogEvent(dev, arg0.getVoltage()));
  }

  /**
   * Open channel to a digitalInputDevice.
   *
   * @param device
   *          the Pio device id
   * @param index
   *          is the physical pin number on the Phidget to which this device is
   *          connected (0-7) e.g., 1 for digital input 1
   */
  private DigitalInput openDigitalInputChannel(final PhysicalDeviceType device, final int index) {

    // Create the Phidget object
    DigitalInput ch;
    try {
      ch = new DigitalInput();

      // Identify the channel index
      ch.setChannel(index);

    } catch (PhidgetException e1) {
      log.error(e1);
      return null;
    }

    // Define a listener for attach events
    ch.addAttachListener(new AttachListener() {
      @Override
      public void onAttach(AttachEvent ae) {
        DigitalInput phid = (DigitalInput) ae.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.debug("Attached " + phid.getDeviceName() + " digital input " + phid.getChannel());
          } else {
            log.debug("Attached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Define a listener for detach events
    ch.addDetachListener(new DetachListener() {
      @Override
      public void onDetach(DetachEvent de) {
        DigitalInput phid = (DigitalInput) de.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber());
          } else {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Add an error listener
    ch.addErrorListener(new ErrorListener() {
      @Override
      public void onError(ErrorEvent ee) {
        log.error("Error: " + ee.getDescription());
      }
    });

    // Define a state change listener for this digital input
    ch.addStateChangeListener(this);

    // Open the channel
    try {
      ch.open(5000);
      return ch;
    } catch (PhidgetException ex) {
      log.error(ex.getDescription());
    }

    return null;
  }

  /**
   * Open channel to a digitalOutputDevice.
   *
   * @param device
   *          the Pio device id
   * @param index
   *          is the physical pin number on the Phidget to which this device is
   *          connected (0-7) e.g., 1 for digital output 1
   */
  private DigitalOutput openDigitalOutputChannel(final PhysicalDeviceType device,
      final int index) {

    // Create the Phidget object
    DigitalOutput ch;
    try {
      ch = new DigitalOutput();

      // Identify the channel index
      ch.setChannel(index);

    } catch (PhidgetException err) {
      log.error(err);
      return null;
    }

    // Define a listener for attach events
    ch.addAttachListener(new AttachListener() {
      @Override
      public void onAttach(AttachEvent ae) {
        DigitalOutput phid = (DigitalOutput) ae.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.debug("Attached " + phid.getDeviceName() + " digital output " + phid.getChannel());
          } else {
            log.debug("Attached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Define a listener for detach events
    ch.addDetachListener(new DetachListener() {
      @Override
      public void onDetach(DetachEvent de) {
        DigitalOutput phid = (DigitalOutput) de.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber());
          } else {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Add an error listener
    ch.addErrorListener(new ErrorListener() {
      @Override
      public void onError(ErrorEvent ee) {
        log.error("Error: " + ee.getDescription());
      }
    });

    // Open the channel
    try {
      ch.open(5000);
      return ch;
    } catch (PhidgetException ex) {
      log.error(ex.getDescription());
    }

    return null;
  }

  /**
   * Open channel to a VoltageInputDevice.
   *
   * @param device
   *          the Pio device id
   * @param index
   *          is the physical pin number on the Phidget to which this device is
   *          connected (0-7) e.g., 1 for analog input 1
   */
  private VoltageInput openVoltageInputChannel(final PhysicalDeviceType device, final int index) {

    VoltageInput ch;

    try {
      // Create the Phidget object
      ch = new VoltageInput();

      // Identify the channel index
      ch.setChannel(index);

    } catch (PhidgetException err) {
      log.error(err);
      return null;
    }

    // Define a listener for attach events
    ch.addAttachListener(new AttachListener() {
      @Override
      public void onAttach(AttachEvent ae) {
        VoltageInput phid = (VoltageInput) ae.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.debug("Attached " + phid.getDeviceName() + " voltage input " + phid.getChannel());
          } else {
            log.debug("Attached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Define a listener for detach events
    ch.addDetachListener(new DetachListener() {
      @Override
      public void onDetach(DetachEvent de) {
        DigitalInput phid = (DigitalInput) de.getSource();
        try {
          if (phid.getDeviceClass() != DeviceClass.VINT) {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber());
          } else {
            log.info("Detached channel " + phid.getChannel() + " on device "
                + phid.getDeviceSerialNumber() + " hub port " + phid.getHubPort());
          }
        } catch (PhidgetException ex) {
          log.error(ex.getDescription());
        }
      }
    });

    // Add an error listener
    ch.addErrorListener(new ErrorListener() {
      @Override
      public void onError(ErrorEvent ee) {
        log.error("Error: " + ee.getDescription());
      }
    });

    // Define a state change listener for this digital input
    ch.addVoltageChangeListener(this);

    // Open the channel
    try {
      ch.open(5000);
      return ch;
    } catch (PhidgetException ex) {
      log.error(ex.getDescription());
    }

    return null;
  }

  /**
   * setChangeTrigger adjusts the sensitivity of an analog input. This is the
   * amount that an inputs must change between successive SensorChangeEvents.
   * This is based on the 0-1000 range provided by getSensorValue. This value is
   * by default set to 10 for most Interface Kits with analog inputs.
   * SensorChangeTrigger is sometimes referred to as sensitivity.
   *
   * @param dev
   *          the physical device
   * @param val
   *          is the voltage delta value
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public final void setAnalogChangeTrigger(final PhysicalDeviceType dev, final double val)
      throws ComponentException {
    if (val < 0 || val > 1000) {
      log.error("Attempt to set sensor change trigger to invalid value: " + val);
      return;
    }

    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to set change trigger for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      ain.setSensorValueChangeTrigger(val);

      log.info("ChangeTrigger for channel " + dev + " set to " + val);
    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
  }

  /**
   * setDataRate adjusts the sampling rate of an analog input.
   *
   * @param dev
   *          the Pio device ID
   * @param val
   *          is the sampling interval (milliseconds)
   * @throws ComponentException
   *           if underlying device is not connected
   */
  public final void setAnalogDataRate(final PhysicalDeviceType dev, final int val)
      throws ComponentException {

    try {
      // Map the VesselPhysicalModel device to a physical pin on the Phidget
      final VesselPhidgetMeta pin = GpioMap.get(dev);

      // Validate input parameter
      if (pin.getType() != PinType.ANALOG_INPUT_TYPE) {
        throw new ComponentException(
            "Attempt to set data rate for a pin of type " + pin.getType());
      }

      // Get the channel (if any) for this pin
      final VoltageInput ain = analogInputChannelMap.get(Integer.valueOf(pin.getIndex()));

      ain.setDataInterval(val);

      log.info("Data rate for " + this.getClass().getName() + " channel " + dev + " = " + val);
    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");
    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see us.steriliz.qd.pdl.PioModel#setDigitalInputState(int, boolean)
   */
  @Override
  public void setDigitalInputState(final PhysicalDeviceType dev, boolean newVal)
      throws ComponentException {

    throw new ComponentException("Update of input fields is not implemented");
  }

  /**
   * Set the value of a digital output pin.
   *
   * @param idx
   *          the digital output pin index
   * @param val
   *          the state to set
   * @throws ComponentException
   *           if an error occurs
   */
  public void setDigitalOutputPin(final int idx, final boolean val) throws ComponentException {

    // Validate input parameters
    if (idx < 0 || idx > 7) {
      log.error("Invalid pin number " + idx);
      throw new ComponentException("Invalid pin number: " + idx);
    }

    try {

      // Get the channel (if any) for this pin
      final DigitalOutput dout = digitalOutputChannelMap.get(Integer.valueOf(idx));

      if (dout == null) {
        log.error("Digital output " + idx + " is not mapped?");
        throw new ComponentException("Digital output for pin " + idx + " is not mapped");
      }

      // Use the channel to update the state of the pin
      dout.setState(val);

    } catch (NullPointerException err) {
      throw new ComponentException("Device " + this.getClass().getName() + " is not attached");

    } catch (PhidgetException pe) {
      throw new ComponentException(
          "Phidget exception " + pe.getMessage() + pe.getMessage() + ": " + pe.getDescription());
    }
  }

  /**
   * setOutputState sets the state of the selected digital output.
   *
   * @param dev
   *          the physical digital output device to update
   * @param val
   *          the boolean value to set.
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public final void setDigitalOutputState(final PhysicalDeviceType dev, final boolean val)
      throws ComponentException {

    // Map the VesselPhysicalModel device to a physical pin on the Phidget
    final VesselPhidgetMeta pin = GpioMap.get(dev);
    // Set the state of this pin
    setDigitalOutputPin(pin.getIndex(), val);
  }

}
