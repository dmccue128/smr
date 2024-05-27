/**
 * BlackBox.java
 * 22 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.control.mission;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * Data recorder. Note this implementation is just in-memory. Complete implementation will need to
 * record in NVM.
 */
public class BlackBox {

  /**
   * Default constructor is private since all methods and fields are static.
   */
  private BlackBox() {

  }

  /**
   * Time-stamped event buffer.
   */
  private static final List<JSONObject> entries = new LinkedList<>();

  /**
   * add a message to the black box.
   * 
   * @param msg
   *          the message
   */
  @SuppressWarnings("unchecked")
  public static void addMessage(final String msg) {
    final JSONObject entry = new JSONObject();
    entry.put("timestamp", Long.valueOf(System.currentTimeMillis()));
    entry.put("message", msg);
    entries.add(entry);
  }

  /**
   * Add data to be recorded in the black box.
   * 
   * @param info
   *          the info to add to the black box.
   */
  public static void addData(final JSONObject info) {
    addMessage(info.toJSONString());
  }

  /**
   * Get the most recent entries in the black box.
   * 
   * @param maxcount
   *          the maximum number of entries to return. -1 means no limit.
   * @return the last 'maxcount' (or fewer) entries where each entry is a JSON object with two
   *         members: { "timestamp": <ms since epoch>, "message":<message> }
   */
  public static JSONObject[] getEntries(final int maxcount) {

    // Compute the size of the array to return
    int returnArraySize = entries.size();
    if (maxcount < returnArraySize) {
      returnArraySize = maxcount;
    }

    // Allocate the return array
    final JSONObject[] result = new JSONObject[returnArraySize];

    // Compute the start and end indexes
    final int startIndex = entries.size() - returnArraySize;
    final int endIndex = entries.size();

    // Copy the entries to the result array
    entries.subList(startIndex, endIndex).toArray(result);

    return result;
  }
}
