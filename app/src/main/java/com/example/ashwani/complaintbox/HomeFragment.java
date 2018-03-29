package com.example.ashwani.complaintbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myReff;
    Button bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myReff = database.getReference().child("complaint");

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        bt = rootView.findViewById(R.id.complaint_button);
        final TextView tv=rootView.findViewById(R.id.hello);
        DatabaseReference db=database.getReference().child("con");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sp=dataSnapshot.getValue(String.class);
                tv.setText(sp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: reff\n\n" + myReff);

                Complaint cp = new Complaint("001", "sds5s", "apsdk", "desp", "12/12/17", "cpu ka problem", "link ka img", "9911416637", "mail@gmail.com", "true");
                myReff.push().setValue(cp);

                cp = new Complaint("002", "sds5s", "shakar dk", "desp", "12/12/17", "cpu ka problem", "link ka img", "9911416637", "mail@gmail.com", "true");
//                myReff.setValue("data aaya");


            }

        });
        return rootView;
    }

}
