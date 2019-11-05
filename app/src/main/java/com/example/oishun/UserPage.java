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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    String[] coverPhotos;
    List<Recording> personalRecordings;
    ImageView userImage;
    TextView userNameText;
    Button subscribeButton;
    Recording recording;
    String userName;
    User user;
    FirebaseStorage storage;
    DatabaseReference ref;
    boolean subscribeFlag;
    String[] myName = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        myName[0] = userName;

        //String ownProfile = intent.getStringExtra("own_profile");

        personalRecordings = new ArrayList<>();
        userImage = (ImageView) findViewById(R.id.userImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        storage = FirebaseStorage.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("subscriptions");

        userNameText.setText(userName);

        //enabling or disabling the subscribe button
        if(userName.equals(OwnProfileValue.userName)){
            subscribeButton.setVisibility(View.GONE);
        }

        ref.child(OwnProfileValue.userName).child(userName).addListenerForSingleValueEvent(isSubscribedListener);

        //Query isSubscribedQuery = ref.orderByChild(userName).equalTo(true);
        //isSubscribedQuery.addListenerForSingleValueEvent(isSubscribedListener);

        personalContentNames = getResources().getStringArray(R.array.contentNames);

        personalContentView = findViewById(R.id.personal_contents);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!subscribeFlag){
                    ref.child(OwnProfileValue.userName).child(userName).setValue(true);
                    subscribeButton.setText(R.string.unsubscribe);
                    subscribeFlag = true;
                    Toast.makeText(UserPage.this, "Subscribed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    ref.child(OwnProfileValue.userName).child(userName).setValue(false);
                    subscribeButton.setText(R.string.subscribe);
                    subscribeFlag = false;
                    Toast.makeText(UserPage.this, "Unsubscribed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Query userClassQuery = FirebaseDatabase.getInstance().getReference("user").orderByChild("name").equalTo(userName);
        //userClassQuery.addListenerForSingleValueEvent(userImageListener);

        FirebaseDatabase.getInstance().getReference("user").child(userName).addListenerForSingleValueEvent(userImageListener);

        Query query = FirebaseDatabase.getInstance().getReference("recordings").orderByChild("recordingUploader").equalTo(userName);
        query.addListenerForSingleValueEvent(valueEventListener);

        // UserAdapter userAdapter = new UserAdapter(this,personalContentNames,coverPhotos);
        //  personalContentView.setAdapter(userAdapter);

        personalContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recording rec = personalRecordings.get(position);
                Intent intent = new Intent(UserPage.this,MusicPlayer.class);
                intent.putExtra("recordingURL", rec.getRecordingURL());
                intent.putExtra("recordingName",rec.getRecordingName());
                intent.putExtra("recordingUploader",rec.getRecordingUploader());
                intent.putExtra("recordingImageURL",rec.getRecordingImageURL());
                //intent.putExtra("recordingDuration",rec.getRecordingDuration());
                startActivity(intent);
            }
        });

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
*/
                String[] durations = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    durations[i] = personalRecordings.get(i).getRecordingDuration();
                }

                String[] coverPhotos = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    coverPhotos[i] = personalRecordings.get(i).getRecordingImageURL();
                }

                UserAdapter userAdapter = new UserAdapter(UserPage.this, names, coverPhotos,myName,durations);
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
                user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getUserAvatarURL()).into(userImage);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener isSubscribedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                boolean sub = dataSnapshot.getValue(Boolean.class);
                if(sub) {
                    //Toast.makeText(UserPage.this, "eikhane ashse", Toast.LENGTH_SHORT).show();
                    subscribeButton.setText(R.string.unsubscribe);
                    subscribeFlag = true;
                }
                else{
                    subscribeButton.setText(R.string.subscribe);
                    subscribeFlag = false;
                }
            }
            else {
                subscribeButton.setText(R.string.subscribe);
                subscribeFlag = false;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
