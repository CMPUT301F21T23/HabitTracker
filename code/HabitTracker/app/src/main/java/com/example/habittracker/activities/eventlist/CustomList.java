/*
 * CustomList
 *
 * Version 1.0.0
 *
 * Date: 2021-09-25
 *
 * Copyright (c) 2021 Yongquan Zhang.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.habittracker.activities.eventlist;

import static com.example.habittracker.utils.DateConverter.arrayListToString;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;

import java.util.ArrayList;

/**
 * This is the CustomList class that allows us to display multiple strings in one row of
 * the list. The idea is from CMPUT 301 Lab 3 instruction.
 * @author Yongquan Zhang
 */
public class CustomList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> events;
    private Context context;

    /**
     * The constructor of CustomList
     * @param context       {@code Context} required context
     * @param events        {@code ArrayList<HabitEvent>} a habit event list
     */
    public CustomList(Context context, ArrayList<HabitEvent> events){
        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * Override the getView method. Display multiple information in a single row of the list.
     * @param position          {@code int} index of the selected item
     * @param convertView       {@code View} convert view
     * @param parent            {@code ViewGroup} parent view
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent,false);
        }
        HabitEvent tempEvent =events.get(position);
        TextView Title = (TextView) view.findViewById(R.id.contentView);;
        TextView startDate = (TextView) view.findViewById(R.id.dateView);;
        Title.setText(tempEvent.getHabit());
        Log.d("eventID", String.valueOf(tempEvent.getEventId()));
        startDate.setText(arrayListToString(tempEvent.getStartDate()));

        ImageView event_image = (ImageView) view.findViewById(R.id.event_image);;
        event_image.setImageResource(R.drawable.riding);
        return view;
    }
}