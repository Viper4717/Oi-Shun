/*
 * User Page Activity Control
 *
 * @author A.M. Aahad
 * */

package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPage extends AppCompatActivity {

    ListView personalContentView;
    String[] personalContentNames;
    private int[] coverPhotos = {R.drawable.music_icon_image};
    List<Recording> personalRecordings;
    ImageView userImage;
    TextView userNameText;
    Button subscribeButton;
    Recording recording;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        personalRecordings = new ArrayList<>();
        userImage = (ImageView) findViewById(R.id.userImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        //Intent tempIntent = getIntent();
        //String userName = tempIntent.getStringExtra("user_name");
        //userNameText.setText(userName);

        personalContentNames = getResources().getStringArray(R.array.contentNames);

        personalContentView = findViewById(R.id.personal_contents);

        Query query = FirebaseDatabase.getInstance().getReference("recordings").orderByChild("recordingUploader").equalTo("viper4717");
        query.addListenerForSingleValueEvent(valueEventListener);

        // UserAdapter userAdapter = new UserAdapter(this,personalContentNames,coverPhotos);
        //  personalContentView.setAdapter(userAdapter);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            personalRecordings.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recording = ds.getValue(Recording.class);
                    personalRecordings.add(recording);
                }

                String[] names = new String[personalRecordings.size()];

                for (int i = 0; i < names.length; i++) {
                    names[i] = personalRecordings.get(i).getRecordingName();
                }

              /*  String[] uploaders = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    uploaders[i] = personalRecordings.get(i).getRecordingUploader();
                }

                String[] durations = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    durations[i] = personalRecordings.get(i).getRecordingDuration();
                }
*/
                UserAdapter userAdapter = new UserAdapter(UserPage.this, names, coverPhotos);
                personalContentView.setAdapter(userAdapter);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
