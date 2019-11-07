/*
 * App Starting Activity Control
 *
 * @author A.M. Aahad
 * */


package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference ref;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("user");

        //startActivity(new Intent(getApplicationContext(), SignInPage.class));

        //Checking if user is already logged in
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    String userEmail = firebaseUser.getEmail();
                    Query query = ref.orderByChild("email").equalTo(userEmail);
                    query.addListenerForSingleValueEvent(eventListener);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, SignInPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                    OwnProfileValue.userName = user.getName();
                }
                Toast.makeText(MainActivity.this, "Signed in as "+OwnProfileValue.userName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
}
