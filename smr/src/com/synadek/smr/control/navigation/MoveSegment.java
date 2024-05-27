/**
 * MoveSegment.java
 * 3 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

import org.json.simple.JSONObject;

/**
 * A navigation pattern that moves the vessel directly from the current location to a new location
 * along a great-circle path.
 */
public class MoveSegment extends AbstractRouteSegmentImpl {

  /**
   * Default constructor.
   * 
   * @param nextStop
   *          is the destination location
   */
  public MoveSegment(final GPSCoordinates start, final GPSCoordinates finish) {
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
  public JSONObject toJSON() {
    return super.toJSON();
  }
}
