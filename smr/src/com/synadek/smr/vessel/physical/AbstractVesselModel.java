/**
 * AbstractVesselModel.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import com.synadek.core.AbstractComponent;
import com.synadek.core.Component;
import com.synadek.core.ComponentException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Methods common to all physical device models for vessels.
 */
public abstract class AbstractVesselModel extends AbstractComponent
    implements
      VesselPhysicalModel {

  /**
   * A registry for handlers that respond to digital input state change events.
   * Mapping GPIO pin numbers to the set of handlers for that GPIO pin.
   */
  private final Map<PhysicalDeviceType, Set<PdlDigitalHandler>> digitalInHdlrMap = new HashMap<>();

  /**
   * A registry for handlers that respond to digital output state change events.
   * Mapping GPIO pin numbers to the set of handlers for that GPIO pin.
   */
  private final Map<PhysicalDeviceType, Set<PdlDigitalHandler>> digitalOutHdlrMap = new HashMap<>();

  /**
   * A registry for handlers that respond to analog input state change events.
   * Mapping GPIO pin numbers to the set of handlers for that GPIO pin.
   */
  private final Map<PhysicalDeviceType, Set<PdlAnalogHandler>> analogInHdlrMap = new HashMap<>();

  /**
   * Queue of analog input events to be signaled to registered event handlers.
   */
  private final LinkedBlockingQueue<AnalogEvent> ainQueue = new LinkedBlockingQueue<>();

  /**
   * Queue of digital input events to be signaled to registered event handlers.
   */
  private final LinkedBlockingQueue<DigitalInputEvent> diQueue = new LinkedBlockingQueue<>();

  /**
   * Analog input event dispatcher.
   */
  private AnalogEventDispatcher analogInputEventDispatcher;

  /**
   * Digital input event dispatcher.
   */
  private DigitalInputEventDispatcher digitalInputEventDispatcher;

  /**
   * Default constructor.
   */
  public AbstractVesselModel() {
    super();
  }

  protected synchronized void startEventDispatchers() {
    analogInputEventDispatcher = new AnalogEventDispatcher(this, ainQueue);
    digitalInputEventDispatcher = new DigitalInputEventDispatcher(this, diQueue);
  }

  protected synchronized void stopEventDispatchers() {
    // Stop the threads
    try {
      analogInputEventDispatcher.stop();
    } catch (NullPointerException err) {
      log.error(err);
    }

    try {
      digitalInputEventDispatcher.stop();
    } catch (NullPointerException err) {
      log.error(err);
    }

    // Discard the objects
    analogInputEventDispatcher = null;
    digitalInputEventDispatcher = null;
  }

  /**
   * Named constructor.
   *
   * @param componentName
   *          the name of this component instance
   */
  public AbstractVesselModel(final String componentName) {
    super(componentName);
  }

  /**
   * Add a digital input change listener. The input change handler method will
   * be called when an input on this Interface Kit has changed. There is no
   * limit on the number of input change handlers that can be registered for a
   * particular VesselPhysicalModel.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void addDigitalInputListener(PhysicalDeviceType dev, PdlDigitalHandler hdlr)
      throws ComponentException {

    if (digitalInHdlrMap == null) {
      log.error("Failed to register a handler because handler map is null");
      return;
    }

    // Look up the list of handlers for this particular digital input pin
    Set<PdlDigitalHandler> idxList = digitalInHdlrMap.get(dev);

    // Create a set of handlers if it does not already exist
    if (idxList == null) {
      idxList = new HashSet<>();
      digitalInHdlrMap.put(dev, idxList);
    }

    // Add this handler to the list
    idxList.add(hdlr);

    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("%s registered a handler for %s", c.getName(), dev.toString()));
    } else {
      log.info(String.format("A %s registered a handler for %s", hdlr.getClass().getSimpleName(),
          dev.toString()));
    }
  }

  /**
   * Remove a digital input change listener from all GPIO pins.
   *
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void removeDigitalInputListener(PdlDigitalHandler hdlr) throws ComponentException {

    // Check parameter
    if (hdlr == null) {
      return;
    }

    // Confirm that handler maps were initialized correctly
    if (digitalInHdlrMap == null) {
      log.error("Failed to unregister a handler because digital handler map is null");
      return;
    }

    // Remove this handler from all pins and count the number of
    // different events (I/O pins) for which it was registered
    int count = 0;
    for (Entry<PhysicalDeviceType, Set<PdlDigitalHandler>> item : digitalInHdlrMap
        .entrySet()) {

      final Collection<PdlDigitalHandler> entryList = item.getValue();

      if (entryList != null && entryList.size() > 0) {
        while (entryList.remove(hdlr)) {
          count += 1;
        }
      }
    }

    // Log a debugging message
    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("Component %s unregistered %d digital input event handler(s)",
          c.getName(), Integer.valueOf(count)));
    } else {
      log.info(String.format("A %s unregistered %d digital input event handler(s)",
          hdlr.getClass().getSimpleName(), Integer.valueOf(count)));
    }
  }

  /**
   * Add a digital output change listener for digital outputs on this device.
   * The output change handler method will be called when an input on this
   * Interface Kit has changed. There is no limit on the number of output change
   * handlers that can be registered for a particular VesselPhysicalModel.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void addDigitalOutputListener(PhysicalDeviceType dev, PdlDigitalHandler hdlr)
      throws ComponentException {

    if (digitalOutHdlrMap == null) {
      log.error("Failed to register a handler because handler map is null");
      return;
    }

    // Look up the list of handlers for this particular digital input
    Set<PdlDigitalHandler> idxList = digitalOutHdlrMap.get(dev);

    // Create it if it does not already exist
    if (idxList == null) {
      idxList = new HashSet<>();
      digitalOutHdlrMap.put(dev, idxList);
    }

    // Add this handler to the list
    idxList.add(hdlr);

    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("Component %s registered a handler for device %s", c.getName(),
          dev.toString()));
    } else {
      log.info(String.format("A %s registered a handler for device %s",
          hdlr.getClass().getSimpleName(), dev.toString()));
    }
  }

  /**
   * Remove a digital output change listener from all GPIO pins.
   *
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void removeDigitalOutputListener(PdlDigitalHandler hdlr) throws ComponentException {

    // Check parameter
    if (hdlr == null) {
      return;
    }

    // Confirm that handler maps were initialized correctly
    if (digitalOutHdlrMap == null) {
      log.error("Failed to unregister a handler because digital handler map is null");
      return;
    }

    // Remove this handler from all pins and count the number of
    // different events (I/O pins) for which it was registered
    int count = 0;
    for (Entry<PhysicalDeviceType, Set<PdlDigitalHandler>> item : digitalOutHdlrMap
        .entrySet()) {

      final Collection<PdlDigitalHandler> entryList = item.getValue();

      if (entryList != null && entryList.size() > 0) {
        // Iterate through the collection removing every instance of hdlr
        while (entryList.remove(hdlr)) {
          count += 1;
        }
      }
    }

    // Log a debugging message
    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("Component %s unregistered %d digital output event handler(s)",
          c.getName(), Integer.valueOf(count)));
    } else {
      log.info(String.format("A %s unregistered %d digital output event handler(s)",
          hdlr.getClass().getSimpleName(), Integer.valueOf(count)));
    }
  }

  /**
   * Add an analog sensor change listener. The sensor change handler method will
   * be called when a sensor on this device has changed by at least the trigger
   * amount (see SetAnalogChangeTrigger) that has been set for this sensor.
   * There is no limit on the number of sensor change handlers that can be
   * registered for a particular VesselPhysicalModel.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void addAnalogInputListener(PhysicalDeviceType dev, PdlAnalogHandler hdlr)
      throws ComponentException {

    if (analogInHdlrMap == null) {
      log.error("Failed to register a handler because handler map is null");
      return;
    }

    // Look up the list of handlers for this particular digital input
    Set<PdlAnalogHandler> idxList = analogInHdlrMap.get(dev);

    // Create it if it does not already exist
    if (idxList == null) {
      idxList = new HashSet<>();
      analogInHdlrMap.put(dev, idxList);
    }

    // Add this handler to the list
    idxList.add(hdlr);

    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("Component %s registered a handler for analog device %s", c.getName(),
          dev.toString()));
    } else {
      log.info(String.format("A %s registered a handler for analog device %s",
          hdlr.getClass().getSimpleName(), dev.toString()));
    }
  }

  /**
   * Remove an analog sensor change listener from all GPIO pins.
   *
   * @param hdlr
   *          the listener
   * @throws ComponentException
   *           if underlying device is not connected
   */
  @Override
  public void removeAnalogInputListener(PdlAnalogHandler hdlr) throws ComponentException {

    // Check parameter
    if (hdlr == null) {
      return;
    }

    // Confirm that handler maps were initialized correctly
    if (analogInHdlrMap == null) {
      log.error("Failed to unregister a handler because analog handler map is null");
      return;
    }

    // Remove this handler from all pins and count the number of
    // different events (I/O pins) for which it was registered
    int count = 0;
    for (Entry<PhysicalDeviceType, Set<PdlAnalogHandler>> item : analogInHdlrMap
        .entrySet()) {

      final Collection<PdlAnalogHandler> entryList = item.getValue();

      if (entryList != null && entryList.size() > 0) {
        // Iterate through the collection removing every instance of hdlr
        while (entryList.remove(hdlr)) {
          count += 1;
        }
      }
    }

    // Log a debugging message
    if (hdlr instanceof Component) {
      final Component c = (Component) hdlr;
      log.info(String.format("Component %s unregistered %d analog input event handlers",
          c.getName(), Integer.valueOf(count)));
    } else {
      log.info(String.format("A %s unregistered %d analog input event handlers",
          hdlr.getClass().getSimpleName(), Integer.valueOf(count)));
    }
  }

  /**
   * Get the (possibly empty) collection of digital input handlers registered
   * for this gpio index.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @return the handlers
   */
  protected Collection<PdlDigitalHandler> getDigitalInputHandlers(final PhysicalDeviceType dev) {
    return digitalInHdlrMap.get(dev);
  }

  /**
   * Get the (possibly empty) collection of digital output handlers registered
   * for this gpio index.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @return the handlers
   */
  protected Collection<PdlDigitalHandler> getDigitalOutputHandlers(final PhysicalDeviceType dev) {
    return digitalOutHdlrMap.get(dev);
  }

  /**
   * Get the (possibly empty) collection of analog input handlers registered for
   * this gpio index.
   *
   * @param dev
   *          the GPIO Pin number of the I/O
   * @return the handlers
   */
  protected Collection<PdlAnalogHandler> getAnalogInputHandlers(final PhysicalDeviceType dev) {
    return analogInHdlrMap.get(dev);
  }

  /**
   * Dispatch an analog input event to registered handlers (typically logical
   * devices).
   *
   * @param evt
   *          the analog event descriptor
   */
  protected void dispatchEvent(final AnalogEvent evt) {

    // Validate input parameters
    if (evt == null) {
      return;
    }

    // Get the analog input handlers (if any) associated with this device
    final Collection<PdlAnalogHandler> handlers = this.getAnalogInputHandlers(evt.getDevice());

    // No handlers?
    if (handlers == null) {
      return;
    }

    // Queue the event to be processed by the event processing thread of the Pio
    ainQueue.offer(evt);
  }

  /**
   * Dispatch a digital input event to registered handlers (typically logical
   * devices).
   *
   * @param evt
   *          the digital input event descriptor
   */
  protected void dispatchEvent(final DigitalInputEvent evt) {

    // Validate input parameters
    if (evt == null) {
      return;
    }

    // Get the analog input handlers (if any) associated with this device
    final Collection<PdlAnalogHandler> handlers = this.getAnalogInputHandlers(evt.getDevice());

    // No handlers?
    if (handlers == null) {
      return;
    }

    // Queue the event to be processed by the event processing thread of the Pio
    diQueue.offer(evt);
  }

  /**
   * Invoke registered handlers (if any) for analog input events.
   *
   * @param evt
   *          the event to dispatch to the handlers
   */
  @Override
  public void invokeAnalogHandlers(final AnalogEvent evt) {

    // Validate input parameters
    if (evt == null) {
      return;
    }

    // Get the analog input handlers (if any) associated with this device
    final Collection<PdlAnalogHandler> handlers = this.getAnalogInputHandlers(evt.getDevice());

    // No handlers?
    if (handlers == null) {
      return;
    }

    log.info("Invoking handler(s) for event (" + evt.getDevice() + "," + evt.getNewValue() + ")");

    // Invoke each handler in turn
    for (PdlAnalogHandler hdlr : handlers) {
      try {
        hdlr.physicalDeviceStateChange(evt.getDevice(), evt.getNewValue());
      } catch (Exception err) {
        log.error(err);
      }
    }
  }

  /**
   * Invoke registered handlers (if any) for digital input events.
   *
   * @param evt
   *          the event to dispatch to the handlers
   */
  @Override
  public void invokeDigitalInputHandlers(final DigitalInputEvent evt) {

    // Validate input parameters
    if (evt == null) {
      return;
    }

    // Get the analog input handlers (if any) associated with this device
    final Collection<PdlDigitalHandler> handlers = this.getDigitalInputHandlers(evt.getDevice());

    // No handlers?
    if (handlers == null) {
      return;
    }

    log.info("Invoking handler(s) for event (" + evt.getDevice() + "," + evt.getNewState() + ")");

    // Invoke each handler in turn
    for (PdlDigitalHandler hdlr : handlers) {
      try {
        hdlr.physicalDeviceStateChange(evt.getDevice(), evt.getNewState());
      } catch (Exception err) {
        log.error(err);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * us.steriliz.qd.pdl.PioModel#getDeviceIterator(us.steriliz.qd.pdl.PioModel.
   * PinType)
   */
  @Override
  public PdmIterator getDeviceIterator(PinType type) {
    return new PdmIterator(this, type);
  }

}
