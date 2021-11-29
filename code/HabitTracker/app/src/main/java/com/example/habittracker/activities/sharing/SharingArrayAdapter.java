package com.example.habittracker.activities.sharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.Habit;
import com.example.habittracker.R;
import com.example.habittracker.utils.CustomHabitList;

import java.util.ArrayList;

/**
 * SharingArrayAdapter is used to contain and show a list of Habits along with their progress.
 */
public class SharingArrayAdapter extends CustomHabitList {
    private Context context;
    private ArrayList<Habit> habitsList;
    private String TAG = "SharingArrayAdapter";

    /**
     * Constructs SharingArrayAdapter.
     * @param context       {@code Context} Global information about the application environment
     * @param habitsList    {@code ArrayList<Habit>} List of habits that the current user should see
     */
    public SharingArrayAdapter(@NonNull Context context, ArrayList<Habit> habitsList) {
        // need to call super
        super(context, habitsList);
        this.context = context;
        this.habitsList = habitsList;
    }

    /**
     * Overrides the getView function to customize the list view content.
     * @param position      {@code int} Index of data in the data list
     * @param convertView   {@code View} View object
     * @param parent
     * @return              {@code View} Modified view object
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view for a Habit in the SharingList
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sharing_list_view_content, parent, false);
        }

        // get the View objects
        TextView habitTitle = convertView.findViewById(R.id.habitTitle);
        TextView userid = convertView.findViewById(R.id.userid);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        TextView progressValue = convertView.findViewById(R.id.progressValue);

        // update the View objects
        Habit habit = habitsList.get(position);
        habitTitle.setText(habit.getTitle());
        if (habit.getUser() != null) {
            userid.setText(habit.getUser().getUsername());
        }
        // update the progress bar
        Integer progress = habit.getProgress();
        progressBar.setProgress(progress);
        progressValue.setText(progress.toString() + "%");
        return convertView;
    }
}
