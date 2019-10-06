package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the default activity to MainActivity (App will start at this page)
        //setContentView(R.layout.activity_main);

        //Starting the Voice Recorder activity (Button needs to be added)
        startActivity(new Intent(getApplicationContext(), HomePage.class));
    }
}
