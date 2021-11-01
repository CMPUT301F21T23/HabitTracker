package com.example.habittracker.activities.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> followingList;
    private HashMap<String, Boolean> pendingStatus;
    FollowingArrayAdapter(@NonNull Context context, ArrayList<String> followingList,
                          HashMap<String, Boolean> pendingStatus) {
        super(context, 0, followingList);
        this.context = context;
        this.followingList = followingList;
        this.pendingStatus = pendingStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view for a user in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.following_list_view_content, parent, false);
        }
        // populate the list view content
        String userid = followingList.get(position);
        TextView useridTextView = convertView.findViewById(R.id.userid);
        TextView followStatus = convertView.findViewById(R.id.followStatus);
        useridTextView.setText(userid);
        if (pendingStatus.get(userid) == true) {
            // this user has not accepted the follow request yet - follow request pending
            followStatus.setText("Pending");
        }
        return convertView;
    }
}
