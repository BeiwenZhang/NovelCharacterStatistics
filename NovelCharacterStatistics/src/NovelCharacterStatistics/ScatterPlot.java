package NovelCharacterStatistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ScatterPlot extends JFrame {

    public ScatterPlot(String title, Map<String, List<Integer>> characterPositions) {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset(characterPositions);

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "人物出现位置图",
                "Character",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize chart
        chart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(173, 216, 230)); // Light Blue

        // Create Panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        // Display character names below the chart
        JLabel characterNamesLabel = new JLabel(getCharacterNames(characterPositions));
        characterNamesLabel.setHorizontalAlignment(JLabel.CENTER);
        add(characterNamesLabel, BorderLayout.SOUTH);

        // Set up frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private XYDataset createDataset(Map<String, List<Integer>> characterPositions) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Character Data");

        int xCoordinate = 1; // Start with x-coordinate 1 for the first character

        for (Map.Entry<String, List<Integer>> entry : characterPositions.entrySet()) {
            String character = entry.getKey();
            List<Integer> values = entry.getValue();

            // Add the character's data to the series
            for (Integer value : values) {
                series.add(xCoordinate, value);
            }

            // Move to the next x-coordinate for the next character
            xCoordinate++;
        }

        dataset.addSeries(series);

        return dataset;
    }

    private String getCharacterNames(Map<String, List<Integer>> characterPositions) {
        StringBuilder characterNames = new StringBuilder("Character Names: ");
        for (String character : characterPositions.keySet()) {
            characterNames.append(character).append(" ");
        }
        return characterNames.toString();
    }

}