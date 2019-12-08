package com.example.picified.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.picified.Activities.FragmentsHandlerActivity;
import com.example.picified.Activities.PictureViewActivity;
import com.example.picified.Activities.SettingsActivity;
import com.example.picified.Classes.User;
import com.example.picified.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ImageView profilePicture;
    TextView userNameText, userEmailText;
    String userPhotoUrl;
    String userName;
    String userEmail;
    CalendarView calendarView;

    FirebaseUser fuser;
    DatabaseReference reference;

    Toolbar mToolbar;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);
        setCurrentUser();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
               String a = Integer.toString(year);
               a = a + Integer.toString(month);
               a = a + Integer.toString(dayOfMonth);
               pictureViewActivity(a);
            }
        });





        return view;
    }

    //Initiate all variables and views
    private void init (View view) {
        setHasOptionsMenu(true);

        profilePicture = view.findViewById(R.id.image_user_profile);
        userNameText = view.findViewById(R.id.text_username_profile);
        userEmailText = view.findViewById(R.id.text_useremail_profile);
        mToolbar = view.findViewById(R.id.profile_toolbar);
        calendarView = view.findViewById(R.id.calendarView_profile);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    //Method for setting up the current user
    private void setCurrentUser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (isVisible() && user != null) {
                    userName = user.getName();
                    Glide.with(getActivity()).load(user.getProfile_picture()).into(profilePicture);
                    userEmailText.setText(user.getEmail());
                    userNameText.setText(user.getName());
                    //set all attributes.


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);

        switch(item.getItemId()) //Switch case for different toolbar buttons
        {
            case R.id.search: //Add search code here

                break;

            case R.id.settings: //Sends the user to settings page
                Log.d("ANTON", "onOptionsItemSelected: OPTIONS SLECTED ");
              //  intent = new Intent(FragmentsHandlerActivity.this, SettingsActivity.class);
                intent.putExtra("userpic",userPhotoUrl);
                intent.putExtra("username",userName);
                intent.putExtra("useremail",userEmail);

                getActivity().startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pictureViewActivity(String a) {
        Intent intent = new Intent(getActivity(), PictureViewActivity.class);
        intent.putExtra("a", a);
        startActivity(intent);
    }
}
