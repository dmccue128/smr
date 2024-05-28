/**
 * CrewMember.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

/**
 * CrewMembers are the processes (threads) controlling the operations of the
 * vessel.
 */
public interface CrewMember {

  /**
   * Captain is the overall commander of the vessel responsible for coordinating
   * the actions of other crew members and overseeing the accomplishment of the
   * mission.
   */
  public static final String ROLE_CAPTAIN = "Master";

  /**
   * First Mate is responsible for the safety and security of the ship, as well
   * as the welfare of the crew on board.
   */
  public static final String ROLE_FIRST_MATE = "First Mate";

  /**
   * Navigator is responsible for all aspects of ship navigation including
   * long-range route planning, short-range route planning, charts, maps, etc.
   * The Navigator computes and shares the current position of the vessel at all
   * times.
   */
  public static final String ROLE_NAVIGATOR = "Navigator";

  /**
   * Engineer is responsible for all operations and maintenance that have to do
   * with machinery and equipment throughout the ship. This includes any
   * equipment related to propulsion or steering (motor, propeller, sails), any
   * equipment related to seaworthiness (bilge pump, ballast tanks), and any
   * equipment related to docking (anchor, lines).
   */
  public static final String ROLE_ENGINEER = "Engineer";

  /**
   * Researcher is a general class of research fellow onboard. Researcher may be
   * specialized to a specific type of research. Researchers are responsible for
   * data gathering and analysis including operation of any specific equipment
   * used for research or data gathering.
   */
  public static final String ROLE_RESEARCHER = "Researcher";

  /**
   * Communications officer is responsible for all inbound and outbound
   * communications including ship-to-ship, ship-to-shore, and ship-to-aircraft.
   * Communications officer abstracts the mechanism(s) used to communicate which
   * may include radio, visual (light or flags), audio/echo, or any other means.
   */
  public static final String ROLE_COMMUNICATIONS_OFFICER = "Communications Officer";

  /**
   * Mate is a general assistant who may perform functions in support of any
   * other role.
   */
  public static final String ROLE_MATE = "Mate";

  /**
   * Return name identifying a specific instance of a crew member e.g., a Thread
   * ID.
   *
   * @return the name
   */
  String getCrewName();

  /**
   * Return the role for a crew member. Crew members do not hold multiple roles.
   *
   * @return the role
   */
  String getCrewRole();

}
