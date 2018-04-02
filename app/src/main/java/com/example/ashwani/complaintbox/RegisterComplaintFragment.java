package com.example.ashwani.complaintbox;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class RegisterComplaintFragment extends Fragment {
    int IMAGE_GALLERY_REQUEST = 20;
    int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    Button camera, gallery;
    String mCurrentPhotoPath;
    FirebaseDatabase database;
    Bitmap bitmap;
    DatabaseReference myReff;
    int CAMERA_PIC_REQUEST = 8, SELECT_IMAGE = 4;
    String complaintNo = "02", userId = "", schoolName = "", description = "", date = "", problems = "", imageLink = "", phoneNumber = "", emailId = "", status = "";
    private ImageView imgPicture;
    private View rootView;
    private Button sendComplaint;
    private Spinner spinner;
    private ImageButton mImageAddButton;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_complaint, container, false);
        database = FirebaseDatabase.getInstance();
        myReff = database.getReference("complaint");

        spinner = (Spinner) rootView.findViewById(R.id.school_spinner);
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
                getComplaintDetails();
                Complaint theComplaint = new Complaint(complaintNo, userId, schoolName, description, date, problems, imageLink, phoneNumber, emailId, status);
                myReff.child(getUserId()).push().setValue(theComplaint);
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
                            Toast.makeText(getActivity(), "Camera se loge", Toast.LENGTH_SHORT).show();
                            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                invokeCamera();
                            } else {
                                // let's request permission.
                                String[] permissionRequest = {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                            }

                        }
                        if (which == 1) {
                            Toast.makeText(getActivity(), "Gallery ke dhikhaoge", Toast.LENGTH_SHORT).show();
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

    private String getUserId() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        return mFirebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 5) {
                new CompressFilesTask().execute(mCurrentPhotoPath);

                File imgFile = new File(mCurrentPhotoPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ImageView myImage = rootView.findViewById(R.id.image_registerComp);
                    myImage.setImageBitmap(myBitmap);
                }
            }
            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    // show the image to the user
                    ImageView myImage = rootView.findViewById(R.id.image_registerComp);
                    myImage.setImageBitmap(image);


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

                startActivityForResult(takePictureIntent, 5);
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

    private void getComplaintDetails() {
        schoolName = spinner.getSelectedItem().toString();
        StringBuilder stringBuilder = new StringBuilder("");
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
        EditText editText = rootView.findViewById(R.id.descriptionOfComplaint);
        description = (editText).getText().toString();
        emailId = mFirebaseAuth.getCurrentUser().getEmail();
    }

    private class CompressFilesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
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
            return mCurrentPhotoPath;
        }

        @Override
        protected void onPostExecute(String imgFile) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile);
            ImageView myImage = rootView.findViewById(R.id.image_registerComp);
            myImage.setImageBitmap(myBitmap);
            Toast.makeText(getActivity(), "Compressed Bro", Toast.LENGTH_SHORT).show();
        }
    }


}