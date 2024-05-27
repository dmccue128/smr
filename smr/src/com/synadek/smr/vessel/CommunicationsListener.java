/**
 * CommunicationsListener.java
 * 18 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import org.json.simple.JSONObject;

/**
 * A Communications Listener implements a method that is invoked with inbound communications from
 * off-vessel such as shore-to-ship or ship-to-ship.
 */
public interface CommunicationsListener {

  /**
   * Receive a message from off-ship.
   * 
   * @param source
   *          the name of the ship or shore entity sending the message
   * @param message
   *          the message
   */
  void receiveMessage(String source, JSONObject message);
}
