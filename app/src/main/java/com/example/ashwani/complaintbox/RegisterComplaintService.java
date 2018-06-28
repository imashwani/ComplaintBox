package com.example.ashwani.complaintbox;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RegisterComplaintService extends IntentService {


    public RegisterComplaintService(String name) {
        super("RegisterComplaintService");

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String mCurrentPhotoPath = "";
        Complaint complaint = null;
        if (intent.hasExtra("filePath")) {
            mCurrentPhotoPath = intent.getStringExtra("filePath");
        }

        if (intent.hasExtra("complaint")) {
            complaint = intent.getParcelableExtra("complaint");
        }

        //decoding the selected file
        Bitmap bp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        OutputStream file = null;
        try {
            file = new FileOutputStream(mCurrentPhotoPath);

            if (file != null) {
                bp.compress(Bitmap.CompressFormat.WEBP, 50, file);
                file.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

        //uploading the file to  FirebaseStorage
        // Create a storage reference from our app

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Create a reference to "image.jpg"
        StorageReference mountainsRef = storageRef.child("images/" + complaint.getComplaintNo() + ".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) file;
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mountainsRef.putBytes(data);
        final Complaint finalComplaint = complaint;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                finalComplaint.setImageLink(downloadUrl);
                send(finalComplaint);

            }
        });

    }

    private void send(Complaint complaint) {

        FirebaseDatabase database;
        DatabaseReference myReff;
        //register the complain tot firebase databse
        database = FirebaseDatabase.getInstance();
        myReff = database.getReference("complaint");

        //pushing complaint details to the database
        myReff.child(complaint.getUserId()).push().setValue(complaint);
    }
}
