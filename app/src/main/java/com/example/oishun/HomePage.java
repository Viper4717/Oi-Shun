package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView subscribedContentList;
    private String[] contentNames;
    private int[] coverPhotos = {R.drawable.music_icon_image};
    FloatingActionButton recordActionButton;
    DrawerLayout drawerLayout;
    NavigationView navigationBar;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setNavigationViewListener();
        Intent tempIntent = getIntent();
        userName = tempIntent.getStringExtra("user_name");
        //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        recordActionButton = findViewById(R.id.recordActionButton);
        drawerLayout = findViewById(R.id.DrawerLayout);
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

    private void setNavigationViewListener() {
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        navigationBar.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {
            case R.id.userProfile: {
                Intent intent = new Intent(HomePage.this, UserPage.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
                break;
            }
            case R.id.signOut: {
                finish();
                break;
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
