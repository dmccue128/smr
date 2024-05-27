/**
 * HoldSegment.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

import org.json.simple.JSONObject;

/**
 * A navigation pattern that holds the vessel in a location for a specified amount of time.
 */
public class HoldSegment extends AbstractRouteSegmentImpl {

  private final long holdTimeMillis;

  /**
   * Default constructor.
   * 
   * @param start
   *          the start location
   * @param duration
   *          the number of milliseconds to wait in this location
   */
  public HoldSegment(final GPSCoordinates start, final long duration) {
    super(SegmentType.HOLD_SEGMENT, start, start);
    holdTimeMillis = duration;
  }

  /**
   * JSON parsing constructor.
   * 
   * @param obj
   *          the JSON Object representing this segment
   */
  public HoldSegment(final JSONObject obj) {
    super(obj);
    holdTimeMillis = ((Long) obj.get("durationMillis")).longValue();
  }

  /**
   * @return the holdTimeMillis
   */
  public long getHoldTimeMillis() {
    return holdTimeMillis;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.RouteSegment#toJSON()
   */
  @SuppressWarnings("unchecked")
  @Override
  public JSONObject toJSON() {
    final JSONObject result = super.toJSON();
    result.put("durationMillis", Long.valueOf(getHoldTimeMillis()));
    return result;
  }
}
