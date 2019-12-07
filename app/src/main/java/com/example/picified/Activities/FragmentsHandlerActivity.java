package com.example.picified.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.picified.Fragments.HomeFragment;
import com.example.picified.Fragments.ProfileFragment;
import com.example.picified.Fragments.SearchFragment;
import com.example.picified.R;

public class FragmentsHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentshandler);

        ProfileFragment profileFragment = new ProfileFragment();
        changeFragment(profileFragment);


    }
    //Calling the Profile Fragment when FragmentsHandlerActivity is called
    public void changeFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.replace(R.id.fragment_holder, frag);
        fragmentTransaction.commit();
    }

    //Button for calling the Profile Fragment
    public void btnProfileFragment(View view) {
        ProfileFragment profileFragment = new ProfileFragment();
        changeFragment(profileFragment);
    }

    //Button for calling the Home Fragment
    public void btnHomeFragment(View view) {
        HomeFragment homeFragment = new HomeFragment();
        changeFragment(homeFragment);
    }

    //Button for calling the Search Fragment
    public void btnSearchFragment(View view) {
        SearchFragment searchFragment = new SearchFragment();
        changeFragment(searchFragment);
    }
}
