package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.music_icon_image};
    FloatingActionButton recordActionButton;
    DrawerLayout drawerLayout;
    NavigationView navigationBar;
    String userName;
    ImageButton menuButton;
    ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent tempIntent = getIntent();
        userName = tempIntent.getStringExtra("user_name");

        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        navigationBar.setNavigationItemSelectedListener(this);
        recordActionButton = findViewById(R.id.recordActionButton);
        drawerLayout = findViewById(R.id.DrawerLayout);
        menuButton = (ImageButton) findViewById(R.id.menuButton);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        recordActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, VoiceRecorder.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,Search.class);
                startActivity(intent);
            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomePage.this,R.layout.activity_content_layout,)

        //CustomAdapter adapter = new CustomAdapter(this,contentNames,coverPhotos);
        //subscribedContentList.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        if(id == R.id.userProfile){
            Intent intent = new Intent(HomePage.this, UserPage.class);
            intent.putExtra("user_name", userName);
            intent.putExtra("own_profile", "yes");
            startActivity(intent);
        }
        else if(id == R.id.signOut){
            finish();
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
