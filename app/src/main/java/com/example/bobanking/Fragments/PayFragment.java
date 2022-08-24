package com.example.bobanking.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bobanking.R;
import com.example.bobanking.Views.LoginActivity;
import com.example.bobanking.Views.TransferActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PayFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    Button sendSomeoneBt, logoutBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pay, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        sendSomeoneBt = v.findViewById(R.id.pay_frag_send_someone_bt);
        sendSomeoneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TransferActivity.class));
            }
        });
        logoutBt = v.findViewById(R.id.pay_frag_log_out);
        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are you sure to logout?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
                builder.show();
            }
        });

        return v;
    }
}