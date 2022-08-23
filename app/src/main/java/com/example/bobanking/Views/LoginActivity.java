package com.example.bobanking.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bobanking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextInputLayout inputEmail, inputPass;
    private EditText editEmail, editPass;
    private String txtEmail, txtPass;

    public void init() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        inputEmail = findViewById(R.id.login_inputEmail);
        inputPass = findViewById(R.id.login_inputPass);
        editEmail = findViewById(R.id.login_EmailET);
        editPass = findViewById(R.id.login_passET);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        if (mUser != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }
    }

    public void login(View view) {
        txtEmail = editEmail.getText().toString();
        txtPass = editPass.getText().toString();
        if (!TextUtils.isEmpty(txtEmail)) {
            if (!TextUtils.isEmpty(txtPass)) {
                mAuth.signInWithEmailAndPassword(txtEmail, txtPass)
                        .addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,
                                                    "Log in succesfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(LoginActivity.this,
                                                    MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException()
                                                    .getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
            } else {
                inputPass.setError("Don't leave it blank");
            }
        } else {
            inputEmail.setError("Don't leave it blank");
        }
    }

    public void reg(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
}