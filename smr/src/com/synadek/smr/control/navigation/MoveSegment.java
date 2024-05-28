/**
 * MoveSegment.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GpsCoordinates;
import org.json.simple.JSONObject;

/**
 * A navigation pattern that moves the vessel directly from the current location
 * to a new location along a great-circle path.
 */
public class MoveSegment extends AbstractRouteSegmentImpl {

  /**
   * Default constructor.
   *
   * @param start
   *          the start location
   * @param finish
   *          the destination location
   */
  public MoveSegment(final GpsCoordinates start, final GpsCoordinates finish) {
    super(SegmentType.MOVE_SEGMENT, start, finish);
  }

  /**
   * JSON parsing constructor.
   *
   * @param obj
   *          the JSON Object representing this segment
   */
  public MoveSegment(final JSONObject obj) {
    super(obj);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.RouteSegment#toJSON()
   */
  @Override
  public JSONObject toJson() {
    return super.toJson();
  }
}
