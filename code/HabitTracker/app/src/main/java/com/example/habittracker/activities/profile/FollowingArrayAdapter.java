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
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.UserListOperationCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FollowingArrayAdapter is used to display both the people the user is currently following
 * as well as the people that the user has requested to follow and the request is pending
 */
public class FollowingArrayAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> followingList;
    private HashMap<User, Boolean> pendingStatus;
    private String TAG = "FollowingArrayAdapter";

    /**
     * Constructs FollowingArrayAdapter
     * @param context
     * @param followingList
     * @param pendingStatus
     */
    FollowingArrayAdapter(@NonNull Context context, ArrayList<User> followingList,
                          HashMap<User, Boolean> pendingStatus) {
        super(context, 0, followingList);
        this.context = context;
        this.followingList = followingList;
        this.pendingStatus = pendingStatus;
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
        // get the view for a user in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.following_list_view_content, parent, false);
        }
        // populate the list view content
        User user = followingList.get(position);
        String userid = user.getUsername();
        TextView useridTextView = convertView.findViewById(R.id.userid);
        useridTextView.setText(userid);

        // get the unfollow button
        Button unfollowButton = convertView.findViewById(R.id.unfollow);
        if (pendingStatus.get(user) == true) {
            // this user has not accepted the follow request yet - follow request pending
            unfollowButton.setText("Cancel Request");
        }

        // either unfollow a user or cancel a follow request when the unfollow button is pressed
        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String removeField = "following";
                if (pendingStatus.get(user) == true) {
                    removeField = "pendingFollowReqs";
                }
                DatabaseManager.get().removeUserListItem(SharedInfo.getInstance().getCurrentUser().getUsername(),
                        user.getUsername(),
                        removeField,
                        new UserListOperationCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                // remove the user from pendingStatus and followingList
                                followingList.remove(position);
                                pendingStatus.remove(user);
                                FollowingArrayAdapter.super.notifyDataSetChanged();
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
