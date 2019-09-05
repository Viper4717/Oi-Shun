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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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
    String savePath = "";
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_reorder);
        if(checkPermissionFromDevice()) {
            //Initialization
            recordButton = (ImageButton) findViewById(R.id.recordButton);
            pauseButton = (ImageButton) findViewById(R.id.pauseButton);
            backButton = (ImageButton) findViewById(R.id.backButton);
            recordTimer = (TextView) findViewById(R.id.recordTimer);
            storageRemaining = (TextView) findViewById(R.id.storageRemaining);
            mediaRecorder =  new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp");

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
        else{
            requestPermissionFromDevice();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

            //Folder directory to store the file
            /*File folder = new File(Environment.getExternalStorageDirectory()
                    +"/OiShun/"+ UUID.randomUUID().toString()+"audio_record.3gp");
            if(!folder.exists()){
                folder.mkdir();
            }*/
            try{
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        else{
            recordButton.setBackgroundResource(R.drawable.record_button_image);
            Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            mediaRecorder.stop();
        }
    }

    //Method for pausing
    private void PauseMethod(boolean paused) {
    }
}
