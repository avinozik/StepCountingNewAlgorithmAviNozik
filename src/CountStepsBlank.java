import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class CountStepsBlank {

  public static final double COMPLETELY_IDENTICAL = 100;
  public static final double HUNDRED_PERCENT = 100;

  /***
   * Counts the number of steps based on sensor data.
   *
   * @param times
   *          a 1d-array with the elapsed times in miliseconds for each row in
   *          the sensorData array.
   *
   * @param sensorData
   *          a 2d-array where rows represent successive sensor data samples,
   *          and the columns represent different sensors. We assume there are 6
   *          columns. Columns 0 - 2 are data from the x, y, and z axes of an
   *          accelerometer, and 3-5 are data from the x, y, and z axes of a
   *          gyro.
   *
   * @return an int representing the number of steps
   */
  public static int countSteps(double[] times, double[][] sensorData) {
    double[] accData = new double[3];
    int stepCount = 0;

    // accelerometer data
    for (int row = 0; row < sensorData.length; row++) {
      for (int col = 1; col <= 3; col++) {
        accData = calculateMagnitudesFor(sensorData);
      }
    }

    double mean = calculateMean(accData);

    double accStandardDev = calculateStandardDeviation(accData, mean);

    for (int i = 1; i < accData.length - 1; i++) {
      if (accData[i] > accData[i - 1] && accData[i] > accData[i + 1]) {
        double threshold = accStandardDev + mean;
        System.out.println("standard dev: " + accStandardDev + " mean: " + mean
            + " threshold: " + threshold);
        if (accData[i] > threshold) {
          stepCount++;
        } else {// new algorithm

          double percentOfCloseness = (accData[i]) / (threshold);// how close it
                                                                 // is to the
                                                                 // threshold
          percentOfCloseness = percentOfCloseness * HUNDRED_PERCENT;// make it a
                                                                    // percent
          if (COMPLETELY_IDENTICAL - percentOfCloseness <= 5.9) {// is it close
            // enough
            stepCount++;
            threshold = threshold - (HUNDRED_PERCENT - percentOfCloseness);// adjust
                                                                           // the
            // threshold
          }
        }
      }
    }
    return stepCount;
  }

  /***
   * Calculate the magnitude for a vector with x, y, and z components.
   *
   * @param x
   *          the x component
   * @param y
   *          the y component
   * @param z
   *          the z component
   * @return the magnitude of the vector
   */
  public static double calculateMagnitude(double x, double y, double z) {
    return Math.sqrt(x * x + y * y + z * z);
  }

  /***
   * Takes a 2d array with 3 columns representing the 3 axes of a sensor.
   * Calculates the magnitude of the vector represented by each row. Returns a
   * new array with the same number of rows where each element contains this
   * magnitude.
   *
   * @param sensorData
   *          an array with n rows and 3 columns. Each row represents a
   *          different measurement, and each column represents a different axis
   *          of the sensor.
   *
   * @return an array with n rows and each element is the magnitude of the
   *         vector for the corresponding row in the sensorData array
   */
  public static double[] calculateMagnitudesFor(double[][] sensorData) {
    double[] mags = new double[sensorData.length];

    for (int r = 0; r < mags.length; r++) {
      mags[r] = calculateMagnitude(sensorData[r][0], sensorData[r][1],
          sensorData[r][2]);
    }

    return mags;
  }

  /***
   * Return the standard deviation of the data.
   *
   * @param arr
   *          the array of the data
   * @param mean
   *          the mean of the data (must be pre-calculated).
   * @return the standard deviation of the data.
   */
  public static double calculateStandardDeviation(double[] arr, double mean) {
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      double diff = Math.pow((arr[i] - mean), 2);
      sum += diff;
    }
    double num = sum / (arr.length - 1);
    return Math.sqrt(num);
  }

  /***
   * Return the mean of the data in the array
   *
   * @param arr
   *          the array of values
   * @return the mean of the data
   */
  public static double calculateMean(double[] arr) {
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    return sum / arr.length;
  }

  public static void displayJFrame(Plot2DPanel plot) {
    JFrame frame = new JFrame("Results");
    frame.setSize(800, 600);
    frame.setContentPane(plot);
    frame.setVisible(true);
  }
}
