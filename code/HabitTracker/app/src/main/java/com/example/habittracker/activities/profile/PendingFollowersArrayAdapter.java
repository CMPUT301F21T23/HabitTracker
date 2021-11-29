package com.example.habittracker.activities.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.utils.UserListOperationCallback;
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
     * @param pendingFollowersList      {@code ArrayList<User>} List of userids representing pending requests
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

        // declines a follow request upon pressing the decline button
        Button declineButton = convertView.findViewById(R.id.decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Processing your request!", Toast.LENGTH_SHORT).show();
                DatabaseManager.get().declineFollowRequest(SharedInfo.getInstance().getCurrentUser().getUsername(),
                        userid,
                        new UserListOperationCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                // update the ArrayAdapter
                                pendingFollowersList.remove(position);
                                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
                            }

                            @Override
                            public void onCallbackFailure(String reason) {
                                Log.d(TAG, reason);
                                Toast.makeText(getContext(), "Failed to decline " + userid, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // accepts a follow request upon pressing the accept button
        Button acceptButton = convertView.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Processing your request!", Toast.LENGTH_SHORT).show();
                DatabaseManager.get().acceptFollowRequest(SharedInfo.getInstance().getCurrentUser().getUsername(),
                        userid,
                        new UserListOperationCallback() {
                            @Override
                            public void onCallbackSuccess(String userid) {
                                // update the ArrayAdapter
                                pendingFollowersList.remove(position);
                                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
                            }

                            @Override
                            public void onCallbackFailure(String reason) {
                                Log.d(TAG, reason);
                                Toast.makeText(getContext(), "Failed to accept " + userid, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return convertView;
    }
}
