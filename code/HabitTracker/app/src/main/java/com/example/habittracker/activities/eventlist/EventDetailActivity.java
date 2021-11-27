package com.example.habittracker.activities.eventlist;

import static com.example.habittracker.utils.DateConverter.arrayListToString;
import static com.example.habittracker.utils.DateConverter.stringToArraylist;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;
import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.fragments.OnFragmentInteractionListener;
import com.example.habittracker.utils.CustomDatePicker;
import com.google.android.gms.location.LocationServices;
import com.example.habittracker.activities.eventlist.EventListActivity;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.content.ContentResolver;


/**
 * The activity for habit event detail view
 */
public class EventDetailActivity extends AppCompatActivity {
    private HabitEvent event;
    private Habit habit;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    public static final int RESULT_OK = -1; // From android.app.Activity http://developer.android.com/reference/android/app/Activity.html#RESULT_OK
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private Context currentContext;
    private EditText editText1;
    private EditText date;
    private EditText location1;
    private ImageView event_image;
    private Button addImage;
    private Button addLocation;
    private Button edit_bt;
    private Button delete_bt;
    private Button cancel_bt;
    private Button confirm_bt;
    private Button add_bt;
    private boolean editFlag;
    OnFragmentInteractionListener listener;

    /**
     * Override onCreate method to populate settings for this detail view screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        editText1 = findViewById(R.id.comment_body);
        date = findViewById(R.id.date_editText);
        location1 = findViewById(R.id.address);
        event_image = (ImageView) findViewById(R.id.img_f);
        addImage = findViewById(R.id.addPhoto);
        addLocation = findViewById(R.id.getPermission);
        edit_bt = findViewById(R.id.edit);
        delete_bt = findViewById(R.id.delete);
        cancel_bt = findViewById(R.id.cancel);
        confirm_bt = findViewById(R.id.confirm);
        add_bt = findViewById(R.id.add);



        Intent intent = getIntent();
        event = (HabitEvent) intent.getSerializableExtra("event");
        habit = (Habit) intent.getSerializableExtra("habit");

        location1.setEnabled(false);

        // If no event object is passed to this activity, set up for adding new habit event.
        if(event != null) {
            ArrayList<Integer> temp_date = event.getStartDate();

            date.setText(arrayListToString(temp_date));
            Log.d("date", date.getText().toString());
            editText1.setText(String.valueOf(event.getComment()));
            location1.setText(event.getLocation());
            Picasso.with(EventDetailActivity.this)
                    .load(event.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(event_image);
            addImage.setVisibility(View.INVISIBLE);
            addLocation.setVisibility(View.INVISIBLE);
            cancel_bt.setVisibility(View.INVISIBLE);
            confirm_bt.setVisibility(View.INVISIBLE);
            add_bt.setVisibility(View.INVISIBLE);
            editText1.setEnabled(false);
            date.setEnabled(false);
            location1.setEnabled(false);
        }
        // If an event object is passed from the list activity,
        // set up for editing existed habit event
        else {
            cancel_bt.setVisibility(View.INVISIBLE);
            confirm_bt.setVisibility(View.INVISIBLE);
            edit_bt.setVisibility(View.INVISIBLE);
            delete_bt.setVisibility(View.INVISIBLE);

        }
        CustomDatePicker dp = new CustomDatePicker(EventDetailActivity.this, findViewById(android.R.id.content).getRootView(), R.id.date_editText);

        delete_bt.setOnClickListener(new View.OnClickListener() {
            /**
             * implement actions after clicking delete button
             * @param v
             */
            @Override
            public void onClick(View v) {
                event.deleteDB();
                Toast toast = Toast.makeText(EventDetailActivity.this, "Delete Successful", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_VERTICAL, 0, 0);                toast.show();
                Intent temp_intent = new Intent(EventDetailActivity.this, EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) habit);
                temp_intent.putExtras(bundle);
                startActivity(temp_intent);
                finish();
            }
        });

        Button selectPhoto = findViewById(R.id.addPhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            /**
             * implement actions after clicking add photo button
             * @param v
             */
            @Override
            public void onClick(View v) {
                System.out.println("Choosing an image");

                final CharSequence[] items;

                items = new CharSequence[2];
                items[0] = "Camera";
                items[1] = "Gallery";

                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(EventDetailActivity.this);
                alertdialog.setTitle("Add Image");
                alertdialog.setItems(items, new DialogInterface.OnClickListener() {
                    /**
                     * Override onClick function and display a dialog to
                     * allow user select imgae from gallery or camera
                     * @param dialog
                     * @param item
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Camera")) {
                            openCamera();
                        } else if (items[item].equals("Gallery")) {
                            openFileChooser();
                        }
                    }
                });
                alertdialog.show();
            }
        });

        edit_bt.setOnClickListener(new View.OnClickListener() {
            /**
             * implement actions after click edit button
             * @param v
             */
            @Override
            public void onClick(View v) {
                addImage.setVisibility(View.VISIBLE);
                addLocation.setVisibility(View.VISIBLE);
                editText1.setEnabled(true);
                date.setEnabled(true);
                location1.setEnabled(true);
                cancel_bt.setVisibility(View.VISIBLE);
                confirm_bt.setVisibility(View.VISIBLE);
                edit_bt.setVisibility(View.INVISIBLE);
                delete_bt.setVisibility(View.INVISIBLE);

                cancel_bt.setOnClickListener(new View.OnClickListener() {
                    /**
                     * implement actions after clicking cancel button
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        resetEventDetail();
                        addImage.setVisibility(View.INVISIBLE);
                        addLocation.setVisibility(View.INVISIBLE);
                        editText1.setEnabled(false);
                        date.setEnabled(false);
                        location1.setEnabled(false);
                        cancel_bt.setVisibility(View.INVISIBLE);
                        confirm_bt.setVisibility(View.INVISIBLE);
                        edit_bt.setVisibility(View.VISIBLE);
                        delete_bt.setVisibility(View.VISIBLE);
                    }
                });
                confirm_bt.setOnClickListener(new View.OnClickListener() {
                    /**
                     * implement actions after clicking confirm button
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {

                        event.setStartDate(stringToArraylist(date.getText().toString()));
                        event.setLocation(location1.getText().toString());
                        event.setComment(editText1.getText().toString());
                        editFlag = true;
//                        event.setHabitInDB(habit.getTitle(), 1);
                        uploadImage(event);
                        Toast toast = Toast.makeText(EventDetailActivity.this, "Edit Successful", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();                        addImage.setVisibility(View.INVISIBLE);
                        addLocation.setVisibility(View.INVISIBLE);
                        editText1.setEnabled(false);
                        date.setEnabled(false);
                        location1.setEnabled(false);
                        cancel_bt.setVisibility(View.INVISIBLE);
                        confirm_bt.setVisibility(View.INVISIBLE);
                        edit_bt.setVisibility(View.VISIBLE);
                        delete_bt.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        add_bt.setOnClickListener(new View.OnClickListener() {
            /**
             * implement actions after clicking add button
             * @param v
             */
            @Override
            public void onClick(View v) {
                ArrayList<Integer> tempDate;
                HabitEvent tempE = new HabitEvent();
                tempE.setHabit(habit.getTitle());
                String temp_dateString = date.getText().toString();
                // check if start date is empty
                // if it is empty, set it to today's date
                if(temp_dateString.isEmpty()) {
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    tempDate = stringToArraylist(sdf.format(Calendar.getInstance().getTime()));
                }
                else {
                    tempDate = stringToArraylist(date.getText().toString());
                }
                tempE.setStartDate(tempDate);
                tempE.setLocation(location1.getText().toString());
                tempE.setComment(editText1.getText().toString());
                editFlag = false;
                uploadImage(tempE);
                Toast toast = Toast.makeText(EventDetailActivity.this, "Add new habit event Successful", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_VERTICAL, 0, 0);                toast.show();
                Intent intent = new Intent(EventDetailActivity.this, EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) habit);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void openCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mImageUri = data.getData();
                    // Can use mImageView.setImageURI(mImageUri); to display image natively
                    System.err.println("Image URL onActivityResult for image chooser coming up: " + mImageUri.toString());
                    Picasso.with(EventDetailActivity.this).load(mImageUri).into(event_image);
                    //mImageView.setImageURI(mImageUri);
                }
                break;
            case TAKE_PHOTO_REQUEST:
                if (resultCode == RESULT_OK) {
                    mImageUri = data.getData();
                    if (mImageUri == null) {
                        System.err.println("its nullahh");
                    }
                    //System.err.println("Image URL onActivityResult for camera coming up: " + mImageUri.toString());
                    System.err.println("plez work " + data.toString());
                    //Picasso.with(getContext()).load(mImageUri).into(event_image);

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    event_image.setImageBitmap(bitmap);
                    mImageUri = getImageUri(EventDetailActivity.this, bitmap);
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

                            Toast.makeText(EventDetailActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
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
                                    if (editFlag) {
                                        habitEvent.setHabitInDB(habit.getTitle(), 1);
                                    }
                                    else {
                                        habitEvent.setHabitInDB(habit.getTitle(), 2);
                                    }
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
                            Toast.makeText(EventDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (editFlag) {
                habitEvent.setHabitInDB(habit.getTitle(), 1);
            }
            else {
                habitEvent.setHabitInDB(habit.getTitle(), 2);
            }
        }
        //return imageUrl;
    }
/*
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventDetailActivity.this, EventListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", (Serializable) habit);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void resetEventDetail() {
        ArrayList<Integer> temp_date = event.getStartDate();

        date.setText(arrayListToString(temp_date));
        Log.d("date", date.getText().toString());
        editText1.setText(String.valueOf(event.getComment()));
        location1.setText(event.getLocation());
        Picasso.with(EventDetailActivity.this)
                .load(event.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(event_image);
    }
}
