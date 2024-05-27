/**
 * Engineer.java
 * 19 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

import java.util.Set;

import org.json.simple.JSONObject;

/**
 * Engineer -- Responsible for all operations and maintenance that have to do with all machinery and
 * equipment throughout the ship.
 */
public class Engineer extends AbstractCrewMemberFactory {

  /**
   * Default constructor.
   * 
   * @param name
   *          the name of this Engineer
   */
  public Engineer(final String name) {
    super(name, ROLE_ENGINEER);
  }

  /**
   * Process a message to this role.
   * 
   * @param msg
   *          the message
   */
  @Override
  @SuppressWarnings("unchecked")
  protected final void processMessage(final CrewMessage msg) {

    // Get the contents of the message
    final JSONObject contents = msg.getMessage();
    final String command = (String) contents.get("cmd");

    switch (command) {

      // Poll the crew for the answer to a yes/no question
      case "poll":
        final JSONObject question = (JSONObject) contents.get("question");
        final Set<CrewMember> whoToSurvey;

        // If a specific list of crew members was identified, use it
        // Otherwise, send the request to the entire crew
        if (contents.containsKey("toList")) {
          whoToSurvey = (Set<CrewMember>) contents.get("toList");
        } else {
          whoToSurvey = crewList();
        }

        // Create a survey object with the question and send it
        announce(new Survey(whoToSurvey, this, question));

        break;

      case "survey":

        // Process the survey question and respond by updating the survey.
        final Survey survey = (Survey) contents.get("survey");

        processSurveyQuestion(survey);

        break;

      case "cast off":
        // Poll all crew members for readiness to depart
        // When all crew are ready, report back to requestor
        break;

      default:
        log.warn("Unknown message type, " + contents.get("cmd") + " ignored.");
    }

  }

  /**
   * Process and respond to a survey (yes/no) question from another Crew Member.
   * 
   * @param survey
   *          the survey
   */
  private void processSurveyQuestion(final Survey survey) {
    final JSONObject question = survey.getQuestion();
    switch ((String) question.get("question")) {
      case "Active?":
        survey.respond(this, true);
        break;
      default:
        log.error(this.getCrewRole() + " " + this.getCrewName()
            + " does not understand the question, " + question.toJSONString());
    }
  }
}
