/**
 * Address.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.core;

import org.json.simple.JSONObject;

/**
 * Postal Address.
 */
public class PostalAddress {

  /**
   * Keys to JSON representation of address.
   */
  public static final String JSON_KEY_ADDR1 = "addr1";
  public static final String JSON_KEY_ADDR2 = "addr2";
  public static final String JSON_KEY_CITY = "city";
  public static final String JSON_KEY_STATE = "state";
  public static final String JSON_KEY_POSTCODE = "postcode";
  public static final String JSON_KEY_COUNTRY = "country";

  /**
   * Fields of a postal address.
   */
  private String addr1;
  private String addr2;
  private String city;
  private String state;
  private String postcode;
  private String country;

  /**
   * Default constructor.
   */
  public PostalAddress() {
    addr1 = null;
    addr2 = null;
    city = null;
    state = null;
    postcode = null;
    country = null;
  }

  /**
   * JSON parsing constructor.
   *
   * @param obj
   *          the JSON object
   */
  public PostalAddress(final JSONObject obj) {
    addr1 = (String) obj.get(JSON_KEY_ADDR1);
    addr2 = (String) obj.get(JSON_KEY_ADDR2);
    city = (String) obj.get(JSON_KEY_CITY);
    state = (String) obj.get(JSON_KEY_STATE);
    postcode = (String) obj.get(JSON_KEY_POSTCODE);
    country = (String) obj.get(JSON_KEY_COUNTRY);
  }

  /**
   * Get first line of address.
   *
   * @return the addr1
   */
  public String getAddr1() {
    return addr1;
  }

  /**
   * Set first line of address.
   *
   * @param addr1
   *          the addr1 to set
   */
  public void setAddr1(String addr1) {
    this.addr1 = addr1;
  }

  /**
   * Get second line of address.
   *
   * @return the addr2
   */
  public String getAddr2() {
    return addr2;
  }

  /**
   * Set second line of address.
   *
   * @param addr2
   *          the addr2 to set
   */
  public void setAddr2(String addr2) {
    this.addr2 = addr2;
  }

  /**
   * Get city.
   *
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Get country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Set country.
   *
   * @param countryName
   *          the name of the country
   */
  public void setCountry(final String countryName) {
    this.country = countryName;
  }

  /**
   * Set city.
   *
   * @param city
   *          the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Get state.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Set state.
   *
   * @param state
   *          the state to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Get postcode.
   *
   * @return the postcode
   */
  public String getPostcode() {
    return postcode;
  }

  /**
   * Set postcode.
   *
   * @param postcode
   *          the postcode to set
   */
  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  /**
   * Convert to JSONObject representation.
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJson() {
    final JSONObject result = new JSONObject();

    result.put(JSON_KEY_ADDR1, addr1);
    result.put(JSON_KEY_ADDR2, addr2);
    result.put(JSON_KEY_CITY, city);
    result.put(JSON_KEY_STATE, state);
    result.put(JSON_KEY_POSTCODE, postcode);
    result.put(JSON_KEY_COUNTRY, country);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (addr1 != null && !addr1.isBlank()) {
      sb.append(addr1);
    }
    if (addr2 != null && !addr2.isBlank()) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      sb.append(addr2);
    }
    if (city != null && !city.isBlank()) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append(city);
    }
    if (state != null && !state.isBlank()) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append(state);
    }
    if (postcode != null && !postcode.isBlank()) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      sb.append(postcode);
    }
    if (country != null && !country.isBlank()) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      sb.append(country);
    }
    return sb.toString();
  }
}
