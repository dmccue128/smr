/**
 * AnalogEventDispatcher.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel.physical;

import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Analog event dispatcher.
 */
public class AnalogEventDispatcher implements Runnable {
  /**
   * Acquire a reference to the application logger.
   */
  protected final Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

  /**
   * Queue of analog input events to be signaled to registered event handlers.
   */
  private final LinkedBlockingQueue<AnalogEvent> analogEventQueue;

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
  public AnalogEventDispatcher(final VesselPhysicalModel mdl,
      final LinkedBlockingQueue<AnalogEvent> myQueue) {
    analogEventQueue = myQueue;
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

    log.info("Analog Input Event Dispatcher starting");

    while (!done) {
      try {
        AnalogEvent ae = analogEventQueue.take();
        if (ae != null) {
          myPio.invokeAnalogHandlers(ae);
        }
      } catch (InterruptedException ie) {
        if (!done) {
          log.debug("Thread interrupted");
        }
      }
    }

    myThread = null;

    log.info("Analog Input Event Dispatcher exiting");
  }

}
