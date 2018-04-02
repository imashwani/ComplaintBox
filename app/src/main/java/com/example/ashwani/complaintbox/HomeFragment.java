package com.example.ashwani.complaintbox;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myReff;
    CardView viewComplaintCV, newComplaintCV, iCardServiceCV, feedbackCV;
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        //getting reference to the card view items
        viewComplaintCV = rootView.findViewById(R.id.viewComplaintCardView_home);
        newComplaintCV = rootView.findViewById(R.id.addNewComplaintCV_home);
        iCardServiceCV = rootView.findViewById(R.id.iCard_cardView_home);
        feedbackCV = rootView.findViewById(R.id.feedbackCV_home);

        //setting on click events on the items in home fragment
        viewComplaintCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComplaintRVList();
            }
        });
        newComplaintCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewComplaintFragment();
            }
        });
        //todo: crate a icard service fragments with all the details and form to send query;
        iCardServiceCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        feedbackCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayStore();
            }
        });


        return rootView;
    }

    private void openPlayStore() {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void addNewComplaintFragment() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        RegisterComplaintFragment fragment = new RegisterComplaintFragment();
        transaction.add(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void openComplaintRVList() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ComplaintRVListFragment fragment = new ComplaintRVListFragment();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}


/*        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cp = new Complaint("002", "sds5s", "shakar dk", "desp", "12/12/17", "cpu ka problem", "link ka img", "9911416637", "mail@gmail.com", "true");
                myReff.push().setValue(cp);

                addNewComplaintFragment();

            }

        });
*/