package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    ImageButton backButton;
    ImageButton searchButton;
    EditText searchBox;
    String searchKey;
    List<User> userResults;
    User tempUser;
    ListView listView;
    String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backButton = (ImageButton)findViewById(R.id.backButton);
        searchButton = (ImageButton)findViewById(R.id.searchButtonYellow);
        searchBox = findViewById(R.id.search_src_text);
        userResults = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userResults.clear();
                searchKey = searchBox.getText().toString().trim();
                Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("name").startAt(searchKey).endAt(searchKey+"\uf8ff");
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = names[position];
                Toast.makeText(Search.this, str, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Search.this,UserPage.class);
                intent.putExtra("user_search", str);
                //intent.putExtra("recordingDuration",rec.getRecordingDuration());
                startActivity(intent);
            }
        });




    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userResults.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tempUser = ds.getValue(User.class);
                    userResults.add(tempUser);
                }

                names = new String[userResults.size()];

                for (int i = 0; i < names.length; i++) {
                    names[i] = userResults.get(i).getName();
                }

              /*  String[] uploaders = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    uploaders[i] = personalRecordings.get(i).getRecordingUploader();
                }

                String[] durations = new String[personalRecordings.size()];

                for(int i = 0 ; i < names.length ; i++){
                    durations[i] = personalRecordings.get(i).getRecordingDuration();
                }
*/
                //UserAdapter userAdapter = new UserAdapter(UserPage.this, names, coverPhotos);
                //personalContentView.setAdapter(userAdapter);



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search.this,R.layout.custom_textview, R.id.tv, names);
                listView.setAdapter(adapter);



            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
