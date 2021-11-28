package com.example.habittracker.utils;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.R;
import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.fragments.HabitInputFragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomHabitList extends ArrayAdapter<Habit>{
    private final Context context;
    private final ArrayList<Habit> habitList;
    private ArrayList<Button> buttonList;
    private boolean keepOn;

    /**
     * The constructor for this custom list containing Habits
     * @param context   {Context}           the application context
     * @param habitList {ArrayList<Habit>}  the list of habits to adapt
     */
    public CustomHabitList(Context context, ArrayList<Habit> habitList) {
        super(context,0, habitList);
        this.context = context;
        this.habitList = habitList;
        this.buttonList = new ArrayList<>();
        this.keepOn = false;
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if ( view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_content, parent, false);
        }

        Habit habit = habitList.get(position);

        // get the views
        TextView habitTitle = view.findViewById(R.id.habitTitle);
        TextView habitReason = view.findViewById(R.id.habitReason);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView progressText = view.findViewById(R.id.progressValue);

        final Button moveUp = view.findViewById(R.id.move_habit_up);
        final Button moveDown = view.findViewById(R.id.move_habit_down);

        if(!keepOn){
            moveUp.setVisibility(View.GONE);
            moveDown.setVisibility(View.GONE);
        }

        if(!buttonList.contains(moveUp)){
            buttonList.add(moveUp);
        }
        if(!buttonList.contains(moveDown)){
            buttonList.add(moveDown);
        }

        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>0){
                    Habit h1 = habitList.get(position);
                    Habit h2 = habitList.get(position-1);
                    habitList.set(position-1,h1);
                    habitList.set(position,h2);
                    HashMap<String,Object> doc1 = h1.toDocument();
                    doc1.put("order",position-1);
                    HashMap<String,Object> doc2 = h2.toDocument();
                    doc2.put("order",position);
                    DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),h1.getTitle(),h1.getTitle(),doc1);
                    DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),h2.getTitle(),h2.getTitle(),doc2 );
                }
                notifyDataSetChanged();
            }
        });

        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position<habitList.size()-1){
                    Habit h1 = habitList.get(position);
                    Habit h2 = habitList.get(position+1);
                    habitList.set(position+1,h1);
                    habitList.set(position,h2);
                    HashMap<String,Object> doc1 = h1.toDocument();
                    doc1.put("order",position+1);
                    HashMap<String,Object> doc2 = h2.toDocument();
                    doc2.put("order",position);
                    DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),h1.getTitle(),h1.getTitle(),doc1);
                    DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),h2.getTitle(),h2.getTitle(),doc2 );
                }
                notifyDataSetChanged();
            }
        });

//        if(reorder != null){
//            reorder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final Button moveUp = view.findViewById(R.id.move_habit_up);
//                    final Button moveDown = view.findViewById(R.id.move_habit_down);
//                    toggleVis(moveDown);
//                    toggleVis(moveUp);
//                }
//            });
//        }



        // modify the text the views display so it's up to date
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        progressBar.setProgress(habit.getProgress());
        progressText.setText(habit.getProgress()+"%");

        return (view);
    }

    /**
     * Toggles the buttons for the reordering habits
     */
    public void toggleButtons(Boolean ison){
        for(Button b:buttonList){
            if(ison){
                b.setVisibility(View.VISIBLE);
            }
            else{
                b.setVisibility(View.GONE);
            }
        }
        keepOn = true;
    }

    /**
     * Toggles the buttons for the reordering habits
     */
    private void toggleButton(Button b){
        if(b.getVisibility()==View.VISIBLE){
            b.setVisibility(View.GONE);
        }
        else{
            b.setVisibility(View.VISIBLE);
        }
    }

    public void clearButtons(){
        buttonList.clear();
    }
}
