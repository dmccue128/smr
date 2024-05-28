/**
 * RequestForProposals.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.mission;

import com.synadek.core.AbstractComponent;
import com.synadek.core.ComponentException;
import com.synadek.core.GpsCoordinates;
import com.synadek.core.PostalAddress;
import com.synadek.smr.control.navigation.Route;
import com.synadek.smr.control.navigation.RouteImpl;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Request for proposals to achieve mission parameters specified in the RFP.
 */
public class RequestForProposals extends AbstractComponent {

  /**
   * Mission Id.
   */
  private String missionId;
  /**
   * Customer name.
   */
  private String customer;
  /**
   * Customer address.
   */
  private PostalAddress address;
  /**
   * Proposal date.
   */
  private Calendar proposalDate;
  /**
   * Earliest acceptable departure date.
   */
  private Calendar earliestDepartureDate;
  /**
   * Latest acceptable departure date.
   */
  private Calendar latestDepartureDate;
  /**
   * Latest acceptable arrival date.
   */
  private Calendar latestArrivalDate;
  /**
   * Minimum acceptable width of cargo area (meters).
   */
  private double minimumCargoWidth;
  /**
   * Minimum acceptable height of cargo area (meters).
   */
  private double minimumCargoHeight;
  /**
   * Minimum acceptable depth of cargo area (meters).
   */
  private double minimumCargoDepth;

  /**
   * Minimum acceptable weight of cargo area. (kilograms)
   */
  private double minimumCargoWeight;

  /**
   * Departure location.
   */
  private GpsCoordinates departureLocation;

  /**
   * Route myRoute.
   */
  private Route plan;

  /**
   * Default constructor.
   */
  public RequestForProposals() {

    // Define a calendar for initializing the earliest start date.
    final Calendar now = Calendar.getInstance();

    // Define a calendar for initializing the latest start date.
    final Calendar soon = Calendar.getInstance();
    soon.add(Calendar.HOUR, 12);

    // Define a calendar for initializing the latest arrival date.
    final Calendar tomorrow = Calendar.getInstance();
    tomorrow.add(Calendar.HOUR, 24);

    earliestDepartureDate = now;
    latestDepartureDate = soon;
    latestArrivalDate = tomorrow;
    minimumCargoWidth = 0.1;
    minimumCargoHeight = 0.1;
    minimumCargoDepth = 0.1;
    minimumCargoWeight = 0.5;
    // Set default departure location to Wollongong NSW
    setDepartureLocation(new GpsCoordinates(-34.425072, 150.893143));
    plan = null;
  }

  /**
   * Get mission ID.
   *
   * @return the missionId
   */
  public final String getMissionId() {
    return missionId;
  }

  /**
   * Set mission ID.
   *
   * @param missionId
   *          the missionId to set
   */
  public final void setMissionId(String missionId) {
    this.missionId = missionId;
  }

  /**
   * Get customer name.
   *
   * @return the customer
   */
  public final String getCustomer() {
    return customer;
  }

  /**
   * Set customer name.
   *
   * @param customer
   *          the customer to set
   */
  public final void setCustomer(String customer) {
    this.customer = customer;
  }

  /**
   * Get address.
   *
   * @return the address
   */
  public final PostalAddress getAddress() {
    return address;
  }

  /**
   * Set address.
   *
   * @param address
   *          the address to set
   */
  public final void setAddress(PostalAddress address) {
    this.address = address;
  }

  /**
   * Get proposal date.
   *
   * @return the proposalDate
   */
  public final Calendar getProposalDate() {
    return proposalDate;
  }

  /**
   * Set proposal date.
   *
   * @param proposalDate
   *          the proposalDate to set
   */
  public final void setProposalDate(Calendar proposalDate) {
    this.proposalDate = proposalDate;
  }

  /**
   * Get earliest acceptable departure date.
   *
   * @return the earliestDepartureDate
   */
  public final Calendar getEarliestDepartureDate() {
    return earliestDepartureDate;
  }

  /**
   * Set earliest acceptable departure date.
   *
   * @param earliestDepartureDate
   *          the earliestDepartureDate to set
   */
  public final void setEarliestDepartureDate(Calendar earliestDepartureDate) {
    this.earliestDepartureDate = earliestDepartureDate;
  }

  /**
   * Get latest acceptable departure date.
   *
   * @return the latestDepartureDate
   */
  public final Calendar getLatestDepartureDate() {
    return latestDepartureDate;
  }

  /**
   * Set latest acceptable departure date.
   *
   * @param latestDepartureDate
   *          the latestDepartureDate to set
   */
  public final void setLatestDepartureDate(Calendar latestDepartureDate) {
    this.latestDepartureDate = latestDepartureDate;
  }

  /**
   * Get latest acceptable arrival date.
   *
   * @return the latestArrivalDate
   */
  public final Calendar getLatestArrivalDate() {
    return latestArrivalDate;
  }

  /**
   * Set latest acceptable arrival date.
   *
   * @param latestArrivalDate
   *          the latestArrivalDate to set
   */
  public final void setLatestArrivalDate(Calendar latestArrivalDate) {
    this.latestArrivalDate = latestArrivalDate;
  }

  /**
   * Get minimum cargo width.
   *
   * @return the minimumCargoWidth
   */
  public final double getMinimumCargoWidth() {
    return minimumCargoWidth;
  }

