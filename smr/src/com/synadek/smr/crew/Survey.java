/**
 * Survey.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 * A request that goes to one or more crew members seeking a yes/no answer.
 */
public class Survey extends JSONObject {

  /**
   * Default serial version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Keep track of the responses of each crew member to whom the survey was
   * addressed.
   */
  private final Map<CrewMember, Boolean> responses;

  /**
   * Keep track of the crew member seeking the answer to the survey question.
   */
  private final CrewMember requestor;

  /**
   * Record the question that is asked in the survey.
   */
  private final JSONObject question;

  /**
   * Default constructor.
   *
   * @param toList
   *          the set of Crew Members to whom the survey is addressed
   * @param requestingParty
   *          the Crew Member seeking the answer to the question
   * @param surveyQuestion
   *          the question
   */
  public Survey(final Set<CrewMember> toList, final CrewMember requestingParty,
      final JSONObject surveyQuestion) {
    responses = new HashMap<>();
    for (CrewMember member : toList) {
      responses.put(member, null);
    }
    requestor = requestingParty;
    question = surveyQuestion;
  }

  /**
   * Get the Crew Member seeking the answer to the question.
   *
   * @return the Crew Member
   */
  public CrewMember getRequestor() {
    return requestor;
  }

  /**
   * Get the set of Crew Members to whom the survey is addressed.
   *
   * @return the Crew Members
   */
  public Set<CrewMember> getRecipients() {
    return responses.keySet();
  }

  /**
   * Get the question that is asked in this survey.
   *
   * @return the question
   */
  public JSONObject getQuestion() {
    return question;
  }

  /**
   * Respond to the survey question.
   *
   * @param me
   *          the Crew Member who is responding
   * @param yes
   *          true if this Crew Member's answer to the question is affirmative.
   */
  public synchronized void respond(final CrewMember me, final boolean yes) {
    responses.put(me, Boolean.valueOf(yes));
  }

  /**
   * Get the total number of Crew Members queried in this survey.
   *
   * @return the number
   */
  public synchronized int totalCount() {
    return responses.size();
  }

  /**
   * Get the number of affirmative responses that have been received.
   *
   * @return the number of 'yes' responses
   */
  public synchronized int yesCount() {
    int count = 0;
    for (Boolean response : responses.values()) {
      if (response != null && response == Boolean.TRUE) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Get the number of negative responses that have been received.
   *
   * @return the number of 'no' responses
   */
  public synchronized int noCount() {
    int count = 0;
    for (Boolean response : responses.values()) {
      if (response != null && response == Boolean.FALSE) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Get the response (if any) provided by a specific Crew Member.
   *
   * @param member
   *          the Crew Member
   * @return the response or null if no response has been received yet.
   */
  public synchronized Boolean getResponse(final CrewMember member) {
    return responses.get(member);
  }
}
