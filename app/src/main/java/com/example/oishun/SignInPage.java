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
        //Toast.makeText(MainActivity.this,"Dhuksi",Toast.LENGTH_LONG).show();
        final String name = usernameText.getText().toString();
        password = passwordText.getText().toString();

        // logInUser = new User(name,password);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    logInUser = ds.getValue(User.class);

                    if(logInUser.getName().equals(name) && logInUser.getPasswrod().equals(password)) {
                        Toast.makeText(SignInPage.this,"Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignInPage.this,HomePage.class);
                        flag = 1;
                        startActivity(intent);
                        break;
                    }

                }
                if(flag == 0) {
                    Toast.makeText(SignInPage.this,"Unsuccessful",Toast.LENGTH_LONG).show();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
