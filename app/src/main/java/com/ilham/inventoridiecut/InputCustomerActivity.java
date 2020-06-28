package com.ilham.inventoridiecut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.ilham.inventoridiecut.adapter.CustomerRecyclerAdapter;
import com.ilham.inventoridiecut.data.dataCustomer;
import com.ilham.inventoridiecut.dialog.FormCustomer;

import java.util.ArrayList;

public class InputCustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab_add;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private CustomerRecyclerAdapter adapter;
    private ProgressDialog dialog;
    private ArrayList<dataCustomer> listCustomer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_customer);
        getSupportActionBar().setTitle("Data Customer");

        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialog = new ProgressDialog(this);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormCustomer customer = new FormCustomer("tambah",InputCustomerActivity.this);
                customer.show(getSupportFragmentManager(), "fm-customer");
            }
        });

        showData();

    }

    private void showData() {
        dialog.setMessage("Loading...");
        dialog.show();
        database.child("customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCustomer.clear();
                dialog.setMessage("Loading...");
                dialog.show();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    dataCustomer dataCustomer = item.getValue(dataCustomer.class);
                    listCustomer.add(dataCustomer);
                    dataCustomer.setKey(item.getKey());
                }
                adapter = new CustomerRecyclerAdapter(listCustomer, InputCustomerActivity.this);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
