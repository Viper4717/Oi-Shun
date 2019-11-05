package com.example.oishun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionLayout extends Fragment  {

    Context context;
    private ListView subscribedContentList;
    List<Recording> contents;
    //String[] names;
    //private int[] coverPhotos = {R.drawable.music_icon_image};

    View view;
    DatabaseReference ref;
    DatabaseReference friendsRef;
    Recording recording;
    String[] subNames;
    ValueEventListener eventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addContentList(){
        //System.out.println("hello");
        context = getActivity().getApplicationContext();
        subscribedContentList = (ListView) view.findViewById(R.id.subscription_contents);

        //contentNames = getResources().getStringArray(R.array.contentNames);

        contents = new ArrayList<>();
        subscribedContentList = (ListView)  view.findViewById(R.id.subscription_contents);
       // ref = FirebaseDatabase.getInstance().getReference().child("recordings");
        friendsRef = FirebaseDatabase.getInstance().getReference("subscriptions").child(OwnProfileValue.userName);



        //System.out.println(contents.size());
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contents.clear();
                List<String> friends = new ArrayList<>();
                int c = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Toast.makeText(context, ds.getValue(Boolean.class).toString(), Toast.LENGTH_SHORT).show();

                    if(ds.getValue(Boolean.class) == true){
                        String subscribedName = ds.getKey();
                        friends.add(subscribedName);
                        c++;
                    }


                }


                subNames = new String[friends.size()];


                for(int i = 0 ; i < subNames.length ; i++){
                    subNames[i] = friends.get(i);
                }

                eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //contents.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            recording = ds.getValue(Recording.class);
                            for(int i = 0 ; i < subNames.length; i++){
                                if(recording.getRecordingUploader().equals(subNames[i])){
                                    contents.add(recording);
                                }
                            }

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


                        CustomAdapter adapter = new CustomAdapter(context,names,coverPhotos,uploaders,durations);
                        //CustomAdapter adapter = new CustomAdapter(context,contentNames,coverPhotos);
                        subscribedContentList.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };


                for(int i = 0 ; i < subNames.length ; i++){

                    Query query =  FirebaseDatabase.getInstance().getReference("recordings").orderByChild("recordingUploader").equalTo(subNames[i]);
                    query.addListenerForSingleValueEvent(eventListener);
                }
                //for(int i = 0 ; i < subNames.length ; i++){
                 //   subNames[i] = null;
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




       // addListNames();

        subscribedContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recording rec = contents.get(position);
                Intent intent = new Intent(context,MusicPlayer.class);
                intent.putExtra("recordingURL", rec.getRecordingURL());
                intent.putExtra("recordingName",rec.getRecordingName());
                intent.putExtra("recordingUploader",rec.getRecordingUploader());
                intent.putExtra("recordingImageURL",rec.getRecordingImageURL());
                startActivity(intent);
            }
        });

    }


  /*  public void addListNames(){

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contents.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    recording = ds.getValue(Recording.class);
                    for(int i = 0 ; i < subNames.length; i++){
                        if(recording.getRecordingUploader().equals(subNames[i])){
                            contents.add(recording);
                        }
                    }

                }
                String[] names = new String[contents.size()];

                for(int i = 0 ; i < names.length ; i++){
                    names[i] = contents.get(i).getRecordingName();
                }

                String[] uploaders = new String[contents.size()];

                for(int i = 0 ; i < names.length ; i++){
                    uploaders[i] = contents.get(i).getRecordingUploader();
                }

                String[] durations = new String[contents.size()];

                for(int i = 0 ; i < names.length ; i++){
                    durations[i] = contents.get(i).getRecordingDuration();
                }

                CustomAdapter adapter = new CustomAdapter(context,names,coverPhotos,uploaders,durations);
                //CustomAdapter adapter = new CustomAdapter(context,contentNames,coverPhotos);
                subscribedContentList.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    } */




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.activity_subscription_layout,container,false);
        addContentList();


        return view;
    }
}
