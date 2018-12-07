package com.example.ashwani.complaintbox;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class RegisterComplaintFragment extends Fragment {

    Thread thread;
    int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    Button camera, gallery;
    String mCurrentPhotoPath, fireId;
    Bitmap bitmap;
    FirebaseDatabase database;
    DatabaseReference myReff;
    int CAMERA_PIC_REQUEST = 8, IMAGE_GALLERY_REQUEST = 20;
    String complaintNo = "02", userId = "", schoolName = "", description = "", date = "",
            problems = "", imageLink = "", phoneNumber = "", emailId = "", status = "";
    private ImageView imgPicture;
    private View rootView;
    private Button sendComplaint;
    private Spinner spinner;
    private ImageButton mImageAddButton;
    private FirebaseAuth mFirebaseAuth;

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private void registerComplaint() {
        getComplaintDetails();
        getComplaintNumber();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_complaint, container, false);
        database = FirebaseDatabase.getInstance();
        myReff = database.getReference("complaint");

        mFirebaseAuth = FirebaseAuth.getInstance();


        spinner = rootView.findViewById(R.id.school_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Schools, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        mImageAddButton = rootView.findViewById(R.id.imageButton);

        sendComplaint = rootView.findViewById(R.id.sendComplaintButton);

        sendComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerComplaint();

                if (mCurrentPhotoPath != null && mCurrentPhotoPath.length() > 0) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OutputStream file = null;
                            Log.d(TAG, "run: Thread startedDDD");
                            File imageFile = new File(mCurrentPhotoPath);
                            if (imageFile.exists()) {
                                Log.d(TAG, "run: Image file EXISTS");
                                //decoding the selected file
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                                try {
                                    file = new FileOutputStream(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imageOf" + complaintNo + ".webp"));

                                    if (file != null) {
                                        bp.compress(Bitmap.CompressFormat.WEBP, 50, file);
                                        file.close();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "run:Image file isn't were the path says");
                            }

                            //uploading the file to  FirebaseStorage
                            // Create a storage reference from our app

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                            // Create a reference to "image.jpg"
                            StorageReference mountainsRef = storageRef.child("images/" + complaintNo + ".jpg");


                            final UploadTask uploadTask = mountainsRef.putFile(Uri.fromFile(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imageOf" + complaintNo + ".webp")));

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
                                    Log.d(TAG, "onSuccess: File Uploaded succesfully" + downloadUrl);
                                    Toast.makeText(rootView.getContext(), "image uploaded" + downloadUrl, Toast.LENGTH_LONG);

                                    //pushing complaint details to the database
                                    myReff.child(mFirebaseAuth.getCurrentUser().getUid()).child(fireId).child("imageLink").setValue(downloadUrl);

                                }
                            });
                        }
                    });

                }

            }
        });

        mImageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] listOptions = new String[]{"Camera", "Gallery"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select any one").setCancelable(true);
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setSingleChoiceItems(listOptions, -1, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Do something
                        if (which == 0) {
                            //camera option selected
                            Toast.makeText(getActivity(), "Camera started", Toast.LENGTH_SHORT).show();
                            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                invokeCamera();
                            } else {
                                // let's request permission.
                                String[] permissionRequest = {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                            }

                        }
                        if (which == 1) {
                            Toast.makeText(getActivity(), "Showing Gallery ", Toast.LENGTH_SHORT).show();
                            onImageGalleryClicked();
                        }
                    }
                });

                // Show the AlertDialog
                final AlertDialog dialog = builder.show();

            }
        });

        return rootView;
    }


    private void getComplaintDetails() {
        userId = mFirebaseAuth.getCurrentUser().getUid();

        schoolName = spinner.getSelectedItem().toString();
        EditText editText = rootView.findViewById(R.id.descriptionOfComplaint);

        description = (editText).getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date_ = new Date();
        date = dateFormat.format(date_);

        StringBuilder stringBuilder = new StringBuilder();
        if (rootView.findViewById(R.id.CPU_CB).isEnabled()) {
            stringBuilder.append("CPU, ");
        }
        if (rootView.findViewById(R.id.projector_CB).isEnabled())
            stringBuilder.append("Projector, ");
        if (rootView.findViewById(R.id.sound_CB).isEnabled())
            stringBuilder.append("Sound, ");
        if (rootView.findViewById(R.id.wiring_CB).isEnabled())
            stringBuilder.append("Wiring, ");
        if (rootView.findViewById(R.id.os_CB).isEnabled())
            stringBuilder.append("Operating System/Windows, ");
        problems = stringBuilder.substring(0, stringBuilder.length() - 2);

        emailId = mFirebaseAuth.getCurrentUser().getEmail();

    }

    private String getUserId() {
        return mFirebaseAuth.getCurrentUser().getUid();
    }

    //getComplaintNumber will also register the complaint to data base
    private void getComplaintNumber() {
        final DatabaseReference complaintReff = database.getReference("complaintNo");
        complaintReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                complaintNo = dataSnapshot.getValue().toString();
                complaintReff.setValue(Integer.valueOf(complaintNo) + 1);

                Log.d(TAG, "onDataChange: The complaint number is" + complaintNo);

                Complaint complaint = new Complaint(complaintNo, userId, schoolName, description, date, problems, imageLink, phoneNumber, emailId, status);
                //Adding complaint to the database
                fireId = myReff.child(userId).push().getKey();
                myReff.child(userId).child(fireId).setValue(complaint);
                Toast.makeText(rootView.getContext(), "Complaint No " + complaintNo + " Registered", Toast.LENGTH_LONG);
                getActivity().getFragmentManager().popBackStack();
                if (thread != null) {
                    thread.start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
//                new CompressFilesTask().execute(mCurrentPhotoPath);
                Log.d(TAG, "onActivityResult: camera image current Path " + mCurrentPhotoPath);

                File imgFile = new File(mCurrentPhotoPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ImageView myImage = rootView.findViewById(R.id.image_registerComp);
                    Glide.with(getContext()).load(myBitmap).into(myImage);
                }
            }
            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();
                mCurrentPhotoPath = getPath(getActivity().getApplicationContext(), imageUri);

                Log.d(TAG, "onActivityResult: galley image current Path " + mCurrentPhotoPath);


                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    // show the image to the user
                    ImageView myImage = rootView.findViewById(R.id.image_registerComp);
                    Glide.with(getContext()).load(image).into(myImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(getActivity(), "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we have heard back from our request for camera and write external storage.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(getActivity(), R.string.cannotopencamera, Toast.LENGTH_LONG).show();
            }
        }
    }

    void invokeCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "invokeCamera: " + ex);

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.complaintbox",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST
                );
            }
        }
    }

    public void onImageGalleryClicked() {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


//    private class CompressFilesTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Bitmap bp = BitmapFactory.decodeFile(mCurrentPhotoPath);
//            OutputStream file = null;
//            try {
//                file = new FileOutputStream(mCurrentPhotoPath);
//
//                if (file != null) {
//                    bp.compress(Bitmap.CompressFormat.WEBP, 50, file);
//                    file.close();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return mCurrentPhotoPath;
//        }
//
//        @Override
//        protected void onPostExecute(String imgFile) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile);
//            ImageView myImage = rootView.findViewById(R.id.image_registerComp);
//            Glide.with(getContext()).load(myBitmap).into(myImage);
//            Toast.makeText(getActivity(), "Compressed Bro", Toast.LENGTH_SHORT).show();
//        }
//    }


}
