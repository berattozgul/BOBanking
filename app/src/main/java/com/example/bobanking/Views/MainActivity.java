package com.example.bobanking.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bobanking.Fragments.PayFragment;
import com.example.bobanking.Fragments.UserFragment;
import com.example.bobanking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private UserFragment userFragment;
    private PayFragment payFragment;

    private FragmentTransaction transaction;
    private BottomNavigationView bottomNavigationView;

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        bottomNavigationView = findViewById(R.id.main_activity_bottomView);

        userFragment = new UserFragment();
        payFragment = new PayFragment();

        setFragment(userFragment);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_ic_pay:
                                setFragment(payFragment);
                                return true;
                            case R.id.bottom_nav_ic_profile:
                                setFragment(userFragment);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        if (firebaseUser == null) {
            Toast.makeText(this, "Please Log In to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void setFragment(Fragment frag) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.main_activity_frameLayout, frag);
        transaction.commit();
    }
}