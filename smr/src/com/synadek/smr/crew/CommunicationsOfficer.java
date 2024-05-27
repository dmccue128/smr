/**
 * CommunicationsOfficer.java
 * 18 Mar 2017
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

import org.json.simple.JSONObject;

/**
 * Responsible for all off-ship communications (ship-to-shore, ship-to-ship).
 */
public class CommunicationsOfficer extends AbstractCrewMemberFactory {

  /**
   * Default constructor.
   * 
   * @param name
   *          the name of this Communications Officer
   */
  public CommunicationsOfficer(final String name) {
    super(name, ROLE_COMMUNICATIONS_OFFICER);
  }

  /**
   * Process a message to this role.
   * 
   * @param msg
   *          the message
   */
  @Override
  protected final void processMessage(final CrewMessage msg) {

    // Get the contents of the message
    final JSONObject contents = msg.getMessage();
    final String command = (String) contents.get("cmd");

    switch (command) {

      case "survey":

        // Process the survey question and respond by updating the survey.
        final Survey survey = (Survey) contents.get("survey");

        processSurveyQuestion(survey);

        break;

      case "send message":
        final String dest = (String) contents.get("dest");
        final String destType = (String) contents.get("destType");
        if (destType.equals("ship")) {
          // Intership communications
          log.debug("Sending ship-to-ship communications to " + dest + ": "
              + contents.toJSONString());
        } else {
          // Ship to shore communications
          log.debug("Sending ship-to-shore communications to " + dest + ": "
              + contents.toJSONString());
        }
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
