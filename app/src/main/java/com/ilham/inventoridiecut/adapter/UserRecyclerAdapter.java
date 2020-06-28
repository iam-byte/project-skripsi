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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataUserPPIC;
import com.ilham.inventoridiecut.dialog.FormUserPPIC;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    private List<dataUserPPIC> listData;
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public UserRecyclerAdapter(List<dataUserPPIC> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.admin_item_data_user, null);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserRecyclerAdapter.UserViewHolder holder, int position) {
        holder.dataBind(listData.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama,
                tv_sap,
                tv_bagian,
                tv_hp,
                tv_pass;

        CardView card;
        ImageView hapus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_sap = itemView.findViewById(R.id.tv_sap);
            tv_bagian = itemView.findViewById(R.id.tv_bagian);
            tv_hp = itemView.findViewById(R.id.tv_hp);
            tv_pass = itemView.findViewById(R.id.tv_pass);
            card = itemView.findViewById(R.id.card);
            hapus = itemView.findViewById(R.id.hapus);
        }


        public void dataBind(final dataUserPPIC dataUserPPIC) {
            tv_nama.setText("Nama : " + dataUserPPIC.getNama());
            tv_sap.setText("SAP : " + dataUserPPIC.getSap());
            tv_bagian.setText("Bagian : " + dataUserPPIC.getBagian());
            tv_hp.setText("No Hp : " + dataUserPPIC.getNo_hp());
            tv_pass.setText("Password : " + dataUserPPIC.getPassword());


            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini ?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("user").child(dataUserPPIC.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            database.child("login").child(dataUserPPIC.getKey()).removeValue();
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
                    });
                    builder.show();

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FormUserPPIC formUserPPIC = new FormUserPPIC(
                            dataUserPPIC.getNama(),
                            dataUserPPIC.getSap(),
                            dataUserPPIC.getJenis_kelamin(),
                            dataUserPPIC.getAgama(),
                            dataUserPPIC.getNo_hp(),
                            dataUserPPIC.getBagian(),
                            dataUserPPIC.getSebagai(),
                            dataUserPPIC.getPassword(),
                            dataUserPPIC.getAlamat(),
                            dataUserPPIC.getKey(),
                            "ubah");
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();

                    formUserPPIC.show(manager, "fm-user");

                    return true;
                }
            });
        }
    }
}
