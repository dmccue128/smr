/**
 * RouteSegment.java
 * 3 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

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
  public GPSCoordinates getDestination();

  /**
   * Set/Update the destination location associated with this Segment.
   * 
   * @param finish
   *          the destination location to set
   */
  public void setDestination(GPSCoordinates finish);

  /**
   * Get the start location associated with this Segment.
   * 
   * @return the start location
   */
  public GPSCoordinates getStartLocation();

  /**
   * Set/Update the start location associated with this Segment.
   * 
   * @param startLocation
   *          the start location to set
   */
  public void setStartLocation(GPSCoordinates startLocation);

  /**
   * Generate a JSON Object representing this Segment.
   * 
   * @return the Segment in JSON Object format
   */
  JSONObject toJSON();
}
