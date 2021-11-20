/*
 * AddEventFragment
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
package com.example.habittracker.activities.fragments;

import static com.example.habittracker.utils.DateConverter.arrayListToString;
import static com.example.habittracker.utils.DateConverter.stringToArraylist;
import com.example.habittracker.utils.SharedInfo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;


import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.activities.eventlist.LocationActivity;
import com.example.habittracker.utils.CustomDatePicker;
import com.example.habittracker.utils.HabitListCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.content.ContentResolver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This class specified the behavior of the AddEventFragment fragment.
 * It is a subclass of DialogFragment.
 * Some of the codes are from CMPUT 301 Lab 3 instructions.
 * @author Yongquan Zhang
 */
public class AddEventFragment extends DialogFragment {

    private TextView eventTitle;
    private EditText location1;
    private EditText editText1;
    private EditText date;
    private Spinner s;
    private ImageView event_image;
    private int spinnerIdx;
    private String attachedHabit;
    private OnFragmentInteractionListener listener;
    private boolean editFlag = false;
    FusedLocationProviderClient mFusedLocationClient;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int RESULT_OK = -1; // From android.app.Activity http://developer.android.com/reference/android/app/Activity.html#RESULT_OK
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private Context currentContext;

    /**
     * Override the onAttach method of DialogFragment.
     * @param context       {@code Context} required context
     */
    @Override
    public void onAttach(Context context) {
        currentContext = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "Must implement the listener.");
        }
    }

    /**
     * Override the onCreateDialog method of DialogFragment. Set up a list of EditText for event
     * information and a spinner for doseUnit.
     * @param savedInstanceState
     * @return a AlertDialog object
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_event, null);
        editText1 = view.findViewById(R.id.comment_body);
        date = view.findViewById(R.id.date_editText);
        location1 = view.findViewById(R.id.address);
        event_image = (ImageView) view.findViewById(R.id.img_f);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        Button get_location = view.findViewById(R.id.getPermission);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                } else {
//                    getLocation();
//                }
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });

        Button selectPhoto = view.findViewById(R.id.addPhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), LocationActivity.class);
                //startActivity(intent);
                System.out.println("Choosing an image");
                openFileChooser();
            }
        });

        /* Set up the spinner. */
        s = view.findViewById(R.id.event_spinner); // The spinner is used for dose unit
