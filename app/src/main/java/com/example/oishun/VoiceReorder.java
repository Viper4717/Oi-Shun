package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceReorder extends AppCompatActivity {

    ImageButton recordButton;
    ImageButton pauseButton;
    ImageButton backButton;
    TextView recordTimer;
    TextView storageRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_reorder);

        recordButton = (ImageButton) findViewById(R.id.recordButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        recordTimer = (TextView) findViewById(R.id.recordTimer);
        storageRemaining = (TextView) findViewById(R.id.storageRemaining);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VoiceReorder.this, "Kam Kore!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
