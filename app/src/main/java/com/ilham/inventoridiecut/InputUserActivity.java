package com.ilham.inventoridiecut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.adapter.UserRecyclerAdapter;
import com.ilham.inventoridiecut.data.dataUserPPIC;
import com.ilham.inventoridiecut.dialog.FormUserPPIC;

import java.util.ArrayList;

public class InputUserActivity extends AppCompatActivity {
    private FloatingActionButton fab_add;
    private RecyclerView recyclerView;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ArrayList<dataUserPPIC> listUser = new ArrayList<>();
    private UserRecyclerAdapter adapter;
    private ProgressDialog dialog;
    EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_user);
        fab_add = findViewById(R.id.fab_add);
        cari = findViewById(R.id.cari);
        recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setTitle("Data User");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialog = new ProgressDialog(this);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormUserPPIC formUserPPIC = new FormUserPPIC("tambah");
                formUserPPIC.show(getSupportFragmentManager(), "fm");
            }
        });
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CarishowData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showData();
    }

    private void showData() {
        dialog.setMessage("Loading...");
        dialog.show();

        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linear(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CarishowData(String s) {
        dialog.setMessage("Loading...");
        dialog.show();

        Query query = database.child("user").orderByChild("nama").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linear(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void linear(DataSnapshot dataSnapshot) {
        listUser.clear();

        for (DataSnapshot item : dataSnapshot.getChildren()) {
            dataUserPPIC dataUserPPIC = item.getValue(dataUserPPIC.class);
            listUser.add(dataUserPPIC);
            dataUserPPIC.setKey(item.getKey());
        }

        adapter = new UserRecyclerAdapter(listUser, InputUserActivity.this);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
    }
}