  /**
   * Set minimum cargo width.
   *
   * @param minimumCargoWidth
   *          the minimumCargoWidth to set
   */
  public final void setMinimumCargoWidth(double minimumCargoWidth) {
    this.minimumCargoWidth = minimumCargoWidth;
  }

  /**
   * Get minimum cargo height.
   *
   * @return the minimumCargoHeight
   */
  public final double getMinimumCargoHeight() {
    return minimumCargoHeight;
  }

  /**
   * Set minimum cargo height.
   *
   * @param minimumCargoHeight
   *          the minimumCargoHeight to set
   */
  public final void setMinimumCargoHeight(double minimumCargoHeight) {
    this.minimumCargoHeight = minimumCargoHeight;
  }

  /**
   * Get minimum cargo depth.
   *
   * @return the minimumCargoDepth
   */
  public final double getMinimumCargoDepth() {
    return minimumCargoDepth;
  }

  /**
   * Set minimum cargo depth.
   *
   * @param minimumCargoDepth
   *          the minimumCargoDepth to set
   */
  public final void setMinimumCargoDepth(double minimumCargoDepth) {
    this.minimumCargoDepth = minimumCargoDepth;
  }

  /**
   * Get minimum cargo weight.
   *
   * @return the minimumCargoWeight
   */
  public final double getMinimumCargoWeight() {
    return minimumCargoWeight;
  }

  /**
   * Set minimum cargo weight.
   *
   * @param minimumCargoWeight
   *          the minimumCargoWeight to set
   */
  public final void setMinimumCargoWeight(double minimumCargoWeight) {
    this.minimumCargoWeight = minimumCargoWeight;
  }

  /**
   * Get departure location.
   *
   * @return the departureLocation
   */
  public GpsCoordinates getDepartureLocation() {
    return departureLocation;
  }

  /**
   * Set departure location.
   *
   * @param departureLocation
   *          the departureLocation to set
   */
  public void setDepartureLocation(GpsCoordinates departureLocation) {
    this.departureLocation = departureLocation;
  }

  /**
   * Get the route.
   *
   * @return the myRoute
   */
  public Route getPlan() {
    return plan;
  }

  /**
   * Set the route.
   *
   * @param plan
   *          the route the myRoute to set
   */
  public void setPlan(Route plan) {
    this.plan = plan;
  }

  /**
   * Read RFP from a file.
   *
   * @param filename
   *          the name of the file containing the RFP.
   */
  public RequestForProposals readRfp(final String filename) {
    final RequestForProposals result = new RequestForProposals();
    final JSONParser parser = new JSONParser();
    try (FileReader input = new FileReader(filename)) {
      // Parse the file contents
      final JSONObject parsedInput = (JSONObject) parser.parse(input);

      missionId = (String) parsedInput.get("missionId");
      customer = (String) parsedInput.get("customer");
      address = new PostalAddress((JSONObject) parsedInput.get("address"));
      proposalDate.setTimeInMillis(((Long) parsedInput.get("proposalDate")).longValue());
      departureLocation = new GpsCoordinates((JSONObject) parsedInput.get("departureLocation"));
      earliestDepartureDate
          .setTimeInMillis(((Long) parsedInput.get("earliestDeparture")).longValue());
      latestDepartureDate.setTimeInMillis(((Long) parsedInput.get("latestDeparture")).longValue());
      latestArrivalDate.setTimeInMillis(((Long) parsedInput.get("latestArrival")).longValue());
      minimumCargoDepth = ((Double) parsedInput.get("minimumCargoDepth")).doubleValue();
      minimumCargoWidth = ((Double) parsedInput.get("minimumCargoWidth")).doubleValue();
      minimumCargoHeight = ((Double) parsedInput.get("minimumCargoHeight")).doubleValue();
      plan = new RouteImpl((JSONObject) parsedInput.get("myRoute"));

    } catch (IOException | ParseException err) {
      log.error(err);
    }

    return result;
  }

  /**
   * Write RFP to a file.
   *
   * @param filename
   *          the name of the file to write.
   */
  public void writeRfp(final String filename) {
    try (FileWriter output = new FileWriter(filename)) {
      final JSONObject thisRfp = this.toJson();
      output.write(thisRfp.toJSONString());
    } catch (IOException err) {
      log.error(err);
    }
  }

  /**
   * Create a JSON object representing this RFP.
   *
   * @return the JSON Object
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJson() {
    final JSONObject result = new JSONObject();

    result.put("missionId", missionId);
    result.put("customer", customer);
    result.put("address", address.toJson());
    result.put("proposalDate", Long.valueOf(proposalDate.getTimeInMillis()));
    result.put("departureLocation", departureLocation.toJson());
    result.put("earliestDeparture", Long.valueOf(earliestDepartureDate.getTimeInMillis()));
    result.put("latestDeparture", Long.valueOf(latestDepartureDate.getTimeInMillis()));
    result.put("latestArrival", Long.valueOf(latestArrivalDate.getTimeInMillis()));
    result.put("minimumCargoheight", Double.valueOf(minimumCargoHeight));
    result.put("minimumCargoWidth", Double.valueOf(minimumCargoWidth));
    result.put("minimumCargoDepth", Double.valueOf(minimumCargoDepth));
    result.put("myRoute", plan.toJson());

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.synadek.core.Component#connect(boolean)
   */
  @Override
  public boolean connect(boolean sim) throws ComponentException {
    return true;
  }

}
