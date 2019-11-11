package com.example.oishun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadFromFile extends AppCompatActivity {

    Button chooseImageButton;
    ImageView coverImage;
    EditText userInput;
    Button upload;
    Button cancel;
    String fileDir;
    String outputDir;
    String newFileName;
    String oldFileName;
    String recordDuration;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    final int REQUEST_PERMISSION_CODE = 100;
    final int RESULT_LOAD_IMAGE = 1;
    Recording recordingDetails;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_from_file);

        //Seeking permission from device to write and record
        if(!checkPermissionFromDevice())
            requestPermissionFromDevice();

        Intent intent = getIntent();
        recordDuration = intent.getStringExtra("recordDuration");
        fileDir = intent.getStringExtra("fileDir");
        outputDir = fileDir.substring(0, fileDir.lastIndexOf("/"));
        String fileWithExt = fileDir.substring(fileDir.lastIndexOf("/") + 1);
        oldFileName = fileWithExt.substring(0, fileWithExt.lastIndexOf("."));

        //Initialization
        chooseImageButton = findViewById(R.id.chooseImageButton);
        coverImage = findViewById(R.id.coverImage);
        coverImage.setTag("empty");
        userInput = (EditText) findViewById(R.id.edit_file_name);
        userInput.setText(oldFileName);
        userInput.setSelection(userInput.length());
        upload = (Button) findViewById(R.id.upload);
        cancel = (Button) findViewById(R.id.cancel);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("recordings");
        newFileName = null;
        recordingDetails = new Recording();
        progressDialog = new ProgressDialog(this);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent, RESULT_LOAD_IMAGE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFileName = userInput.getText().toString();
                if (newFileName != null && newFileName.trim().length() > 0) {
                    File newFile = new File(outputDir, newFileName+".mp3");
                    File oldFile = new File(outputDir, oldFileName+".mp3");
                    oldFile.renameTo(newFile);
                    uploadAudio();
                    oldFileName = null;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Method to check write and record permission from device
    private boolean checkPermissionFromDevice() {
        int readExternalStorageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return readExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    //Method to request write and record permission from device
    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    //method to upload audio files to Firebase
    private void uploadAudio() {
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();

        String coverImagePath = coverImage.getTag().toString();

        final StorageReference imageFilePath = storageReference.child("Recording Covers").
                child(OwnProfileValue.userName).child(newFileName+".jpg");
        final StorageReference filePath = storageReference.child("Recordings").
                child(OwnProfileValue.userName).child(newFileName+".mp3");

        final Uri imageUri;

        if(coverImagePath.equals("empty")){
            imageUri = Uri.parse("android.resource://com.example.oishun/drawable/music_icon_image");
        }
        else{
            imageUri = Uri.fromFile(new File(coverImagePath));
        }
        final Uri uri = Uri.fromFile(new File(outputDir, newFileName+".mp3"));

        imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadURL = uri.toString();
                        recordingDetails.setRecordingImageURL(imageDownloadURL);
                    }
                });
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadURL = uri.toString();
                                recordingDetails.setRecordingURL(downloadURL);
                                recordingDetails.setRecordingName(newFileName);
                                recordingDetails.setRecordingUploader(OwnProfileValue.userName);
                                recordingDetails.setRecordingDuration(recordDuration);
                                String uploadID = databaseReference.push().getKey();
                                databaseReference.child(uploadID).setValue(recordingDetails);
                            }
                        });
                        Toast.makeText(UploadFromFile.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String imagePath = GetFilePathFromDevice.getPath(this, selectedImage);
            coverImage.setImageURI(selectedImage);
            coverImage.setTag(imagePath);
        }
    }
}
