package com.ilham.inventoridiecut.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.ilham.inventoridiecut.data.dataPerbaikan;
import com.ilham.inventoridiecut.dialog.FormPerbaikan;
import com.ilham.inventoridiecut.preference.preferences;

import java.util.ArrayList;

public class PerbaikanRecyclerAdapter extends RecyclerView.Adapter<PerbaikanRecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<dataPerbaikan> listPerbaikan;
    Fragment fragment;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public PerbaikanRecyclerAdapter(Context context, ArrayList<dataPerbaikan> listPerbaikan, Fragment fragment) {
        this.context = context;
        this.listPerbaikan = listPerbaikan;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PerbaikanRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item_perbaikan, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PerbaikanRecyclerAdapter.MyViewHolder holder, int position) {
        holder.itemBind(listPerbaikan.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }

    }

    @Override
    public int getItemCount() {
        return listPerbaikan.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mc;
        TextView tv_dc;
        TextView tv_customer;
        TextView tv_tgl_perbaikan;
        TextView tv_mesin;
        TextView tv_keterangan;
        CardView card;
        ImageView hapus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mc = itemView.findViewById(R.id.tv_mc);
            card = itemView.findViewById(R.id.card);
            tv_dc = itemView.findViewById(R.id.tv_dc);
            tv_customer = itemView.findViewById(R.id.tv_customer);
            tv_tgl_perbaikan = itemView.findViewById(R.id.tv_tgl_perbaikan);
            tv_mesin = itemView.findViewById(R.id.tv_mesin);
            hapus = itemView.findViewById(R.id.hapus);
            tv_keterangan = itemView.findViewById(R.id.tv_keterangan);

        }


        @SuppressLint("SetTextI18n")
        public void itemBind(final dataPerbaikan dataPerbaikan) {
            if (!preferences.getUser(itemView.getContext()).equals("user ppic")) {
                hapus.setVisibility(View.GONE);
            }

            if (preferences.getUser(itemView.getContext()).equals("user ppic")) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        FragmentManager fragmentManager = fragment.getChildFragmentManager();
                        FormPerbaikan formPerbaikan = new FormPerbaikan(
                                dataPerbaikan.getKey(),
                                dataPerbaikan.getNomc(),
                                dataPerbaikan.getNodc(),
                                dataPerbaikan.getCustomer(),
                                dataPerbaikan.getTglperbaikan(),
                                dataPerbaikan.getMesinflexo(),
                                dataPerbaikan.getKeterangan(),
                                "ubah",
                                context
                        );
                        formPerbaikan.show(fragmentManager, "fm-perbaikan");
                        return true;
                    }
                });
            }


            tv_mc.setText("No MC : " + dataPerbaikan.getNomc());
            tv_dc.setText("No Die Cut : " + dataPerbaikan.getNodc());
            tv_customer.setText("Customer : " + dataPerbaikan.getCustomer());
            tv_tgl_perbaikan.setText("Tanggal Perbaikan : " + dataPerbaikan.getTglperbaikan());
            tv_mesin.setText("Mesin : " + dataPerbaikan.getMesinflexo());
            tv_keterangan.setText("Keterangan : " + dataPerbaikan.getKeterangan());

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("perbaikan").child(dataPerbaikan.getKey()).removeValue()
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
