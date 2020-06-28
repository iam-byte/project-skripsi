package com.ilham.inventoridiecut.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.ilham.inventoridiecut.data.dataCustomer;

public class FormCustomer extends DialogFragment {
    EditText txt_customer;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Button btn_simpan;

    String customer, key, pilih;
    Context context;

    public FormCustomer(String pilih, Context context) {
        this.pilih = pilih;
        this.context = context;
    }

    public FormCustomer(String customer, String key, String pilih, Context context) {
        this.customer = customer;
        this.key = key;
        this.pilih = pilih;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_customer, null);
        txt_customer = v.findViewById(R.id.txt_customer);
        btn_simpan = v.findViewById(R.id.btn_simpan);

        if (pilih.equals("ubah")) {
            txt_customer.setText(customer);
        }

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String cus = txt_customer.getText().toString();
                if (cus.isEmpty()) {
                    txt_customer.setError("Data tidak boleh kosong");
                    txt_customer.requestFocus();
                } else {
                    if (pilih.equals("tambah")) {
                        database.child("customer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(cus).exists()) {
                                    Toast.makeText(context, "Data tidak boleh sama", Toast.LENGTH_SHORT).show();
                                } else {
                                    database.child("customer").child(cus).setValue(new dataCustomer(cus)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Data gagal ditambah", Toast.LENGTH_SHORT).show();
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
                        database.child("customer").child(key).setValue(new dataCustomer(cus)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Data gagal diubah", Toast.LENGTH_SHORT).show();
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
