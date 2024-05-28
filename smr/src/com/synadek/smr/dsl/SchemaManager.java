/**
 * SchemaManager.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.dsl;

import com.synadek.smr.database.Database;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SchemaManager offers the service of upgrading or downgrading the database
 * schema as required (including data migration where appropriate).
 */
public class SchemaManager {
  /**
   * Version of database schema used by this software release.
   */
  public static final int REQUIRED_SCHEMA_VERSION = 1;

  /**
   * VERSION_1 is a defined schema version.
   */
  private static final int VERSION_1 = 1;
  /**
   * VERSION_2 is a defined schema version.
   */
  private static final int VERSION_2 = 2;

  /**
   * Base Schema Version is the lowest DB version to which we can downgrade and
   * the lowest version from which we can upgrade.
   */
  private static final int BASE_SCHEMA_VERSION = VERSION_1;

  /**
   * Save the file system path to the web application directory root. In case we
   * need to access local files in the webapp tree.
   */
  private static String webappRoot;

  /**
   * Acquire a reference to the application logger.
   */
  private static Logger log = LogManager.getLogger(SchemaManager.class.getPackage().getName());

  /**
   * Default constructor.
   */
  public SchemaManager() {

  }

  /**
   * Retrieve the current schema version of the local database.
   *
   * @return the schema version
   */
  private int getSchemaVersion() {
    return SystemConfiguration.getIntElement("database.schemaversion");
  }

  /**
   * upgradeDowngrade checks the version of the database schema against the
   * required version for this software release and modifies the database to
   * match the required version.
   *
   * @return true if successful
   */
  public final boolean upgradeDowngrade() {

    final int schemaVersion = getSchemaVersion();

    final String preface = "Schema version: Required=" + REQUIRED_SCHEMA_VERSION + ", Discovered="
        + schemaVersion;

    if (schemaVersion == REQUIRED_SCHEMA_VERSION) {
      log.info(preface + " ==> match");
      return true;
    }

    if (schemaVersion < REQUIRED_SCHEMA_VERSION) {

      log.info(preface + " ==> upgrade required");

      // Update table structure
      final boolean result = upgradeSchema(schemaVersion, REQUIRED_SCHEMA_VERSION);

      // Ensure latest stored procedure definitions are loaded
      Database.defineSprocs(webappRoot, REQUIRED_SCHEMA_VERSION);

      return result;
    }

    // Assert: schemaVersion > REQUIRED_SCHEMA_VERSION
    log.info(preface + " ==> downgrade required");
    return downgradeSchema(schemaVersion, REQUIRED_SCHEMA_VERSION);
  }

  /**
   * upgradeSchema advances the schema one version.
   *
   * @param fromVersion
   *          is the current version of the DB on this emitter
   * @param toVersion
   *          is the desired version of DB schema for this emitter
   * @return true if successful
   */
  private boolean upgradeSchema(final int fromVersion, final int toVersion) {

    // If we're there, return success
    if (fromVersion == toVersion) {
      return true;
    }

    // Cannot upgrade prior to BASE SCHEMA VERSION
    if (fromVersion < BASE_SCHEMA_VERSION) {
      log.error(
          "Unable to update schema from version " + fromVersion + " to version " + toVersion);
      return false;
    }

    // Cannot upgrade to a newer release than supported by this software
    // release.
    if (toVersion > REQUIRED_SCHEMA_VERSION) {
      log.error("SW not capable to update schema from version " + fromVersion + " to version "
          + toVersion);
      return false;
    }

    log.warn("Upgrading database from schema " + fromVersion + " to version " + toVersion + "...");

    // Assume we're advancing one version at a time.
    // In each case, this may be modified to skip
    // intermediate versions e.g., from 11 to 14.
    int newVersion = fromVersion + 1;
    final List<String> cmds = new LinkedList<>();

    // Otherwise, advance one version at a time
    switch (fromVersion) {

      case VERSION_1:
        // Insert database commands here...

        // Note new version
        cmds.add("UPDATE configuration set elementvalue='2'"
            + " WHERE elementname='database.schemaversion'");
        newVersion = VERSION_2;
        break;

      // Otherwise, indicate failure
      default:
        return false;
    }

    // Execute update commands
    for (String s : cmds) {
      log.debug("Updating schema with SQL stmt, '" + s + "'...");
      if (Database.executeUpdate(s) < 0) {
        log.error("While upgrading from version " + fromVersion + " to version " + toVersion
            + ", an error occurred executing command: '" + s
            + "'. Manual intervention may be required.");
      }
    }

    // Perform any necessary post processing to update data values
    if (!upgradePostProcessing(fromVersion)) {
      log.error("Upgrade post-processing errors occurred while upgrading from version "
          + fromVersion + " to version " + toVersion + ".  Manual intervention may be required");
      // Ignore error rather than return false;
    }

    // Invoke this method repeatedly until done
    return upgradeSchema(newVersion, toVersion);
  }

