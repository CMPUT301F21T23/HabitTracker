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
import com.example.habittracker.utils.PendingRequestCallback;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;

/**
 * PendingFollowersArrayAdapter shows the user the list of pending follow requests.
 */
public class PendingFollowersArrayAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> pendingFollowersList;
    private String TAG = "PendingFollowersArrayAdapter";

    /**
     * Constructs PendingFollowersArrayAdapter.
     * @param context                   {@code Context} Global information about the application environment
     * @param pendingFollowersList      {@code ArrayList<String>} List of userids representing pending requests
     */
    PendingFollowersArrayAdapter(@NonNull Context context, ArrayList<User> pendingFollowersList) {
        super(context, 0, pendingFollowersList);
        this.context = context;
        this.pendingFollowersList = pendingFollowersList;
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
        // get the view showing a user in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pending_followers_list_view_content, parent, false);
        }
        // populate the list view content
        String userid = pendingFollowersList.get(position).getUsername();
        TextView pendingFollower = convertView.findViewById(R.id.pendingFollowerId);
        pendingFollower.setText(userid);

        // set action to the decline button
        Button declineButton = convertView.findViewById(R.id.decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.get().declineFollowRequest(
                        SharedInfo.getInstance().getCurrentUser().getUsername(),
                        userid,
                        new PendingRequestCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                // remove the user from the pending list
                                pendingFollowersList.remove(position);
                                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
                            }
                            @Override
                            public void onCallbackFailure(String reason) {
                                Log.d(TAG, reason);
                            }
                        });
            }
        });

        // set action for the accept button
        Button acceptButton = convertView.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.get().acceptFollowRequest(
                        SharedInfo.getInstance().getCurrentUser().getUsername(),
                        userid,
                        new PendingRequestCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                // remove the user from the pending list
                                pendingFollowersList.remove(position);
                                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
                            }

                            @Override
                            public void onCallbackFailure(String reason) {
                                Log.d(TAG, reason);
                            }
                        }
                );
            }
        });
        return convertView;
    }
}
