package com.ilham.inventoridiecut.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ilham.inventoridiecut.data.dataLogin;
import com.ilham.inventoridiecut.data.dataUserPPIC;

import java.util.ArrayList;
import java.util.Arrays;

public class FormUserPPIC extends DialogFragment {
    EditText etUsername, etPassword, txt_SAP, txt_Agama, txt_No_HP, txt_Bagian, txt_Alamat;
    Spinner spinner_user;
    RadioGroup rg_jenis_kelamin;
    Button btnSimpan;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    RadioButton radioButton, item_laki_laki, item_perempuan;
    private String username, sap, jk, agama, noHp, bagian, user, password, alamat, key, tanda;


    public FormUserPPIC(String tanda) {
        this.tanda = tanda;
    }

    public FormUserPPIC(String username, String sap, String jk, String agama, String noHp, String bagian, String user, String password, String alamat, String key, String tanda) {
        this.username = username;
        this.sap = sap;
        this.jk = jk;
        this.agama = agama;
        this.noHp = noHp;
        this.bagian = bagian;
        this.user = user;
        this.password = password;
        this.alamat = alamat;
        this.key = key;
        this.tanda = tanda;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_user_ppic, container, false);
        etUsername = v.findViewById(R.id.txt_username);
        etPassword = v.findViewById(R.id.txt_password);
        btnSimpan = v.findViewById(R.id.btn_simpan);
        txt_SAP = v.findViewById(R.id.txt_SAP);
        rg_jenis_kelamin = v.findViewById(R.id.rg_jenis_kelamin);
        txt_Agama = v.findViewById(R.id.txt_Agama);
        txt_No_HP = v.findViewById(R.id.txt_No_HP);
        txt_Bagian = v.findViewById(R.id.txt_Bagian);
        spinner_user = v.findViewById(R.id.spinner_user);
        txt_Alamat = v.findViewById(R.id.txt_Alamat);
        item_laki_laki = v.findViewById(R.id.item_laki_laki);
        item_perempuan = v.findViewById(R.id.item_perempuan);
        int selected = rg_jenis_kelamin.getCheckedRadioButtonId();
        radioButton = v.findViewById(selected);

        String[] nama = {"PPIC", "MESIN"};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(nama));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.item_spinner, arrayList);
        spinner_user.setAdapter(adapter);

        if (tanda.equals("ubah")) {
            etUsername.setText(username);
            etPassword.setText(password);
            txt_SAP.setText(sap);
            txt_Agama.setText(agama);
            txt_No_HP.setText(noHp);
            txt_Bagian.setText(bagian);
            txt_Alamat.setText(alamat);

            if (jk.equals("laki-laki")) {
                item_laki_laki.setSelected(true);
                item_perempuan.setSelected(false);
            } else {
                item_perempuan.setSelected(true);
                item_laki_laki.setSelected(false);
            }

        }
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String nama = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String sap = txt_SAP.getText().toString();
                final String agama = txt_Agama.getText().toString();
                final String nohp = txt_No_HP.getText().toString();
                final String bagian = txt_Bagian.getText().toString();
                final String alamat = txt_Alamat.getText().toString();
                final String user = spinner_user.getSelectedItem().toString();
                final String jk = radioButton.getText().toString();

                database.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(sap).exists()) {
                            Toast.makeText(v.getContext(), "Data Sudah Ada", Toast.LENGTH_SHORT).show();
                        } else if (nama.isEmpty()) {
                            etUsername.setError("Data Tidak Boleh Kosong");
                            etUsername.requestFocus();
                        } else if (password.isEmpty()) {
                            etPassword.setError("Data Tidak Boleh Kosong");
                            etPassword.requestFocus();
                        } else if (sap.isEmpty()) {
                            txt_SAP.setError("Data Tidak Boleh Kosong");
                            txt_SAP.requestFocus();
                        } else if (agama.isEmpty()) {
                            txt_Agama.setError("Data Tidak Boleh Kosong");
                            txt_Agama.requestFocus();
                        } else if (nohp.isEmpty()) {
                            txt_No_HP.setError("Data Tidak Boleh Kosong");
                            txt_No_HP.requestFocus();
                        } else if (bagian.isEmpty()) {
                            txt_Bagian.setError("Data Tidak Boleh Kosong");
                            txt_Bagian.requestFocus();
                        } else if (alamat.isEmpty()) {
                            txt_Alamat.setError("Data Tidak Boleh Kosong");
                            txt_Alamat.requestFocus();
                        } else {
                            dismiss();
                            if (tanda.equals("tambah")) {
                                database.child("user").child(sap).setValue(
                                        new dataUserPPIC(
                                                nama,
                                                password,
                                                sap,
                                                jk,
                                                agama,
                                                nohp,
                                                bagian,
                                                user,
                                                alamat)
                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(), "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                        database.child("login").child(sap).setValue(new dataLogin(nama, sap, password, "user " + user.toLowerCase(),""));
                                        dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(v.getContext(), "Gagal Disimpan", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                });
                            } else {
                                database.child("user").child(key).removeValue();
                                database.child("user").child(sap).setValue(
                                        new dataUserPPIC(
                                                nama,
                                                password,
                                                sap,
                                                jk,
                                                agama,
                                                nohp,
                                                bagian,
                                                user,
                                                alamat)
                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                        database.child("login").child(key).removeValue();
                                        database.child("login").child(sap).setValue(new dataLogin(nama, sap, password, "user",""));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(v.getContext(), "Data gagal diubah", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
