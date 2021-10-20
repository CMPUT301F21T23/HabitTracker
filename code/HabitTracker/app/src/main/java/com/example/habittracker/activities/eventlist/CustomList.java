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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.Medicine;

import java.util.ArrayList;

/**
 * This is the CustomList class that allows us to display multiple strings in one row of
 * the list. The idea is from CMPUT 301 Lab 3 instruction.
 * @author Yongquan Zhang
 */
public class CustomList extends ArrayAdapter<Medicine> {
    private ArrayList<Medicine> meds; // An arraylist that contains Medicine objects.
    private Context context;

    /**
     * The constructor of CustomList
     * @param context
     * @param meds
     */
    public CustomList(Context context, ArrayList<Medicine> meds){
        super(context,0, meds);
        this.meds = meds;
        this.context = context;
    }

    /**
     * Override the getView method. Display multiple information in a single row of the list.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent,false);
        }
        Medicine medicine = meds.get(position);
        TextView medicineName = view.findViewById(R.id.contentView);
        TextView doseNum = view.findViewById(R.id.dose_text);
        TextView dailyFreq = view.findViewById(R.id.freq_text);
        TextView eventDate = view.findViewById(R.id.dateView);
        medicineName.setText(medicine.getName());
//        doseNum.setText(String.valueOf(medicine.getDose())+medicine.getDoseUnit());
        doseNum.setText("University of Alberta, Edmonton, CA");
//        dailyFreq.setText("Daily freq: "+String.valueOf(medicine.getFrequency()));
        dailyFreq.setText("Comments");
        eventDate.setText("2021-01-01");

        ImageView event_image = (ImageView) view.findViewById(R.id.event_image);;
        event_image.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

        ImageView location_image = (ImageView) view.findViewById(R.id.location_image);;
        location_image.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        return view;
    }
}