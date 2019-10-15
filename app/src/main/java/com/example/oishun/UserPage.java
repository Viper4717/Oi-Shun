/*
 * User Page Activity Control
 *
 * @author A.M. Aahad
 * */

package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserPage extends AppCompatActivity {

    ListView personalContentView;
    String[] personalContentNames;
    private int[] coverPhotos = {R.drawable.music_icon_image};
    ImageView userImage;
    TextView userNameText;
    Button subscribeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        userImage = (ImageView) findViewById(R.id.userImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        Intent tempIntent = getIntent();
        String userName = tempIntent.getStringExtra("user_name");
        userNameText.setText(userName);

        personalContentNames = getResources().getStringArray(R.array.contentNames);

        personalContentView = findViewById(R.id.personal_contents);

        UserAdapter userAdapter = new UserAdapter(this,personalContentNames,coverPhotos);
        personalContentView.setAdapter(userAdapter);

    }
}
