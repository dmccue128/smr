/**
 * DigitalInputEventDispatcher.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dispatch digital input events to registered handlers.
 */
public class DigitalInputEventDispatcher implements Runnable {
  /**
   * Acquire a reference to the application logger.
   */
  protected final Logger log =
      LogManager.getLogger(this.getClass().getPackage().getName());

  /**
   * Queue of analog input events to be signaled to registered event handlers.
   */
  private final LinkedBlockingQueue<DigitalInputEvent> aQueue;

  /**
   * VesselPhysicalModel implementation.
   */
  private final VesselPhysicalModel myPio;

  /**
   * Flag to terminate execution of the dispatch thread.
   */
  private boolean done;

  /**
   * Execution thread.
   */
  private Thread myThread;

  /**
   * Default constructor.
   * 
   * @param mdl
   *          PioMode implementation
   * @param myQueue
   *          event queue
   */
  public DigitalInputEventDispatcher(final VesselPhysicalModel mdl,
      final LinkedBlockingQueue<DigitalInputEvent> myQueue) {
    aQueue = myQueue;
    myPio = mdl;
    done = false;
    new Thread(this).start();
  }

  /**
   * Stop the event dispatcher.
   */
  public void stop() {
    done = true;
    try {
      myThread.interrupt();
    } catch (NullPointerException err) {
      log.warn("Analog event dispatch thread is null");
    }
  }

  /**
   * Run the event dispatcher.
   */
  @Override
  public void run() {

    myThread = Thread.currentThread();

    log.info("Digital Input Event Dispatcher starting");

    while (!done) {
      try {
        DigitalInputEvent evt = aQueue.take();
        if (evt != null) {
          myPio.invokeDigitalInputHandlers(evt);
        }
      } catch (InterruptedException ie) {
        if (!done) {
          log.debug("Thread interrupted");
        }
      }
    }

    myThread = null;

    log.info("Digital Input Event Dispatcher exiting");
  }

}
