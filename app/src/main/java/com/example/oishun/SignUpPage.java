/*
 * Sign Up Page activity control
 *
 * @author A.M. Aahad, KB Yamin Rupom
 * */

package com.example.oishun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SignUpPage extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText retypePassword;
    EditText userEmail;
    ImageView userAvatar;
    Button avatarChooseButton;
    Button signUpButton;
    User user;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference ref;
    TextView signInText;
    int RESULT_LOAD_IMAGE;
    ProgressDialog progressDialog;
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //Seeking permission from device to write and record
        if(!checkPermissionFromDevice())
            requestPermissionFromDevice();

       // Toast.makeText(Main2Activity.this,"asfdafs",Toast.LENGTH_LONG);
        storageReference = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.signUpUsername);
        password = findViewById(R.id.signUpPassword);
        userEmail = findViewById(R.id.signUpEmail);
        signUpButton = findViewById(R.id.signUpButton);
        signInText = findViewById(R.id.signInText);
        retypePassword = findViewById(R.id.signUpConfirmPassword);
        userAvatar = findViewById(R.id.userAvatar);
        userAvatar.setTag("empty");
        avatarChooseButton = findViewById(R.id.avatarChoosebutton);
        progressDialog = new ProgressDialog(this);
        RESULT_LOAD_IMAGE = 1;

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
                //if(reg) startActivity(new Intent(SignUpPage.this,SignInPage.class));
            }
        });
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this,SignInPage.class);
                startActivity(intent);
            }
        });

        avatarChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent, RESULT_LOAD_IMAGE);
            }
        });

    }

    //Method to check write and record permission from device
    private boolean checkPermissionFromDevice() {
        int readExternalStorageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return readExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    //Method to request write and record permission from device
    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    public void register(){
        String name = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        String confirmPass = retypePassword.getText().toString().trim();

        if(email.isEmpty()){
            userEmail.setError("Please enter an e-mail");
            userEmail.requestFocus();
        }
        else if(name.isEmpty()){
            username.setError("Please enter a username");
            username.requestFocus();
        }
        else if(pass.isEmpty()){
            password.setError("Please enter a password");
            password.requestFocus();
        }
        else if(confirmPass.isEmpty()){
            retypePassword.setError("Please re-type the password");
            retypePassword.requestFocus();
        }
        else if(pass.equals(confirmPass)) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setUserAvatarURL(null);

            String avatarPath = userAvatar.getTag().toString();

            if(!avatarPath.equals("empty")) {
                progressDialog.setMessage("Registering...");
                progressDialog.show();

                final StorageReference filePath = storageReference.child("User Avatars").child(user.getName())
                        .child(user.getName() + ".jpg");

                Uri uri = Uri.fromFile(new File(avatarPath));

                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadURL = uri.toString();
                                user.setUserAvatarURL(downloadURL);
                                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        ref.child(user.getName()).setValue(user);
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpPage.this, "Registered Successfully!"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });

                filePath.putFile(uri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpPage.this, "Unsuccessful! Check connection and try again"
                                , Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                ref.child(user.getName()).setValue(user);
                Toast.makeText(SignUpPage.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String imagePath = GetFilePathFromDevice.getPath(this, selectedImage);
            userAvatar.setImageURI(selectedImage);
            userAvatar.setTag(imagePath);
        }
    }
}
