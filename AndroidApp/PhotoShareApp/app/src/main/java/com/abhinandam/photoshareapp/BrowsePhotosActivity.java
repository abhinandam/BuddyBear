package com.abhinandam.photoshareapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BrowsePhotosActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final String TAG = "BrowsePhotosActivity";
    private String userUid;

    List<String> photoLocations = new ArrayList<>();
    List<String> photoURLs = new ArrayList<>();

    String[] displayImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_photos);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getPhotoLocations();
        getPhotoURLs(photoLocations);
        updatePhotos();
    }

    public void updatePhotos() {
        ListView photoGrid = (ListView) findViewById(R.id.photoGrid);
        displayImages = new String[photoURLs.size()];
        photoURLs.toArray(displayImages);
        photoGrid.setAdapter(new ImageListAdapter(BrowsePhotosActivity.this, displayImages));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getPhotoLocations() {
        // get the root of the database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference db_root = db.getReference();

        // get the current user id (for accessing their private photos)
        if(mUser != null) { // show private photos + public photos
            String mUserID = mUser.getUid();
            Log.i(TAG, "Allowing access to private photos for user " + mUserID);
            DatabaseReference privateRef = db_root.child("private/" + mUserID);
            privateRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        photoLocations.add((String) postSnapshot.getValue());
                    }
                    getPhotoURLs(photoLocations);
                    updatePhotos();
                    Log.i(TAG, "Updated photo URL array to " + displayImages.toString());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "The read failed: " + databaseError.getCode());
                }
            });
        }
        // add public photos
        DatabaseReference publicRef = db_root.child("public");
        publicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    photoLocations.add((String) postSnapshot.getValue());
                }
                getPhotoURLs(photoLocations);
                updatePhotos();
                Log.i(TAG, "Updated photo URL array to " + displayImages.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database read failed: " + databaseError.getCode());
            }
        });
    }

    public void getPhotoURLs(final List<String> photoPaths) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        // reference to private directory
        StorageReference rootRef = firebaseStorage.getReference();

        for (String path: photoPaths) {
            rootRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(!photoURLs.contains(uri.toString())) {
                        photoURLs.add(uri.toString());
                        Log.i(TAG, "Added following url " + uri.toString() + " to list of paths" + " now length " + photoURLs.size());
                        updatePhotos();
                    }
                }
            });
        }
    }
}