package com.example.habittracker.activities.tracking;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.utils.DateConverter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is for creating and displaying a graph for the progress of a habit
 */
public class GraphUtil {

    /**
     * Adds a habit progress to the graph
     * @param graph {GraphView}
     * @param habit {Habit}
     * @param habitEvents {Arraylist<HabitEvent>}
     * @param idealPerDay {int}
     * @param dataTitle {String}
     * @param dataColor {int}
     * @param autoAxisScale {boolean}
     */
    static public void addHabitToGraph(GraphView graph,Habit habit,ArrayList<HabitEvent> habitEvents,int idealPerDay,String dataTitle,int dataColor,boolean autoAxisScale){
         LineGraphSeries<DataPoint> series = getSeries(habit,habitEvents);
         LineGraphSeries<DataPoint> seriesIdeal = getTargetSeries(habit,idealPerDay);
         seriesIdeal.setTitle("Ideal");
         seriesIdeal.setColor(Color.GREEN);
         series.setTitle(dataTitle);
         series.setColor(dataColor);
         addSeriesToGraph(graph,series);
         addSeriesToGraph(graph,seriesIdeal);
         if(autoAxisScale){
             setXAxisScale(graph,series.getLowestValueX(),series.getHighestValueX());
             setYAxisScale(graph,Math.min(0,series.getLowestValueY()),Math.max(idealPerDay,series.getHighestValueY()));
         }
    }

    /**
     * returns series of datapoints for a target
     * @param habit {Habit}
     * @param idealPerDay {int}
     * @return {LineGraphSeries<DataPoint>}
     */
    static private LineGraphSeries<DataPoint> getTargetSeries(Habit habit, int idealPerDay){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Date habitStart = habit.getStartDate();

        ArrayList<Integer> daysOfHabit = ProgressUtil.getHabitDays(habit);

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar habitTime = Calendar.getInstance();
        habitTime.setTime(habitStart);
        end.setTime(start.getTime());
        start.add(Calendar.MONTH,-1);
        //check if habit start is within last month, otherwise set start to a month ago
        if(!habitStart.before(start.getTime())){
            start.set(habitTime.get(Calendar.YEAR),habitTime.get(Calendar.MONTH),habitTime.get(Calendar.DAY_OF_MONTH));
        }


        int ideal; // ideal num of events for that day
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            //
            if(!daysOfHabit.contains(start.get(Calendar.DAY_OF_WEEK))){
                ideal = 0;
            }
            else{
                ideal = idealPerDay;
            }
            series.appendData(new DataPoint(date,ideal),true,31);
        }
        return series;
    }

    /**
     * reutrns a series of datapoint based on habit and eventlist of habit
     * @param habit {Habit}
     * @param habitEvents {ArrayList<HabitEvent>}
     * @return {LineGraphSeries<DataPoint>}
     */
    static public LineGraphSeries<DataPoint> getSeries(Habit habit, ArrayList<HabitEvent> habitEvents){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        Date habitStart = habit.getStartDate();

        ArrayList<Integer> daysOfHabit = ProgressUtil.getHabitDays(habit);

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.setTime(start.getTime());
        start.add(Calendar.MONTH,-1);
        //check if habit start is within last month, otherwise set start to a month ago
        if(habitStart.before(start.getTime())){
            habitStart = start.getTime();
        }
        start.setTime(habitStart);

        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            int numOnDay = 0;
            for (int i = 0 ; i < habitEvents.size(); i++){
                Calendar first = Calendar.getInstance();
                Calendar sec = Calendar.getInstance();
                first.setTime(DateConverter.arrayListToDate(habitEvents.get(i).getStartDate()));
                sec.setTime(date);
                boolean sameDay = first.get(Calendar.DAY_OF_YEAR) == sec.get(Calendar.DAY_OF_YEAR) && first.get(Calendar.YEAR) == sec.get(Calendar.YEAR);
                if(sameDay){
                    numOnDay++;
                }
            }
            series.appendData(new DataPoint(date,numOnDay),true,31);
        }
        return series;
    }

    /**
     * sets the scale of a graph's x axis
     * @param graph {GraphView}
     * @param minX {double}
     * @param maxX {double}
     */
    static public void setXAxisScale(GraphView graph, double minX, double maxX){
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    /**
     * set the scale of the y axis of a graph
     * @param graph {GraphView}
     * @param minY {double}
     * @param maxY {double}
     */
    static public void setYAxisScale(GraphView graph, double minY, double maxY){
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    /**
     * add a legend using a background color
     * @param graph {GraphView}
     * @param backgroundColor {int}
     */
    static public void addSimpleLegend(GraphView graph, int backgroundColor){
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(backgroundColor);
    }

    /**
     * adds a seris list to a graph
     * @param graph {GraphView}
     * @param series {LineGraphSeries}
     */
    static public void addSeriesToGraph(GraphView graph, LineGraphSeries<DataPoint> series){
        graph.addSeries(series);
    }

    /**
     * sets the x axis type to date type
     * @param graph {GraphView}
     * @param context {Context}
     * @param numberOfLabels {int}
     */
    static public void setDateAsXAxis(GraphView graph, Context context, int numberOfLabels){
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    /**
     * sets the labels of the X and Y axis
     * @param graph {GraphView}
     * @param x {String}
     * @param y {String}
     */
    static public void setLabelAxis(GraphView graph,String x,String y){
         graph.getGridLabelRenderer().setHorizontalAxisTitle(y);
         graph.getGridLabelRenderer().setVerticalAxisTitle(x);
    }
}
