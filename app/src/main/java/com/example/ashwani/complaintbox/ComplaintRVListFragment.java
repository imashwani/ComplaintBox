package com.example.ashwani.complaintbox;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ashwani.complaintbox.ViewModel.ComplaintViewModel;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ComplaintRVListFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ComplaintAdapter mComplaintAdapter;
    View rootView;
    ProgressBar progressBar;
    ArrayList<Complaint> compList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView:" + " fragment crated bro");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_complaint_rvlist, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        startLoading();

        return rootView;
    }

    private void startLoading() {
        mRecyclerView = rootView.findViewById(R.id.recycler_view_complaint);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        compList = new ArrayList<>();
        compList = getArrayList();

        mComplaintAdapter = new ComplaintAdapter(getActivity(), compList);
        mRecyclerView.setAdapter(mComplaintAdapter);


        Log.d(TAG, "onCreateView: list hai " + compList);

    }


    //fake data for time being
    public ArrayList<Complaint> getArrayList() {
        final ArrayList<Complaint> arrayList = new ArrayList<>();
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        ComplaintViewModel complaintviewModel = ViewModelProviders.of(getActivity()).get(ComplaintViewModel.class);

        LiveData<ArrayList<Complaint>> complaintLiveData = complaintviewModel.getDataSnapshotLiveData();

        complaintLiveData.observe(getActivity(), new Observer<ArrayList<Complaint>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Complaint> complaints) {
                progressBar.setVisibility(View.VISIBLE);
                arrayList.clear();
                for (Complaint cp : complaints) {
                    arrayList.add(cp);
                }
                mComplaintAdapter = new ComplaintAdapter(getActivity(), compList);
                mRecyclerView.setAdapter(mComplaintAdapter);
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
            }
        });

        return arrayList;
    }

}
