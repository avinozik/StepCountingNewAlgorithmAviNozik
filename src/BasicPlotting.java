import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class BasicPlotting {
  public static double[][] sampleData;
  public static String datafile = "E:/SensorDataExplorer/data/Jogging.csv";

  public static void main(String[] args) {
    // Create data set
    CSVData dataset = CSVData.createDataSet(datafile, 0);

    // Get 2d array of all data
    sampleData = dataset.getAllData();

    double[] time = ArrayHelper.extractColumn(sampleData, 0);
    double[][] sensorData = ArrayHelper.extractColumns(sampleData,
        new int[] { 1, 2, 3, 4, 5, 6 });

    int steps = CountStepsBlank.countSteps(time, sensorData);
    System.out.println(steps);

    double[][] accel = ArrayHelper.extractColumns(sampleData,
        new int[] { 1, 2, 3 });
    double[] mags = CountStepsBlank.calculateMagnitudesFor(accel);

    System.out.println(CountStepsBlank.calculateMean(mags));
    System.out.println(CountStepsBlank.calculateStandardDeviation(mags,
        CountStepsBlank.calculateMean(mags)));

    Plot2DPanel plot = new Plot2DPanel();

    // add a line plot to the PlotPanel
    plot.addLinePlot("y = x + noise", mags);

    // put the PlotPanel in a JFrame, as a JPanel
    JFrame frame = new JFrame("Results");
    frame.setSize(800, 600);
    frame.setContentPane(plot);
    frame.setVisible(true);
  }

}
