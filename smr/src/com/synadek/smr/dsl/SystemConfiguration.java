/**
 * SystemConfiguration.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.dsl;

import com.synadek.smr.database.Database;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Maintain configuration elements for the device.
 */
public final class SystemConfiguration {

  /**
   * Acquire a reference to the application logger.
   */
  private static Logger log = LogManager
      .getLogger(SystemConfiguration.class.getPackage().getName());

  /**
   * maximum length of a key string.
   */
  private static final int MAX_KEY_SIZE = 64;

  /**
   * maximum length of a value string.
   */
  private static final int MAX_VALUE_SIZE = 2048;

  /**
   * System appropriate new line indicator.
   */
  private static final String NEWLINE = System.getProperty("line.separator");

  /**
   * Constructor.
   */
  private SystemConfiguration() {
  }

  /**
   * getElementNames returns the names of all the defined elements.
   *
   * @return set of names
   */
  public static Set<String> getElementNames() {
    final String query = "SELECT elementname FROM Configuration";
    final Set<String> answer = new HashSet<>();

    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
          answer.add(results.getString(1));
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException e) {
      log.error("Error retrieving element names", e);
    }
    return answer;
  }

  /**
   * Retrieve the string representation of the value of a configuration element.
   *
   * @param key
   *          is the name
   * @return the value as a string
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  private static String getOneElement(final String key) throws NoSuchElementException {

    // Defense against potential software design error
    if (key == null || key.length() < 1) {
      log.error("Attempt to read a configuration element with a null key");
      Thread.dumpStack();
      throw new NoSuchElementException("null");
    }

    // Define the SQL query to retrieve the element value
    final String query = "SELECT elementvalue FROM Configuration WHERE elementname='" + key + "'";
    String answer = null;
    boolean error = false;

    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        if (results.next()) {
          answer = results.getString(1);
        } else {
          error = true;
        }
        results.close();
        stmt.close();
      }
    } catch (SQLException e) {
      log.error("Error retrieving value of element " + key + ": " + e.getMessage());
      error = true;
    }

    if (error) {
      throw new NoSuchElementException(key);
    }

    return answer;
  }

  /**
   * Convenience function to get a string element.
   *
   * @param key
   *          is the name of the element
   * @return the integer or null if an error occurs.
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  public static String getStringElement(final String key) throws NoSuchElementException {
    return SystemConfiguration.getOneElement(key);
  }

  /**
   * Convenience function to get an integer element.
   *
   * @param key
   *          is the name of the element
   * @return the integer or -1 if a conversion error occurs.
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  public static int getIntElement(final String key) throws NoSuchElementException {
    // Get value from system configuration
    try {
      final String dbvalue = SystemConfiguration.getOneElement(key);
      if (dbvalue == null) {
        log.warn("Expected Integer, but retrieved null value for element " + key);
      } else {
        return Integer.parseInt(dbvalue);
      }
    } catch (NumberFormatException e) {
      log.error("Retrieved non-numeric value for element " + key + ": " + e.getMessage());
    } catch (NullPointerException e) {
      log.warn("Expected Integer, but retrieved null value for element " + key);
    }
    return -1;
  }

  /**
   * Convenience function to get an long element.
   *
   * @param key
   *          is the name of the element
   * @return the long value or -1 if a conversion error occurs.
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  public static long getLongElement(final String key) throws NoSuchElementException {
    // Get value from system configuration
    try {
      final String dbvalue = SystemConfiguration.getOneElement(key);
      if (dbvalue == null) {
        log.warn("Expected long integer, but retrieved null value for element " + key);
      } else {
        return Long.parseLong(dbvalue);
      }
    } catch (NumberFormatException e) {
      log.error("Retrieved non-numeric value for element " + key + ": " + e.getMessage());
    } catch (NullPointerException e) {
      log.warn("Expected long integer, but retrieved null value for element " + key);
    }
    return -1L;
  }

  /**
   * Convenience function to get a boolean element.
   *
   * @param key
   *          is the name of the element
   * @return the value or false if an error occurs.
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  public static boolean getBooleanElement(final String key) throws NoSuchElementException {
    // Get value from system configuration
    try {
      final String dbvalue = SystemConfiguration.getOneElement(key);
      if (dbvalue == null) {
        log.warn("Expected Boolean, but retrieved null value for element " + key);
      } else {
        return Boolean.parseBoolean(dbvalue);
      }
    } catch (NumberFormatException e) {
      log.error("Retrieved non-boolean value for element " + key + ": " + e.getMessage());
    } catch (NullPointerException e) {
      log.warn("Expected Boolean, but retrieved null value for element " + key);
    }
    return false;
  }

  /**
   * Convenience function to get a double element.
   *
   * @param key
   *          is the name of the element
   * @return the value or -1.0d if an error occurs.
   * @throws NoSuchElementException
   *           if the key does not name a known system configuration element
   */
  public static double getDoubleElement(final String key) throws NoSuchElementException {
    // Get value from system configuration
    try {
      final String dbvalue = SystemConfiguration.getOneElement(key);
      if (dbvalue == null) {
        log.warn("Expected Double, but retrieved null value for element " + key);
      } else {
        return Double.parseDouble(dbvalue);
      }
    } catch (NumberFormatException e) {
      log.error("Retrieved non-double value for element " + key + ": " + e.getMessage());
    } catch (NullPointerException e) {
      log.warn("Expected Double, but retrieved null value for element " + key);
    }

    return -1.0d;
  }

  /**
   * Update the value of a configuration element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   * @return true if successful
   */
  public static boolean setElement(final String key, final String value) {

    // Key cannot be null or empty
    if (key == null || key.length() < 1) {
      log.error("Attempt to update a element with a null key ignored. Value=" + value);
      return false;
    }

    if (key.length() > MAX_KEY_SIZE) {
      log.error("Error updating configuration element " + key + ": key exceeds maximum size of "
          + MAX_KEY_SIZE);
      return false;
    }

    if (value.length() > MAX_VALUE_SIZE) {
      log.error("Error updating configuration element " + key + ": value '" + value
          + "' exceeds maximum size of " + MAX_VALUE_SIZE);
      return false;
    }

    // Update the value in the database
    final String query = "UPDATE configuration SET elementvalue='" + Database.escapeSql(value)
        + "' WHERE elementname='" + Database.escapeSql(key) + "'";

    if (Database.executeUpdate(query) < 1) {
      log.warn("Error updating element " + key + ". Creating this element now...");

      // Update failure may indicate the element was not defined.
      if (!createElement(key, value)) {
        log.error("Failed to create element " + key + " with value='" + value + "'");
        return false;
      }
    }

    return true;
  }

  /**
   * Convenience method to set a boolean element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   */
  public static void setElement(final String key, final boolean value) {
    setElement(key, String.valueOf(value));
  }

  /**
   * Convenience method to set an integer element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   */
  public static void setElement(final String key, final int value) {
    setElement(key, String.valueOf(value));
  }

  /**
   * Convenience method to set a long element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   */
  public static void setElement(final String key, final long value) {
    setElement(key, String.valueOf(value));
  }

  /**
   * Convenience method to set a double element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   */
  public static void setElement(final String key, final double value) {
    setElement(key, String.valueOf(value));
  }

  /**
   * Create a new configuration element.
   *
   * @param key
   *          is the name of the element
   * @param value
   *          is the new value of the element
   * @return true if successful
   */
  private static boolean createElement(final String key, final String value) {

    // Key cannot be null or empty
    if (key == null || key.length() < 1) {
      log.error("Attempt to create a element with a null key ignored. Value=" + value);
      return false;
    }

    if (key.length() > MAX_KEY_SIZE) {
      log.error("Error creating configuration element " + key + ": key exceeds maximum size of "
          + MAX_KEY_SIZE);
      return false;
    }

    if (value.length() > MAX_VALUE_SIZE) {
      log.error("Error creating configuration element " + key + ": value '" + value
          + "' exceeds maximum size of " + MAX_VALUE_SIZE);
      return false;
    }

    log.debug("Updating system configuration element " + key + " to " + value);

    // Update the value in the database
    final String query = "INSERT INTO configuration(elementname,elementvalue) VALUES('" + key
        + "','" + value + "')";

    if (Database.executeUpdate(query) < 1) {
      log.error("Error creating element " + key);
      return false;
    }

    return true;
  }

  /**
   * Return all configuration elements in a string (in Properties format).
   *
   * @return the configuration elements
   */
  public static String fmtString() {
    final StringBuffer sb = new StringBuffer();
    final Set<String> names = getElementNames();
    for (String name : names) {
      sb.append(name);
      sb.append('=');
      try {
        sb.append(getStringElement(name));
      } catch (NoSuchElementException e) {
        sb.append("unknown");
      }
      sb.append(NEWLINE);
    }
    return sb.toString();
  }

  /**
   * Return all the configuration elements in XML format.
   *
   * @return the configuration elements
   */
  public static String fmtXml() {
    final StringBuffer sb = new StringBuffer();
    final Set<String> names = getElementNames();

    // Output header lines
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    sb.append(NEWLINE);

    sb.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
    sb.append(NEWLINE);

    sb.append("<properties>");
    sb.append(NEWLINE);
    for (String name : names) {
      sb.append("<entry key=\"");
      sb.append(name);
      sb.append("\">");
      try {
        sb.append(StringEscapeUtils.escapeXml10(getStringElement(name)));
      } catch (NoSuchElementException e) {
        sb.append(StringEscapeUtils.escapeXml10("unknown"));
      }
      sb.append("</entry>");
      sb.append(NEWLINE);
    }
    sb.append("</properties>");
    sb.append(NEWLINE);

    return sb.toString();
  }

  /**
   * Return all the configuration elements in JSON format.
   *
   * @return the configuration elements
   */
  @SuppressWarnings("unchecked")
  public static String fmtJson() {
    final JSONArray elements = new JSONArray();
    for (String key : getElementNames()) {
      // Create an entry for this element
      final JSONObject element = new JSONObject();
      element.put("name", key);
      try {
        element.put("value", getStringElement(key));
      } catch (NoSuchElementException e) {
        element.put("value", "unknown");
      }
      // Add this element to the array
      elements.add(element);
    }

    // wrap the array in an object with the key, 'data'
    final JSONObject obj = new JSONObject();
    obj.put("data", elements);

    // return the string representation of this JSON object
    return obj.toJSONString();
  }

  /**
   * Parse and update configuration elements from a string (in Properties
   * format).
   *
   * @param props
   *          is the string containing element updates in Java Properties format
   */
  public static void parse(final String props) {
    final String[] lines = props.split(NEWLINE);
    if (lines == null) {
      log.error("Invalid property file format: '" + props + "'");
      return;
    }

    for (String line : lines) {
      final String[] pv = line.split("=");
      if (pv == null || pv.length != 2) {
        log.error("Invalid property value format: '" + line + "'");
        continue;
      }

      final String key = pv[0];
      final String value = pv[1];
      SystemConfiguration.setElement(key, value);
    }
  }

  /**
   * Parse and update configuration elements from a string (in XML format).
   *
   * @param xml
   *          is the string containing element updates in xml format
   */
  public static void parseXml(final String xml) {
    log.error("Unimplemented function parseXML");
  }

  /**
   * Parse and update configuration elements from a string (in JSON format).
   *
   * @param json
   *          is the string containing element updates in JSON format
   */
  public static void parseJson(final String json) {
    final JSONParser parser = new JSONParser();
    try {
      final Object obj = parser.parse(new StringReader(json));
      final JSONObject jsonObject = (JSONObject) obj;
      final JSONArray data = (JSONArray) jsonObject.get("data");
      // loop array
      @SuppressWarnings("unchecked")
      final Iterator<JSONObject> iterator = data.iterator();
      while (iterator.hasNext()) {
        final JSONObject item = iterator.next();
        final String key = (String) item.get("name");
        final String value = (String) item.get("value");
        setElement(key, value);
      }
    } catch (IOException e) {
      log.error("Error parsing JSON string '" + json + "'", e);
    } catch (ParseException e) {
      log.error("Error parsing JSON string '" + json + "'", e);
    }
  }

  /**
   * non-destructive self-test tests only 'read' functions.
   */
  public static void selftest() {

    // Display default values
    final Set<String> params = getElementNames();
    for (String key : params) {
      try {
        log.info(key + "=" + getStringElement(key));
      } catch (NoSuchElementException e) {
        log.info(key + "=unknown");
      }
    }

    // Get string representation of elements
    final String propfilefmt = fmtString();
    log.info("Property file format: " + propfilefmt);

    // Get XML representation of elements
    final String xmlfilefmt = fmtXml();
    log.info("XML file format" + xmlfilefmt);

    // Get JSON file format
    final String jsonfmt = fmtJson();
    log.info("JSON format:" + jsonfmt);

    // Display database contents
    final String query = "SELECT elementname,elementvalue FROM Configuration";
    try {
      final Statement stmt = Database.createStatement();
      if (stmt != null) {
        final ResultSet results = stmt.executeQuery(query);
        log.info("Database contents:");
        while (results.next()) {
          final String key = results.getString("elementname");
          final String value = results.getString("elementvalue");
          log.info(key + "=" + value);
        }
        log.info("[end of database contents]");
        results.close();
        stmt.close();
      }
    } catch (SQLException sqle) {
      log.error("Error getting configuration elements: ", sqle);
    }

    log.info("[end of self-test]");
  }
}
