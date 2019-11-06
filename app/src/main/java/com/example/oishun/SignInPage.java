package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInPage extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    User logInUser;
    Button signInButton;
    TextView signUpText;
    DatabaseReference ref;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        //Toast.makeText(MainActivity.this,"asfdafs",Toast.LENGTH_LONG).show();

        usernameText = findViewById(R.id.signInUsername);
        passwordText = findViewById(R.id.signInPassword);
        signInButton = (Button)findViewById(R.id.signInButton);
        signUpText = findViewById(R.id.signUpText);

        ref = FirebaseDatabase.getInstance().getReference().child("user");
        firebaseAuth = FirebaseAuth.getInstance();

        /*firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Toast.makeText(SignInPage.this, "You are logged in", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignInPage.this, "Please login", Toast.LENGTH_SHORT).show();
                }
            }
        };*/

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"tip porse",Toast.LENGTH_LONG).show();
                logIn();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInPage.this,SignUpPage.class);
                startActivity(intent);
            }
        });


    }
    int flag = 0;
    String password;
    public void logIn(){
        //Toast.makeText(this,"Dhuksi",Toast.LENGTH_LONG).show();
        final String name = usernameText.getText().toString();
        password = passwordText.getText().toString();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    logInUser = ds.getValue(User.class);

                    if(logInUser.getName().equals(name)) {
                        //Toast.makeText(SignInPage.this,"Successful",Toast.LENGTH_LONG).show();
                        firebaseAuth.signInWithEmailAndPassword(logInUser.getEmail(), password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(SignInPage.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    OwnProfileValue.userName = name;
                                    Intent intent = new Intent(SignInPage.this, HomePage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    flag = 1;
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    if(flag == 1) break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }*/
}
