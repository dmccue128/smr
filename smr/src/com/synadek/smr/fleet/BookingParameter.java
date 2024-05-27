/**
 * BookingParameter.java
 * 3 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.fleet;

import com.synadek.core.GPSCoordinates;

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
  private GPSCoordinates currentLocation;

  /**
   * Default constructor.
   */
  public BookingParameter() {

  }

  /**
   * @return the bookedDates
   */
  public final Set<Date> getBookedDates() {
    return bookedDates;
  }

  /**
   * @param bookedDates
   *          the bookedDates to set
   */
  public final void setBookedDates(Set<Date> bookedDates) {
    this.bookedDates = bookedDates;
  }

  /**
   * @return the cruisingSpeed in knots
   */
  public final double getCruisingSpeed() {
    return cruisingSpeed;
  }

  /**
   * @param cruisingSpeed
   *          the cruisingSpeed in knots
   */
  public final void setCruisingSpeed(double cruisingSpeed) {
    this.cruisingSpeed = cruisingSpeed;
  }

  /**
   * @return the cargoWidth
   */
  public final double getCargoWidth() {
    return cargoWidth;
  }

  /**
   * @param cargoWidth
   *          the cargoWidth to set
   */
  public final void setCargoWidth(double cargoWidth) {
    this.cargoWidth = cargoWidth;
  }

  /**
   * @return the cargoHeight
   */
  public final double getCargoHeight() {
    return cargoHeight;
  }

  /**
   * @param cargoHeight
   *          the cargoHeight to set
   */
  public final void setCargoHeight(double cargoHeight) {
    this.cargoHeight = cargoHeight;
  }

  /**
   * @return the cargoDepth
   */
  public final double getCargoDepth() {
    return cargoDepth;
  }

  /**
   * @param cargoDepth
   *          the cargoDepth to set
   */
  public final void setCargoDepth(double cargoDepth) {
    this.cargoDepth = cargoDepth;
  }

  /**
   * @return the maximumCargoWeight
   */
  public final double getMaximumCargoWeight() {
    return maximumCargoWeight;
  }

  /**
   * @param maximumCargoWeight
   *          the maximumCargoWeight to set
   */
  public final void setMaximumCargoWeight(double maximumCargoWeight) {
    this.maximumCargoWeight = maximumCargoWeight;
  }

  /**
   * @return the currentLocation
   */
  public GPSCoordinates getCurrentLocation() {
    return currentLocation;
  }

  /**
   * @param currentLocation
   *          the currentLocation to set
   */
  public void setCurrentLocation(GPSCoordinates currentLocation) {
    this.currentLocation = currentLocation;
  }

}
