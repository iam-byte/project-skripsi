package com.ilham.inventoridiecut.dialog;

import android.app.ActionBar;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataPengambilan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FormPengambilan extends DialogFragment {
    Spinner sp_mesin;
    Spinner sp_customer;
    EditText txt_no_mc, txt_no_diecut, txt_tgl_keluar, txt_operator, txt_nik;
    Button btn_tgl_keluar, btn_simpan;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> listMesin = new ArrayList<>();
    ArrayList<String> listCustomer = new ArrayList<>();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    SimpleDateFormat simpleDateMonth = new SimpleDateFormat("MMMM", id);

    final Calendar calendar = Calendar.getInstance();

    String _key_, _txt_no_mc_,
            _txt_no_diecut_,
            _sp_customer_,
            _txt_tgl_keluar_,
            _sp_mesin_,
            _txt_operator_,
            _txt_nik_;

    String pilih;
    Context context;


    public FormPengambilan(String pilih, Context context) {
        this.pilih = pilih;
        this.context = context;
    }

    public FormPengambilan(String _key_, String _txt_no_mc_, String _txt_no_diecut_, String _sp_customer_, String _txt_tgl_keluar_, String _sp_mesin_, String _txt_operator_, String _txt_nik_, String pilih, Context context) {
        this._key_ = _key_;
        this._txt_no_mc_ = _txt_no_mc_;
        this._txt_no_diecut_ = _txt_no_diecut_;
        this._sp_customer_ = _sp_customer_;
        this._txt_tgl_keluar_ = _txt_tgl_keluar_;
        this._sp_mesin_ = _sp_mesin_;
        this._txt_operator_ = _txt_operator_;
        this._txt_nik_ = _txt_nik_;
        this.pilih = pilih;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_pengambilan, null);
        sp_mesin = v.findViewById(R.id.sp_mesin);
        sp_customer = v.findViewById(R.id.sp_customer);

        txt_no_mc = v.findViewById(R.id.txt_no_mc);
        txt_no_diecut = v.findViewById(R.id.txt_no_diecut);
        txt_tgl_keluar = v.findViewById(R.id.txt_tgl_keluar);
        txt_operator = v.findViewById(R.id.txt_operator);
        txt_nik = v.findViewById(R.id.txt_nik);


        btn_simpan = v.findViewById(R.id.btn_simpan);
        btn_tgl_keluar = v.findViewById(R.id.btn_tgl_keluar);

        context = v.getContext();

        dataSpinner(v, listMesin, sp_mesin, "mesin", _sp_mesin_);
        dataSpinner(v, listCustomer, sp_customer, "customer", _sp_customer_);

        btn_tgl_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tahun = calendar.get(Calendar.YEAR);
                int bulan = calendar.get(Calendar.MONTH);
                int hari = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        txt_tgl_keluar.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });

        if (pilih.equals("ubah")) {
            txt_no_mc.setText(_txt_no_mc_);
            txt_no_diecut.setText(_txt_no_diecut_);
            txt_tgl_keluar.setText(_txt_tgl_keluar_);
            txt_operator.setText(_txt_operator_);
            txt_nik.setText(_txt_nik_);
        }

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String _txt_no_mc = txt_no_mc.getText().toString();
                String _txt_no_diecut = txt_no_diecut.getText().toString();
                String _txt_tgl_keluar = txt_tgl_keluar.getText().toString();
                String _txt_operator = txt_operator.getText().toString();
                String _txt_nik = txt_nik.getText().toString();
                String _sp_mesin = sp_mesin.getSelectedItem().toString();
                String _sp_customer = sp_customer.getSelectedItem().toString();

                if (_txt_no_mc.isEmpty()) {
                    txt_no_mc.setError("Data tidak boleh kosong");
                    txt_no_mc.requestFocus();
                } else if (_txt_no_diecut.isEmpty()) {
                    txt_no_diecut.setError("Data tidak boleh kosong");
                    txt_no_diecut.requestFocus();
                } else if (_txt_tgl_keluar.isEmpty()) {
                    txt_tgl_keluar.setError("Data tidak boleh kosong");
                    txt_tgl_keluar.requestFocus();
                } else if (_txt_operator.isEmpty()) {
                    txt_operator.setError("Data tidak boleh kosong");
                    txt_operator.requestFocus();
                } else if (_txt_nik.isEmpty()) {
                    txt_nik.setError("Data tidak boleh kosong");
                    txt_nik.requestFocus();

                } else if (_sp_mesin.isEmpty() || _sp_customer.isEmpty()) {
                    Toast.makeText(v.getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    if (pilih.equals("tambah")) {
                        database.child("pengambilan").child(_txt_no_diecut).setValue(new dataPengambilan(
                                _txt_no_mc,
                                _txt_no_diecut,
                                _sp_customer,
                                _txt_tgl_keluar,
                                _sp_mesin,
                                _txt_operator,
                                _txt_nik,
                                simpleDateMonth.format(calendar.getTime()))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
                    } else {
                        database.child("pengambilan").child(_key_).setValue(new dataPengambilan(
                                _txt_no_mc,
                                _txt_no_diecut,
                                _sp_customer,
                                _txt_tgl_keluar,
                                _sp_mesin,
                                _txt_operator,
                                _txt_nik,
                                simpleDateMonth.format(calendar.getTime()))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Data gagal diubah", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
                    }
                }

            }
        });

        return v;
    }

    private void dataSpinner(final View v, final ArrayList<String> list, final Spinner spinner, final String title, final String nilai) {
        database.child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    list.add(item.child(title).getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.item_spinner, list);
                spinner.setAdapter(adapter);
                if(pilih.equals("ubah")){
                    setSpinnerText(spinner, nilai);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setSpinnerText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }
}