  /**
   * upgradePostProcessing performs any necessary post-process any updates on
   * the upgraded structure.
   * <p>
   * This is equivalent to the LLC file used on a reset database but this
   * upgradePostProcessing method assumes it is starting from an existing,
   * populated database that has been updated (in structure) and only needs
   * values for the structures that have changed where LLC file assumes an empty
   * database that needs all values populated.
   * </p>
   *
   * @param fromVersion
   *          is the version from which we are upgrading
   * @return true if successful
   */
  private boolean upgradePostProcessing(final int fromVersion) {

    final List<String> cmds = new LinkedList<>();

    switch (fromVersion) {

      case VERSION_1:
        // add sql commands to cmds to update database values as needed

        break;

      default:
        break;
    }

    // Execute post-processing commands
    boolean success = true;
    for (String s : cmds) {
      log.debug("Post-processing schema with SQL stmt, '" + s + "'...");
      if (Database.executeUpdate(s) < 0) {
        log.error("Error executing command: '" + s + "'.");
        success = false;
      }
    }
    return success;
  }

  /**
   * downgradeSchema reverts to a previous version.
   *
   * @param fromVersion
   *          is the current version of the DB schema for this emitter
   * @param toVersion
   *          is the desired version of DB schema for this emitter
   * @return true if successful
   */
  private boolean downgradeSchema(final int fromVersion, final int toVersion) {

    // If we're there, return success
    if (fromVersion == toVersion) {
      return true;
    }

    // Cannot downgrade below version 1
    if (toVersion < BASE_SCHEMA_VERSION) {
      log.error("Failed attempt to downgrade schema to " + toVersion + ".  Base version is "
          + BASE_SCHEMA_VERSION);
      return false;
    }

    log.warn("Downgrading database schema from version " + fromVersion + " to version " + toVersion
        + "...");

    // To get from fromVersion to toVersion, advance one level at a time.
    final boolean success = downgradeOneLevel(fromVersion);
    if (!success) {
      return false;
    }

    // Recursively invoke until done
    return downgradeSchema(fromVersion - 1, toVersion);
  }

  /**
   * Issue a database command to downgrade one version.
   *
   * @param fromVersion
   *          is the current version of the database
   * @return true if successful
   */
  private boolean downgradeOneLevel(final int fromVersion) {
    // newVersion is the next increment towards toVersion
    final int newVersion = fromVersion - 1;
    final String downgradeMethodName = "downgradeto" + newVersion;
    final boolean success = Database.executeProc(downgradeMethodName);
    return success;
  }

  /**
   * Get the web application root directory.
   *
   * @return the webappRoot
   */
  public static String getWebappRoot() {
    return webappRoot;
  }

  /**
   * Set the web application root directory.
   *
   * @param newWebappRoot
   *          the webappRoot to set
   */
  public static void setWebappRoot(final String newWebappRoot) {
    webappRoot = newWebappRoot;
  }
}
