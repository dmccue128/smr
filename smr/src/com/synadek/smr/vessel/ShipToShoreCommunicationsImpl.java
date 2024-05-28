/**
 * ShipToShoreCommunicationsImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.ComponentException;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 * Ship to shore communications support.
 */
public class ShipToShoreCommunicationsImpl extends VesselComponentImpl
    implements
      ShipToShoreCommunications {

  private Set<CommunicationsListener> myListeners;

  /**
   * Default constructor.
   */
  public ShipToShoreCommunicationsImpl() {
    super(VesselComponentType.VESSEL_SHIP_TO_SHORE, "ship-to-shore communications");
    resetConfiguration();
  }

  /**
   * Named constructor.
   *
   * @param name
   *          a name for this component
   */
  public ShipToShoreCommunicationsImpl(final String name) {
    super(VesselComponentType.VESSEL_SHIP_TO_SHORE, name);
    resetConfiguration();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.synadek.smr.vessel.ShipToShoreCommunications#addListener(com.synadek.
   * smr.vessel.CommunicationsListener)
   */
  @Override
  public void addListener(CommunicationsListener comm) throws NullPointerException {
    if (comm == null) {
      throw new NullPointerException();
    }
    myListeners.add(comm);
  }

  /**
   * Connect parameter indicates whether to connect to a physical or simulated
   * component.
   *
   * @param sim
   *          true if the connection is to a simulation of the component
   * @throws ComponentException
   *           if an error occurs
   */
  @Override
  public boolean connect(final boolean sim) throws ComponentException {

    // Simulation is not yet supported for this component
    if (sim) {
      this.simulated = true;
      this.connected = true;
      return true;
    }

    this.simulated = false;
    this.connected = true;
    return true;
  }

  private void dispatchMessage(String source, JSONObject message) {
    for (CommunicationsListener listener : myListeners) {
      listener.receiveMessage(source, message);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.vessel.ShipToShoreCommunications#removeListener(com.
   * synadek. smr.vessel.CommunicationsListener)
   */
  @Override
  public void removeListener(CommunicationsListener comm) throws NullPointerException {
    if (comm == null) {
      throw new NullPointerException();
    }
    myListeners.remove(comm);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.synadek.smr.vessel.ShipToShoreCommunications#sendMessage(java.lang.
   * String, org.json.simple.JSONObject)
   */
  @Override
  public void sendMessage(String dest, JSONObject message) {
    if (this.simulated) {
      log.info("Simulated message sent to shore: {}", message.toJSONString());
    } else {
      // TODO implement ship to shore communications
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.synadek.smr.vessel.ShipToShoreCommunications#sendMessage(java.lang.
   * String, org.json.simple.JSONObject, java.lang.String)
   */
  @Override
  public void sendMessage(String dest, JSONObject metadata, String imagePath) {
    if (this.simulated) {
      log.info("Simulated image message sent to shore: {}, path: {}", metadata.toJSONString(),
          imagePath);
    } else {
      // TODO implement ship to shore image communications
    }
  }

  /**
   * Simulate the receipt of a message from shore.
   *
   * @param source
   *          simulated source of the message
   * @param message
   *          simulated message
   */
  public void simulateMessage(String source, JSONObject message) {
    dispatchMessage(source, message);
  }
}
