/**
 * MissionExecution.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.mission;

import com.synadek.core.GpsCoordinates;
import com.synadek.smr.vessel.Vessel;

/**
 * Mission Status.
 */
public class MissionExecution {

  private final Vessel myShip;
  private final Contract myContract;
  private final MissionPlan myMission;

  // Mission parameters
  // private float distanceTraveled;
  private long missionStartTime;
  private GpsCoordinates missionStartLocation;

  /**
   * Current MissionPlan step. With this information, can get previous waypoint
   * and next waypoint and calculate, for example distance from previous
   * waypoint, distance/time to next waypoint
   *
   * @param vessel
   *          the vessel to operate
   * @param contract
   *          the contract to fulfill
   * @param mission
   *          the mission
   */
  public MissionExecution(final Vessel vessel, final Contract contract,
      final MissionPlan mission) {
    myShip = vessel;
    myContract = contract;
    myMission = mission;
  }

  /**
   * Start the mission.
   */
  public void startMission() {
    missionStartTime = System.currentTimeMillis();
    missionStartLocation = myMission.getStartLocation();
    // TODO define "inRange(50 meters) of a location rather than location !=
    // location
    if (myShip.getLocation() != missionStartLocation) {
      // If ship not at start location, push a step onto the mission myRoute to
      // go to the start
      // location
    }
  }

  public long getMissionStartTime() {
    return missionStartTime;
  }

  public GpsCoordinates getMissionStartLocation() {
    return missionStartLocation;
  }

  public long getMissionElapsedTime() {
    return System.currentTimeMillis() - missionStartTime;
  }

  /**
   * End the mission.
   */
  public void closeMission() {

    // Secure the boat
    myShip.secureVessel();

    // De-provision
    myShip.offloadVessel();

    // Launch routine maintenance and repair tasks

    // Complete contract
    myContract.completeContract();

    // Await new contract
  }
}
