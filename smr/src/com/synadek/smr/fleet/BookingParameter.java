/**
 * BookingParameter.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.fleet;

import com.synadek.core.GpsCoordinates;
import java.util.Date;
import java.util.Set;

/**
 * Data about a vessel that is used to determine its suitability for booking.
 */
public class BookingParameter {

  /**
   * A list of dates when the vessel is unavailable i.e., booked.
   */
  private Set<Date> bookedDates;

  /**
   * Cruising speed of the vessel in knots.
   */
  private double cruisingSpeed;
  /**
   * Width of cargo area (meters).
   */
  private double cargoWidth;
  /**
   * Height of cargo area (meters).
   */
  private double cargoHeight;
  /**
   * Depth of cargo area (meters).
   */
  private double cargoDepth;

  /**
   * Maximum acceptable weight (kilograms) of cargo area.
   */
  private double maximumCargoWeight;

  /**
   * Current location of vessel.
   */
  private GpsCoordinates currentLocation;

  /**
   * Default constructor.
   */
  public BookingParameter() {

  }

  /**
   * Get the booked dates.
   *
   * @return the bookedDates
   */
  public final Set<Date> getBookedDates() {
    return bookedDates;
  }

  /**
   * Set the booked dates.
   *
   * @param bookedDates
   *          the bookedDates to set
   */
  public final void setBookedDates(Set<Date> bookedDates) {
    this.bookedDates = bookedDates;
  }

  /**
   * Get the vessel's cruising speed.
   *
   * @return the cruisingSpeed in knots
   */
  public final double getCruisingSpeed() {
    return cruisingSpeed;
  }

  /**
   * Set the vessel's cruising speed.
   *
   * @param cruisingSpeed
   *          the cruisingSpeed in knots
   */
  public final void setCruisingSpeed(double cruisingSpeed) {
    this.cruisingSpeed = cruisingSpeed;
  }

  /**
   * Get the cargo width.
   *
   * @return the cargoWidth
   */
  public final double getCargoWidth() {
    return cargoWidth;
  }

  /**
   * Set the cargo width.
   *
   * @param cargoWidth
   *          the cargoWidth to set
   */
  public final void setCargoWidth(double cargoWidth) {
    this.cargoWidth = cargoWidth;
  }

  /**
   * Get the cargo height.
   *
   * @return the cargoHeight
   */
  public final double getCargoHeight() {
    return cargoHeight;
  }

  /**
   * Set the cargo height.
   *
   * @param cargoHeight
   *          the cargoHeight to set
   */
  public final void setCargoHeight(double cargoHeight) {
    this.cargoHeight = cargoHeight;
  }

  /**
   * Get the cargo depth.
   *
   * @return the cargoDepth
   */
  public final double getCargoDepth() {
    return cargoDepth;
  }

  /**
   * Set the cargo depth.
   *
   * @param cargoDepth
   *          the cargoDepth to set
   */
  public final void setCargoDepth(double cargoDepth) {
    this.cargoDepth = cargoDepth;
  }

  /**
   * Get the cargo weight.
   *
   * @return the maximumCargoWeight
   */
  public final double getMaximumCargoWeight() {
    return maximumCargoWeight;
  }

  /**
   * Set the cargo maximum weight.
   *
   * @param maximumCargoWeight
   *          the maximumCargoWeight to set
   */
  public final void setMaximumCargoWeight(double maximumCargoWeight) {
    this.maximumCargoWeight = maximumCargoWeight;
  }

  /**
   * Get the current location.
   *
   * @return the currentLocation
   */
  public GpsCoordinates getCurrentLocation() {
    return currentLocation;
  }

  /**
   * Set the current location.
   *
   * @param currentLocation
   *          the currentLocation to set
   */
  public void setCurrentLocation(GpsCoordinates currentLocation) {
    this.currentLocation = currentLocation;
  }

}
