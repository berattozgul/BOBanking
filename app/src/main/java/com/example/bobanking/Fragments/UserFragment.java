package com.example.bobanking.Fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bobanking.Models.User;
import com.example.bobanking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private TextView txtProgress, txtWelcome;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ObjectAnimator objectAnimator;

    private User u;

    private int balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        swipeRefreshLayout = v.findViewById(R.id.user_fragment_refresh_layout);

        txtWelcome = v.findViewById(R.id.welcome_txt);
        txtProgress = v.findViewById(R.id.txtProgress);
        progressBar = v.findViewById(R.id.progressBar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String text1 = "Welcome, <b><i>" + u.getName() + "</i></b>";
                        txtWelcome.setText(Html.fromHtml(text1));
                        balance = u.getBalance();
                        txtProgress.setText(balance + " TL");
                        objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", balance);
                        objectAnimator.start();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                String text1 = "Welcome, <b><i>" + u.getName() + "</i></b>";
                txtWelcome.setText(Html.fromHtml(text1));
                balance = u.getBalance();
                txtProgress.setText(balance + " TL");
                objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", balance);
                objectAnimator.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

}