package com.example.ashwani.complaintbox;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ashwani.complaintbox.ViewModel.ComplaintViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ComplaintRVListFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ComplaintAdapter mComplaintAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_complaint_rvlist, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view_complaint);

        mLayoutManager = new LinearLayoutManager(getActivity());

        ArrayList<Complaint> compList = getArrayList();
        Log.d(TAG, "onCreateView: list hai " + compList);

        mComplaintAdapter = new ComplaintAdapter(getActivity(), compList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mComplaintAdapter);

        return rootView;
    }

    //fake data for time being
    public ArrayList<Complaint> getArrayList() {
        final ArrayList<Complaint> arrayList = new ArrayList<>();
        checkConnection();
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        ComplaintViewModel complaintviewModel = ViewModelProviders.of(getActivity()).get(ComplaintViewModel.class);

        MediatorLiveData<ArrayList<Complaint>> complaintLiveData = complaintviewModel.getDataSnapshotLiveData();

        complaintLiveData.observe(this, new Observer<ArrayList<Complaint>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Complaint> complaints) {
                for (Complaint cp:complaints) {
                    arrayList.add(cp);
                }
            }

        });


//        liveData.observe(this, new Observer<DataSnapshot>() {
//            @Override
//            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null) {
//                    // update the UI here with values in the snapshot
//                   for(DataSnapshot dsp:dataSnapshot.getChildren())
//                   {
//                       Log.d(TAG, "onChanged: dsp ki value\n\n"+dsp);
//                       arrayList.add(dsp.getValue(Complaint.class));
//                   }
//                }
//            }
//        });

     //   Complaint cp = new Complaint("999", "sds5s", "apsdk","desp", "12/12/17", "cpu ka problem", "link ka img",
           //     "9911416637", "mail@gmail.com", "true");
//        arrayList.add(cp);
        return arrayList;
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }

    public void checkConnection() {

        if (isOnline()) {

            Toast.makeText(getActivity(), "You are connected to Internet", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();

        }

    }


}
