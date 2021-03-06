package com.example.ashwani.complaintbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    String TAG = "MainActivity";

    int RC_SIGN_IN = 1;
    private String mUsername = null;
    private String mUseremail = null;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is logged in
//                    Log.d(TAG, "onAuthStateChanged: You are logged in friend");
                    mUsername = user.getDisplayName();
                    mUseremail = user.getEmail();
                    setUserdata();
                    android.util.Log.d("", "onAuthStateChanged: CCIN Email:" + mUseremail + "name: " + mUsername);

                } else {
                    //user signed out
                    startActivityForResult(
                            com.firebase.ui.auth.AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.add_page)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new com.firebase.ui.auth.AuthUI.IdpConfig.FacebookBuilder().build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

        if (savedInstanceState == null) {
            showHomeFragment();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.settings:
                        openSettingsFragment();
                        break;
                    case R.id.home:
                        showHomeFragment();
                        break;
                    case R.id.complaint:
                        showComplaintListFragment();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            com.firebase.ui.auth.IdpResponse response = com.firebase.ui.auth.IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(MainActivity.this, "signed in bro", Toast.LENGTH_SHORT).show();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    private void openSettingsFragment() {
        transaction = fragmentManager.beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        transaction.replace(R.id.frame_layout, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showComplaintListFragment() {
        transaction = fragmentManager.beginTransaction();
        ComplaintRVListFragment fragment = new ComplaintRVListFragment();
        transaction.replace(R.id.frame_layout, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showHomeFragment() {
        transaction = fragmentManager.beginTransaction();
        HomeFragment Hfragment = new HomeFragment();
        transaction.replace(R.id.frame_layout, Hfragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setUserdata() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }


}
