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
import com.ilham.inventoridiecut.data.dataPengambilan;
import com.ilham.inventoridiecut.dialog.FormPengambilan;
import com.ilham.inventoridiecut.preference.preferences;

import java.util.ArrayList;

public class PengambilanRecyclerAdapter extends RecyclerView.Adapter<PengambilanRecyclerAdapter.PengambilanViewHolder> {
    Context context;
    ArrayList<dataPengambilan> listPengambilan;
    Fragment fragment;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public PengambilanRecyclerAdapter(Context context, ArrayList<dataPengambilan> listPengambilan, Fragment fragment) {
        this.context = context;
        this.listPengambilan = listPengambilan;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PengambilanRecyclerAdapter.PengambilanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item_pengambilan, null);
        return new PengambilanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PengambilanRecyclerAdapter.PengambilanViewHolder holder, int position) {
        holder.itemBind(listPengambilan.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return listPengambilan.size();
    }

    public class PengambilanViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mc;
        TextView tv_dc;
        TextView tv_customer;
        TextView tv_tgl_keluar;
        TextView tv_mesin;
        TextView tv_operator;
        TextView tv_nik;
        CardView card;
        ImageView hapus;

        public PengambilanViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mc = itemView.findViewById(R.id.tv_mc);
            card = itemView.findViewById(R.id.card);
            tv_dc = itemView.findViewById(R.id.tv_dc);
            tv_customer = itemView.findViewById(R.id.tv_customer);
            tv_tgl_keluar = itemView.findViewById(R.id.tv_tgl_keluar);
            tv_mesin = itemView.findViewById(R.id.tv_mesin);
            hapus = itemView.findViewById(R.id.hapus);
            tv_operator = itemView.findViewById(R.id.tv_operator);
            tv_nik = itemView.findViewById(R.id.tv_nik);


        }

        @SuppressLint("SetTextI18n")
        public void itemBind(final dataPengambilan dataPengambilan) {
            if (!preferences.getUser(itemView.getContext()).equals("user ppic")) {
                hapus.setVisibility(View.GONE);
            }

            tv_mc.setText("No MC : " + dataPengambilan.getNomc());
            tv_dc.setText("No Die Cut : " + dataPengambilan.getNodc());
            tv_customer.setText("Customer : " + dataPengambilan.getCustomer());
            tv_tgl_keluar.setText("Tanggal Keluar : " + dataPengambilan.getTglkeluar());
            tv_mesin.setText("Mesin : " + dataPengambilan.getMesinflexo());
            tv_operator.setText("Nama Operator : " + dataPengambilan.getNamaoperator());
            tv_nik.setText("NIK : " + dataPengambilan.getNik());

            if (preferences.getUser(itemView.getContext()).equals("user ppic")) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        FragmentManager fragmentManager = (fragment).getChildFragmentManager();
                        FormPengambilan formPengambilan = new FormPengambilan(
                                dataPengambilan.getKey(),
                                dataPengambilan.getNomc(),
                                dataPengambilan.getNodc(),
                                dataPengambilan.getCustomer(),
                                dataPengambilan.getTglkeluar(),
                                dataPengambilan.getMesinflexo(),
                                dataPengambilan.getNamaoperator(),
                                dataPengambilan.getNik(),
                                "ubah",
                                context
                        );
                        formPengambilan.show(fragmentManager, "fm-pengambilan");
                        return true;
                    }
                });
            }

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("pengambilan").child(dataPengambilan.getKey()).removeValue()
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
