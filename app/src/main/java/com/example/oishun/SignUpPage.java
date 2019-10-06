package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    EditText username;
    EditText password;
    Button signUpButton;
    User user;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView signInText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

       // Toast.makeText(Main2Activity.this,"asfdafs",Toast.LENGTH_LONG);
        ref = FirebaseDatabase.getInstance().getReference("user");

        username = findViewById(R.id.signUpUsername);
        password = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpButton);
        signInText = findViewById(R.id.signInText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
                startActivity(new Intent(SignUpPage.this,SignInPage.class));
            }
        });
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this,SignInPage.class);
                startActivity(intent);
            }
        });

    }

    public void register(){
        String name = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        user = new User(name,pass);

        ref.child(user.getName()).setValue(user);

    }
}
