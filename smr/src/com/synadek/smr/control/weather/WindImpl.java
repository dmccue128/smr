/**
 * WindImpl.java
 * 28 May 2024
 *
 * @author Daniel McCue
 */

package com.synadek.smr.control.weather;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implement interface to wind measuring instruments.
 */
public class WindImpl implements Wind {

  /**
   * Establish a default size of the sample set over which wind measurements
   * will be averaged.
   */
  private static final int DEFAULT_SAMPLE_SIZE = 10;

  /**
   * Define the control variable for heading sample size and initialize to the
   * default.
   */
  private int maxHeadingSamples = DEFAULT_SAMPLE_SIZE;

  /**
   * Define the control variable for speed sample size and initialize to the
   * default.
   */
  private int maxSpeedSamples = DEFAULT_SAMPLE_SIZE;
  /**
   * Keep a list of recent wind direction data.
   */
  private final List<WindDirectionSample> wdSamples = new LinkedList<>();

  /**
   * Keep a list of recent wind speed data.
   */
  private final List<WindSpeedSample> wsSamples = new LinkedList<>();

  /**
   * Average wind direction (degrees) over the sample period.
   */
  private double headingAvg;

  /**
   * Standard deviation of wind direction over the sample period.
   */
  private double headingSd;

  /**
   * Average wind speed (m/s) over the sample period.
   */
  private double speedAvg;

  /**
   * Standard deviation of wind speed over the sample period.
   */
  private double speedSd;

  /**
   * Desired period over which to collect and average samples for wind
   * measurements.
   */
  private int sampleTimeSeconds;

  /**
   * Track average sample arrival interval for wind headings in milliseconds.
   */
  private long headingIntersampleTime = 1000;

  /**
   * Track average sample arrival interval for wind speed milliseconds.
   */
  private long speedIntersampleTime = 1000;

  /**
   * Acquire a reference to the application logger.
   */
  private static Logger log = LogManager.getLogger(WindImpl.class.getPackage().getName());

