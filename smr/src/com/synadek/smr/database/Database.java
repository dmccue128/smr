/**
 * Database.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

/**
 * Database interface layer.
 */
public class Database {

  /**
   * If it is necessary to test the validity of the database connection, wait no
   * longer than DB_VALID_TIMEOUT seconds for the test to complete.
   */
  private static final int DB_VALID_TIMEOUT = 2;

  /**
   * Class private variable to hold database connection.
   */
  private static Connection conn;
  /**
   * Acquire a reference to the application logger.
   */
  private static Logger log = LogManager.getLogger(Database.class.getPackage().getName());

  /**
   * Acquire a reference to the data source for this database.
   */
  private static DataSource smrDB;

  /**
   * Temporary hack to generate UIDs.
   */
  private static long nextId = 0;

  /**
   * Default constructor for DB.
   */
  protected Database() {

  }

  /**
   * Static method getConnInstance manages the database connection. If we have
   * not yet established a connection to the database, getConnInstance connects
   * If we already connected once, getConnInstance returns that cached
   * connection
   *
   * @return a database connection
   */
  private static synchronized Connection getConnInstance() {

    // Note: context.xml in META-INF defines the data sources in
    // java component environment
    final String myDriverResource = "java:comp/env/jdbc/pgsmr";

    try {
      if (conn == null || conn.isClosed()) {

        final InitialContext initialContext = new InitialContext();

        smrDB = (DataSource) initialContext.lookup(myDriverResource);
        if (smrDB == null) {
          log.fatal("Accessing initial context to find data source, " + myDriverResource
              + ", returned null");
          return null;
        }

        // Use the database resource to get a connection to the database
        conn = smrDB.getConnection();

        if (conn == null || !conn.isValid(DB_VALID_TIMEOUT)) {
          log.fatal("Failed to connect to database using data source: " + myDriverResource);
        }
      }
    } catch (SQLException err) {
      log.error("Unable to connect to database: " + err.getMessage() + " (Error code: "
          + err.getErrorCode() + ")");
    } catch (NullPointerException np) {
      log.error("Unable to dereference database resource " + myDriverResource
          + " -- Resource Injection failed.");
    } catch (NamingException ne) {
      log.error("Naming exception attempting to resolve database resource reference: "
          + ne.getMessage() + ":" + ne.getExplanation());
    }

    return conn;
  }

  /**
   * Create a new Statement object for the local DB.
   *
   * @return the new Statement.
   */
  public static Statement createStatement() {
    try (final Connection c = Database.getConnInstance();) {
      if (c != null) {
        return c.createStatement();
      }
    } catch (SQLException err) {
      log.error("SQL exception attempting to create a statement: " + err.getMessage()
          + " (Error code=" + err.getErrorCode() + ")");
    }

    log.error("Unable to create a statement for the DB -- No connection to DB");
    return null;
  }

  /**
   * executeUpdate performs a SQL update.
   *
   * @param sql
   *          - SQL string defining the update to perform
   * @return number of rows updated or -1 if exception or error occurred
   */
  public static int executeUpdate(final String sql) {
    // the number of rows affected by the update or insert
    int numRows = -1;
    try {
      final Statement stmt = createStatement();
      if (stmt != null) {
        numRows = stmt.executeUpdate(sql);
        stmt.close();
      } else {
        log.warn("Unable to create DB statement");
        return -1;
      }
    } catch (SQLException sqlExcept) {
      // Needs to be org.postgresql.util.PSQLException instead, but
      // library not loaded.
      log.error("Error executing update: '" + sql + "', error: " + sqlExcept.getMessage());
      log.error("SQL Exception error code: " + sqlExcept.getErrorCode() + ", SQL State: "
          + sqlExcept.getSQLState());
      sqlExcept.printStackTrace();
      numRows = -1;
    } finally {
      log.debug("Update Query '" + sql + "' updated " + numRows + " rows.");
    }
    return numRows;
  }

