package com.ilham.inventoridiecut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.preference.preferences;

public class LoginActivity extends AppCompatActivity {

    EditText txt_username,
            txt_password;
    Button btn_masuk;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    TextView built;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btn_masuk = findViewById(R.id.btn_masuk);
        built = findViewById(R.id.built);
        getSupportActionBar().hide();

        built.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, BuildBy.class));
            }
        });

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                database.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String user = txt_username.getText().toString();
                        String pass = txt_password.getText().toString();
                        if (user.isEmpty()) {
                            txt_username.setError("Data tidak boleh kosong");
                            txt_username.requestFocus();
                        } else if (pass.isEmpty()) {
                            txt_password.setError("Data tidak boleh kosong");
                            txt_password.requestFocus();
                        } else if (dataSnapshot.child(user).exists()) {
                            String sandi = txt_password.getText().toString();
                            String s = dataSnapshot.child(user).child("password").getValue().toString();
                            String sebagai = dataSnapshot.child(user).child("sebagai").getValue().toString();
                            String username = dataSnapshot.child(user).child("username").getValue().toString();
                            String sap = dataSnapshot.child(user).child("sap").getValue().toString();

                            if (s.equals(sandi)) {
                                Toast.makeText(v.getContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                if (sebagai.equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                    preferences.setKey(LoginActivity.this, username);
                                    preferences.setId(LoginActivity.this, sap);
                                    preferences.setUser(LoginActivity.this, "admin");
                                    preferences.setLoginStatus(LoginActivity.this, true);
                                    finish();
                                } else if (sebagai.equals("user ppic")) {
                                    startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                    preferences.setKey(LoginActivity.this, username);
                                    preferences.setUser(LoginActivity.this, "user ppic");
                                    preferences.setId(LoginActivity.this, sap);
                                    preferences.setLoginStatus(LoginActivity.this, true);
                                    finish();
                                } else if (sebagai.equals("user mesin")) {
                                    startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                    preferences.setKey(LoginActivity.this, username);
                                    preferences.setId(LoginActivity.this, sap);
                                    preferences.setUser(LoginActivity.this, "user mesin");
                                    preferences.setLoginStatus(LoginActivity.this, true);
                                    finish();
                                }

                            } else {
                                Toast.makeText(v.getContext(), "Kata Sandi Salah", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(v.getContext(), "Username Salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getLoginStatus(this)) {
            if (preferences.getUser(this).equals("admin")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                finish();
            }
        }

    }
}
