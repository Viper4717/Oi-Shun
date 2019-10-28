/*
 * User Page Activity Control
 *
 * @author A.M. Aahad
 * */

package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    String userName;
    User user;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        String ownProfile = intent.getStringExtra("own_profile");

        personalRecordings = new ArrayList<>();
        userImage = (ImageView) findViewById(R.id.userImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        storage = FirebaseStorage.getInstance();

        userNameText.setText(userName);
        if(ownProfile.equals("yes")){
            subscribeButton.setEnabled(false);
        }

        personalContentNames = getResources().getStringArray(R.array.contentNames);

        personalContentView = findViewById(R.id.personal_contents);

        Query userClassQuery = FirebaseDatabase.getInstance().getReference("user").orderByChild("name").equalTo(userName);
        userClassQuery.addListenerForSingleValueEvent(userImageListener);

        Query query = FirebaseDatabase.getInstance().getReference("recordings").orderByChild("recordingUploader").equalTo(userName);
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

    ValueEventListener userImageListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                }
                Glide.with(getApplicationContext()).load(user.getUserAvatarURL()).into(userImage);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
