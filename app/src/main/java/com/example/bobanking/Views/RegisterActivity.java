package com.example.bobanking.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bobanking.Models.User;
import com.example.bobanking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private User u;


    private TextInputLayout inputName, inputEmail, inputPass, inputrePass, inputNickname;
    private EditText nameET, emailET, passET, rePassET, nicknameET;
    private String txtName, txtEmail, txtPass, txtrePass, txtNickName;

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        inputNickname = findViewById(R.id.register_nickname);
        inputName = findViewById(R.id.register_inputName);
        inputEmail = findViewById(R.id.register_inputEmail);
        inputPass = findViewById(R.id.register_inputPass);
        inputrePass = findViewById(R.id.register_inputrePass);

        nicknameET = findViewById(R.id.nickNameET);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        rePassET = findViewById(R.id.rePassET);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void register(View view) {
        txtName = nameET.getText().toString();
        txtEmail = emailET.getText().toString();
        txtPass = passET.getText().toString();
        txtrePass = rePassET.getText().toString();
        txtNickName = nicknameET.getText().toString();

        if (!TextUtils.isEmpty(txtName)) {
            if (!TextUtils.isEmpty(txtNickName)) {
                if (!TextUtils.isEmpty(txtEmail)) {
                    if (!TextUtils.isEmpty(txtPass)) {
                        if (!TextUtils.isEmpty(txtrePass)) {
                            if (txtPass.equals(txtrePass)) {
                                firebaseAuth.createUserWithEmailAndPassword(txtEmail, txtPass)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    firebaseUser = firebaseAuth.getCurrentUser();
                                                    if (firebaseUser != null) {
                                                        u = new User(firebaseUser.getUid(), txtName, txtNickName, txtEmail, 0);
                                                        databaseReference.child(firebaseUser.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Registered Succesfully.", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                                } else {
                                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(this, "Passwords are not equal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            inputPass.setError("Please don't leave blank!");
                        }
                    } else {
                        inputEmail.setError("Please don't leave blank");
                    }
                } else {
                    inputNickname.setError("Please don't leave blank");
                }
            } else {
                inputName.setError("Please don't leave blank");
            }
        }
    }
}