/**
 * VesselAttitude.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Report linear and rotational motion of the vessel.
 */
public interface Attitude extends VesselComponent {

  /*
   * @formatter:off
   * 
   * Six dimensions of movement (three linear and three rotational) are recognized:
   * 
   * 1) Heave - up/down motion of the vessel
   * 2) Sway - side-to-side motion from water and wind currents, the ship's propulsion, or inertia.
   * 3) Surge - front/back motion due to wind/water conditions
   *
   * 4) Pitch - up/down rotation about the lateral axis. 
   *            An offset from normal on this axis is referred to as 'trim' or 'out of trim'.
   * 5) Roll -  tilting rotation about the longitudinal axis. 
   *            Unexpected offset from normal on this axis is referred to as list. 
   * 6) Yaw -   turning rotation about the vertical/Z axis. 
   *            Unexpected offset from normal on this axis is referred to as deviation.
   * 
   * @formatter:on
   */

  /**
   * getHeave returns the heave or up/down motion of the vessel.
   *
   * @return the Heave
   */
  int getHeave();

  /**
   * getSway returns the sway or side-to-side motion of the vessel.
   *
   * @return the Sway
   */
  int getSway();

  /**
   * getSurge returns the surge or front/back motion of the vessel.
   *
   * @return the Surge
   */
  int getSurge();

  /**
   * getPitch returns the pitch or up/down rotation about the latitudinal axis
   * i.e., bow-down/stern-up or vice versa.
   *
   * @return the pitch
   */
  int getPitch();

  /**
   * getRoll returns the tilting rotation about the longitudinal axis i.e.,
   * tilting to port or to starboard
   *
   * @return the Roll
   */
  int getRoll();

  /**
   * getYaw returns the turning rotation about the vertical axis i.e.,
   * bow-left/stern-right or vice versa.
   *
   * @return the Yaw
   */
  int getYaw();
}
