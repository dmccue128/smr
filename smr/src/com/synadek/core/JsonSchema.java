/**
 * JsonSchema.java
 *
 * @author dmccu
 */
package com.synadek.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON schema.
 */
public class JsonSchema {

	// Keys defined by JSON Schema standard
	private static final String JSON_KEY_EXCLUSIVE_MINIMUM = "exclusiveMinimum";
	private static final String JSON_KEY_SCHEMA_MINIMUM = "minimum";
	private static final String JSON_KEY_SCHEMA_REQUIRED = "required";
	private static final String JSON_KEY_SCHEMA_PROPERTIES = "properties";
	private static final String JSON_KEY_SCHEMA_TYPE = "type";
	private static final String JSON_KEY_DESCRIPTION = "description";
	private static final String JSON_KEY_TITLE = "title";
	private static final String JSON_KEY_SCHEMA_ID = "$schema";

	/**
	 * Create a JSON schema definition for a number with a minimum.
	 *
	 * @param desc         description of the field
	 * @param min          minimum value allowed for this field value
	 * @param exclusiveMin is minimum exclusive
	 * @return the schema object
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject schemaNumber(final String desc, final double min, final boolean exclusiveMin) {
		final JSONObject result = new JSONObject();
		result.put(JSON_KEY_DESCRIPTION, "Maximum depth of anchor i.e., length of anchor chain in meters.");
		result.put(JSON_KEY_SCHEMA_TYPE, "number");
		result.put(JSON_KEY_SCHEMA_MINIMUM, Double.valueOf(0));
		result.put(JSON_KEY_EXCLUSIVE_MINIMUM, Boolean.TRUE);
		return result;
	}

	/**
	 * The JSON object representing the schema.
	 */
	final JSONObject schema;

	/**
	 * Constructor for a well-formed JSON schema.
	 *
	 * @param nam   the schema name
	 * @param desc  description of the component
	 * @param props Object containing descriptions of each schema item
	 * @param reqd  Array identifying which schema items are required i.e. not
	 *              optional
	 * @return the schema
	 */
	@SuppressWarnings("unchecked")
	public JsonSchema(final String nam, final String desc, final JSONObject props, final JSONArray reqd) {
		schema = new JSONObject();
		schema.put(JSON_KEY_SCHEMA_ID, "http://json-schema.org/draft-04/schema#");
		schema.put(JSON_KEY_TITLE, nam);
		schema.put(JSON_KEY_DESCRIPTION, desc);
		schema.put(JSON_KEY_SCHEMA_TYPE, "object");
		schema.put(JSON_KEY_SCHEMA_PROPERTIES, props);
		schema.put(JSON_KEY_SCHEMA_REQUIRED, reqd);
	}

	/**
	 * Add a property to the schema.
	 *
	 * @param key        the key/name of the property
	 * @param propSchema the schema definition
	 * @param required   true if this property is required
	 */
	@SuppressWarnings("unchecked")
	public void setProperty(final String key, final JSONObject propSchema, final boolean required) {
		JSONObject props = (JSONObject) schema.get("properties");
		JSONArray reqd = (JSONArray) schema.get("reqd");

		// Update the property definition
		props.put(key, propSchema);

		// Add/remove from reqd array as appropriate
		if (required && !reqd.contains(key)) {
			reqd.add(key);
		} else if (!required && reqd.contains(key)) {
			reqd.remove(key);
		}
	}

	/**
	 * Is a property defined in this schema.
	 *
	 * @param key the name of the property
	 * @return true if it is defined
	 */
	public boolean isDefined(final String key) {
		JSONObject props = (JSONObject) schema.get(JSON_KEY_SCHEMA_PROPERTIES);
		return props.containsKey(key);
	}

	/**
	 * Is a property required by this schema.
	 *
	 * @param key the name of the property
	 * @return true if the property is required
	 */
	public boolean isRequired(final String key) {
		JSONArray reqd = (JSONArray) schema.get(JSON_KEY_SCHEMA_REQUIRED);
		return reqd.contains(key);
	}
}
