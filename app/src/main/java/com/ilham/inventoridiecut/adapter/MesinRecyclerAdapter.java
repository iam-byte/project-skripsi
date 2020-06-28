package com.ilham.inventoridiecut.adapter;

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
import com.ilham.inventoridiecut.data.dataMesin;
import com.ilham.inventoridiecut.dialog.FormMesin;

import java.util.List;

public class MesinRecyclerAdapter extends RecyclerView.Adapter<MesinRecyclerAdapter.MesinViewHolder> {
    private List<dataMesin> listMesin;
    Context context;
    DatabaseReference database  = FirebaseDatabase.getInstance().getReference();

    public MesinRecyclerAdapter(List<dataMesin> listMesin, Context context) {
        this.listMesin = listMesin;
        this.context = context;
    }


    @NonNull
    @Override
    public MesinRecyclerAdapter.MesinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_mesin, null);
        return new MesinRecyclerAdapter.MesinViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MesinRecyclerAdapter.MesinViewHolder holder, int position) {
        holder.bindItem(listMesin.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return listMesin.size();
    }

    public class MesinViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mesin;
        ImageView hapus;
        CardView card;


        public MesinViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mesin = itemView.findViewById(R.id.tv_mesin);
            hapus = itemView.findViewById(R.id.hapus);
            card = itemView.findViewById(R.id.card);

        }


        public void bindItem(final dataMesin dataMesin) {
            tv_mesin.setText(dataMesin.getMesin());

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("mesin").child(dataMesin.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(v.getContext(),"Data berhasil dihapus",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(),"Data gagal dihapus",Toast.LENGTH_SHORT).show();
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FormMesin formMesin  = new FormMesin(
                            dataMesin.getMesin(),
                            dataMesin.getKey(),
                            "ubah");
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    formMesin.show(fragmentManager,"fm-mesin");

                    return true;
                }
            });
        }
    }
}
