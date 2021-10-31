package com.example.bayongapp.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bayongapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String userID;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.loginBtn);
        mLoginBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);



            mRegisterBtn.setOnClickListener(view -> {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();



                if (TextUtils.isEmpty(fullName)) {
                    mFullName.setError("Password is Required!");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required!");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    mPhone.setError("Phone Number is Required!");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be atleast 6 or more characters!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //registering to firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                    //send verification link

                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fName", fullName);
                        user.put("email", email);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "onSuccess : user is created for " + userID)).addOnFailureListener(e -> Log.d(TAG, "on:Failure :" + e.toString()));
                        startActivity(new Intent(getApplicationContext(), OptionActivity.class));


                    } else {
                        Toast.makeText(RegisterActivity.this, "Error!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            });

            mLoginBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));

    }

}