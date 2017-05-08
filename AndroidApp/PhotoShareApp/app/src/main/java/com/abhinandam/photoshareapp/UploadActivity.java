package com.abhinandam.photoshareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "UploadActivity";
    TextView textTargetUri;
    ImageView targetImage;

    private FirebaseUser user;
    private Uri uploadPhoto;

    private StorageReference mStorage;
    private ProgressDialog mProgress;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        textTargetUri = (TextView)findViewById(R.id.targeturi);
        targetImage = (ImageView)findViewById(R.id.targetimage);

        mProgress = new ProgressDialog(this);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            uploadPhoto = targetUri;
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void uploadPrivate(View v) {
        String description = ((EditText)findViewById(R.id.description)).getText().toString();

        if(!description.isEmpty()) {
            StorageReference filepath = mStorage;
            mProgress.setMessage("Uploading image...");
            mProgress.show();
            // set image metadata fields
            Uri fileToUpload = uploadPhoto;
            String userId = user.getUid();
            StorageMetadata uploadImageMetadata = new StorageMetadata.Builder()
                    .setCustomMetadata("User", userId)
                    .setCustomMetadata("Description", description)
                    .setCustomMetadata("Private", "true")
                    .build();

            // retrieve image and upload to firebase object
            String path = "images/private/" + userId + "/" + fileToUpload.getLastPathSegment();
            StorageReference finalReference = filepath.child(path);
            UploadTask uploadTask = finalReference.putFile(fileToUpload, uploadImageMetadata);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Log.d(TAG, "Upload Success");
                    updateUI();
                }
            });
            addToDatabase(path);
        }
        else {
            Log.d(TAG, "Missing description");
        }
    }

    public void uploadPublic(View v) {
        String description = ((EditText)findViewById(R.id.description)).getText().toString();

        if(!description.isEmpty()) {
            StorageReference filepath = mStorage;
            mProgress.setMessage("Uploading image...");
            mProgress.show();

            // set image metadata fields
            Uri fileToUpload = uploadPhoto;
            String userId = user.getUid();
            StorageMetadata uploadImageMetadata = new StorageMetadata.Builder()
                    .setCustomMetadata("User", userId)
                    .setCustomMetadata("Description", description)
                    .setCustomMetadata("Private", "false")
                    .build();

            String path = "images/public/" + userId + "/" + fileToUpload.getLastPathSegment();
            StorageReference finalReference = filepath.child(path);
            UploadTask uploadTask = finalReference.putFile(fileToUpload, uploadImageMetadata);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Log.d(TAG, "Upload Success");
                    updateUI();
                }
            });
            addToDatabase(path);
        }
        else {
            Log.d(TAG, "Missing description");
        }
    }

    public void updateUI(){
        uploadPhoto = null;

        ((ImageView) findViewById(R.id.targetimage)).setImageResource(0);
        ((EditText) findViewById(R.id.description)).setText("");
    }

    public void addToDatabase(String filePath) {
        // update database with new file path
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRoot = db.getReference();

        // do the transaction on the database
        if (filePath.split("/")[1].equals("public")) {
            DatabaseReference publicRef = dbRoot.child("public");
            publicRef.push().setValue(filePath);
        }
        else {
            DatabaseReference privateRef = dbRoot.child("private/" + filePath.split("/")[2]);
            privateRef.push().setValue(filePath);
            Log.i(TAG, "Pushed file reference to " + "private/" + filePath.split("/")[2]);
        }
    }

    public void browsePhotos() {
        Intent browsePage = new Intent(UploadActivity.this, BrowsePhotosActivity.class);
        startActivity(browsePage);
    }
}