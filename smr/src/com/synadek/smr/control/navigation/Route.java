/**
 * Route.java
 * 22 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import java.util.List;

import org.json.simple.JSONObject;

/**
 * The route is a list of segments. Each segment defines an action to take along the route.
 */
public interface Route {

  /**
   * Get a specific segment of the route myRoute.
   * 
   * @param idx
   *          index of the segment
   * @return the segment object
   */
  RouteSegment getSegment(final int idx);

  /**
   * Add a segment to the end of a Route.
   * 
   * @param segment
   *          the new segment to add
   */
  void addSegment(final AbstractRouteSegmentImpl segment);

  /**
   * Get the list of route segments that make up the route myRoute.
   * 
   * @return the list of Route Segments
   */
  List<RouteSegment> getRoute();

  /**
   * Get the route myRoute as a JSON object.
   * 
   * @return the JSON Object representing this Route
   */
  JSONObject toJSON();
}
