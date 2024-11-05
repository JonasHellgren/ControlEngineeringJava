package mpc.helpers;

import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.ArrayList;
import java.util.List;

public class Plotter {

    List<XYChart> charts;

    public Plotter() {
        this.charts = new ArrayList<>();
    }


    public void addPlot(double[] lineData, String title, Pair<Double, Double> yMinMax) {
        XYChart chart = new XYChartBuilder().xAxisTitle("Step").yAxisTitle(title).width(300).height(200).build();
        chart.getStyler().setYAxisMin(yMinMax.getFirst());
        chart.getStyler().setYAxisMax(yMinMax.getSecond());
        chart.getStyler().setLegendVisible(false);
        XYSeries series = chart.addSeries("input", null, lineData);
        series.setMarker(SeriesMarkers.NONE);
        charts.add(chart);
    }

    public void show() {
        new SwingWrapper<>(charts).displayChartMatrix();
    }

}