  /**
   * Execute a stored procedure with no parameters and no result (i.e., void).
   *
   * @param procname
   *          is the name of the stored procedure
   * @return true if successful
   */
  public static synchronized boolean executeProc(final String procname) {

    boolean success = true;
    CallableStatement exeProc = null;

    final Connection con = Database.getConnInstance();
    if (con == null) {
      log.error("Failed to execute stored procedure " + procname + " -- no connection available");
      return false;
    }

    // Prepare and execute the proc
    try {
      con.setAutoCommit(false);
      exeProc = con.prepareCall("{ call " + procname + "() }");
      exeProc.execute();
    } catch (SQLException err) {
      log.error("SQL exception attempting to execute " + procname + ": " + err.getMessage()
          + " (Error code=" + err.getErrorCode() + ")");
      success = false;
    }

    // Close the call
    try {
      exeProc.close();
    } catch (SQLException err) {
      log.error("SQL exception attempting to close call to " + procname + ": " + err.getMessage()
          + " (Error code=" + err.getErrorCode() + ")");
    } catch (NullPointerException e) {
      log.error(e);
    }

    // Close the connection
    try {
      con.close();
    } catch (SQLException err) {
      log.error("SQL exception attempting to close connection: " + err.getMessage()
          + " (Error code=" + err.getErrorCode() + ")");
    }

    // Indicate success or failure
    return success;
  }

  /**
   * static method closeDB closes the database connection.
   */
  public static synchronized void closeDb() {

    try {
      if (conn != null) {
        if (!conn.getAutoCommit()) {
          conn.commit();
        }
        conn.close();
      }

    } catch (SQLException sqle) {
      log.error("Error closing database, " + "Error code: " + sqle.getErrorCode() + ", Message: '"
          + sqle.getMessage() + "'" + ", SQL State: " + sqle.getSQLState() + ", Error info: '"
          + sqle.getMessage() + "'");
    }
    conn = null;
    log.info("Database closed.");
  }

