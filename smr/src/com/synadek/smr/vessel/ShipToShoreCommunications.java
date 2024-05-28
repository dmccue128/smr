/**
 * ShipToShoreCommunications.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import org.json.simple.JSONObject;

/**
 * Ship to shore communications support.
 */
public interface ShipToShoreCommunications extends VesselComponent {

  /**
   * Send a message to shore.
   *
   * @param dest
   *          the name of the shore entity to which the message is being sent
   * @param message
   *          the message
   */
  void sendMessage(String dest, JSONObject message);

  /**
   * Send an image to shore.
   *
   * @param dest
   *          the name of the shore entity to which the message is being sent
   * @param metadata
   *          metadata associated with this image
   * @param imagePath
   *          local filesystem path to the image file
   */
  void sendMessage(String dest, JSONObject metadata, String imagePath);

  /**
   * Add a listener for messages coming from off-ship.
   *
   * @param comm
   *          the listener
   * @throws NullPointerException
   *           if the listener is null
   */
  void addListener(CommunicationsListener comm) throws NullPointerException;

  /**
   * Remove a listener for messages coming from off-ship.
   *
   * @param comm
   *          the listener
   * @throws NullPointerException
   *           if the listener is null
   */
  void removeListener(CommunicationsListener comm) throws NullPointerException;
}
