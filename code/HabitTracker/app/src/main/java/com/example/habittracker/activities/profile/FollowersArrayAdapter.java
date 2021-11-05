package com.example.habittracker.activities.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.utils.UserListOperationCallback;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;

/**
 * FollowersArrayAdapter is used to display a list of the user's followers
 */
public class FollowersArrayAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> followersList;
    private String TAG = "FollowersArrayAdapter";

    /**
     * Constructs FollowersArrayAdapter
     * @param context               {@code Context} Global information about the application environment
     * @param followersList         {@code ArrayList<User>} List of userids representing followers
     */
    public FollowersArrayAdapter(@NonNull Context context, ArrayList<User> followersList) {
        // need to call super
        super(context, 0, followersList);
        this.context = context;
        this.followersList = followersList;
    }

    /**
     * Overrides the getView function to customize the list view content
     * @param position      {@code int} Index of data in the data list
     * @param convertView   {@code View} View object
     * @param parent
     * @return              {@code View} Modified view object
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view for a follower in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.followers_list_view_content, parent, false);
        }
        // populate the list view content
        String followerId = followersList.get(position).getUsername();
        TextView followerIdView = convertView.findViewById(R.id.pendingFollowerId);
        followerIdView.setText(followerId);

        // pressing the remove button removes the follower
        Button removeButton = convertView.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.get().removeUserListItem(SharedInfo.getInstance().getCurrentUser().getUsername(),
                        followerId,
                        "followers",
                        new UserListOperationCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                followersList.remove(position);
                                FollowersArrayAdapter.super.notifyDataSetChanged();
                            }

                            @Override
                            public void onCallbackFailure(String reason) {
                                Log.d(TAG, reason);
                            }
                        });
            }
        });
        return convertView;
    }
}
