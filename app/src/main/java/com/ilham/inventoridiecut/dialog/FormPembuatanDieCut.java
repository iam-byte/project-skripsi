package com.ilham.inventoridiecut.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataCustomer;
import com.ilham.inventoridiecut.data.dataPembuatanDieCut;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class FormPembuatanDieCut extends DialogFragment {
    EditText txt_no_mc,
            txt_no_diecut,
            txt_tgl_buat,
            txt_tgl_selesai,
            txt_papan,
            txt_pisau,
            txt_creasing,
            txt_mold;
    Button btn_tgl_buat,
            btn_tgl_selesai,
            btn_simpan;
    Spinner sp_customer,
            sp_lokasi;

    String _key, _noMc, _DieCUt, _customer, _tglBuat, _tglSelesai, _lokasi, _mold, _papan, _pisau, _creasing;
    Context context;
    String pilih;

    public FormPembuatanDieCut(String _key, String _noMc, String _DieCUt, String _customer, String _tglBuat, String _tglSelesai, String _lokasi, String _mold, String _papan, String _pisau, String _creasing, Context context, String pilih) {
        this._key = _key;
        this._noMc = _noMc;
        this._DieCUt = _DieCUt;
        this._customer = _customer;
        this._tglBuat = _tglBuat;
        this._tglSelesai = _tglSelesai;
        this._lokasi = _lokasi;
        this._mold = _mold;
        this._papan = _papan;
        this._pisau = _pisau;
        this._creasing = _creasing;
        this.context = context;
        this.pilih = pilih;
    }

    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> menampilkanSpinner = new ArrayList<>();


    public FormPembuatanDieCut(Context context, String pilih) {
        this.context = context;
        this.pilih = pilih;
    }

    Calendar calendar = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_pembuatan, container, false);
        txt_no_mc = v.findViewById(R.id.txt_no_mc);
        txt_no_diecut = v.findViewById(R.id.txt_no_diecut);
        txt_tgl_buat = v.findViewById(R.id.txt_tgl_buat);
        txt_tgl_selesai = v.findViewById(R.id.txt_tgl_selesai);
        txt_papan = v.findViewById(R.id.txt_papan);
        txt_pisau = v.findViewById(R.id.txt_pisau);
        txt_creasing = v.findViewById(R.id.txt_creasing);
        txt_mold = v.findViewById(R.id.txt_mold);


        btn_tgl_buat = v.findViewById(R.id.btn_tgl_buat);
        btn_tgl_selesai = v.findViewById(R.id.btn_tgl_selesai);
        btn_simpan = v.findViewById(R.id.btn_simpan);

        sp_customer = v.findViewById(R.id.sp_customer);
        sp_lokasi = v.findViewById(R.id.sp_lokasi);
        context = v.getContext();

        txt_tgl_buat.setText(simpleDateFormat.format(calendar.getTime()));
        txt_tgl_selesai.setText(simpleDateFormat.format(calendar.getTime()));

        String[] lokasi = {"GD-1", "GD-2", "GD-3", "GD-4", "GD-5", "GD-6"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(lokasi));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, arrayList);
        sp_lokasi.setAdapter(arrayAdapter);

        showItemSpinner();

        if (pilih.equals("ubah")) {
            txt_no_mc.setText(_noMc);
            txt_no_diecut.setText(_DieCUt);
            txt_tgl_buat.setText(_tglBuat);
            txt_tgl_selesai.setText(_tglSelesai);
            txt_papan.setText(_papan);
            txt_pisau.setText(_pisau);
            txt_creasing.setText(_creasing);
            txt_mold.setText(_mold);
            setSpinnerText(sp_lokasi, _lokasi);

        }

        btn_tgl_buat.setOnClickListener(new View.OnClickListener() {
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
                        txt_tgl_buat.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });

        btn_tgl_selesai.setOnClickListener(new View.OnClickListener() {
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
                        txt_tgl_selesai.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String noMc = txt_no_mc.getText().toString();
                String DieCUt = txt_no_diecut.getText().toString();
                String customer = sp_customer.getSelectedItem().toString();
                String tglBuat = txt_tgl_buat.getText().toString();
                String tglSelesai = txt_tgl_selesai.getText().toString();
                String lokasi = sp_lokasi.getSelectedItem().toString();
                String mold = txt_mold.getText().toString();
                String papan = txt_papan.getText().toString();
                String pisau = txt_pisau.getText().toString();
                String creasing = txt_creasing.getText().toString();

                if (noMc.isEmpty()) {
                    txt_no_mc.setError("Data tidak boleh kosong");
                    txt_no_mc.requestFocus();
                } else if (DieCUt.isEmpty()) {
                    txt_no_diecut.setError("Data tidak boleh kosong");
                    txt_no_diecut.requestFocus();
                } else if (tglBuat.isEmpty()) {
                    txt_tgl_buat.setError("Data tidak boleh kosong");
                    txt_tgl_buat.requestFocus();
                } else if (tglSelesai.isEmpty()) {
                    txt_tgl_selesai.setError("Data tidak boleh kosong");
                    txt_tgl_selesai.requestFocus();
                } else if (mold.isEmpty()) {
                    txt_mold.setError("Data tidak boleh kosong");
                    txt_mold.requestFocus();
                } else if (papan.isEmpty()) {
                    txt_papan.setError("Data tidak boleh kosong");
                    txt_papan.requestFocus();
                } else if (pisau.isEmpty()) {
                    txt_pisau.setError("Data tidak boleh kosong");
                    txt_pisau.requestFocus();
                } else if (creasing.isEmpty()) {
                    txt_creasing.setError("Data tidak boleh kosong");
                    txt_creasing.requestFocus();
                } else if (sp_customer.getSelectedItem() == null) {
                    Toast.makeText(v.getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (sp_lokasi.getSelectedItem() == null) {
                    Toast.makeText(v.getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    if (pilih.equals("tambah")) {
                        dismiss();
                        database.child("pembuatan-die-cut").push().setValue(new dataPembuatanDieCut(noMc,
                                DieCUt,
                                customer,
                                tglBuat,
                                tglSelesai,
                                lokasi,
                                mold,
                                papan,
                                pisau,
                                creasing)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        database.child("pembuatan-die-cut").child(_key).setValue(new dataPembuatanDieCut(_noMc,
                                _DieCUt,
                                _customer,
                                _tglBuat,
                                _tglSelesai,
                                _lokasi,
                                _mold,
                                _papan,
                                _pisau,
                                _creasing)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Data gagal diubah", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            }
        });
        return v;
    }

    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showItemSpinner() {
        database.child("customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menampilkanSpinner.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    menampilkanSpinner.add(item.child("customer").getValue(String.class));
                }
                ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.item_spinner, menampilkanSpinner);
                sp_customer.setAdapter(adapter);
                if(pilih.equals("ubah")){
                    setSpinnerText(sp_customer, _customer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setSpinnerText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }
}
