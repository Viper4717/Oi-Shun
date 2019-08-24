package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the default activity to Voice Recorder
        setContentView(R.layout.activity_voice_reorder);

        //Starting the activity
        startActivity(new Intent(getApplicationContext(), VoiceReorder.class));
    }
}
