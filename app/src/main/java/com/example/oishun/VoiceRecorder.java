/*
* Voice Recorder Activity Control
*
* @author A.M. Aahad
* */

package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoiceRecorder extends AppCompatActivity {

    ImageButton recordButton;
    ImageButton pauseButton;
    ImageButton backButton;
    Chronometer recordTimer;
    TextView storageRemaining;
    TextView pausedStatus;
    MediaRecorder mediaRecorder;
    boolean recording = true;
    boolean paused = true;
    final int REQUEST_PERMISSION_CODE = 1000;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String outputDir;
    String oldFileName;
    String newFIleName;
    String userName;
    long intDuration;
    String recordDuration;
    Recording recordingDetails;
    long pauseOffset;
    int RESULT_LOAD_IMAGE;
    //ImageView coverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recorder);

        //Seeking permission from device to write and record
        if(!checkPermissionFromDevice())
            requestPermissionFromDevice();

        //Initialization
        recordButton = (ImageButton) findViewById(R.id.recordButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        recordTimer = (Chronometer) findViewById(R.id.recordTimer);
        storageRemaining = (TextView) findViewById(R.id.storageRemaining);
        pausedStatus = (TextView) findViewById(R.id.pausedStatus);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("recordings");
        progressDialog = new ProgressDialog(this);
        oldFileName = null;
        newFIleName = null;
        outputDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+File.separator+"OiShun";
        File file = new File(outputDir);
        if(!file.exists()) file.mkdir();
        double freeBytesExternal = new File(getExternalFilesDir(null).toString()).getFreeSpace();
        storageRemaining.setText(formatSize(freeBytesExternal));
        Intent tempIntent = getIntent();
        userName = tempIntent.getStringExtra("user_name");
        recordingDetails = new Recording();
        RESULT_LOAD_IMAGE = 1;
        //coverImage = (ImageView) findViewById(R.id.coverImage);

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
            pauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();

            //preparing the mediaRecorder
            mediaRecorder =  new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(date.getTime());
            oldFileName = timeStamp;
            mediaRecorder.setOutputFile(outputDir+File.separator+oldFileName+".mp3");

            //starting the mediaRecorder
            try{
                mediaRecorder.prepare();
                mediaRecorder.start();
                startChronometer();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        else{
            //changing the recordbutton image to record
            recordButton.setBackgroundResource(R.drawable.record_button_image);
            pauseButton.setVisibility(View.GONE);
            Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();

            //stopping the mediaRecorder
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            //resetting the timer and saving the recording
            intDuration = resetChronometer();
            recordDuration = createTimeLabel(intDuration);
            saveRecording();

            //getting the available free space again
            long freeBytesExternal = new File(getExternalFilesDir(null).toString()).getFreeSpace();
            storageRemaining.setText(formatSize(freeBytesExternal));
        }
    }

    //method to save the recording with custom name
    private void saveRecording() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.activity_set_file_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        //Button chooseImageButton = promptsView.findViewById(R.id.chooseImageButton);
        //final ImageView coverImage = promptsView.findViewById(R.id.coverImage);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.edit_file_name);
        userInput.setText(oldFileName);
        userInput.setSelection(userInput.length());
        Button upload = (Button) promptsView.findViewById(R.id.upload);
        Button save = (Button) promptsView.findViewById(R.id.save);

        //set dialog message
        alertDialogBuilder.setCancelable(false);

        //create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        /*chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(imageIntent, RESULT_LOAD_IMAGE);

                //Intent imageIntent = new Intent();
                //imageIntent.setType("image/*");
                //imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"),RESULT_LOAD_IMAGE);

                Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent, RESULT_LOAD_IMAGE);
            }
        });*/


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFIleName = userInput.getText().toString();
                if (newFIleName != null && newFIleName.trim().length() > 0) {
                    File newFile = new File(outputDir, newFIleName+".mp3");
                    File oldFile = new File(outputDir, oldFileName+".mp3");
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
                    File newFile = new File(outputDir, newFIleName+".mp3");
                    File oldFile = new File(outputDir, oldFileName+".mp3");
                    oldFile.renameTo(newFile);
                    oldFileName = null;
                    alertDialog.dismiss();
                    Toast.makeText(VoiceRecorder.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //showing it
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            //Toast.makeText(this, "ei function e dhukse", Toast.LENGTH_LONG).show();
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.activity_set_file_name, null);
            //ImageView coverImage = promptsView.findViewById(R.id.coverImage);
            //Uri selectedImage = data.getData();
            //coverImage.setImageURI(selectedImage);

            /*Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString());
                coverImage.setImageBitmap(bitmap);
            } catch (Exception e){
                e.printStackTrace();
            }*/
        }
    }

    //method to upload audio files to Firebase
    private void uploadAudio() {
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();
        final StorageReference filePath = storageReference.child("Recordings").child(userName).child(newFIleName+".mp3");

        Uri uri = Uri.fromFile(new File(outputDir, newFIleName+".mp3"));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL = uri.toString();
                        //recordingDetails.setRecordingURL(taskSnapshot.getStorage().getDownloadUrl().toString());
                        recordingDetails.setRecordingURL(downloadURL);
                        recordingDetails.setRecordingName(newFIleName);
                        recordingDetails.setRecordingUploader(userName);
                        recordingDetails.setRecordingDuration(recordDuration);
                        String uploadID = databaseReference.push().getKey();
                        databaseReference.child(uploadID).setValue(recordingDetails);
                    }
                });
                Toast.makeText(VoiceRecorder.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Method for pausing
    private void PauseMethod(boolean paused) {
        if(paused){
            //Showing the paused status
            Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
            pausedStatus.setVisibility(View.VISIBLE);

            //pausing the mediaRecorder
            mediaRecorder.pause();
            pauseChronometer();
        }

        else{
            //hiding the paused status;
            Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
            pausedStatus.setVisibility(View.GONE);

            //resuming the mediaRecorder
            mediaRecorder.resume();
            startChronometer();
        }
    }

    //Methods to control the recordTimer
    private void startChronometer() {
        recordTimer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        recordTimer.start();
    }

    private void pauseChronometer() {
        recordTimer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - recordTimer.getBase();
    }

    private long resetChronometer() {
        recordTimer.stop();
        long finishTime = SystemClock.elapsedRealtime() - recordTimer.getBase();
        recordTimer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        return finishTime;
    }

    public String createTimeLabel(long time) {
        String timeLabel = "";
        long min = time / 1000 / 60;
        long sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

    //method to get available size
    private static String formatSize(double size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        }
        String sizeString = new DecimalFormat("#.##").format(size);
        return sizeString+" "+suffix+" remaining";
    }
}
