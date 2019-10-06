/*
* Voice Recorder Activity Control
*
* @author A.M. Aahad
* */

package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class VoiceReorder extends AppCompatActivity {

    ImageButton recordButton;
    ImageButton pauseButton;
    ImageButton backButton;
    TextView recordTimer;
    TextView storageRemaining;
    MediaRecorder mediaRecorder;
    boolean recording = true;
    boolean paused = true;
    final int REQUEST_PERMISSION_CODE = 1000;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String outputDir;
    String oldFileName;
    String newFIleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_reorder);

        //Seeking permission from device to write and record
        if(!checkPermissionFromDevice())
            requestPermissionFromDevice();

        //Initialization
        recordButton = (ImageButton) findViewById(R.id.recordButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        recordTimer = (TextView) findViewById(R.id.recordTimer);
        storageRemaining = (TextView) findViewById(R.id.storageRemaining);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        oldFileName = null;
        newFIleName = null;
        outputDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+File.separator+"OiShun";
        File file = new File(outputDir);
        if(!file.exists()) file.mkdir();

        //Recordbutton action
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordMethod(recording);
                recording = (!recording);
            }
        });

        //Pausebutton action
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PauseMethod(paused);
                paused = (!paused);
            }
        });
    }

    //Method to check write and record permission from device
    private boolean checkPermissionFromDevice() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED
                && recordAudioResult == PackageManager.PERMISSION_GRANTED;
    }

    //Method to request write and record permission from device
    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    //Method to show if permission is granted or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    //Method for recording
    private void RecordMethod(boolean recording) {
        if(recording){
            //Changing the recordbutton image to stop
            recordButton.setBackgroundResource(R.drawable.stop_button_image);
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();

            //preparing the mediaRecorder
            mediaRecorder =  new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(date.getTime());
            oldFileName = timeStamp+".3gp";
            mediaRecorder.setOutputFile(outputDir+File.separator+oldFileName);
            try{
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        else{
            //changing the recordbutton image to record
            recordButton.setBackgroundResource(R.drawable.record_button_image);
            Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            saveRecording();
        }
    }

    private void saveRecording() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.set_file_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.edit_file_name);
        userInput.setText(oldFileName);
        Button upload = (Button) promptsView.findViewById(R.id.upload);
        Button save = (Button) promptsView.findViewById(R.id.save);

        //set dialog message
        alertDialogBuilder.setCancelable(false);

        //create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFIleName = userInput.getText().toString();
                if (newFIleName != null && newFIleName.trim().length() > 0) {
                    File newFile = new File(outputDir, newFIleName);
                    File oldFile = new File(outputDir, oldFileName);
                    oldFile.renameTo(newFile);
                    uploadAudio();
                    oldFileName = null;
                    alertDialog.dismiss();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFIleName = userInput.getText().toString();
                if (newFIleName != null && newFIleName.trim().length() > 0) {
                    File newFile = new File(outputDir, newFIleName);
                    File oldFile = new File(outputDir, oldFileName);
                    oldFile.renameTo(newFile);
                    oldFileName = null;
                    alertDialog.dismiss();
                }
            }
        });
        //showing it
        alertDialog.show();
    }

    //method to upload audio files to firebase
    private void uploadAudio() {
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();
        StorageReference filePath = storageReference.child("Recordings").child(newFIleName);

        Uri uri = Uri.fromFile(new File(outputDir, newFIleName));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        });
    }

    //Method for pausing
    private void PauseMethod(boolean paused) {
    }
}
