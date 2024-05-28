/**
 * Anchor.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.vessel;

/**
 * Control of anchor.
 */
public interface Anchor extends VesselComponent {

  /**
   * Get the length of the anchor chain i.e., max depth anchor can be extended.
   *
   * @return the length in meters
   */
  float anchorChainLength();

  /**
   * Anchor extension (depth) in meters. Zero means anchor is fully retracted to
   * vessel.
   *
   * @return the depth in meters
   */
  float anchorDepth();

  /**
   * Raise anchor to fully retracted position.
   */
  void raiseAnchor();

  /**
   * Lower anchor to a specified depth.
   *
   * @param desiredDepth
   *          is the desired extension (depth) of the anchor in meters.
   */
  void lowerAnchor(float desiredDepth);
}
