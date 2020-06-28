package com.ilham.inventoridiecut.adapter;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataPembuatanDieCut;
import com.ilham.inventoridiecut.dialog.FormPembuatanDieCut;
import com.ilham.inventoridiecut.preference.preferences;

import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {
    private List<dataPembuatanDieCut> list;
    Activity context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public ItemRecyclerViewAdapter(List<dataPembuatanDieCut> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(context).inflate(R.layout.admin_item_data_allitem, null);
        return new ItemViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ItemViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }

        holder.itemBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mc, tv_dc,
                tv_customer,
                tv_lokasi;

        ImageView hapus;

        CardView card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mc = itemView.findViewById(R.id.tv_mc);
            tv_dc = itemView.findViewById(R.id.tv_dc);
            tv_customer = itemView.findViewById(R.id.tv_customer);
            tv_lokasi = itemView.findViewById(R.id.tv_lokasi);
            card = itemView.findViewById(R.id.card);
            hapus = itemView.findViewById(R.id.hapus);
        }

        public void itemBind(final dataPembuatanDieCut dataPembuatanDieCut) {
            tv_mc.setText(dataPembuatanDieCut.getNomc());
            tv_dc.setText(dataPembuatanDieCut.getNodc());
            tv_customer.setText(dataPembuatanDieCut.getCustomer());
            tv_lokasi.setText(dataPembuatanDieCut.getLokasi());

//            if(!preferences.getUser(itemView.getContext()).equals("user ppic")){
            if(!preferences.getUser(itemView.getContext()).equals("admin")){
                hapus.setVisibility(View.GONE);
            }

            if(preferences.getUser(itemView.getContext()).equals("admin")){
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FormPembuatanDieCut formPembuatanDieCut = new FormPembuatanDieCut(
                                dataPembuatanDieCut.getKey(),
                                dataPembuatanDieCut.getNomc(),
                                dataPembuatanDieCut.getNodc(),
                                dataPembuatanDieCut.getCustomer(),
                                dataPembuatanDieCut.getTglbuat(),
                                dataPembuatanDieCut.getTglselesai(),
                                dataPembuatanDieCut.getLokasi(),
                                dataPembuatanDieCut.getMold(),
                                dataPembuatanDieCut.getPapan(),
                                dataPembuatanDieCut.getPisau(),
                                dataPembuatanDieCut.getCreasing(),
                                v.getContext(), "ubah");
                        formPembuatanDieCut.show(fragmentManager, "fm-diecut");
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
                                    database.child("pembuatan-die-cut").child(dataPembuatanDieCut.getKey()).removeValue()
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
