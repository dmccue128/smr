/**
 * Calc.java
 * 5 Dec 2017
 * @author Daniel McCue
 */

package astro;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.synadek.core.InvalidValueException;
import com.synadek.core.Solar;

/**
 * 
 */
public class Calc {

	private static final double MINUTES_PER_DAY = 1440;

	public static void main(String args[]) throws InvalidValueException {

		double glat = 30.394913;
		double glong = 		-95.50320800000001;
		int year = 2022;
		int month = 5;
		int day = 28;
		int hour = 12;
		int minute = 7;
		int second = 0;
		int zone = -5;
		/*
		 * 
		 * // Read input parameters Scanner in = new Scanner(System.in);
		 * 
		 * print("   Rise and set for Sun and Moon");
		 * print("   =============================");
		 * print("   Year (yyyy) - - - - - - - - : "); year = in.nextInt();
		 * print("   Month  (mm) - - - - - - - - :"); month = in.nextInt();
		 * print("   Day    (dd) - - - - - - - - :"); day = in.nextInt();
		 * print("   Time zone (East +) - - - -  :"); zone = in.nextInt();
		 * print("   Longitude (w neg, decimals) :"); glong = in.nextDouble();
		 * print("   Latitude  (n pos, decimals) :"); glat = in.nextDouble();
		 * 
		 * in.close();
		 * 
		 * 
		 * double julianDay = computeJulianDay(year, month, day, zone, 0, 6, 0);
		 */
		double pctOfDay = (hour + minute / 60.0 + second / 3600.0) / 24.0;
		print("minutes past midnight=" + (pctOfDay * MINUTES_PER_DAY));

		final OffsetDateTime datetime = OffsetDateTime.of(year, month, day, hour, minute, second, 0,
				ZoneOffset.ofTotalSeconds(zone * 3600));

		double julianDay = Solar.computeJulianDay(datetime);
		print("offsetdatetime value=" + julianDay);
		displayResults2(datetime, zone, glong, glat);
	}

	private static void print(String str) {
		System.out.println(str);
	}

