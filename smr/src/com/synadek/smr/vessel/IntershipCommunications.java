/**
 * IntershipCommunications.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import org.json.simple.JSONObject;

/**
 * Communications from this vessel to other vessels in communications range.
 */
public interface IntershipCommunications extends VesselComponent {
  /**
   * Send a message to another ship.
   *
   * @param shipName
   *          the name of the ship to which to send the communication
   * @param message
   *          the message
   */
  void sendMessage(String shipName, JSONObject message);

  /**
   * Send an image to another ship.
   *
   * @param shipName
   *          the name of the ship to which to send the communication
   * @param metadata
   *          metadata associated with this image
   * @param imagePath
   *          local filesystem path to the image file
   */
  void sendMessage(String shipName, JSONObject metadata, String imagePath);

  /**
   * Add a listener for messages coming from off-ship.
   *
   * @param comm
   *          the listener
   */
  void addListener(CommunicationsListener comm);
}
