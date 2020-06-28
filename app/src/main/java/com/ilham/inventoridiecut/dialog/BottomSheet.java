package com.ilham.inventoridiecut.dialog;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.preference.preferences;

public class BottomSheet extends BottomSheetDialogFragment {
    EditText sandi_sekarang,
            sandi_baru,
            sandi_konfirmasi;
    Button btn_simpan;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    String sandiNow = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_sandi, container, false);

        context = v.getContext();
        sandi_sekarang = v.findViewById(R.id.sandi_sekarang);
        sandi_baru = v.findViewById(R.id.sandi_baru);
        sandi_konfirmasi = v.findViewById(R.id.sandi_konfirmasi);
        btn_simpan = v.findViewById(R.id.btn_simpan);

        showSandiSaatIni();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sekarang = sandi_sekarang.getText().toString();
                String baru = sandi_baru.getText().toString();
                String konfirmasi = sandi_konfirmasi.getText().toString();

                if (sekarang.isEmpty()) {
                    Toast.makeText(context,"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                    sandi_sekarang.requestFocus();
                } else if (baru.isEmpty()) {
                    Toast.makeText(context,"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                    sandi_baru.requestFocus();
                } else if (konfirmasi.isEmpty()) {
                    Toast.makeText(context,"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                    sandi_konfirmasi.requestFocus();
                } else if (!sekarang.equals(sandiNow)) {
                    Toast.makeText(context,"Sandi saat ini salah",Toast.LENGTH_SHORT).show();
                    sandi_sekarang.requestFocus();
                } else if (!konfirmasi.equals(baru)) {
                    Toast.makeText(context,"Konfirmasi sandi salah",Toast.LENGTH_SHORT).show();
                    sandi_sekarang.requestFocus();
                } else {
                    database.child("login").child(preferences.getId(context)).child("password").setValue(baru).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Sandi berhasil diubah", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Sandi gagal diubah", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }

            }
        });
        return v;
    }

    private void showSandiSaatIni() {
        database.child("login").child(preferences.getId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sandiNow = dataSnapshot.child("password").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