	/**
	 * Compute and display sunset, sunrise. Algorithm translated directly from
	 * NOAA_Solar_Calculations_days.xls.
	 * 
	 * @param zone  time zone (+ to E)
	 * @param glong longitude
	 * @param glat  latitude
	 * @throws InvalidValueException
	 */
	private static void displayResults2(OffsetDateTime datetime, int zone, double glong, double glat)
			throws InvalidValueException {

		double julianDay = Solar.computeJulianDay(datetime);

		print("----------------");
		print("Julian Day at 0600 local time: " + julianDay);

		double julianCentury = (julianDay - 2451545.0) / 36525.0;
		print("Julian Century: " + julianCentury);

		double geoMeanLongSunDegrees = (280.46646 + julianCentury * (36000.76983 + julianCentury * 0.0003032)) % 360.0;
		print("GeoMeanSunLongDegrees=" + geoMeanLongSunDegrees);

		double geoMeanAnomSun = 357.52911 + julianCentury * (35999.05029 - 0.0001537 * julianCentury);
		print("GeoMeanAnomSun=" + geoMeanAnomSun);

		double eccentEarthOrbit = 0.016708634 - julianCentury * (0.000042037 + 0.0000001267 * julianCentury);
		print("eccent Earth Orbit=" + eccentEarthOrbit);

		double sunEqOfCtr = Math.sin(Math.toRadians(geoMeanAnomSun))
				* (1.914602 - julianCentury * (0.004817 + 0.000014 * julianCentury))
				+ Math.sin(Math.toRadians(2 * geoMeanAnomSun)) * (0.019993 - 0.000101 * julianCentury)
				+ Math.sin(Math.toRadians(3 * geoMeanAnomSun)) * 0.000289;
		print("Sun eq of Ctr=" + sunEqOfCtr);

		double sunTrueLongDegrees = geoMeanLongSunDegrees + sunEqOfCtr;
		print("Sun true long degrees=" + sunTrueLongDegrees);

		double sunTrueAnomDegrees = geoMeanAnomSun + sunEqOfCtr;
		print("Sun true anom degrees=" + sunTrueAnomDegrees);

		double sunRadVectorAus = (1.000001018 * (1 - eccentEarthOrbit * eccentEarthOrbit))
				/ (1 + eccentEarthOrbit * Math.cos(Math.toRadians(sunTrueAnomDegrees)));
		print("Sun Rad Vector AUs=" + sunRadVectorAus);

		double sunAppLongDegrees = sunTrueLongDegrees - 0.00569
				- 0.00478 * Math.sin(Math.toRadians(125.04 - 1934.136 * julianCentury));
		print("sun app long degrees=" + sunAppLongDegrees);

		double meanObliqueEclipticDegrees = 23.0 + (26.0
				+ ((21.448 - julianCentury * (46.815 + julianCentury * (0.00059 - julianCentury * 0.001813)))) / 60.0)
				/ 60.0;
		print("mean oblique ecliptic degrees=" + meanObliqueEclipticDegrees);

		double obliqueCorrDegrees = meanObliqueEclipticDegrees
				+ 0.00256 * Math.cos(Math.toRadians(125.04 - 1934.136 * julianCentury));
		print("oblique corr degrees=" + obliqueCorrDegrees);

		double sunRtAscenDegrees = Math.toDegrees(
				Math.atan2(Math.cos(Math.toRadians(obliqueCorrDegrees)) * Math.sin(Math.toRadians(sunAppLongDegrees)),
						Math.cos(Math.toRadians(sunAppLongDegrees))));
		print("sun rt ascen degrees=" + sunRtAscenDegrees);

		double sunDeclinDegrees = Math.toDegrees(
				Math.asin(Math.sin(Math.toRadians(obliqueCorrDegrees)) * Math.sin(Math.toRadians(sunAppLongDegrees))));
		print("sun declin degrees=" + sunDeclinDegrees);
		print("sunlight sun declin=" + Solar.getSolarDeclination(datetime));

		double varY = Math.tan(Math.toRadians(obliqueCorrDegrees / 2.0))
				* Math.tan(Math.toRadians(obliqueCorrDegrees / 2.0));
		print("var Y=" + varY);

		double eqOfTimeMinutes = 4.0 * Math.toDegrees(varY * Math.sin(2 * Math.toRadians(geoMeanLongSunDegrees))
				- 2 * eccentEarthOrbit * Math.sin(Math.toRadians(geoMeanAnomSun))
				+ 4 * eccentEarthOrbit * varY * Math.sin(Math.toRadians(geoMeanAnomSun))
						* Math.cos(2 * Math.toRadians(geoMeanLongSunDegrees))
				- 0.5 * varY * varY * Math.sin(4 * Math.toRadians(geoMeanLongSunDegrees))
				- 1.25 * eccentEarthOrbit * eccentEarthOrbit * Math.sin(2 * Math.toRadians(geoMeanAnomSun)));
		print("eq of time minutes=" + eqOfTimeMinutes);
		print("sunlight eq of time=" + Solar.getEquationOfTime(datetime));

		double c1 = Math.toRadians(90.833);
		double cosC1 = Math.cos(c1);
		double latRadians = Math.toRadians(glat);
		double cosLat = Math.cos(latRadians);
		double declinRadians = Math.toRadians(sunDeclinDegrees);
		double cosDeclin = Math.cos(declinRadians);
		double tanLat = Math.tan(latRadians);
		double tanDeclin = Math.tan(declinRadians);
		double sunriseRadians = Math.acos(cosC1 / (cosLat * cosDeclin) - tanLat * tanDeclin);
		print("c1=" + c1 + ", cosC1=" + cosC1 + ", latRadians=" + latRadians + ", cosLat=" + cosLat + ", declinRadians="
				+ declinRadians + ", cosDeclin=" + cosDeclin + ", tanLat=" + tanLat + ", tanDeclin=" + tanDeclin);

		double haSunriseDegrees = Math.toDegrees(sunriseRadians);
		// double haSunriseDegrees =
		// Math.toDegrees(Math.acos(Math.cos(Math.toRadians(90.833))
		// / (Math.cos(Math.toRadians(glat)) *
		// Math.cos(Math.toRadians(sunDeclinDegrees)))
		// - Math.tan(Math.toRadians(glat)) *
		// Math.tan(Math.toRadians(sunDeclinDegrees))));
		print("HA sunrise degrees=" + haSunriseDegrees);

		double solarNoon = (720 - (4 * glong) - eqOfTimeMinutes + (zone * 60)) / MINUTES_PER_DAY;
		print("solar noon: " + solarNoon);
		print("sunlight solar noon=" + Solar.getSolarNoon(glong, datetime));

		double sunriseTime = solarNoon - haSunriseDegrees * 4.0 / MINUTES_PER_DAY;
		print("sunrise: " + sunriseTime);
		print("sunlight sunrise: " + Solar.getSunrise(glat, glong, datetime));

		double sunsetTime = solarNoon + haSunriseDegrees * 4.0 / MINUTES_PER_DAY;
		print("sunset: " + sunsetTime);
		print("sunlight sunset: " + Solar.getSunset(glat, glong, datetime));

		double sunlightDurationMinutes = 8.0 * haSunriseDegrees;
		print("sunlight duration (mins): " + sunlightDurationMinutes);
		double durationSeconds = Solar.getSunlightDuration(glat, datetime).getSeconds();
		print(String.format("Sunlight Duration: %02d:%02d:%02d or %f minutes", Math.round(durationSeconds / 3600),
				Math.round((durationSeconds % 3600) / 60), Math.round(durationSeconds % 60),
				Double.valueOf(durationSeconds / 60.0)));

		double pctOfDay = (julianDay % 86400.0) / (60.0 * 60.0 * 24.0);
		double minutesPastMidnight = pctOfDay * MINUTES_PER_DAY;
		double trueSolarTimeSubtotal = (minutesPastMidnight + eqOfTimeMinutes + (4.0 * glong) - (60.0 * zone));
		print("pctOfDay=" + pctOfDay + ", minPastMidnight=" + minutesPastMidnight + ", subtotal="
				+ trueSolarTimeSubtotal);
		double trueSolarTimeMinutes = (trueSolarTimeSubtotal + MINUTES_PER_DAY) % MINUTES_PER_DAY;
		print("true solar time (mins): " + trueSolarTimeMinutes);

		double hourAngleDegrees = trueSolarTimeMinutes / 4.0 < 0 ? trueSolarTimeMinutes / 4 + 180
				: trueSolarTimeMinutes / 4 - 180;
		print("hourAngleDegrees=" + hourAngleDegrees);

		double solarZenithAngleDegrees = Math
				.toDegrees(Math.acos(Math.sin(Math.toRadians(glat)) * Math.sin(Math.toRadians(sunDeclinDegrees))
						+ Math.cos(Math.toRadians(glat)) * Math.cos(Math.toRadians(sunDeclinDegrees))
								* Math.cos(Math.toRadians(hourAngleDegrees))));
		print("Solar zenith angle (degrees)=" + solarZenithAngleDegrees);

		double solarElevationAngleDegrees = 90.0 - solarZenithAngleDegrees;
		print("Solar Elevation Angle (degrees)=" + solarElevationAngleDegrees);
		print("sunlight solar elevation angle=" + Solar.getSolarElevation(glat, glong, datetime));

		//
		// =IF (AE111>85, 0,
		// ELSE IF(AE111>5,
		// 58.1/TAN(RADIANS(AE111))-0.07/POWER(TAN(RADIANS(AE111)),3)+0.000086/POWER(TAN(RADIANS(AE111)),5),
		// ELSE IF(AE111>-0.575,
		// 1735+AE111*(-518.2+AE111*(103.4+AE111*(-12.79+AE111*0.711))),
		// ELSE -20.772/TAN(RADIANS(AE111)))))/3600
		//
		double approxAtmosphericRefractionDegrees = 0.0;
		if (solarElevationAngleDegrees > 85.0) {
			approxAtmosphericRefractionDegrees = 0.0;
		} else if (solarElevationAngleDegrees > 5.0) {
			approxAtmosphericRefractionDegrees = 58.1 / Math.tan(Math.toRadians(solarElevationAngleDegrees))
					- 0.07 / Math.pow(Math.tan(Math.toRadians(solarElevationAngleDegrees)), 3.0)
					+ 0.000086 / Math.pow(Math.tan(Math.toRadians(solarElevationAngleDegrees)), 5.0);
		} else if (solarElevationAngleDegrees > -0.575) {
			approxAtmosphericRefractionDegrees = 1735.0
					+ solarElevationAngleDegrees * (-518.2 + solarElevationAngleDegrees
							* (103.4 + solarElevationAngleDegrees * (-12.79 + solarElevationAngleDegrees * 0.711)));
		} else {
			approxAtmosphericRefractionDegrees = -20.772 / Math.tan(Math.toRadians(solarElevationAngleDegrees));
		}
		approxAtmosphericRefractionDegrees = approxAtmosphericRefractionDegrees / 3600.0;
		print("Approx. Atmospheric Refraction (deg)=" + approxAtmosphericRefractionDegrees);

		double solarElevationCorrectedDegrees = solarElevationAngleDegrees + approxAtmosphericRefractionDegrees;
		print("Corrected solar elevation=" + solarElevationCorrectedDegrees);
		print("sunlight corrected solar elevation=" + Solar.getSolarElevationCorrected(glat, glong, datetime));

		double solarAzimuthAngle;
		double zenithRadians = Math.toRadians(solarZenithAngleDegrees);
		double sinZenith = Math.sin(zenithRadians);
		double cosZenith = Math.cos(zenithRadians);
		double sinDeclin = Math.sin(declinRadians);
		double sinLat = Math.sin(latRadians);

		double sinLcosZ = sinLat * cosZenith;
		double cosLsinZ = cosLat * sinZenith;

		print("zenithRadians=" + zenithRadians + ", sinZenith=" + sinZenith + ", cosZenith=" + cosZenith
				+ ", sinDeclin=" + sinDeclin + ", sinLat=" + sinLat + ", sinLcosZ=" + sinLcosZ + ", cosLsinZ="
				+ cosLsinZ);

		if (hourAngleDegrees > 0) {
			solarAzimuthAngle = (Math.toDegrees(Math.acos((sinLcosZ - sinDeclin) / cosLsinZ)) + 180.0) % 360.0;
		} else {
			solarAzimuthAngle = (540.0 - Math.toDegrees(Math.acos((sinLcosZ - sinDeclin) / cosLsinZ))) % 360.0;
		}
		print("Solar Azimuth Angle=" + solarAzimuthAngle);
		print("Solar value=" + Solar.getSolarAzimuth(glat, glong, datetime));
	}

}
