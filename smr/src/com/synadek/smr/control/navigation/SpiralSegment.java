/**
 * SpiralSegment.java
 * 3 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

import org.json.simple.JSONObject;

/**
 * A navigation pattern that covers an area by navigating in expanding circles from the current
 * location.
 */
public class SpiralSegment extends AbstractRouteSegmentImpl {

  /**
   * the distance between loops of the spiral (meters).
   */
  private final int spacing;
  /**
   * the number of degrees to travel e.g., 720 => two complete circles around the initial point.
   */
  private final int degrees;

  /**
   * Default constructor.
   * 
   * @param start
   *          the start location
   * @param initialBearing
   *          the bearing at the start of the spiral
   * @param spacing
   *          the distance between loops of the spiral (meters)
   * @param degrees
   *          the number of degrees to travel e.g., 720 => two complete circles around the initial
   *          point
   */
  public SpiralSegment(final GPSCoordinates start, final int initialBearing,
      final int loopSpacing, final int totalDegrees) {
    super(SegmentType.SPIRAL_SEGMENT, start, start.getEndLocation(
        (initialBearing + totalDegrees) % 360, loopSpacing / 360 * totalDegrees));
    spacing = loopSpacing;
    degrees = totalDegrees;
  }

  /**
   * JSON parsing constructor.
   * 
   * @param obj
   *          the JSON Object representing this segment
   */
  public SpiralSegment(final JSONObject obj) {
    super(obj);
    spacing = ((Integer) obj.get("spacing")).intValue();
    degrees = ((Integer) obj.get("degrees")).intValue();
  }

  /**
   * @return the spacing
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * @return the degrees
   */
  public int getDegrees() {
    return degrees;
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
    result.put("spacing", Integer.valueOf(getSpacing()));
    result.put("degrees", Integer.valueOf(getDegrees()));
    return result;
  }
}
