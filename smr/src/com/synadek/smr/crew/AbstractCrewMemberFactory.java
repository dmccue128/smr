/**
 * AbstractCrewMemberFactory.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.crew;

import com.synadek.smr.control.mission.BlackBox;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Abstract class implementing general crew member functions.
 */
public abstract class AbstractCrewMemberFactory implements CrewMember, Runnable {

  /**
   * All available roles for crew members.
   */
  public static final List<String> crewRoles = new LinkedList<>();

  static {
    crewRoles.add(ROLE_CAPTAIN);
    crewRoles.add(ROLE_FIRST_MATE);
    crewRoles.add(ROLE_NAVIGATOR);
    crewRoles.add(ROLE_ENGINEER);
    crewRoles.add(ROLE_RESEARCHER);
    crewRoles.add(ROLE_COMMUNICATIONS_OFFICER);
    crewRoles.add(ROLE_MATE);
  }

  /**
   * Maintain an inbound message queue for each crew member.
   */
  private static final Map<CrewMember, BlockingQueue<CrewMessage>> crewQueues = new HashMap<>();

  /**
   * Maximum number of milliseconds to wait for a Crew Member thread to
   * terminate.
   */
  private static final long MAX_THREAD_TERMINATION_WAIT = 3000;

  /**
   * Thread implementing the functions of this crew member.
   */
  private Thread myThread;

  /**
   * Name of this crew member.
   */
  private final String myName;

  /**
   * Role for this crew member.
   */
  private final String myRole;

  /**
   * Signal a thread to terminate for a specific crew member.
   */
  protected boolean isTerminateRequested;

  /**
   * Acquire a reference to the application logger.
   */
  protected static Logger log = LogManager
      .getLogger(AbstractCrewMemberFactory.class.getPackage().getName());

  /**
   * Default constructor.
   *
   * @param crewName
   *          the name of the crew member
   * @param rolename
   *          is the role name for this crew member.
   */
  AbstractCrewMemberFactory(final String crewName, final String rolename) {
    // Create a message queue for the crew member
    crewQueues.put(this, new LinkedBlockingQueue<CrewMessage>());
    // Remember my name
    myName = crewName;
    // Remember my role
    myRole = rolename;
    // Clear the termination flag
    isTerminateRequested = false;
  }

  /**
   * Start the thread that animates this crew role.
   */
  public void goOnDuty() {

    // Clear the termination flag
    isTerminateRequested = false;

    // Create a thread to implement this role.
    myThread = new Thread(this);

    // Start the thread.
    myThread.start();
  }

  /**
   * Stop the thread that animates this crew role.
   */
  public void goOffDuty() {

    // Signal request to terminate
    isTerminateRequested = true;

    // No further action if thread is already gone
    if (myThread == null) {
      return;
    }

    // Interrupt the thread and await termination
    try {
      myThread.interrupt();
      myThread.join(MAX_THREAD_TERMINATION_WAIT);
    } catch (InterruptedException err) {
      // If this thread interrupted during join, report the error
      err.printStackTrace();
    }
    myThread = null;
    log.debug("Crew member " + this.getCrewName() + " is off duty");
  }

  /**
   * Crew Members implement their own processing for each message they receive.
   *
   * @param msg
   *          the message to be processed
   */
  protected abstract void processMessage(final CrewMessage msg);

  /**
   * Return a list of crew members.
   *
   * @return the list
   */
  public Set<CrewMember> crewList() {
    return crewQueues.keySet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.crew.CrewMember#getCrewName()
   */
  @Override
  public final String getCrewName() {
    return myName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.crew.CrewMember#getCrewRole()
   */
  @Override
  public final String getCrewRole() {
    return myRole;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public final void run() {
    // For debugging convenience, set the name of the thread to the name of the
    // crew member.
    Thread.currentThread().setName(myName);

    // Initialize
    log.debug("Crew member " + this.getCrewName() + " is on duty.");

    // Operate
    while (!this.isTerminateRequested) {
      try {
        final CrewMessage msg = listen(0);

        // Handle special case of null message (should never occur).
        if (msg == null) {
          log.error("processMessage invoked with null message");
          break;
        }

        // Get the contents of the message
        final JSONObject contents = msg.getMessage();

        // Confirm that message is not null
        if (contents == null) {
          log.error("processMessage invoked with a message that contains no contents.");
          break;
        }

        // Confirm that the message contents are well-formed
        if (!contents.containsKey("cmd")) {
          log.error("processMessage invoked with a message that contains no command: "
              + contents.toJSONString());
          break;
        }

        // Message is valid, pass it to the crew member to process
        processMessage(msg);

      } catch (InterruptedException err) {
        log.warn("Crew member " + this.getCrewName() + " interrupted while awaiting a message.");
        try {
          throw err;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    // Shutdown

    // Remove the message queue for this Crew Member
    crewQueues.remove(this);

    // Terminate the thread for this Crew Member
    goOffDuty();
  }

  /**
   * Tell a message to a specific crew member.
   *
   * @param member
   *          Crew Member to whom the message is addressed
   * @param msg
   *          the message
   */
  protected final void tell(final CrewMember member, final JSONObject msg) {
    // Get the inbound message queue for the Crew Member
    final BlockingQueue<CrewMessage> queue = crewQueues.get(member);
    // Formulate the message
    final CrewMessage cm = new CrewMessage(this, member, msg);
    // Queue the message
    queue.add(cm);
    // Record the message in the black box
    BlackBox.addMessage(cm.toString());
  }

  /**
   * Tell a message to a set of crew members.
   *
   * @param members
   *          the set of members to receive the message
   * @param msg
   *          the message
   */
  protected final void tell(final Set<CrewMember> members, final JSONObject msg) {
    // Sequentially send to each crew member
    for (CrewMember member : members) {
      tell(member, msg);
    }
  }

  /**
   * Tell a message to all crew members.
   *
   * @param msg
   *          the message
   */
  protected final void announce(final JSONObject msg) {
    tell(crewList(), msg);
  }

  /**
   * Listen for a message.
   *
   * @param timeoutMillis
   *          maximum time to wait (milliseconds). If timeoutMillis is zero,
   *          listen until a message arrives.
   * @return the message
   * @throws InterruptedException
   *           if interrupted or no response after a short time.
   */
  protected final CrewMessage listen(final long timeoutMillis) throws InterruptedException {
    final BlockingQueue<CrewMessage> myQueue = crewQueues.get(this);
    if (timeoutMillis > 0) {
      return myQueue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    }
    return myQueue.take();
  }
}
