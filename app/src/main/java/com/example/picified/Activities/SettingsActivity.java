package com.example.picified.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.picified.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsActivity extends AppCompatActivity {

    ImageView userPicture;
    TextView viewUserEmail , viewUserName;
    Intent intent;
    String userName;
    String userEmail;
    String userPhotoUrl;
    private Uri imageUri;
    FirebaseUser user;
    private StorageReference mStorageRef;
    DatabaseReference reference;

    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

    }

    @Override //Handle what happens when i press the back button.
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, FragmentsHandlerActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    //Init all views and setting all variables
    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userPicture = findViewById(R.id.image_settings_picture);
        viewUserEmail = findViewById(R.id.text_settings_useremail);
        viewUserName = findViewById(R.id.text_settings_username);
        intent = getIntent();
        userPhotoUrl = intent.getStringExtra("userpic");
        userName = intent.getStringExtra("username");
        userEmail = intent.getStringExtra("useremail");

        viewUserEmail.setText(userEmail);
        viewUserName.setText(userName);

        Glide.with(this).load(userPhotoUrl).into(userPicture);
    }

    //Onclick for changing the profile picture of the user
    public void btnChangePicture(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //Button to log out user and sending back to MainActivity
    public void btnLogoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override //Method that gets the user when changing picture and adding to firebase storage, also setting userProfile to that picture and updating.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Database reference
        reference = FirebaseDatabase.getInstance().getReference();

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            String path = imageUri.toString();
            String filename = path.substring(path.lastIndexOf("/")+1);
            String uid = user.getUid();

            final StorageReference riverRef = mStorageRef.child("images/"+uid+"/"+filename+".jpg");
            riverRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("ANTON", "onSuccess: Picture added" + uri);

                            Glide.with(SettingsActivity.this).load(uri).into(userPicture);

                            reference.child("users").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child("profile_picture").
                                    setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("ANTON", "onSuccess: image url added to user database table");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ANTON", "onFailure: couldn't save imageurl" + e.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ANTON", "onFailure: Picture couldn't be added " + e.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ANTON", "onFailure: Couldn't  " + e.toString());
                }
            });
        }
    }

    public void btnChangePassword(View view) {
        //Code for changing password
    }

    public void btnTermsOfService(View view) {
        //Code for terms of service
    }

    public void btnChangeTheme(View view) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Day theme activated", Toast.LENGTH_SHORT).show();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(this, "Night theme activated", Toast.LENGTH_SHORT).show();
        }
    }
}