//        String[] items = new String[]{"Habit 1", "Habit 2", "Habit 3"};
        ArrayList<String> items = new ArrayList<>();
        items.add("Habit 1");
        items.add("Habit 2");
        items.add("Habit 3");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        s.setAdapter(spinnerAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Override the onItemSelected method. Record selected spinner item and index.
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                attachedHabit = items.get(position); // The selected dose unit
                spinnerIdx = position; // The index of selected dose unit
            }

            /**
             * Override the onNothingSelected method (no actual modification).
             *
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // set up snapshot listener
        String usersColName = "Users";
        String habitsColName = "Habits";
        String habitEventsColName = "HabitEvents";
        String DB_TAG = "DatabaseManager";

        ArrayList<String> habit_list = new ArrayList<>();

        DatabaseManager dm = DatabaseManager.get();
        CollectionReference colRef;
//        dm.getAllHabits("John_test_user", new HabitListCallback() {
        dm.getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(), new HabitListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<Habit> habitList) {
                items.clear();
                for (int i = 0; i < habitList.size(); i++) {
                    System.out.println(habitList.get(i).getTitle());
                    items.add(habitList.get(i).getTitle());
                    Log.d("Here", items.get(i));
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCallbackFailed() {

            }
        });
        /* If we received HabitEvent object passed from the main activity.
         * If yes, then we display its information.
         * If not, we display empty EditTexts.
         */
        if (getArguments() != null) {
            HabitEvent selectedEvent = (HabitEvent) getArguments().getSerializable("event"); // get the HabitEvent object.
            ArrayList<Integer> temp_date = selectedEvent.getStartDate();

            date.setText(arrayListToString(temp_date));
            editText1.setText(String.valueOf(selectedEvent.getComment()));
            spinnerIdx = spinnerAdapter.getPosition(selectedEvent.getHabit());
            s.setSelection(spinnerIdx);
            location1.setText(selectedEvent.getLocation());
            //event_image.setImageResource(R.drawable.riding);

            Picasso.with(getContext())
                    .load(selectedEvent.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    // .centerInside()
                    .into(event_image);
        }
        CustomDatePicker dp = new CustomDatePicker(getContext(), view, R.id.date_editText);
        Calendar ccc = dp.getDate();
        Date date_calendar = ccc.getTime();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle b = getArguments();

        /*
         * Check if we received an object from MainActivity or not. If we received the
         * object we should do edit/delete operation. If we did not receive the object
         * we should do add operation.
         */
        if (b != null) {
            return builder
                    .setView(view)
                    .setTitle("Edit event/Delete event")
                    .setNegativeButton("Cancel", null)
                    /* Neutral button is for delete operation */
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HabitEvent editE = (HabitEvent) getArguments().getSerializable("event");
                            editFlag = true;
                            listener.onDeletePressed(editE);
                        }
                    })
                    /* Positive button is for edit operation */
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HabitEvent editM = (HabitEvent) getArguments().getSerializable("event");
                            if(editM.getHabit()!=attachedHabit) {
                                // If the habit this habit event is attached to changes
                                // Update: The TA said habit events should always be under the same habit, so this functionality is unnecessary but we can leave it
                                editM.deleteDB();
                                editM.setStartDate(stringToArraylist(date.getText().toString()));
                                editM.setLocation(location1.getText().toString());
                                editM.setHabit(attachedHabit);
                                editM.setComment(editText1.getText().toString());
//                                editM.setCalendar(date_calendar);
                                editFlag = false;
                            }
                            else {
                                // No need to move habit event to under a different habit. Just edit it in place.
                                editM.setStartDate(stringToArraylist(date.getText().toString()));
                                editM.setLocation(location1.getText().toString());
                                editM.setHabit(attachedHabit);
                                editM.setComment(editText1.getText().toString());
//                                editM.setCalendar(date_calendar);
                                editFlag = true;
                            }

                            // Upload the image
                            uploadImage(editM);
                        }
                    }).create();
        }
        else {
            return builder
                    .setView(view)
                    .setTitle("Add Event")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String title = eventTitle.getText().toString();
                            String temp_dateString = date.getText().toString();
                            ArrayList<Integer> tempDate;
                            String tempHabit = attachedHabit;
                            String tempLocation = location1.getText().toString();
                            String tempComments = editText1.getText().toString();
                            HabitEvent tempHabitEvent = new HabitEvent();
                            if(temp_dateString.isEmpty()) {
                                String myFormat = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                tempDate = stringToArraylist(sdf.format(Calendar.getInstance().getTime()));
//                                tempHabitEvent.setCalendar(Calendar.getInstance().getTime());
                            }
                            else {
//                                tempHabitEvent.setCalendar(date_calendar);
                                tempDate = stringToArraylist(date.getText().toString());
                            }
                            tempHabitEvent.setLocation(tempLocation);
                            tempHabitEvent.setStartDate(tempDate);
                            tempHabitEvent.setComment(tempComments);
                            tempHabitEvent.setHabit(tempHabit);

                            // Upload the image
                            uploadImage(tempHabitEvent);
                        }
                    }).create();
        }
    }

    /**
     * create a new instance of the selected habit event
     * @param event
     * @return
     */
    public static AddEventFragment newInstance(HabitEvent event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Check is the input string could be parsed into double
     * @param str
     * @return boolean
     */
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check is the input string could be parsed into integer
     * @param str
     * @return boolean
     */
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * get location function, should be replaced by Aparna's function in the future.
     */
    private void getLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(requireContext(),
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        location1.setText(addresses.get(0).getAddressLine(0).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, PICK_IMAGE_REQUEST);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        //registerForActivityResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            // Can use mImageView.setImageURI(mImageUri); to display image natively
            System.err.println("Image URL onActivityResult coming up: " + mImageUri.toString());
            Picasso.with(getContext()).load(mImageUri).into(event_image);
            //mImageView.setImageURI(mImageUri);
        }
    }

    private void uploadImage(HabitEvent habitEvent) {
        System.out.println("In uploadImage()!!!!!!!");
        String imageUrl = "";
        if (mImageUri != null) {
            //DatabaseManager.get().getUsersColRef().
            StorageReference mStorageRef = DatabaseManager.get().getStorageRef();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".png"); //getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // mProgressBar.setProgress(0); This will set the progress to zero before the user even sees it at 100%, so we need to delay it to give the user visual feedback
                            // Delay this by 0.5 seconds
                            /*
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);*/

                            Toast.makeText(currentContext, "Upload Successful", Toast.LENGTH_LONG).show();
                            //Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();

                            // Following code taken from: https://stackoverflow.com/questions/50467814/tasksnapshot-getdownloadurl-is-deprecated
                            String url;
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    System.out.println("Image URL for upload: " + url);

                                    //Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), url);

                                    //taskSnapshot.getStorage().getDownloadUrl()

                                    //String uploadId = mDatabaseRef.push().getKey();
                                    //mDatabaseRef.child(uploadId).setValue(upload);
                                    // Add the image URL to the Firestore database under the habit event
                                    habitEvent.setImageUrl(url);
                                    listener.onOkPressed(habitEvent, editFlag);
                                    //String userId = SharedInfo.getInstance().getCurrentUser().getUsername();
                                    //String habitTitle = "Get my crap done";
                                    //HashMap<String, Object> habitEventDocument = new HashMap<>();
                                    //habitEventDocument.put("imageUrl", "www.totallyanimage.com");
                                    //DatabaseManager.get().updateHabitEventDocument(userId, habitTitle, "dlkaFDBHSsOhf2n5cI0q", habitEventDocument);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(currentContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            //mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            // The user created a habit event without selecting a photo to go along with it. Lame.
            //Toast.makeText(currentContext, "No file selected", Toast.LENGTH_SHORT).show();
            listener.onOkPressed(habitEvent, editFlag);
        }
        //return imageUrl;
    }
/*
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/
}
