/**
 * smrControl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control;

import com.synadek.smr.dsl.SchemaManager;
import com.synadek.smr.dsl.SystemConfiguration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry for the smr vessel application.
 */
public final class SmrControl implements Runnable, ServletContextListener {

  /**
   * initialization timeout indicates the maximum amount of time (milliseconds)
   * to wait to initialize.
   */
  private static final long INITIALIZATION_TIMEOUT = 60000L;

  /**
   * Shutdown flag key.
   */
  private static final String NORMALSHUTDOWN_KEY = "control.shutdownflag";

  /**
   * Acquire a reference to the application logger.
   */
  private static Logger log = LogManager.getLogger(SmrControl.class.getPackage().getName());

  /**
   * initThread terminates as soon as the application is initialized.
   */
  private Thread initThread;

  /**
   * Default constructor.
   */
  public SmrControl() {

  }

  /**
   * contextDestroyed is an event listener that detects that the application
   * container is about to be destroyed and affords the application a chance to
   * cleanup up threads, etc. Receives notification that the ServletContext is
   * about to be shut down. All servlets and filters will have been destroyed
   * before this ServletContextListener is notified of context destruction.
   *
   * @param arg0
   *          - the ServletContextEvent containing the ServletContext that is
   *          being destroyed
   */
  @Override
  public void contextDestroyed(final ServletContextEvent arg0) {
    // To facilitate debugging, attempt to rename the thread
    try {
      Thread.currentThread().setName("ApplicationStop");
    } finally {
      // Regardless of whether or not the rename worked, shutdown
      applicationShutdown();

      // Set a flag in the configuration db indicating normal application
      // shutdown
      setShutdownFlag();

      log.info("++++++++ application exiting ++++++++");
    }
  }

  /**
   * contextInitialized is invoked when the web application container is
   * deployed. Receives notification that the web application initialization
   * process is starting. Notified of context initialization before any filters
   * or servlets in the web application are initialized.
   *
   * @param arg0
   *          - the ServletContextEvent containing the ServletContext that is
   *          being initialized
   */
  @Override
  public void contextInitialized(final ServletContextEvent arg0) {

    final long startTime = System.currentTimeMillis();

    final ServletContext ctx = arg0.getServletContext();

    // remember the Web application root path
    if (ctx != null) {
      SchemaManager.setWebappRoot(ctx.getRealPath("/"));
    }

    // To facilitate debugging, attempt to rename the thread
    try {

      // Change the thread name to assist in debugging
      Thread.currentThread().setName("ApplicationStart");

      // Create a thread to initialize the application
      this.initThread = new Thread(this);

      // Start the initialization thread
      initThread.start();

      // Await completion of the thread or time out if initialization is
      // taking too long
      initThread.join(INITIALIZATION_TIMEOUT);

      // Calculate total initialization time
      final long durationMillis = System.currentTimeMillis() - startTime;
      final double millisPerSec = TimeUnit.SECONDS.toMillis(1);

      // Convert to double precision seconds
      final double durationSecs = durationMillis / millisPerSec;

      // Log the message
      log.info(String.format("Application initialized in %1$,.1f seconds",
          Double.valueOf(durationSecs)));

    } catch (InterruptedException e) {
      log.error("Failure to initialize application ", e);
    } catch (SecurityException s) {
      log.error("Failure to initialize application ", s);
    }
  }

  /**
   * A last step in an orderly shutdown of the application is to set the
   * ShutdownFlag in the configuration database. On application start-up, the
   * flag can be tested to determine whether the previous shutdown was orderly
   * or abrupt.
   */
  private final void setShutdownFlag() {
    SystemConfiguration.setElement(NORMALSHUTDOWN_KEY, true);
  }

  /**
   * On application startup, the shutdown flag can be queried (and cleared). A
   * 'normal' shutdown simply means that all shutdown activities programmed in
   * this emitter application were executed before the system shutdown.
   *
   * @return true if previous shutdown was normal
   */
  private final boolean checkAndClearShutdownFlag() {

    boolean wasShutdownNormal;

    try {
      wasShutdownNormal = SystemConfiguration.getBooleanElement(NORMALSHUTDOWN_KEY);
    } catch (NoSuchElementException e) {
      // First time feature is used, give a pass and assume shutdown was OK
      wasShutdownNormal = true;
    }

    if (!wasShutdownNormal) {
      log.warn("Previous shutdown was abnormal.  Some date may be corrupted.");
    }

    // Clear the shutdown flag in the configuration database
    // On normal shutdown, this value will be set to true
    SystemConfiguration.setElement(NORMALSHUTDOWN_KEY, false);

    // Return true if the previous system shutdown was normal
    return wasShutdownNormal;
  }

  @Override
  public void run() {

    // To facilitate debugging, attempt to rename the thread
    try {
      Thread.currentThread().setName("ApplicationStart");
    } catch (SecurityException s) {
      log.warn("Security exception raised when attempting to set thread name");
    }

    // If previous shutdown was not normal, log an informational message.
    final boolean normalShutdown = checkAndClearShutdownFlag();
    if (!normalShutdown) {
      // Should probably log a device event so it gets posted to web site,
      // but for now just log locally
      log.warn("System integrity check failed, suggesting previous device shutdown was abrupt. "
          + "Some shutdown activities did not complete; data may be corrupted.");
    } else {
      log.debug("System initialization integrity check successful.");
    }

    // initialize system
    applicationStartup();

    // No further need for this initialization task, let it exit normally
  }

  /**
   * Initialization method invoked at application start by the application
   * server.
   */
  private final void applicationStartup() {
    log.info("Application starting...");
    // final String serialNumber =
    // SystemConfiguration.getStringElement(VESSEL_SERIAL_NUMBER_KEY);
  }

  /**
   * Finalization method invoked at application termination by the application
   * server.
   */
  private final void applicationShutdown() {
    log.info("Application shutting down...");
  }

}
