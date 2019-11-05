package com.example.oishun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreLayout extends Fragment {
    Context context;
    private ListView subscribedContentList;
    List<Recording> contents;
    //String[] names;
    //private int[] coverPhotos = {R.drawable.music_icon_image};

    View view;
    DatabaseReference ref;
    Recording recording;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addContentList() {
        //System.out.println("hello");
        context = getActivity().getApplicationContext();
        subscribedContentList = (ListView) view.findViewById(R.id.subscription_contents);

        //contentNames = getResources().getStringArray(R.array.contentNames);

        contents = new ArrayList<>();
        subscribedContentList = (ListView) view.findViewById(R.id.subscription_contents);
        ref = FirebaseDatabase.getInstance().getReference().child("recordings");
        //contentNames = getResources().getStringArray(R.array.contentNames);
        //System.out.println("hello");


       /* ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    recording = ds.getValue(Recording.class);
                    contents.add(recording);
                    System.out.println(contents.size());
                    System.out.println(recording.getRecordingName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //System.out.println(contents.size());
        addListNames();

        subscribedContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recording rec = contents.get(position);
                Intent intent = new Intent(context, MusicPlayer.class);
                intent.putExtra("recordingURL", rec.getRecordingURL());
                intent.putExtra("recordingName", rec.getRecordingName());
                intent.putExtra("recordingImageURL",rec.getRecordingImageURL());
                intent.putExtra("recordingUploader", rec.getRecordingUploader());
                //intent.putExtra("recordingDuration",rec.getRecordingDuration());
                startActivity(intent);
            }
        });
        //contents.clear();
        //System.out.println(names[0]);


    }


    public void addListNames() {

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contents.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recording = ds.getValue(Recording.class);
                    contents.add(recording);
                }
                String[] names = new String[contents.size()];

                for (int i = 0; i < names.length; i++) {
                    names[i] = contents.get(i).getRecordingName();
                }

                String[] uploaders = new String[contents.size()];

                for (int i = 0; i < names.length; i++) {
                    uploaders[i] = contents.get(i).getRecordingUploader();
                }

                String[] durations = new String[contents.size()];

                for (int i = 0; i < names.length; i++) {
                    durations[i] = contents.get(i).getRecordingDuration();
                }

                String[] coverPhotos = new String[contents.size()];

                for(int i = 0 ; i < names.length ; i++){
                    coverPhotos[i] = contents.get(i).getRecordingImageURL();
                }

                CustomAdapter adapter = new CustomAdapter(context, names, coverPhotos, uploaders, durations);
                //CustomAdapter adapter = new CustomAdapter(context,contentNames,coverPhotos);
                subscribedContentList.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //System.out.println("hello");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_subscription_layout, container, false);
        addContentList();


        return view;
    }
}