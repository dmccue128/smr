/**
 * GridSegment.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GpsCoordinates;
import org.json.simple.JSONObject;

/**
 * A navigation pattern that covers an area by navigating in strips across the
 * area, top-to-bottom, left-to-right.
 */
public class GridSegment extends AbstractRouteSegmentImpl {

  /**
   * The distance between strips of the grid (meters).
   */
  private final int spacing;

  /**
   * Default constructor.
   *
   * @param startLocation
   *          the start location
   * @param gridSpacing
   *          the distance between loops of the spiral (meters)
   * @param corner
   *          the opposite corner of the grid area (initial corner is current
   *          location)
   */
  public GridSegment(final GpsCoordinates startLocation, final int gridSpacing,
      final GpsCoordinates corner) {
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
   * Get the spacing.
   *
   * @return the spacing
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * Get the opposite corner.
   *
   * @return the oppositeCorner
   */
  public GpsCoordinates getOppositeCorner() {
    return this.getDestination();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.RouteSegment#toJSON()
   */
  @SuppressWarnings("unchecked")
  @Override
  public JSONObject toJson() {
    final JSONObject result = super.toJson();
    result.put("spacing", Integer.valueOf(getSpacing()));
    return result;
  }
}