  /**
   * Default constructor.
   */
  public WindImpl() {
    /*
     * No initialization required.
     */
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindDirectionAvg()
   */
  @Override
  public double getWindDirectionAvg() {
    return headingAvg;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindDirectionStdDev()
   */
  @Override
  public double getWindDirectionStdDev() {
    return headingSd;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSampleTime()
   */
  @Override
  public int getWindSampleTime() {
    return sampleTimeSeconds;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#setWindDirectionSampleTime(int)
   */
  @Override
  public void setWindSampleTime(int samplePeriodSeconds) {
    // Error check inputs
    if (samplePeriodSeconds < 1) {
      log.error("Invalid sample period: " + samplePeriodSeconds + "-- ignored");
      return;
    }
    sampleTimeSeconds = samplePeriodSeconds;
    final long desiredSampleTimeMillis = samplePeriodSeconds * 1000;
    maxHeadingSamples = (int) Math
        .round(desiredSampleTimeMillis / (double) headingIntersampleTime);
    maxSpeedSamples = (int) Math.round(desiredSampleTimeMillis / (double) speedIntersampleTime);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSpeedAvg()
   */
  @Override
  public double getWindSpeedAvg() {
    return speedAvg;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSpeedStdDev()
   */
  @Override
  public double getWindSpeedStdDev() {
    return speedSd;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSpeedBeaufort()
   */
  @Override
  public int getWindSpeedBeaufort() {
    return Beaufort.index(this.speedAvg);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSpeedLabel()
   */
  @Override
  public String getWindSpeedLabel(final Locale locale) {
    final int beaufortNumber = Beaufort.index(this.speedAvg);
    return Beaufort.label(beaufortNumber, locale);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.smr.control.weather.Wind#getWindSpeedDescription()
   */
  @Override
  public String getWindSpeedDescription(final Locale locale) {
    final int beaufortNumber = Beaufort.index(this.speedAvg);
    return Beaufort.description(beaufortNumber, locale);
  }

  /**
   * Capture a new sample and update average direction as appropriate.
   *
   * @param heading
   *          is the current direction heading in degrees
   */
  public void addDirectionSample(int heading) {
    WindDirectionSample newSample = new WindDirectionSample(heading);

    // Add the new sample the the head of the list
    wdSamples.add(newSample);

    // Keep list to the desired size
    if (wdSamples.size() > maxHeadingSamples) {
      wdSamples.remove(wdSamples.size() - 1);
    }

    // Note the size of the sample set after any necessary adjustment
    final int sampleCount = wdSamples.size();

    // Update calculated average and standard deviation for wind direction
    calculateAvgDirection();

    // If enough samples have been collected, update the average intersample
    // arrival rate
    if (sampleCount > 1) {
      final long tsLatest = wdSamples.get(0).getTimestamp();
      final long tsOldest = wdSamples.get(sampleCount - 1).getTimestamp();
      headingIntersampleTime = (tsLatest - tsOldest) / sampleCount;
    }
  }

  /**
   * Capture a new sample and update average speed as appropriate.
   *
   * @param speed
   *          is the current speed in meters per second
   */
  public void addSpeedSample(double speed) {
    WindSpeedSample newSample = new WindSpeedSample(speed);

    // Add the new sample the the head of the list
    wsSamples.add(newSample);

    // Keep list to the desired size
    if (wsSamples.size() > maxSpeedSamples) {
      wsSamples.remove(wsSamples.size() - 1);
    }

    // Note the size of the sample set after any necessary adjustment
    final int sampleCount = wsSamples.size();

    // Update calculated average and standard deviation for wind speed
    calculateAvgSpeed();

    // If enough samples have been collected, update the average intersample
    // arrival rate
    if (sampleCount > 1) {
      final long tsLatest = wsSamples.get(0).getTimestamp();
      final long tsOldest = wsSamples.get(sampleCount - 1).getTimestamp();
      speedIntersampleTime = (tsLatest - tsOldest) / sampleCount;
    }
  }

  /**
   * Update average and standard deviation for wind speed. Per Wikipedia, this
   * algorithm is due to Knuth, who cites Welford.
   */
  private final void calculateAvgSpeed() {

    int count = 0;
    double mean = 0.0;
    double mean2 = 0.0;
    double sum = 0.0;

    for (WindSpeedSample sample : wsSamples) {
      final double speed = sample.getSpeed();
      count += 1;
      sum += speed;
      double delta = speed - mean;
      mean += delta / count;
      mean2 += delta * (speed - mean);
    }

    this.speedAvg = sum / count;

    if (count < 2) {
      this.speedSd = Double.NaN;
    } else {
      this.speedSd = mean2 / (count - 1);
    }
  }

  /**
   * Update the average and standard deviation for wind direction from a set of
   * samples. Uses Yamartino method as described in Wikipedia. See
   * http://en.wikipedia.org/wiki/Yamartino_method
   */
  private void calculateAvgDirection() {
    // Compute updated average direction using Yamartino algorithm
    double ssSum = 0;
    double ccSum = 0;

    // Sum the sin and cos of the samples
    for (WindDirectionSample sample : wdSamples) {
      ssSum += sample.getSin();
      ccSum += sample.getCos();
    }

    double ccAlpha = ccSum / wdSamples.size();
    double ssAlpha = ssSum / wdSamples.size();

    // calculate the average heading
    double tangent = Math.atan2(ccAlpha, ssAlpha);
    if (tangent <= 0) {
      headingAvg = tangent + 2.0 * Math.PI;
    } else {
      headingAvg = tangent;
    }

    // calculate the standard deviation
    double ssAlphaSquared = ssAlpha * ssAlpha;
    double ccAlphaSquared = ccAlpha * ccAlpha;
    double epsilon = Math.sqrt(1.0 - (ssAlphaSquared + ccAlphaSquared));

    // See http://en.wikipedia.org/wiki/Yamartino_method
    if (Double.isNaN(epsilon)) {
      epsilon = 0.0;
    }

    headingSd = Math.asin(epsilon) * (1.0 + (2.0 / Math.sqrt(3.0) - 1.0) * Math.pow(epsilon, 3.0));
  }
}
