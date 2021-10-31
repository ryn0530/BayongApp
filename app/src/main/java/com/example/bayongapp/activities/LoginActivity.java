package com.example.bayongapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bayongapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);
        forgotTextLink = findViewById(R.id.forgotPassword);


        mLoginBtn.setOnClickListener(view -> {


            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required!");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password must be atleast 6 or more characters!");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), OptionActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this,"Error!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            });
        });


        mCreateBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

        forgotTextLink.setOnClickListener(view -> {

            EditText resetMail = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter Your Email To Received The Reset Link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error Reset Link Is Not Sent", Toast.LENGTH_SHORT).show());
            });
            passwordResetDialog.setNegativeButton("No", (dialogInterface, i) -> {

            });

            passwordResetDialog.create().show();
        });
    }

}
