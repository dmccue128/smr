/**
 * Navigation.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.navigation;

/**
 * Manage navigation of the vessel.
 */
public interface Navigation {

  /**
   * Routing actions determine the action to be taken relative to a waypoint.
   */
  public enum RoutingAction {
    /**
     * Hold at this position.
     */
    holdAt,
    /**
     * Navigate to this position.
     */
    proceedTo
  }

  /**
   * Get the route.
   *
   * @return the route.
   */
  Route getRoute();

  /**
   * Get the speed over ground in km/h.
   *
   * @return Speed Over Ground in km/h. i.e., Vessel rate of progress in the
   *         direction of travel.
   */
  float getSog();

  /**
   * Get the velocity made good in km/h.
   *
   * @return Velocity Made Good in km/h, i.e., Vessel rate of progress towards
   *         the next defined waypoint.
   */
  float getVmg();

  /**
   * Get the estimated distance to destination.
   *
   * @return Estimated Distance to Destination in kilometers following the
   *         planned route.
   */
  float getDtD();

  /**
   * Get the estimated distance to the next waypoint.
   *
   * @return Estimated Distance to next Waypoint in kilometers.
   */
  float getDtW();
}
