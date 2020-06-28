package com.ilham.inventoridiecut.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataPenarikan;
import com.ilham.inventoridiecut.dialog.FormPenarikan;
import com.ilham.inventoridiecut.preference.preferences;
import com.ilham.inventoridiecut.user.PenarikanFragment;

import java.util.ArrayList;

public class PenarikanRecyclerAdapter extends RecyclerView.Adapter<PenarikanRecyclerAdapter.PenarikanViewHolder> {
    Context context;
    ArrayList<dataPenarikan> listPenarikan;
    Fragment fragment;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public PenarikanRecyclerAdapter(Context context, ArrayList<dataPenarikan> listPenarikan, Fragment fragment) {
        this.context = context;
        this.listPenarikan = listPenarikan;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PenarikanRecyclerAdapter.PenarikanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item_penarikan, null);
        return new PenarikanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PenarikanRecyclerAdapter.PenarikanViewHolder holder, int position) {
        holder.itemBind(listPenarikan.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }

    }

    @Override
    public int getItemCount() {
        return listPenarikan.size();
    }

    public class PenarikanViewHolder extends RecyclerView.ViewHolder {

        TextView tv_mc;
        TextView tv_dc;
        TextView tv_customer;
        TextView tv_tgl_masuk;
        TextView tv_mesin;
        TextView tv_nama;
        TextView tv_nik;
        CardView card;
        ImageView hapus;

        public PenarikanViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mc = itemView.findViewById(R.id.tv_mc);
            card = itemView.findViewById(R.id.card);
            tv_dc = itemView.findViewById(R.id.tv_dc);
            tv_customer = itemView.findViewById(R.id.tv_customer);
            tv_tgl_masuk = itemView.findViewById(R.id.tv_tgl_masuk);
            tv_mesin = itemView.findViewById(R.id.tv_mesin);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_nik = itemView.findViewById(R.id.tv_nik);
            hapus = itemView.findViewById(R.id.hapus);

        }

        @SuppressLint("SetTextI18n")
        public void itemBind(final dataPenarikan dataPenarikan) {

            if (!preferences.getUser(itemView.getContext()).equals("user ppic")) {
                hapus.setVisibility(View.GONE);
            }

            if (preferences.getUser(itemView.getContext()).equals("user ppic")) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        FragmentManager fragmentManager = (fragment).getChildFragmentManager();
                        FormPenarikan formPenarikan = new FormPenarikan(
                                dataPenarikan.getKey(),
                                dataPenarikan.getNomc(),
                                dataPenarikan.getNodc(),
                                dataPenarikan.getTglmasuk(),
                                dataPenarikan.getNama(),
                                dataPenarikan.getNik(),
                                dataPenarikan.getMesinflexo(),
                                dataPenarikan.getCustomer(),
                                "ubah",
                                context
                        );
                        formPenarikan.show(fragmentManager, "fm-penarikan");
                        return true;
                    }
                });
            }

            tv_mc.setText("No MC : " + dataPenarikan.getNomc());
            tv_dc.setText("No Die Cut : " + dataPenarikan.getNodc());
            tv_customer.setText("Customer : " + dataPenarikan.getCustomer());
            tv_tgl_masuk.setText("Tanggal Masuk : " + dataPenarikan.getTglmasuk());
            tv_mesin.setText("Mesin : " + dataPenarikan.getMesinflexo());
            tv_nama.setText("Nama Personil : " + dataPenarikan.getNama());
            tv_nik.setText("NIK : " + dataPenarikan.getNik());

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("penarikan").child(dataPenarikan.getKey()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(v.getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(), "Data gagal dihapus", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }
    }
}
