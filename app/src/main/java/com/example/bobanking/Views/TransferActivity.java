package com.example.bobanking.Views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bobanking.Models.Logs;
import com.example.bobanking.Models.User;
import com.example.bobanking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference, reference2, reference3, reference4, dateReference;

    private List<String> userList;
    private Button sendButton;
    private Logs logs;
    private Spinner spinner;
    private SeekBar seekbar;

    private User u, u2;
    private int max, toTransfer, userBalance, newBalance, targetBalance;
    private String transferID, userName;

    //    private List<String> userIDs;
    private List<Integer> userBalances;
    private List<User> allUsers;
    private TextView textView;

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        spinner = findViewById(R.id.transfer_activity_spinner);
        textView = findViewById(R.id.textview1);
        seekbar = findViewById(R.id.seekBar2);
        sendButton = findViewById(R.id.sendButton);

        //userIDs = new ArrayList<>();
        userBalances = new ArrayList<>();
        allUsers = new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        init();


        reference = firebaseDatabase.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    u = snap.getValue(User.class);
                    if (!u.getId().equals(firebaseUser.getUid())) {
                        userList.add(u.getName());
                        //userIDs.add(u.getId());
                        allUsers.add(u);
                    } else {
                        u2 = snap.getValue(User.class);
                        userBalance = u2.getBalance();
                        max = userBalance;
                        seekbar.setMax(max);
                    }
                }
                ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(TransferActivity.this, android.R.layout.simple_spinner_item, userList);
                userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reference2 = firebaseDatabase.getReference("Users").child(allUsers.get(i).getId());
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transferID = snapshot.child("id").getValue(String.class);
                        userName = snapshot.child("name").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(String.valueOf(i));
                toTransfer = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // yenibalance = hedefbalance + gönderilen;
    // kullanıcıBalance=kullanıcıBalance -gönderilen
    public void transferToAccount(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        reference3 = firebaseDatabase.getReference("Users").child(transferID);
        reference4 = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
        builder.setMessage("Are you sure do you want send money to " + userName + " ?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        newBalance = snapshot.child("balance").getValue(Integer.class) + toTransfer;
                        HashMap hashMap = new HashMap();
                        hashMap.put("balance", newBalance);
                        reference3.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.e("Task = ", "Task completed");
                                } else {
                                    Log.e("Task = ", task.getException().toString());
                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int newUserBalance = snapshot.child("balance").getValue(Integer.class) - toTransfer;
                        HashMap hashMap = new HashMap();
                        hashMap.put("balance", newUserBalance);
                        reference4.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Log.e("Task = ", "Task completed");
                                    transactionsLog(transferID,firebaseUser.getUid(),toTransfer);
                                } else {
                                    Log.e("Task = ", task.getException().toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });
        builder.show();
    }

    public void transactionsLog(String to,String from,int money) {
        Date date = new Date();
        dateReference=firebaseDatabase.getReference("TransactionsLog");
        String key=dateReference.push().getKey();
        logs=new Logs(to,from,date.toString(),money);
        dateReference.child(key).setValue(logs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(TransferActivity.this, "Transferred Succesfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TransferActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}