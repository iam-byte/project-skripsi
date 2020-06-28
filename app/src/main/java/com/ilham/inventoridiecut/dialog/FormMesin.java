package com.ilham.inventoridiecut.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.ilham.inventoridiecut.data.dataMesin;

public class FormMesin extends DialogFragment {
    EditText txt_mesin;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Button btn_simpan;

    String mesin, key, pilih;

    public FormMesin(String pilih) {
        this.pilih = pilih;
    }

    public FormMesin(String mesin, String key, String pilih) {
        this.mesin = mesin;
        this.key = key;
        this.pilih = pilih;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_mesin, null);
        txt_mesin = v.findViewById(R.id.txt_mesin);
        btn_simpan = v.findViewById(R.id.btn_simpan);

        if (pilih.equals("ubah")) {
            txt_mesin.setText(mesin);
        }

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String mesins = txt_mesin.getText().toString();
                if (mesins.isEmpty()) {
                    txt_mesin.setError("Data tidak boleh kosong");
                    txt_mesin.requestFocus();
                } else {
                    if (pilih.equals("tambah")) {
                        database.child("mesin").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(mesins).exists()){
                                    Toast.makeText(v.getContext(), "Data tidak boleh sama", Toast.LENGTH_SHORT).show();
                                }else {
                                    database.child("mesin").child(mesins).setValue(new dataMesin(mesins)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(v.getContext(), "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(), "Data gagal ditambah", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        database.child("mesin").child(key).setValue(new dataMesin(mesins)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
