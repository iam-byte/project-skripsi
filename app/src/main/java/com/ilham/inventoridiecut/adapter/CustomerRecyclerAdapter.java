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
import com.ilham.inventoridiecut.data.dataCustomer;
import com.ilham.inventoridiecut.dialog.FormCustomer;

import java.util.List;

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.CustomerViewHolder> {
    private List<dataCustomer> listCustomer;
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public CustomerRecyclerAdapter(List<dataCustomer> listCustomer, Context context) {
        this.listCustomer = listCustomer;
        this.context = context;
    }


    @NonNull
    @Override
    public CustomerRecyclerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_item_customer, null);
        return new CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRecyclerAdapter.CustomerViewHolder holder, int position) {
        holder.bindItem(listCustomer.get(position));

        if (position % 2 == 0) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return listCustomer.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_customer;
        ImageView hapus;
        CardView card;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_customer = itemView.findViewById(R.id.tv_customer);
            hapus = itemView.findViewById(R.id.hapus);
            card = itemView.findViewById(R.id.card);
        }

        public void bindItem(final dataCustomer dataCustomer) {
            tv_customer.setText(dataCustomer.getCustomer());

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Apakah yakin ingin menghapus data ini?")
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("customer").child(dataCustomer.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FormCustomer formCustomer = new FormCustomer(
                            dataCustomer.getCustomer(),
                            dataCustomer.getKey(),
                            "ubah"
                            , context);
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    formCustomer.show(fragmentManager, "fm-customer");

                    return true;
                }
            });
        }
    }
}
