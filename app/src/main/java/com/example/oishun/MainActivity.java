package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the default activity to MainActivity (App will start at this page)
        //setContentView(R.layout.activity_main);

        //Starting the Voice Recorder activity (Button needs to be added)
       // System.out.println("hello");
        startActivity(new Intent(getApplicationContext(), UserPage.class));
    }
}
