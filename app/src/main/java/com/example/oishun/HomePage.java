package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.rock};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

       // fab.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
            }
       // });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomePage.this,R.layout.activity_content_layout,)

        //CustomAdapter adapter = new CustomAdapter(this,contentNames,coverPhotos);
        //subscribedContentList.setAdapter(adapter);



    //}
}
