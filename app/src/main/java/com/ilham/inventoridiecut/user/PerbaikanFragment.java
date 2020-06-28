package com.ilham.inventoridiecut.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.adapter.PerbaikanRecyclerAdapter;
import com.ilham.inventoridiecut.data.dataPerbaikan;
import com.ilham.inventoridiecut.dialog.FormPerbaikan;
import com.ilham.inventoridiecut.preference.preferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PerbaikanFragment extends Fragment {
    EditText cari;
    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    ProgressDialog dialog;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataPerbaikan> list = new ArrayList<>();
    PerbaikanRecyclerAdapter adapter;
    Context context;

    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date date;

    EditText et_search;
    Button btn_search_date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perbaikan, container, false);

        date = new Date();

        et_search = v.findViewById(R.id.et_search);
        btn_search_date = v.findViewById(R.id.btn_search_date);

        cari = v.findViewById(R.id.cari);
        recyclerView = v.findViewById(R.id.recyclerView);
        fab_add = v.findViewById(R.id.fab_add);
        context = v.getContext();

        et_search.setText(simpleDateFormat.format(date.getTime()));
        btn_search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int tahun = calendar.get(Calendar.YEAR);
                int bulan = calendar.get(Calendar.MONTH);
                int hari = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        et_search.setText(simpleDateFormat.format(calendar.getTime()));
                        showData(et_search.getText().toString());
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });

        dialog = new ProgressDialog(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (preferences.getUser(getContext()).equals("admin")) {
            fab_add.setVisibility(View.GONE);
        }

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormPerbaikan formPerbaikan = new FormPerbaikan("tambah", context);
                formPerbaikan.show(getChildFragmentManager(), "fm-perbaikan");
            }
        });

        if (preferences.getUser(v.getContext()).equals("user mesin")) {
            fab_add.setVisibility(View.GONE);
        }

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    showData(et_search.getText().toString());
                } else {
                    QueryDataSearchPerbaikan(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showData(et_search.getText().toString());
        return v;
    }

    private void QueryDataSearchPerbaikan(String s) {
        Query query = database.child("perbaikan").orderByChild("nomc").startAt(s).endAt(s + "/uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lisenerData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(String s) {
        Query query = database.child("perbaikan").orderByChild("tglperbaikan").equalTo(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lisenerData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lisenerData(DataSnapshot dataSnapshot) {
        list.clear();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            dataPerbaikan dataPerbaikan = item.getValue(dataPerbaikan.class);
            if (dataPerbaikan != null) {
                dataPerbaikan.setKey(item.getKey());
            }
            list.add(dataPerbaikan);
        }
        adapter = new PerbaikanRecyclerAdapter(context, list, this);
        recyclerView.setAdapter(adapter);
    }


}
