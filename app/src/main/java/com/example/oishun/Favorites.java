package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {

    ListView favouriteContentsView;
    List<Recording> contents;
    DatabaseReference ref;
    Recording recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favouriteContentsView = (ListView) findViewById(R.id.favourite_contents);
        contents = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("favorites").child(OwnProfileValue.userName);
        addContents();

        favouriteContentsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recording rec = contents.get(position);
                Intent intent = new Intent(Favorites.this,MusicPlayer.class);
                intent.putExtra("recordingURL", rec.getRecordingURL());
                intent.putExtra("recordingName",rec.getRecordingName());
                intent.putExtra("recordingUploader",rec.getRecordingUploader());
                intent.putExtra("recordingImageURL",rec.getRecordingImageURL());
                startActivity(intent);

            }
        });

    }

    public void addContents(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    recording = ds.getValue(Recording.class);
                    contents.add(recording);

                }

                String[] names = new String[contents.size()];
                String[] coverPhotos = new String[contents.size()];
                String[] uploaders = new String[contents.size()];
                String[] durations = new String[contents.size()];

                for(int i = 0 ; i < names.length ; i++){
                    names[i] = contents.get(i).getRecordingName();
                    uploaders[i] = contents.get(i).getRecordingUploader();
                    durations[i] = contents.get(i).getRecordingDuration();
                    coverPhotos[i] = contents.get(i).getRecordingImageURL();
                }


                CustomAdapter adapter = new CustomAdapter(Favorites.this,names,coverPhotos,uploaders,durations);
                favouriteContentsView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
