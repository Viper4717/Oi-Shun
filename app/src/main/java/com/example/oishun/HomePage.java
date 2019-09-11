package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomePage extends AppCompatActivity {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.rock};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        subscribedContentList = (ListView) findViewById(R.id.subscription_contents);
        contentNames = getResources().getStringArray(R.array.contentNames);

        subscribedContentList = (ListView) findViewById(R.id.subscription_contents);
        contentNames = getResources().getStringArray(R.array.contentNames);

        CustomAdapter adapter = new CustomAdapter(this,contentNames,coverPhotos);
        subscribedContentList.setAdapter(adapter);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomePage.this,R.layout.activity_content_layout,)

        //CustomAdapter adapter = new CustomAdapter(this,contentNames,coverPhotos);
        //subscribedContentList.setAdapter(adapter);



    }
}
