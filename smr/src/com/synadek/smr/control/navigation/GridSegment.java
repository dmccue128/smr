/**
 * GridSegment.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

import org.json.simple.JSONObject;

/**
 * A navigation pattern that covers an area by navigating in strips across the area, top-to-bottom,
 * left-to-right.
 */
public class GridSegment extends AbstractRouteSegmentImpl {

  /**
   * The distance between strips of the grid (meters).
   */
  private final int spacing;

  /**
   * Default constructor.
   * 
   * @param spacing
   *          the distance between loops of the spiral (meters)
   * @param corner
   *          the opposite corner of the grid area (initial corner is current location)
   */
  public GridSegment(final GPSCoordinates startLocation, final int gridSpacing,
      final GPSCoordinates corner) {
    super(SegmentType.GRID_SEGMENT, startLocation, corner);
    spacing = gridSpacing;
  }

  /**
   * JSON parsing constructor.
   * 
   * @param obj
   *          the JSON Object representing this segment
   */
  public GridSegment(final JSONObject obj) {
    super(obj);
    spacing = ((Integer) obj.get("spacing")).intValue();
  }

  /**
   * @return the spacing
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * @return the oppositeCorner
   */
  public GPSCoordinates getOppositeCorner() {
    return this.getDestination();
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
    return result;
  }
}
