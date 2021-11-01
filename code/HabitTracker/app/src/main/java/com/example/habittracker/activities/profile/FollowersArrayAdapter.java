package com.example.habittracker.activities.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.R;
import com.example.habittracker.User;

import java.util.ArrayList;

public class FollowersArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> followersList;
    public FollowersArrayAdapter(@NonNull Context context, ArrayList<String> followersList) {
        // need to call super
        super(context, 0, followersList);
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view for a follower in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.followers_list_view_content, parent, false);
        }
        // populate the list view content
        String followerId = followersList.get(position);
        TextView followerIdView = convertView.findViewById(R.id.followerId);
        followerIdView.setText(followerId);
        return convertView;
    }
}
