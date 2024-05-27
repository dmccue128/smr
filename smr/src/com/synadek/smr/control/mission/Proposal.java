/**
 * Proposal.java
 * 1 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.control.mission;

/**
 * A contract response, completing the contract terms and conditions with the offer parameters.
 */
public class Proposal {

  /**
   * MissionPlan to achieve the mission parameters specified in the RFP.
   */
  private final MissionPlan myPlan;

  /**
   * Generate a bid in response to an RFP.
   * 
   * @param rfp
   *          the contract request for proposals.
   */
  public Proposal(final RequestForProposals rfp) {
    // Construct a MissionPlan that achieves the objectives of the contract
    myPlan = generateMissionPlan();
    // Assign a vessel from the fleet

    // TODO generate complete proposal based on RFP
  }

  public MissionPlan getMissionPlan() {
    return myPlan;
  }

  /**
   * Contract awarded, proposal accepted.
   */
  public MissionExecution acceptProposal() {
    // TODO process an accepted proposal, create the mission execution object
    return null;
  }

  /**
   * Contract denied, proposal rejected.
   */
  public void rejectProposal() {
    // Acknowledge the rejection
    // Archive the proposal
  }

  private MissionPlan generateMissionPlan() {
    return new MissionPlan("new name");
  }

}
