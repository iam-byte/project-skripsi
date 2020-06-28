package com.ilham.inventoridiecut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.adapter.MesinRecyclerAdapter;
import com.ilham.inventoridiecut.data.dataMesin;
import com.ilham.inventoridiecut.dialog.FormMesin;

import java.util.ArrayList;

public class InputMesinActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab_add;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog dialog;
    private ArrayList<dataMesin> listMesin = new ArrayList<>();
    private MesinRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mesin);

        getSupportActionBar().setTitle("Data Mesin");

        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialog = new ProgressDialog(this);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormMesin formMesin = new FormMesin("tambah");
                formMesin.show(getSupportFragmentManager(),"fm-mesin");
            }
        });

        showData();
    }

    private void showData() {
        dialog.setMessage("Loading...");
        dialog.show();
        database.child("mesin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMesin.clear();
                dialog.setMessage("Loading...");
                dialog.show();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    dataMesin dataMesin  = item.getValue(dataMesin.class);
                    listMesin.add(dataMesin);
                    dataMesin.setKey(item.getKey());
                }
                adapter = new MesinRecyclerAdapter(listMesin,InputMesinActivity.this);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
