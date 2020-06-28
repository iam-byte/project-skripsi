package com.ilham.inventoridiecut.ui_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ilham.inventoridiecut.InputCustomerActivity;
import com.ilham.inventoridiecut.InputMesinActivity;
import com.ilham.inventoridiecut.InputUserActivity;
import com.ilham.inventoridiecut.LoginActivity;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.ShowAllItem;
import com.ilham.inventoridiecut.data.dataPenarikan;
import com.ilham.inventoridiecut.data.dataPengambilan;
import com.ilham.inventoridiecut.data.dataPerbaikan;
import com.ilham.inventoridiecut.dialog.BottomSheet;
import com.ilham.inventoridiecut.dialog.FormPembuatanDieCut;
import com.ilham.inventoridiecut.pdf.Common;
import com.ilham.inventoridiecut.preference.preferences;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragmentAdmin extends Fragment {
    Button btnKelolaUserPPIC, kelola_pembuatan,
            customer,
            all_mesin,
            all_item,
            laporan;
    CircleImageView imageView;
    TextView title;
    Context context;
    ProgressDialog dialog;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Toolbar toolbar;
    private int PERMISSION_DATA = 10;
    private int ACCESS_DATA = 20;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    SimpleDateFormat simpleDateMonth = new SimpleDateFormat("MMMM", id);
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    String month = simpleDateMonth.format(System.currentTimeMillis());
    String tanggal;


    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);

        context = v.getContext();
        dialog = new ProgressDialog(v.getContext());
        btnKelolaUserPPIC = v.findViewById(R.id.kelola_user);
        kelola_pembuatan = v.findViewById(R.id.kelola_pembuatan);
        customer = v.findViewById(R.id.customer);
        all_mesin = v.findViewById(R.id.all_mesin);
        all_item = v.findViewById(R.id.all_item);
        laporan = v.findViewById(R.id.laporan);
        imageView = v.findViewById(R.id.imageView);
        title = v.findViewById(R.id.title);
        toolbar = v.findViewById(R.id.toolbar);

        title.setText(preferences.getKey(v.getContext()));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), 20);
                }
            }
        });

        toolbar.inflateMenu(R.menu.toolbar_logout);
        toolbar.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(v.getContext());
                alertLogout.setMessage("Apakah yakin ingin keluar?");
                alertLogout.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(v.getContext(), LoginActivity.class));
                        preferences.clearUser(v.getContext());
                        getActivity().finish();
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

        toolbar.getMenu().findItem(R.id.ubah_sandi).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showBottomSheet();
                return true;
            }
        });


        kelola_pembuatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormPembuatanDieCut formPembuatanDieCut = new FormPembuatanDieCut(getContext(), "tambah");
                formPembuatanDieCut.show(getChildFragmentManager(), "fm-diecut");
            }
        });


        btnKelolaUserPPIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), InputUserActivity.class));

            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), InputCustomerActivity.class));

            }
        });

        all_mesin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), InputMesinActivity.class));

            }
        });

        all_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ShowAllItem.class));
            }
        });

        showDataMonth("perbaikan", simpleDateMonth.format(System.currentTimeMillis()));
        showDataMonth("pengambilan", simpleDateMonth.format(System.currentTimeMillis()));
        showDataMonth("penarikan", simpleDateMonth.format(System.currentTimeMillis()));


        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_DATA);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final String Cetak_Perbaikan = "perbaikan";
                    final String Cetak_Pengambilan = "pengambilan";
                    final String Cetak_Penarikan = "penarikan";


                    builder.setItems(new CharSequence[]{"Perbaikan", "Pengambilan", "Penarikan"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    createPdf(Common.getAppPath(context) + "Laporan " + Cetak_Perbaikan + " " + simpleDateFormat.format(System.currentTimeMillis()) + ".pdf", Cetak_Perbaikan);
                                    break;
                                case 1:
                                    createPdf(Common.getAppPath(context) + "Laporan " + Cetak_Pengambilan + " " + simpleDateFormat.format(System.currentTimeMillis()) + ".pdf", Cetak_Pengambilan);
                                    break;
                                case 2:
                                    createPdf(Common.getAppPath(context) + "Laporan " + Cetak_Penarikan + " " + simpleDateFormat.format(System.currentTimeMillis()) + ".pdf", Cetak_Penarikan);
                                    break;
                            }
                        }
                    }).show();


                }
            }
        });

        showProfile();
        return v;
    }

    private void createPdf(String dest, String name) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Budyfriend");
            document.addCreator("Budiono");

            Font FontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
            Font FontBold = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);

            Chunk chunkTitle = new Chunk("LAPORAN " + name.toUpperCase(), FontBold);
            Paragraph paragraphTitle = new Paragraph(chunkTitle);
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphTitle);

            addEmptyLine(document, 1);

            PdfPTable table = null;
            PdfPCell cell;

            if (name.equals("perbaikan")) {
                table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3, 2, 4, 4, 2, 5});


                cell = new PdfPCell(new Phrase("No MC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("No DC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Customer", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Tanggal Perbaikan", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Mesin", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Keterangan", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);


                for (int i = 0; i < dataPerbaikanArrayList.size(); i++) {
                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getNomc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getNodc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getCustomer(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getTglperbaikan(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getMesinflexo(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPerbaikanArrayList.get(i).getKeterangan(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }

                cell = new PdfPCell(new Phrase(simpleDateFormat.format(System.currentTimeMillis()), FontNormal));
                cell.setColspan(6);
                cell.setPaddingTop(20);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

            } else if (name.equals("pengambilan")) {
                table = new PdfPTable(7);
                table.setWidths(new float[]{3, 3, 5, 4, 2, 4, 3});
                table.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("No MC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("No DC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Customer", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Tanggal Keluar", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Mesin", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Nama Operator", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NIK", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);


                for (int i = 0; i < dataPengambilanArrayList.size(); i++) {
                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getNomc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getNodc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getCustomer(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getTglkeluar(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getMesinflexo(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getNamaoperator(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPengambilanArrayList.get(i).getNik(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }

                cell = new PdfPCell(new Phrase(simpleDateFormat.format(System.currentTimeMillis()), FontNormal));
                cell.setColspan(7);
                cell.setPaddingTop(20);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

            } else if (name.equals("penarikan")) {
                table = new PdfPTable(7);
                table.setWidths(new float[]{3, 3, 5, 4, 2, 4, 3});
                table.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("No MC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("No DC", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Customer", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Tanggal Masuk", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Mesin", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Nama Personil", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NIK", FontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);


                for (int i = 0; i < dataPenarikanArrayList.size(); i++) {
                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getNomc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getNodc(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getCustomer(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getTglmasuk(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getMesinflexo(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getNama(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(dataPenarikanArrayList.get(i).getNik(), FontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }

                cell = new PdfPCell(new Phrase(simpleDateFormat.format(System.currentTimeMillis()), FontNormal));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingTop(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }


            document.add(table);
            document.close();
            printPDF(name);


        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void printPDF(String name) {
        Common.openFilePdf(context, new File(Common.getAppPath(context) + "Laporan " + name + " " + simpleDateFormat.format(System.currentTimeMillis()) + ".pdf"));
    }

    private void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                changeProfileImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeProfileImage(Bitmap bitmap) {
        final StorageReference base = storageReference.child("profile").child(preferences.getId(context));
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, boas);
        byte[] dataByte = boas.toByteArray();
        dialog.setCancelable(false);

        base.putBytes(dataByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                base.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("login").child(preferences.getId(context))
                                .child("image").setValue(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                dialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                float progress = 100.0f * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                dialog.setMessage(String.format("Upload %.2f", progress) + " %");
                dialog.show();
            }
        });

    }

    private void showProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("login").child(preferences.getId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue(String.class);
                if (isValidContextForGlid(context)) {
                    Glide.with(context).load(image).placeholder(R.drawable.ic_account).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isValidContextForGlid(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public void showBottomSheet() {
        BottomSheet bottomSheet = new BottomSheet();
        bottomSheet.show(getChildFragmentManager(), "fm-bottom-sheet");
    }

    ArrayList<dataPerbaikan> dataPerbaikanArrayList = new ArrayList<>();
    ArrayList<dataPenarikan> dataPenarikanArrayList = new ArrayList<>();
    ArrayList<dataPengambilan> dataPengambilanArrayList = new ArrayList<>();

    private void showDataMonth(final String data, String val) {


        Query query = database.child(data).orderByChild("bulan").equalTo(val);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshotLisener(dataSnapshot, data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void showData(final String data) {
//        database.child(data).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshotLisener(dataSnapshot, data);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void dataSnapshotLisener(DataSnapshot dataSnapshot, String data) {
        if (data.equals("perbaikan")) {
            dataPenarikanArrayList.clear();
            for (DataSnapshot item : dataSnapshot.getChildren()) {
                dataPerbaikan dataPerbaikanNew = item.getValue(dataPerbaikan.class);
                dataPerbaikanArrayList.add(dataPerbaikanNew);
            }

        } else if (data.equals("penarikan")) {
            dataPenarikanArrayList.clear();
            for (DataSnapshot item : dataSnapshot.getChildren()) {
                dataPenarikan dataPenarikanNew = item.getValue(dataPenarikan.class);
                dataPenarikanArrayList.add(dataPenarikanNew);
            }

        } else if (data.equals("pengambilan")) {
            dataPengambilanArrayList.clear();
            for (DataSnapshot item : dataSnapshot.getChildren()) {
                dataPengambilan dataPengambilanNew = item.getValue(dataPengambilan.class);
                dataPengambilanArrayList.add(dataPengambilanNew);
            }

        }
    }
}