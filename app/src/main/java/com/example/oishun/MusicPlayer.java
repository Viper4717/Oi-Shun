package com.example.oishun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnPreparedListener{

    ImageButton playButton;
    SeekBar seekBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    String playingURL;
    String imageURL;
    String recordingName;
    String userName;
    TextView recordNameText;
    TextView userNameText;
    int totalTime;
    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //totalTime = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        //playingURL = intent.getStringExtra("recordingURL");
        Toast.makeText(this, playingURL, Toast.LENGTH_SHORT).show();
        recordingName = intent.getStringExtra("recordingName");
        userName = intent.getStringExtra("recordingUploader");

        if(!checkPermissionFromDevice())
            requestPermissionFromDevice();

        playButton = (ImageButton)findViewById(R.id.playButton);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        recordNameText = (TextView) findViewById(R.id.recordNameText);
        userNameText = (TextView) findViewById(R.id.userNameText);
        recordNameText.setText(recordingName);
        userNameText.setText(userName);

        // Media Player
        //mp = MediaPlayer.create(this,R.raw.music);
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        fetchAudioUrlFromFirebase();
        //totalTime = 0;

        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);


        //Toast.makeText(this, String.valueOf(totalTime), Toast.LENGTH_SHORT).show();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    // Stopping
                    try {
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    playButton.setImageResource(R.drawable.stop);

                } else {
                    // Playing
                    mp.pause();
                    playButton.setImageResource(R.drawable.play);
                }
            }
        });

        // Seek Bar
        seekBar = (SeekBar) findViewById(R.id.seekBar);

    }

    private boolean checkPermissionFromDevice() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    //Method to request write and record permission from device
    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET
        }, REQUEST_PERMISSION_CODE);
    }

    private void fetchAudioUrlFromFirebase() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        //Toast.makeText(MusicPlayer.this, "SUCCESS", Toast.LENGTH_SHORT).show();
        playingURL = "https://firebasestorage.googleapis.com/v0/b/oishun-73200.appspot.com/o/Recordings%2Ftest%2Fabcd.mp3?alt=media&token=a3e7bd3e-f59c-441d-bcfe-566a4bf7a849";
        StorageReference storageRef = storage.getReferenceFromUrl(playingURL);
        //Toast.makeText(MusicPlayer.this,storageRef.getDownloadUrl().toString(),Toast.LENGTH_LONG).show();
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            //Toast.makeText(MusicPlayer.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            @Override
            public void onSuccess(Uri uri) {
                //Toast.makeText(MusicPlayer.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                try {
                    // Download url of file
                    final String url = uri.toString();
                    mp.setDataSource(url);
                    // wait for media player to get prepare
                    mp.setOnPreparedListener(MusicPlayer.this);
                    mp.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //totalTime = mp.getDuration();
            }




        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MusicPlayer.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", e.getMessage());
                    }
                });

    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        totalTime = mp.getDuration();
        mp.start();
        // Seek Bar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        //Toast.makeText(this, ""+String.valueOf(totalTime), Toast.LENGTH_SHORT).show();
        seekBar.setMax(totalTime);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            seekBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            seekBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
}
