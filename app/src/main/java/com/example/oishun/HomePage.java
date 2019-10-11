package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.rock};
    FloatingActionButton recordActionButton;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent tempIntent = getIntent();
        userName = tempIntent.getStringExtra("user_name");
        //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        recordActionButton = findViewById(R.id.recordActionButton);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        recordActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, VoiceReorder.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomePage.this,R.layout.activity_content_layout,)

        //CustomAdapter adapter = new CustomAdapter(this,contentNames,coverPhotos);
        //subscribedContentList.setAdapter(adapter);



    }
}
