/**
 * Configuration.java
 *
 * @author dmccu
 */
package com.synadek.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 */
public class Configuration {

	/**
	 * Maintain a single property list per configuration.
	 */
	private Properties properties;
	private final String name;
	private final String description;
	private final JsonSchema schema;

	/**
	 * Acquire a reference to the application logger.
	 */
	private final Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	/**
	 * Constructor.
	 */
	public Configuration(final String configurationName, final String configurationDescription) {
		properties = new Properties();
		name = configurationName;
		description = configurationDescription;
		schema = new JsonSchema(name, description, new JSONObject(), new JSONArray());
	}

	/**
	 * Get the description of this configuration.
	 *
	 * @return the description
	 */
	public String getConfigurationDescription() {
		return description;
	}

	/**
	 * Get the name associated with this configuration.
	 *
	 * @return the name
	 */
	public String getConfigurationName() {
		return name;
	}

	public JsonSchema getConfigurationSchema() {
		return schema;
	}

	/**
	 * Get the value of a configuration parameter of this component.
	 * 
	 * @param key the name of the parameter
	 * @return the value of the component or null if parameter is not defined
	 * @throws NumberFormatException if the value is not a Boolean
	 */
	public Boolean getPropertyBoolean(String key) {
		final String val = getPropertyString(key);
		return Boolean.valueOf(val);
	}

	/**
	 * Get the value of a configuration parameter of this component.
	 * 
	 * @param key the name of the parameter
	 * @return the value of the component or null if parameter is not defined
	 * @throws NumberFormatException if the value is not a Double
	 */
	public Double getPropertyDouble(String key) throws NumberFormatException {
		final String val = getPropertyString(key);
		return Double.valueOf(val);
	}

	/**
	 * Get the value of a configuration parameter of this component.
	 * 
	 * @param key the name of the parameter
	 * @return the value of the component or null if parameter is not defined
	 * @throws NumberFormatException if the value is not an Integer
	 */
	public Integer getPropertyInteger(String key) throws NumberFormatException {
		final String val = getPropertyString(key);
		return Integer.valueOf(val);
	}

	/**
	 * Get the names of all the properties defined in this configuration.
	 */
	public Set<String> getPropertyNames() {
		return properties.stringPropertyNames();
	}

	/**
	 * Get the value of a configuration parameter of this component.
	 * 
	 * @param key the name of the parameter
	 * @return the value of the component or null if parameter is not defined
	 */
	public String getPropertyString(String key) {
		// Get the value associated with this key
		return properties.getProperty(key);
	}

	/**
	 * Load properties from a file.
	 *
	 * @throws FileNotFoundException if filename is not found
	 * @throws IOException           if an error occurs reading the file
	 */
	public boolean loadProperties(final String filename) throws FileNotFoundException, IOException {
		boolean result = false;
		// create a reader object on the properties file
		try (FileReader reader = new FileReader(filename);) {
			// Add a wrapper around reader object
			properties.load(reader);
			result = true;
		} catch (IOException err) {
			log.error(err);
		}
		return result;
	}

	/**
	 * Empty the configuration.
	 */
	public void clear() {
		properties = new Properties();
	}

	/**
	 * Save properties to a file.
	 *
	 * @param filename the name of the file
	 * @param desc     a description to put in the file
	 * @throws IOException if the filename specified cannot be created or written
	 */
	public void saveProperties(final String filename, final String desc) {
		// store the properties to a file
		try (FileWriter f = new FileWriter(filename)) {
			properties.store(f, description);
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Set/update the value of a configuration parameter of this component
	 * 
	 * @param key   the name of the parameter
	 * @param value the value of the parameter
	 */
	public void setProperty(String key, boolean value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Set/update the value of a configuration parameter of this component
	 * 
	 * @param key   the name of the parameter
	 * @param value the value of the parameter
	 */
	public void setProperty(String key, double value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Set/update the value of a configuration parameter of this component
	 * 
	 * @param key   the name of the parameter
	 * @param value the value of the parameter
	 */
	public void setProperty(String key, int value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Set/update the value of a configuration parameter of this component
	 * 
	 * @param key   the name of the parameter
	 * @param value the value of the parameter
	 */
	public void setProperty(String key, String value) {
		properties.put(key, value);
		if (!schema.isDefined(key)) {
			log.warn("Configuration property {} is not defined in the schema", key);
		}
	}

	/**
	 * Get all the configuration parameters associated with this component in a
	 * single request.
	 * 
	 * @return the configuration as a JSON object with all values as strings
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		Set<String> keys = getPropertyNames();
		for (String key : keys) {
			result.put(key, getPropertyString(key));
		}
		return result;
	}

}
