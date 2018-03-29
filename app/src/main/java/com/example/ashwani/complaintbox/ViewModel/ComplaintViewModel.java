package com.example.ashwani.complaintbox.ViewModel;

import android.arch.core.util.Function;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ashwani.complaintbox.Complaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ComplaintViewModel extends ViewModel {
    private static final DatabaseReference COMPLAINT_REF =
            FirebaseDatabase.getInstance().getReference("/complaint");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(COMPLAINT_REF);

    private final MediatorLiveData<ArrayList<Complaint> > complaintLiveData =new MediatorLiveData<>();
//            Transformations.map(liveData, new Deserializer());

    /**
     * on the thread started in onChage fun. in constructor
     * NOTE: I don't recommend starting up a new thread like this in your production app. This is not an example of "best practice" threading behavior. Optimally, you might want to use an Executor with a pool of reusable threads (for example) for a job like this.
     */
    public ComplaintViewModel(){
        // Set up the MediatorLiveData to convert DataSnapshot objects into HotStock objects
        complaintLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Complaint> clist=new ArrayList<>();
                            for (DataSnapshot dsp:dataSnapshot.getChildren()) {
                                clist.add(dsp.getValue(Complaint.class));
                            }
                            complaintLiveData.postValue(clist);
                            Log.d(TAG, "run: compliant ki  list in view model\n\n"+clist);
//                            complaintLiveData.postValue(dataSnapshot.getValue(Complaint.class));
                        }
                    }).start();
                } else {
                    complaintLiveData.setValue(null);
                }
            }
        });

    }

    @NonNull
    public MediatorLiveData<ArrayList<Complaint>> getDataSnapshotLiveData() {
        return complaintLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, Complaint> {
        @Override
        public Complaint apply(DataSnapshot dataSnapshot) {
            return dataSnapshot.getValue(Complaint.class);
        }
    }
}

