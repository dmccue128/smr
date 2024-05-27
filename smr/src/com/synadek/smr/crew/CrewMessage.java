/**
 * CrewMessage.java
 * 21 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

import org.json.simple.JSONObject;

/**
 * A message from one crew member to another.
 */
public class CrewMessage {
  /**
   * Time the message was sent.
   */
  private final long timestamp;
  /**
   * Identity of the sender.
   */
  private final CrewMember sender;
  /**
   * Identity of the receiver.
   */
  private final CrewMember receiver;
  /**
   * The message.
   */
  private final JSONObject message;

  /**
   * Default constructor.
   * 
   * @param fromCrew
   *          Crew Member sending the message
   * @param toCrew
   *          Crew Member to receive the message
   * @param msg
   *          the message
   */
  public CrewMessage(final CrewMember fromCrew, final CrewMember toCrew,
      final JSONObject msg) {
    timestamp = System.currentTimeMillis();
    sender = fromCrew;
    receiver = toCrew;
    message = msg;
  }

  /**
   * Crew member sending the message.
   * 
   * @return the Crew Member
   */
  public final CrewMember getSender() {
    return sender;
  }

  /**
   * Crew Member receiving the message.
   * 
   * @return the Crew Member
   */
  public final CrewMember getReciever() {
    return receiver;
  }

  /**
   * The message.
   * 
   * @return the message
   */
  public final JSONObject getMessage() {
    return message;
  }

  /**
   * Time the message was sent.
   * 
   * @return time in millis since the epoch.
   */
  public final long getTimestamp() {
    return timestamp;
  }

  /**
   * Canonical string representation of a crew message.
   * 
   * @return the contents of the message (including timestamp)
   */
  @Override
  public final String toString() {
    return String.format("%d %s->%s: %s", Long.valueOf(this.timestamp),
        this.sender.getCrewName(), this.receiver.getCrewName(),
        this.message.toJSONString());
  }
}
