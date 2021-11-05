package com.example.habittracker.utils;

import com.example.habittracker.Habit;
import com.example.habittracker.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomHabitList extends ArrayAdapter<Habit> {
    private final Context context;
    private final ArrayList<Habit> habitList;

    /**
     * The constructor for this custom list containing Habits
     * @param context   {Context}           the application context
     * @param habitList {ArrayList<Habit>}  the list of habits to adapt
     */
    public CustomHabitList(Context context, ArrayList<Habit> habitList) {
        super(context,0, habitList);
        this.context = context;
        this.habitList = habitList;
    }

    /**
     * For each item in the list, displays the correct information.
     * @param position - the index of the item in the list
     * @param convertView - the view
     * @param parent - the view group of the view
     * @return the new view to display
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if ( view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_content, parent, false);
        }

        Habit habit = habitList.get(position);

        // get the views
        TextView habitTitle = view.findViewById(R.id.habitTitle);
        TextView habitReason = view.findViewById(R.id.habitReason);

        // modify the text the views display so it's up to date
        habitTitle.setText(habit.getTitleDisplay());
        habitReason.setText(habit.getReason());

        return (view);
    }
}
