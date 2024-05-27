/**
 * GnssReceiver.java
 * 2 Dec 2017
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

import com.synadek.core.GPSCoordinates;

/**
 * 
 */
public interface GnssReceiver extends VesselComponent {

  /**
   * Get location.
   * 
   * @return GPS location
   */
  public GPSCoordinates getLocation();

}
