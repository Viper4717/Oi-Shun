/*
 * User Page Activity Control
 *
 * @author A.M. Aahad
 * */

package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class UserPage extends AppCompatActivity {

    ListView personalContentView;
    String[] personalContentNames;
    private int[] coverPhotos = {R.drawable.music_icon_image};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        personalContentNames = getResources().getStringArray(R.array.contentNames);

        personalContentView = findViewById(R.id.personal_contents);

        UserAdapter userAdapter = new UserAdapter(this,personalContentNames,coverPhotos);
        personalContentView.setAdapter(userAdapter);

    }
}
