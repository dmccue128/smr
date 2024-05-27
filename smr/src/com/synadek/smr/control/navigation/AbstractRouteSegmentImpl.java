/**
 * AbstractRouteSegmentImpl.java
 * 22 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import com.synadek.core.GPSCoordinates;

import org.json.simple.JSONObject;

/**
 * Heading:
 * This is where my nose points - and seeing as my nose is attached to my head, this is where my head (and thus my machine) is pointing relative to North.
 * Course:
 * This is my INTENDED path of travel that I have calculated taking into consideration winds, variation and declination.
 * Track:
 * This is my ACTUAL path traveled over ground - just like a set of tracks I would leave behind in the snow or sand, relative to North
 * Bearing:
 * This is the angle between the location of an object, machine or destination and either:
 * - my heading. This is called 'Relative Bearing'.
 * - or magnetic north (direction toward the magnetic north pole). This is called 'Magnetic Bearing'.
 */
/**
 * A Route Segment defines an action to be taken along the route.
 */
public abstract class AbstractRouteSegmentImpl implements RouteSegment {

  /**
   * Segment type e.g., grid, hold, move, spiral.
   */
  private SegmentType myType;

  /**
   * Planned start location (may be updated).
   */
  private GPSCoordinates startLocation;

  /**
   * Planned finish location (may be updated).
   */
  private GPSCoordinates endLocation;

  /**
   * Default constructor.
   * 
   * @param type
   *          is the segment type e.g., grid, hold, move, spiral
   */
  public AbstractRouteSegmentImpl(final SegmentType type, final GPSCoordinates start,
      final GPSCoordinates finish) {
    myType = type;
    startLocation = start;
    endLocation = finish;
  }

  /**
   * JSON parsing constructor.
   * 
   * @param obj
   *          the JSON Object representing the base components of a segment
   */
  public AbstractRouteSegmentImpl(final JSONObject obj) {
    myType = SegmentType.valueOf((String) obj.get("type"));
    startLocation = new GPSCoordinates((JSONObject) obj.get("startLocation"));
    endLocation = new GPSCoordinates((JSONObject) obj.get("endLocation"));
  }

  /**
   * Get the destination location associated with this Segment.
   * 
   * @return the location
   */
  @Override
  public GPSCoordinates getDestination() {
    return endLocation;
  }

  /**
   * @param finish
   *          the destination location to set
   */
  @Override
  public void setDestination(final GPSCoordinates finish) {
    endLocation = finish;
  }

  /**
   * @return the startLocation
   */
  @Override
  public final GPSCoordinates getStartLocation() {
    return startLocation;
  }

  /**
   * @param startLocation
   *          the start location to set
   */
  @Override
  public final void setStartLocation(GPSCoordinates startLocation) {
    this.startLocation = startLocation;
  }

  /**
   * Get the segment type.
   * 
   * @return the segmentType
   */
  @Override
  public SegmentType getType() {
    return myType;
  }

  /**
   * Set/Update the segment type.
   * 
   * @param typ
   *          the segmentType to set
   */
  public void setType(SegmentType typ) {
    this.myType = typ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.RouteSegment#toJSON()
   */
  @SuppressWarnings("unchecked")
  @Override
  public JSONObject toJSON() {
    final JSONObject result = new JSONObject();
    result.put("type", myType.toString());
    result.put("startLocation", this.getStartLocation().toJSON());
    result.put("endLocation", this.getDestination().toJSON());
    return result;
  }

}
