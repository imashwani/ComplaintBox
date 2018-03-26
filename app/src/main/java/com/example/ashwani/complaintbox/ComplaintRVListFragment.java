package com.example.ashwani.complaintbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ComplaintRVListFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ComplaintAdapter mComplaintAdapter;

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
        mComplaintAdapter = new ComplaintAdapter(getActivity(), getArrayList());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mComplaintAdapter);

        return rootView;
    }

    //fake data for time being
    public ArrayList<Complaint> getArrayList() {
        ArrayList<Complaint> arrayList = new ArrayList<>();
        Complaint complaint = new Complaint("001", "userid", "apsdk", "desc", "12 jan 18", "cpu problem", "9911416634", "ash@gmail.com", "false");
        arrayList.add(complaint);
        complaint.setComplaintNo("12");
        arrayList.add(complaint);
        Log.d(TAG, "getArrayList: size" + arrayList.size());


        return arrayList;
    }
}
