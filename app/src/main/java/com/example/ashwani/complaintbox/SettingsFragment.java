package com.example.ashwani.complaintbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    View rootView;
    Button logout;
    TextView email_tv, name_tv, phone_tv;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        logout = rootView.findViewById(R.id.logout_settings);
        email_tv = rootView.findViewById(R.id.emailid_settings);
        name_tv = rootView.findViewById(R.id.user_name_settings);
        phone_tv = rootView.findViewById(R.id.user_phoneno_settings);

        email_tv.setText(firebaseAuth.getCurrentUser().getEmail());
        if (firebaseAuth.getCurrentUser().getDisplayName() != null)
            name_tv.setText(firebaseAuth.getCurrentUser().getDisplayName());
        if (firebaseAuth.getCurrentUser().getPhoneNumber() != null)
            phone_tv.setText(firebaseAuth.getCurrentUser().getPhoneNumber());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
            }
        });


        return rootView;
    }


}
