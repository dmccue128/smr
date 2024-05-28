/**
 * RouteImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A Route is a list of steps from departure to destination.
 */
public class RouteImpl implements Route {

  /**
   * The (ordered) list of route segments that make up the myRoute.
   */
  final List<RouteSegment> myRoute;

  /**
   * Default constructor.
   */
  public RouteImpl() {
    myRoute = new LinkedList<>();
  }

  /**
   * JSON parsing constructor.
   *
   * @param jsonRoute
   *          the route represented in a JSON Object
   */
  public RouteImpl(final JSONObject jsonRoute) {

    // Initialize the route to an empty list of segments
    myRoute = new LinkedList<>();

    // Get the segments from the JSON object, jRoute
    final JSONArray steps = (JSONArray) jsonRoute.get("steps");

    // Handle the case of an empty route
    if (steps == null || steps.isEmpty()) {
      return;
    }

    // Parse each segment according to its type and add it to the myRoute
    for (int i = 0; i < steps.size(); i++) {

      RouteSegment segment;
      final JSONObject step = (JSONObject) steps.get(i);
      final String type = (String) step.get("type");
      final JSONObject contents = (JSONObject) step.get("contents");

      switch (type) {
        case "grid":
          segment = new GridSegment(contents);
          break;
        case "hold":
          segment = new HoldSegment(contents);
          break;
        case "move":
          segment = new MoveSegment(contents);
          break;
        case "spiral":
          segment = new SpiralSegment(contents);
          break;
        default:
          segment = null;
          break;
      }
      // Add this segment to the route
      myRoute.add(segment);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.Route#getSegment(int)
   */
  @Override
  public RouteSegment getSegment(int idx) {
    return myRoute.get(idx);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.synadek.smr.control.navigation.Route#addSegment(com.synadek.smr.control
   * .navigation. AbstractRouteSegmentImpl)
   */
  @Override
  public void addSegment(final AbstractRouteSegmentImpl segment) {
    myRoute.add(segment);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.Route#getRoute()
   */
  @Override
  public List<RouteSegment> getRoute() {
    return myRoute;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.smr.control.navigation.Route#toJSON()
   */
  @SuppressWarnings("unchecked")
  @Override
  public JSONObject toJson() {
    final JSONObject result = new JSONObject();
    final JSONArray steps = new JSONArray();

    for (RouteSegment segment : myRoute) {
      steps.add(segment.toJson());
    }

    result.put("steps", steps);

    return result;
  }

}
