/**
 * Longitude.java
 *
 * @author Daniel
 */

package com.synadek.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A geographic coordinate that specifies the east-west position of a point on
 * the Earth's surface. An angular measurement, usually expressed in degrees and
 * denoted by the Greek letter lambda (λ). Meridians (lines running from the
 * North Pole to the South Pole) connect points with the same longitude. By
 * convention, one of these, the Prime Meridian, which passes through the Royal
 * Observatory, Greenwich, England, was allocated the position of zero degrees
 * longitude. The longitude of other places is measured as the angle east or
 * west from the Prime Meridian, ranging from 0° at the Prime Meridian to +180°
 * eastward and −180° westward.
 */
public class Longitude {

	public static final double LONGITUDE_PRIME_MERIDIAN = 0.0;

	/**
	 * Unicode character for degree symbol.
	 */
	private static String DEGREE_SYMBOL = "\u00B0";

	/**
	 * double,Minutes,Seconds (DMS) format for longitude. Example: 84�13'53.3"W
	 */
	private static Pattern DMS_LONGITUDE_PATTERN = Pattern.compile("([0-9]+)\u00B0 *([0-9]+)' *([0-9]+)\" *([EeWw])");

	/**
	 * double, Decimal Minutes (DDM) format for longitude. Example: -84 13.888333
	 */
	private static Pattern DDM_LONGITUDE_PATTERN = Pattern.compile("([-+]*[0-9]+) +([0-9]+.[0-9]+)");

	/**
	 * Parse a longitude in double, Decimal Minutes (DDM) format.
	 * 
	 * @param lon the longitude in DDM format, e.g., -84 13.888333
	 * @return the longitude
	 * @throws NumberFormatException if the string cannot be parsed
	 */
	public static Longitude parseDDM(final String lon) throws NumberFormatException {

		double result = 0.0;

		// Attempt to match the string against the DDM longitude pattern
		final Matcher ddmLonMatch = DDM_LONGITUDE_PATTERN.matcher(lon);

		if (ddmLonMatch.matches()) {
			final double degrees = Double.parseDouble(ddmLonMatch.group(1));
			final double minutes = Double.parseDouble(ddmLonMatch.group(2));
			final double seconds = Double.parseDouble(ddmLonMatch.group(3));
			final String direction = ddmLonMatch.group(4);
			result = degrees + (minutes * 60 + seconds) / 3600;
			if (direction.equalsIgnoreCase("S")) {
				result = -result;
			}
			return Longitude.fromDegrees(result);
		}

		throw new NumberFormatException(lon);
	}

	/**
	 * Parse a longitude in double,Minutes,Seconds (DMS) format.
	 * 
	 * @param lon the longitude in DMS format, e.g., 84�13'53.3"W
	 * @return the longitude
	 * @throws NumberFormatException if the string cannot be parse
	 */
	public static Longitude parseDMS(final String lon) throws NumberFormatException {

		double result = 0.0;

		// Attempt to match the string against the DMS lonitude pattern
		final Matcher dmsLonMatch = DMS_LONGITUDE_PATTERN.matcher(lon);

		if (dmsLonMatch.matches()) {
			final double degrees = Double.parseDouble(dmsLonMatch.group(1));
			final double minutes = Double.parseDouble(dmsLonMatch.group(2));
			final double seconds = Double.parseDouble(dmsLonMatch.group(3));
			final String direction = dmsLonMatch.group(4);
			result = degrees + (minutes * 60 + seconds) / 3600;
			if (direction.equalsIgnoreCase("W")) {
				result = -result;
			}
			return Longitude.fromDegrees(result);
		}

		throw new NumberFormatException(lon);
	}

	/**
	 * Longitude measurement in radians.
	 */
	double radianValue;

	/**
	 * Default constructor.
	 * 
	 * @param lon the longitude in degrees
	 */
	public static Longitude fromDegrees(final double lon) {
		return new Longitude(Math.toRadians(lon));
	}

	/**
	 * Default constructor.
	 * 
	 * @param lon the longitude in radians
	 */
	public Longitude(final double lon) {
		this.radianValue = lon;
	}

	/**
	 * Get the latitude in double, Decimal Minutes format. Example: 36 59.35333
	 * 
	 * @return the latitude
	 */
	public final String getDDM() {
		final double lon = Math.toDegrees(this.radianValue);
		final double degrees = Math.floor(lon);
		final double minutes = lon - degrees;
		return String.format("%g %8.5g", Double.valueOf(degrees), Double.valueOf(minutes));
	}

	/**
	 * Get longitude in double, Minutes, Seconds (DMS) format. Example: 84�13'53.3"W
	 * 
	 * @return the longitude
	 */
	public final String getDMS() {
		final double lon = Math.toDegrees(this.radianValue);
		final double degrees = Math.floor(lon);
		final double allSeconds = lon - degrees;
		final double minutes = Math.floor(allSeconds / 60.0);
		final double seconds = allSeconds % 60;
		final char ew = (lon >= 0.0) ? 'E' : 'W';
		return String.format("%g%s%g'%4.1g\"%c", Double.valueOf(degrees), DEGREE_SYMBOL, Double.valueOf(minutes),
				Double.valueOf(seconds), Character.valueOf(ew));
	}

	public final double degrees() {
		return Math.toDegrees(this.radianValue);
	}

	public final double radians() {
		return this.radianValue;
	}
}
