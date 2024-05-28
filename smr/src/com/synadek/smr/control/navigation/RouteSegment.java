/**
 * RouteSegment.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GpsCoordinates;
import org.json.simple.JSONObject;

/**
 * Operations common to all route segments.
 */
public interface RouteSegment {

  /**
   * Segment types.
   */
  public enum SegmentType {
    /**
     * Grid segment.
     */
    GRID_SEGMENT,
    /**
     * Hold segment.
     */
    HOLD_SEGMENT,
    /**
     * Move segment.
     */
    MOVE_SEGMENT,
    /**
     * Spiral segment.
     */
    SPIRAL_SEGMENT,
  }

  /**
   * Get the segment type.
   *
   * @return the segmentType
   */
  public SegmentType getType();

  /**
   * Get the destination location associated with this Segment.
   *
   * @return the location
   */
  public GpsCoordinates getDestination();

  /**
   * Set/Update the destination location associated with this Segment.
   *
   * @param finish
   *          the destination location to set
   */
  public void setDestination(GpsCoordinates finish);

  /**
   * Get the start location associated with this Segment.
   *
   * @return the start location
   */
  public GpsCoordinates getStartLocation();

  /**
   * Set/Update the start location associated with this Segment.
   *
   * @param startLocation
   *          the start location to set
   */
  public void setStartLocation(GpsCoordinates startLocation);

  /**
   * Generate a JSON Object representing this Segment.
   *
   * @return the Segment in JSON Object format
   */
  JSONObject toJson();
}
