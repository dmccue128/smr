/**
 * GnssReceiver.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.GpsCoordinates;

/**
 * GPS/GNSS receiver.
 */
public interface GnssReceiver extends VesselComponent {

  /**
   * Get location.
   *
   * @return GPS location
   */
  public GpsCoordinates getLocation();

}
