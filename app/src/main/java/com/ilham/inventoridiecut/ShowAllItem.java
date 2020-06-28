package com.ilham.inventoridiecut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.adapter.ItemRecyclerViewAdapter;
import com.ilham.inventoridiecut.data.dataPembuatanDieCut;

import java.util.ArrayList;

public class ShowAllItem extends AppCompatActivity {
    EditText cari;
    RecyclerView rv_item;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ItemRecyclerViewAdapter adapter;
    ArrayList<dataPembuatanDieCut> list = new ArrayList<>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_item);
        getSupportActionBar().setTitle("Die Cut");
        cari = findViewById(R.id.cari);
        rv_item = findViewById(R.id.rv_item);
        dialog = new ProgressDialog(this);
        rv_item.setLayoutManager(new GridLayoutManager(this, 1));
        rv_item.setItemAnimator(new DefaultItemAnimator());

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    showData();
                } else {
                    showDataCari(s.toString());
                }

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
        database.child("pembuatan-die-cut").addValueEventListener(new ValueEventListener() {
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
        list.clear();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            dataPembuatanDieCut data = item.getValue(dataPembuatanDieCut.class);
            if (data != null) {
                data.setKey(item.getKey());
            }
            list.add(data);
        }
        dialog.dismiss();
        adapter = new ItemRecyclerViewAdapter(list, ShowAllItem.this);
        rv_item.setAdapter(adapter);
    }

    private void showDataCari(String s) {
        dialog.setMessage("Loading...");
        dialog.show();
        Query query = database.child("pembuatan-die-cut").orderByChild("nodc").startAt(s).endAt(s + "/uf8ff");
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
}