  /**
   * Return a list of all the table names in the DB.
   *
   * @return a list containing zero or more table names.
   */
  public static Collection<String> getTableNames() {
    final Collection<String> result = new LinkedList<>();
    final String query = "SELECT table_name FROM information_schema.tables"
        + " WHERE table_schema = 'public' ORDER BY table_name";
    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
          result.add(results.getString(1));
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error("Error getting table names: " + sqle.getMessage());
    }
    return result;
  }

  /**
   * Test whether or not the input parameter is a valid table name in the DB.
   *
   * @param name
   *          is the name to be confirmed
   * @return true if the table name is valid
   */
  public static boolean isTableName(final String name) {
    if (name == null) {
      log.error("Null table name passed to DB.isTableName");
      return false;
    }

    boolean result = false;
    final String query = "SELECT table_name FROM information_schema.tables"
        + " WHERE table_schema = 'public' AND table_name='" + name + "';";
    final Statement stmt = Database.createStatement();
    if (stmt == null) {
      return false;
    }

    try {

      final ResultSet resultset = stmt.executeQuery(query);
      if (resultset.next()) {
        result = true;
      }
      resultset.close();

    } catch (SQLException sqle) {
      log.error("Error confirming table name " + name + ": " + sqle.getMessage());
    }

    try {
      stmt.close();
    } catch (SQLException sqle) {
      log.error("Error closing statment", sqle);
    }

    return result;
  }

  /**
   * Get table columns returns the column labels for a database table.
   *
   * @param tableName
   *          name of the table in the database
   * @return a list of the column labels
   */
  public static Collection<String> getTableColumns(final String tableName) {
    final Collection<String> resultList = new LinkedList<>();
    // Note: Absence of an ORDER BY clause means the row returned by the
    // select statement will be
    // non-deterministically determined. Since all rows contain the same
    // column labels and
    // we are ignoring the data i.e., we are simply retrieving the column
    // labels, any row will do.
    final String query = "SELECT * FROM " + tableName + " LIMIT 1";
    final Statement stmt = Database.createStatement();
    if (stmt == null) {
      return resultList;
    }

    try {
      final ResultSet results1 = stmt.executeQuery(query);
      final ResultSetMetaData rsmd = results1.getMetaData();

      // gather the column labels
      for (int h = 1; h <= rsmd.getColumnCount(); h++) {
        resultList.add(rsmd.getColumnLabel(h));
      }

      results1.close();

    } catch (SQLException sqle) {
      log.error("Error getting column labels for table " + tableName + ": " + sqle.getMessage());
    }

    try {
      stmt.close();
    } catch (SQLException sqle) {
      log.error("Error closing statement", sqle);
    }

    return resultList;
  }

  /**
   * Get table data returns all the rows of a database table with optional sort
   * order.
   *
   * @param tableName
   *          name of the table in the database
   * @param preferredSortOrder
   *          optional (can be null) String containing field name <space/> ASC |
   *          DESC if not null, will be appended to a sort order clause on the
   *          query
   * @param offset
   *          if greater than zero, offset is the offset to the first record to
   *          return.
   * @param count
   *          if greater than zero, count limits the count of records to return.
   *          Count less than or equal to zero is interpreted to mean return all
   *          records.
   * @return a list of JSONArray objects each of which represents a single row
   *         of data. Note: numbers and booleans are returned in type, all other
   *         types are returned as Strings
   */
  @SuppressWarnings("unchecked")
  public static Collection<JSONArray> getTableData(final String tableName,
      final String preferredSortOrder, final long offset, final long count) {

    final Collection<JSONArray> resultList = new LinkedList<>();
    String query = "SELECT * FROM " + tableName;
    if (preferredSortOrder != null && preferredSortOrder.length() > 0) {
      query = query + " ORDER BY " + preferredSortOrder;
    }
    if (count > 0) {
      query = query + " LIMIT " + count;
    }
    if (offset > 0) {
      query = query + " OFFSET " + offset;
    }
    final Statement stmt = Database.createStatement();
    if (stmt == null) {
      return resultList;
    }

    try {
      final ResultSet results1 = stmt.executeQuery(query);
      final ResultSetMetaData rsmd = results1.getMetaData();
      final int numberCols = rsmd.getColumnCount();

      while (results1.next()) {

        final JSONArray aRow = new JSONArray();
        for (int i = 1; i <= numberCols; i++) {
          // Retrieve the column type
          final int colType = rsmd.getColumnType(i);
          switch (colType) {
            case java.sql.Types.BOOLEAN:
              aRow.add(Boolean.valueOf(results1.getBoolean(i)));
              break;
            case java.sql.Types.DOUBLE:
              aRow.add(Double.valueOf(results1.getDouble(i)));
              break;
            case java.sql.Types.FLOAT:
              aRow.add(Float.valueOf(results1.getFloat(i)));
              break;
            case java.sql.Types.INTEGER:
              aRow.add(Integer.valueOf(results1.getInt(i)));
              break;
            case java.sql.Types.SMALLINT:
              aRow.add(Short.valueOf(results1.getShort(i)));
              break;
            default:
              aRow.add(results1.getString(i));
          }
        }
        resultList.add(aRow);
      }
      results1.close();

    } catch (SQLException sqle) {
      log.error("Error getting data for table " + tableName + " with query '" + query + ": "
          + sqle.getMessage());
    }
    try {
      stmt.close();
    } catch (SQLException sqle) {
      log.error("Error closing statement", sqle);
    }

    return resultList;
  }

  /**
   * Return the number of rows in a table.
   *
   * @param table
   *          is the name of the table to check
   * @return record count or zero if no records exist
   */
  public static long getTableCount(final String table) {
    final String query = "SELECT COUNT(*) FROM " + table;
    long result = -1L;
    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        try {
          if (results.next()) {
            result = results.getLong(1);
          } else {
            log.warn("Unable to get table count using query, " + query);
          }
        } catch (SQLException sqle) {
          log.error(sqle.getMessage());
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error("Error getting table count with query " + query + ": " + sqle.getMessage());
    }
    return result;
  }

  /**
   * Return the total disk space size used by a table including index and
   * toasted data.
   *
   * @param table
   *          is the name of the table to check
   * @return size in bytes
   */
  public static long getTableSize(final String table) {
    final String query = "SELECT pg_total_relation_size('" + table + "')";
    long result = 0;

    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        try {
          if (results.next()) {
            result = results.getLong(1);
          } else {
            log.warn("Unable to get table size for table " + table + " using query, " + query);
          }
        } catch (SQLException sqle) {
          log.error("Error extracting table size", sqle.getMessage());
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error("Error getting disk usage of table " + table + " with query " + query + ": "
          + sqle.getMessage());
    }
    return result;
  }

  /**
   * Generate a unique ID for a record in a table of the database.
   *
   * @param tableName
   *          the table name
   * @return the unique id
   */
  public static long generateUid(final String tableName) {
    return nextId++;
  }

  /**
   * Return the total disk space size used by the database including all data
   * and metadata.
   *
   * @return size in bytes
   */
  public static long getDatabaseSize() {
    final String query = "SELECT pg_database_size(current_database())";
    long result = 0;

    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        if (results.next()) {
          result = results.getLong(1);
        } else {
          log.warn("Unable to get database size using query, " + query);
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error(
          "Error getting disk usage of database with query " + query + ": " + sqle.getMessage());
    }
    return result;
  }

  /**
   * executeSQLGroup is a utility function that allows arbitrary SQL commands to
   * be executed.
   *
   * @param cmdList
   *          is the list of SQL commands to execute.
   * @return the number of successfully executed commands i.e., no exception was
   *         raised while executing the command.
   */
  public static int executeSqlGroup(final List<String> cmdList) {
    int successCount = 0;
    for (String cmd : cmdList) {
      if (executeUpdate(cmd) >= 0) {
        successCount += 1;
      } else {
        log.warn("Error executing DB command: " + cmd);
      }
    }
    return successCount;
  }

  /**
   * resetDB deletes the existing local database entirely (data and schema),
   * creates a new empty local database, loads the schema from the sql file
   * included with this software release, and creates minimum required initial
   * records in the new database.
   *
   * @param dirRoot
   *          is the root directory of this web application
   * @param schemaVersion
   *          is the version number to record for the new DB schema
   * @return true if successful
   */
  public static boolean resetDb(final String dirRoot, final int schemaVersion) {

    log.info(
        "Resetting database using '" + dirRoot + "', loading schema version " + schemaVersion);

    // Load the schema file
    if (!loadSql(dirRoot, "smrDB", schemaVersion)) {
      log.error("Failed to parse schema file");
      return false;
    }

    // Load schema downgrade procedure definitions and any other stored
    // procedures
    if (!defineSprocs(dirRoot, schemaVersion)) {
      log.error("Failed to parse stored procedures definition file");
      return false;
    }

    // Load initial records for the new database
    if (!loadSql(dirRoot, "smrLLC", schemaVersion)) {
      log.error("Failed to parse database inital records load file");
      return false;
    }

    // Success!
    return true;
  }

  /**
   * defineSPROCS loads into the database the stored procedures for a defined
   * version.
   *
   * @param dirRoot
   *          is the root directory of this web application
   * @param schemaVersion
   *          is the version of stored procedures to be loaded
   * @return true if successful
   */
  public static boolean defineSprocs(final String dirRoot, final int schemaVersion) {
    // Load stored procedure procedure definitions
    if (!loadSql(dirRoot, "smrSP", schemaVersion)) {
      log.error("Failed to parse database stored procedures definition file");
      return false;
    }
    return true;
  }

  /**
   * loadSQL loads a sql file from the standard directory.
   *
   * @param dirRoot
   *          is the root directory of this web application
   * @param basename
   *          is the base name of the file to load e.g., SterilizDB or
   *          SterilizLLC
   * @param schemaVersion
   *          is the version number to record for the new DB schema
   * @return true if successful
   */
  private static boolean loadSql(final String dirRoot, final String basename,
      final int schemaVersion) {
    // Use the SQL files in the emitter SW load to ensure consistency.
    final String sqlDirPath = dirRoot + "sql/";
    final String filename = basename + "v" + schemaVersion + ".sql";
    final String dbFilePath = sqlDirPath + filename;

    log.debug("Reading sql file '" + dbFilePath + "'...");

    // Read in the lines of the file
    final List<String> cmdList = readSql(dbFilePath);
    if (cmdList == null) {
      log.error("Failed to parse sql file " + filename);
      return false;
    }

    // Load the new file
    final int successCount = Database.executeSqlGroup(cmdList);
    if (successCount <= 0) {
      log.error("Failed to execute all commands in file " + filename);
      return false;
    }

    return true;
  }

  /**
   * Read and process a SQL string.
   *
   * @param dbFilePath
   *          is the path to the schema file.
   * @return list of commands from the file or null if an error occurs while
   *         reading the file
   */
  private static List<String> readSql(final String dbFilePath) {

    try {
      final BufferedReader dbStream = new BufferedReader(new FileReader(dbFilePath));
      final List<String> cmdList = new LinkedList<>();
      final StringBuffer cmdBuffer = new StringBuffer();

      int ch = dbStream.read();
      int peek = dbStream.read();
      while (ch != -1) {
        // skip comments
        if (ch == '-' && peek == '-') {
          while (ch != -1 && ch != '\r' && ch != '\n') {
            ch = peek;
            peek = dbStream.read();
          }
        }

        // convert special characters to spaces
        if ("\r\n".indexOf(ch) != -1) {
          ch = ' ';
        } else if (ch == '\'' || ch == '"') {
          // pass thru anything in quotes (will fail if EOL or EOF hit
          // before end quote)
          final int quoteChar = ch;
          do {
            // copy chars until end quote
            cmdBuffer.append((char) ch);
            ch = peek;
            peek = dbStream.read();
          } while (ch != quoteChar);
          // append end quote
          cmdBuffer.append((char) ch);
        } else if (ch == '$') {
          // Handle dollar-quoting

          // Extract the dollar quote keyword incl trailing dollar
          // sign
          final StringBuffer keyword = new StringBuffer();
          do {
            cmdBuffer.append((char) ch);
            ch = peek;
            peek = dbStream.read();
            keyword.append((char) ch);
          } while (ch != '$');

          // Append trailing dollar sign to output buffer
          cmdBuffer.append((char) ch);

          // Pass thru characters until the trailing dollar quote
          // keyword is found
          StringBuffer matchword;
          do {
            // scan until next dollar sign
            do {
              ch = peek;
              peek = dbStream.read();
              cmdBuffer.append((char) ch);
            } while (ch != '$');

            // Extract the dollar quote keyword following this
            // dollar sign
            matchword = new StringBuffer();
            do {
              ch = peek;
              peek = dbStream.read();
              cmdBuffer.append((char) ch);
              matchword.append((char) ch);
            } while (ch != '$');

          } while (!keyword.toString().equalsIgnoreCase(matchword.toString()));

        } else if (ch != ' ' || peek != ' ') {
          if (ch == ';') {
            // Reached end of a command, add it to the list
            cmdList.add(cmdBuffer.toString());
            cmdBuffer.setLength(0);
          } else if (ch != -1) {
            // add this character to the current command line
            cmdBuffer.append((char) ch);
          }
        }
        ch = peek;
        peek = dbStream.read();
      }
      dbStream.close();
      return cmdList;
    } catch (FileNotFoundException fnf) {
      log.error("File not found: " + dbFilePath);
    } catch (IOException ioe) {
      log.error("IO Exception reading " + dbFilePath);
    }
    return null;
  }

  /**
   * Return the version of the database server software that is in use.
   *
   * @return the version string or "[unknown]" if unavailable
   */
  public static String getDbVersion() {
    final String query = "SELECT VERSION()";
    String answer = "[unknown]";

    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        if (results.next()) {
          answer = results.getString(1);
        } else {
          log.warn("Unable to get database version using query, " + query);
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error("Error getting version information: " + sqle.getMessage());
    }
    return answer;
  }

  /**
   * Begin a transaction block.
   *
   * @return a Savepoint if successful, null if unsuccessful
   */
  public static final Savepoint beginTransaction() {

    try (final Connection con = Database.getConnInstance();) {
      con.setAutoCommit(false);
      return con.setSavepoint();
    } catch (SQLException sqle) {
      log.error(sqle.getMessage());
    }
    return null;
  }

  /**
   * Commit a transaction.
   *
   * @return true if successful
   */
  public static final boolean commitTransaction() {

    boolean success = false;
    try (final Connection con = Database.getConnInstance();) {
      con.commit();
      success = true;
      con.setAutoCommit(true);
    } catch (SQLException sqle) {
      success = false;
      log.error(sqle.getMessage());
    }
    return success;
  }

  /**
   * Roll back a transaction.
   *
   * @param savept
   *          is the save point to which to roll back.
   * @return true if successful
   */
  public static final boolean rollbackTransaction(final Savepoint savept) {

    boolean success = false;
    try (final Connection con = Database.getConnInstance();) {
      con.rollback(savept);
      success = true;
      con.setAutoCommit(true);
    } catch (SQLException sqle) {
      success = false;
      log.error(sqle.getMessage());
    }
    return success;
  }

  /**
   * escape a string for SQL special characters.
   *
   * @param value
   *          to be escaped
   * @return escaped string suitable for storage in SQL
   */
  public static String escapeSql(final String value) {
    if (value == null) {
      return null;
    }
    final String noSingles = value.replace("'", "''");
    final String noDoubles = noSingles.replace("\"", "\"\"");
    return noDoubles;

  }
}
