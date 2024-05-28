/**
 * MissionPlan.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.mission;

import com.synadek.core.GpsCoordinates;
import com.synadek.smr.control.navigation.Route;
import com.synadek.smr.crew.CrewMember;
import java.util.Calendar;
import java.util.Set;

/**
 * Describe mission parameters.
 */
public class MissionPlan {

  /**
   * Potential states of a mission.
   */
  public enum MissionStatus {
    /**
     * Defined - MissionPlan is defined, resources not yet assigned.
     */
    Defined,
    /**
     * Accepted - MissionPlan is defined and resources are assigned.
     */
    Accepted,
    /**
     * Active - MissionPlan is underway.
     */
    Active,
    /**
     * Completed - MissionPlan is completed successfully.
     */
    Completed,
    /**
     * Aborted - MissionPlan is completed abnormally.
     */
    Aborted
  }

  /**
   * Name for this mission.
   */
  private String name;
  /**
   * Unique ID for the mission.
   */
  private final String id;
  /**
   * Description of the mission objectives.
   */
  private String description;
  /**
   * MissionPlan status.
   */
  private MissionStatus status;
  /**
   * Date/time when the mission is expected to start.
   */
  private long plannedStart;
  /**
   * Date/time when the mission actually started or zero if the mission has not
   * started yet.
   */
  private long actualStart;
  /**
   * Date/time when the mission is expected to end.
   */
  private long plannedFinish;
  /**
   * Date/time when the mission actually ended or zero if the mission has not
   * ended yet.
   */
  private long actualFinish;
  /**
   * Location of departure.
   */
  private GpsCoordinates startLocation;
  /**
   * Location of destination.
   */
  private GpsCoordinates finishLocation;
  /**
   * Crew members required for the mission.
   */
  private Set<CrewMember> crewList;
  /**
   * Route of travel.
   */
  private Route route;

  /**
   * Default constructor.
   *
   * @param missionName
   *          the name of this mission
   */
  public MissionPlan(final String missionName) {
    setName(missionName);
    final Calendar now = Calendar.getInstance();
    final Integer year = Integer.valueOf(now.get(Calendar.YEAR));
    final Integer month = Integer.valueOf(now.get(Calendar.MONTH) + 1);
    final Integer day = Integer.valueOf(now.get(Calendar.DAY_OF_MONTH));
    final Integer hour = Integer.valueOf(now.get(Calendar.HOUR_OF_DAY));
    final Integer minute = Integer.valueOf(now.get(Calendar.MINUTE));
    final Integer second = Integer.valueOf(now.get(Calendar.SECOND));

    id = String.format("%d%d%d-%02d%02d%02d-%s", year, month, day, hour, minute, second,
        missionName);
    status = MissionStatus.Defined;
  }

  /**
   * Get the mission plan ID.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Get status.
   *
   * @return the status
   */
  public MissionStatus getStatus() {
    return status;
  }

  /**
   * Set status.
   *
   * @param status
   *          the status to set
   */
  public void setStatus(MissionStatus status) {
    this.status = status;
  }

  /**
   * Get mission start time.
   *
   * @return the actualStart
   */
  public long getActualStart() {
    return actualStart;
  }

  /**
   * Set mission start time.
   *
   * @param actualStart
   *          the actualStart to set
   */
  public void setActualStart(long actualStart) {
    this.actualStart = actualStart;
  }

  /**
   * Get mission finish time.
   *
   * @return the actualFinish
   */
  public long getActualFinish() {
    return actualFinish;
  }

  /**
   * Set mission finish time.
   *
   * @param actualFinish
   *          the actualFinish to set
   */
  public void setActualFinish(long actualFinish) {
    this.actualFinish = actualFinish;
  }

  /**
   * Get mission route.
   *
   * @return the route
   */
  public Route getRoute() {
    return route;
  }

  /**
   * Set mission route.
   *
   * @param route
   *          the route to set
   */
  public void setRoute(Route route) {
    this.route = route;
  }

  /**
   * Get mission description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set/update the description of the mission.
   *
   * @param desc
   *          the description
   */
  public void setDescription(final String desc) {
    description = desc;
  }

  /**
   * Get planned start time.
   *
   * @return the plannedStart
   */
  public long getPlannedStart() {
    return plannedStart;
  }

  /**
   * Set planned start time. Set/update planned start date/time.
   *
   * @param start
   *          the planned start date/time
   */
  public void setPlannedStart(final long start) {
    plannedStart = start;
  }

  /**
   * Get planned finish time.
   *
   * @return the plannedFinish
   */
  public long getPlannedFinish() {
    return plannedFinish;
  }

  /**
   * Set/update the planned finish date/time.
   *
   * @param finish
   *          the planned finish date/time.
   */
  public void setPlannedFinish(final long finish) {
    plannedFinish = finish;
  }

  /**
   * Get the starting location (departure).
   *
   * @return the location
   */
  public GpsCoordinates getStartLocation() {
    return startLocation;
  }

  /**
   * Set/update the starting location (departure).
   *
   * @param loc
   *          the staring location
   */
  public void setStartLocation(final GpsCoordinates loc) {
    startLocation = loc;
  }

  /**
   * Get the finish location (destination).
   *
   * @return the location
   */
  public GpsCoordinates getFinishLocation() {
    return finishLocation;
  }

  /**
   * Set/update the finish location (destination).
   *
   * @param loc
   *          the finish location
   */
  public void setFinishLocation(final GpsCoordinates loc) {
    finishLocation = loc;
  }

  /**
   * Get the crew list.
   *
   * @return the crewList
   */
  public Set<CrewMember> getCrewList() {
    return crewList;
  }

  /**
   * Set/update the crew list.
   *
   * @param crew
   *          the list of crew members
   */
  public void setCrewList(final Set<CrewMember> crew) {
    crewList = crew;
  }

  /**
   * Get the name of the mission.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the mission.
   *
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
