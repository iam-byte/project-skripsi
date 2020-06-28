package com.ilham.inventoridiecut.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.data.dataCommunication;
import com.ilham.inventoridiecut.preference.preferences;
import com.ilham.inventoridiecut.user.CommunicationFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CommunicationRecyclerAdapter extends RecyclerView.Adapter<CommunicationRecyclerAdapter.SendViewHolder> {

    Context context;
    List<dataCommunication> listCommunication;
    List<String> listData;
    Toolbar toolbar;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    public CommunicationRecyclerAdapter(Context context, List<dataCommunication> listCommunication, List<String> listData, Toolbar toolbar) {
        this.context = context;
        this.listCommunication = listCommunication;
        this.listData = listData;
        this.toolbar = toolbar;
    }

    @NonNull
    @Override
    public SendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_communication, parent, false);
        return new SendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SendViewHolder holder, int position) {
        holder.bindView(listCommunication.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listCommunication.size();
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {
        TextView dari,
                pesan,
                waktu,
                textTanggal,
                titleDocumentasi;
        CircleImageView imageView;
        LinearLayout linear, linear2, linearTanggal, linearDocumentasi;
        CardView cardView, cardTanggal;
        ImageView messageImage,imageDocumentasi;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            dari = itemView.findViewById(R.id.dari);
            linearDocumentasi = itemView.findViewById(R.id.linearDocumentasi);
            titleDocumentasi = itemView.findViewById(R.id.titleDocumentasi);
            pesan = itemView.findViewById(R.id.pesan);
            waktu = itemView.findViewById(R.id.waktu);
            imageView = itemView.findViewById(R.id.imageView);
            linear = itemView.findViewById(R.id.linear);
            linear2 = itemView.findViewById(R.id.linear2);
            cardView = itemView.findViewById(R.id.cardView);
            linearTanggal = itemView.findViewById(R.id.linearTanggal);
            cardTanggal = itemView.findViewById(R.id.cardTanggal);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            messageImage = itemView.findViewById(R.id.messageImage);
            imageDocumentasi = itemView.findViewById(R.id.imageDocumentasi);


        }

        public void bindView(final dataCommunication dataCommunication, int position) {
            String itemData = listData.get(position);
            dataCommunication itemNormal = listCommunication.get(position);

            final Date date = new Date();
            Locale locale = new Locale("in", "ID");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMMM/yyyy", locale);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa", locale);

            long kemarin = date.getTime() - (1000 * 60 * 60 * 24);

            if (itemNormal.getTanggal().equals(itemData)) {
                linearTanggal.setVisibility(View.GONE);
            }

            if (itemNormal.getTanggal().equals(simpleDateFormat.format(kemarin))) {
                textTanggal.setText("Kemarin");
            } else if (itemNormal.getTanggal().equals(simpleDateFormat.format(date.getTime()))) {
                textTanggal.setText("Sekarang");
            } else {
                textTanggal.setText(itemNormal.getTanggal());
            }

            dari.setText(dataCommunication.getDari());
            pesan.setText(dataCommunication.getPesan());
            waktu.setText(simpleDateFormat2.format(dataCommunication.getWaktu()));

            database.child("login").child(dataCommunication.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String srcImage = dataSnapshot.child("image").getValue(String.class);
                    Glide.with(context).load(srcImage).placeholder(R.drawable.profile).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            if (dataCommunication.getKey().equals(preferences.getId(context))) {
                dari.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                linear.setGravity(Gravity.CENTER | Gravity.END);
                linear2.setGravity(Gravity.CENTER | Gravity.END);
                pesan.setTextColor(context.getResources().getColor(android.R.color.white));
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (dataCommunication.getKey().equals(preferences.getId(context))) {
                        long dateNow = System.currentTimeMillis() - dataCommunication.getWaktu();

                        if (dateNow <= (1000 * 60 * 5)) {
                            if (dataCommunication.getJenis().equals("text")) {
                                toolbarHiden();

                                toolbar.getMenu().findItem(R.id.menu_copy).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_delete).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                                setMenuClose();
                                setMenuCopy(dataCommunication.getPesan(), imageView);
                                setMenuDelete(itemView, dataCommunication.getPush());
                            } else {
                                toolbarHiden();

                                toolbar.getMenu().findItem(R.id.menu_download).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_delete).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                                setMenuClose();
                                setMenuDeleteDownload(dataCommunication.getTitle(), dataCommunication.getJenis(), dataCommunication.getPush());
                                setMenuDownload(dataCommunication.getTitle(), dataCommunication.getJenis());

                            }

                        } else {
                            if (dataCommunication.getJenis().equals("text")) {
                                toolbarHiden();

                                toolbar.getMenu().findItem(R.id.menu_copy).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                                setMenuClose();
                                setMenuCopy(dataCommunication.getPesan(), imageView);
                            } else {
                                toolbarHiden();

                                toolbar.getMenu().findItem(R.id.menu_download).setVisible(true);
                                toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                                setMenuClose();
                                setMenuDownload(dataCommunication.getTitle(), dataCommunication.getJenis());
                            }

                        }


                    } else {
                        if (dataCommunication.getJenis().equals("text")) {
                            toolbarHiden();

                            toolbar.getMenu().findItem(R.id.menu_copy).setVisible(true);
                            toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                            setMenuClose();
                            setMenuCopy(dataCommunication.getPesan(), imageView);
                        } else {
                            toolbarHiden();

                            toolbar.getMenu().findItem(R.id.menu_download).setVisible(true);
                            toolbar.getMenu().findItem(R.id.menu_close).setVisible(true);

                            setMenuClose();
                            setMenuDownload(dataCommunication.getTitle(), dataCommunication.getJenis());
                        }
                    }


                    return true;
                }
            });


            if (dataCommunication.getJenis().equals("image")) {
                messageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(dataCommunication.getPesan()).placeholder(R.drawable.image_chat).into(messageImage);
            } else if (dataCommunication.getJenis().equals("text")) {
                pesan.setVisibility(View.VISIBLE);
            } else {
                linearDocumentasi.setVisibility(View.VISIBLE);
                titleDocumentasi.setText(dataCommunication.getTitle());
                if (dataCommunication.getKey().equals(preferences.getId(context))){
                    imageDocumentasi.setColorFilter(Color.WHITE);
                    titleDocumentasi.setTextColor(Color.WHITE);
                }
            }
        }
    }

    private void setMenuClose() {
        toolbar.getMenu().findItem(R.id.menu_close).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toolbarHiden();
                return true;
            }
        });
    }

    private void setMenuDeleteDownload(final String title, final String jenis, final String push) {
        toolbar.getMenu().findItem(R.id.menu_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apa kamu yakin ingin menghapus pesan ini")
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StorageReference strg = storage.child("media").child(jenis).child(title);
                                strg.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        database.child("communication").child(push).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Pesan berhasil dihapus", Toast.LENGTH_LONG).show();
                                                toolbarHiden();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                toolbarHiden();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        toolbarHiden();
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return true;
            }
        });
    }

    private void setMenuDownload(final String title, final String jenis) {
        toolbar.getMenu().findItem(R.id.menu_download).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                StorageReference strg = storage.child("media").child(jenis).child(title);
                strg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, title);
                        downloadManager.enqueue(request);
                        Toast.makeText(context, "Gambar berhasil didownload", Toast.LENGTH_LONG).show();
                        toolbarHiden();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
        });
    }

    private void setMenuDelete(final View v, final String key) {
        toolbar.getMenu().findItem(R.id.menu_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apa kamu yakin ingin menghapus pesan ini")
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                database.child("communication").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(), "Pesan berhasil dihapus", Toast.LENGTH_LONG).show();
                                        toolbarHiden();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toolbarHiden();
                                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toolbarHiden();
                        dialog.dismiss();
                    }
                }).show();
                return true;
            }
        });
    }


    private void copyToClipboard(String copyText, View v) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", copyText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(v.getContext(), "Pesan berhasil disalin", Toast.LENGTH_LONG).show();
    }

    private void setMenuCopy(final String pesan, final View v) {
        toolbar.getMenu().findItem(R.id.menu_copy).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                copyToClipboard(pesan, v);
                toolbarHiden();
                return true;
            }
        });
    }

    private void toolbarHiden() {
        toolbar.getMenu().findItem(R.id.menu_copy).setVisible(false);
        toolbar.getMenu().findItem(R.id.menu_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.menu_download).setVisible(false);
        toolbar.getMenu().findItem(R.id.menu_close).setVisible(false);
    }
}
