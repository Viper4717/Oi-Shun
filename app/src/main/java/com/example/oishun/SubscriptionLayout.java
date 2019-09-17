package com.example.oishun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SubscriptionLayout extends Fragment  {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.rock};
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void addContentList(){
        final Context context = getActivity().getApplicationContext();
        subscribedContentList = (ListView) view.findViewById(R.id.subscription_contents);
        contentNames = getResources().getStringArray(R.array.contentNames);

        subscribedContentList = (ListView)  view.findViewById(R.id.subscription_contents);
        contentNames = getResources().getStringArray(R.array.contentNames);

        CustomAdapter adapter = new CustomAdapter(context,contentNames,coverPhotos);
        subscribedContentList.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(context,VoiceReorder.class);
                startActivity(intent);
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.activity_subscription_layout,container,false);
        addContentList();


        return view;
    }
}
