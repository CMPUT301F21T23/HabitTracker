package com.example.habittracker.activities.tracking;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphUtil {



    static public void addHabitToGraph(GraphView graph,Habit habit,ArrayList<HabitEvent> habitEvents,int idealPerDay,String dataTitle,int dataColor,boolean autoXAxisScale){
         LineGraphSeries<DataPoint> series = getSeries(habit,habitEvents);
         LineGraphSeries<DataPoint> seriesIdeal = getTargetSeries(habit,idealPerDay);
         seriesIdeal.setTitle("Ideal");
         seriesIdeal.setColor(Color.GREEN);
         series.setTitle(dataTitle);
         series.setColor(dataColor);
         addSeriesToGraph(graph,series);
         addSeriesToGraph(graph,seriesIdeal);
         if(autoXAxisScale){
             setXAxisScale(graph,series.getLowestValueX(),series.getHighestValueX());
         }
    }

    static private LineGraphSeries<DataPoint> getTargetSeries(Habit habit, int idealPerDay){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Date habitStart = habit.getStartDate();

        ArrayList<Integer> daysOfHabit = DaysUtil.getHabitDays(habit);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH,-1);
        //check if habit start is within last month, otherwise set start to a month ago
        if(habitStart.before(start.getTime())){
            habitStart = start.getTime();
        }
        start.setTime(habitStart);
        Calendar end = Calendar.getInstance();
        int ideal; // ideal num of events for that day
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            //
            if(!daysOfHabit.contains(start.get(Calendar.DAY_OF_WEEK))){
                ideal = 0;
            }
            else{
                ideal = idealPerDay;
            }
            series.appendData(new DataPoint(date,ideal),true,30);
        }
        return series;
    }

    static public LineGraphSeries<DataPoint> getSeries(Habit habit, ArrayList<HabitEvent> habitEvents){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        Date habitStart = habit.getStartDate();

        ArrayList<Integer> daysOfHabit = DaysUtil.getHabitDays(habit);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH,-1);
        //check if habit start is within last month, otherwise set start to a month ago
        if(habitStart.before(start.getTime())){
            habitStart = start.getTime();
        }
        start.setTime(habitStart);
        Calendar end = Calendar.getInstance();

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            int numOnDay = 0;
            for (int i = 0 ; i < habitEvents.size(); i++){
                Calendar first = Calendar.getInstance();
                Calendar sec = Calendar.getInstance();
                first.setTime(habitEvents.get(i).getStartDate());
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

    static public void setXAxisScale(GraphView graph, double minX, double maxX){
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    static public void setYAxisScale(GraphView graph, double minY, double maxY){
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    static public void addSimpleLegend(GraphView graph, int backgroundColor){
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(backgroundColor);
    }

    static public void addSeriesToGraph(GraphView graph, LineGraphSeries<DataPoint> series){
        graph.addSeries(series);
    }

    static public void setDateAsXAxis(GraphView graph, Context context, int numberOfLabels){
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    static public void setLabelAxis(GraphView graph,String x,String y){
         graph.getGridLabelRenderer().setHorizontalAxisTitle(y);
         graph.getGridLabelRenderer().setVerticalAxisTitle(x);
    }
}
