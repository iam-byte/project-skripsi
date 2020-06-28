package com.ilham.inventoridiecut.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.adapter.CommunicationRecyclerAdapter;
import com.ilham.inventoridiecut.data.dataCommunication;
import com.ilham.inventoridiecut.preference.preferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CommunicationFragment extends Fragment {
    RecyclerView recyclerView;
    EditText inputMessage;
    FloatingActionButton sendMessage;
    Toolbar toolbar;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataCommunication> listCommunication = new ArrayList<>();
    ArrayList<String> listData = new ArrayList<>();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    ProgressDialog dialog;
    CommunicationRecyclerAdapter communicationRecyclerAdapter;
    Context context;
    ImageView add;
    Uri capturedImageURI;
    Locale locale = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMMM/yyyy", locale);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_communication, container, false);

        context = v.getContext();
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);

        recyclerView = v.findViewById(R.id.recyclerView);
        add = v.findViewById(R.id.add);
        inputMessage = v.findViewById(R.id.inputMessage);
        sendMessage = v.findViewById(R.id.sendMessage);
        toolbar = v.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_action);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMessageData();
            }
        });
        sendDataMessageShow();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 65);
                } else {
                    capturedImageURI = Uri.fromFile(createImageFile());

                    String[] mimeTypes =
                            {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                    "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                    "application/trap",
                                    "application/pdf",
                                    "application/zip",
                                    "image/jpeg"};


                    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI);

                    Intent galeryIntent = new Intent();
                    galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galeryIntent.addCategory(Intent.CATEGORY_OPENABLE);

                    galeryIntent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        galeryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }

                    galeryIntent.setType("image/jpeg");

                    Intent chooserIntent = Intent.createChooser(galeryIntent, "Select Picture");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
                    startActivityForResult(chooserIntent, 34);
                }
            }
        });

        return v;
    }

    Bitmap bitmap;
    String extension;
    String stringUpload;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 34 && resultCode == Activity.RESULT_OK) {
            extension = clipString(capturedImageURI);

            if (data != null) {
                capturedImageURI = data.getData();

                String mimeType = getActivity().getContentResolver().getType(capturedImageURI);
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

            }

            stringUpload = filePathStorage(extension);

            if (extension.equals("doc") || extension.equals("docx")) {
                uploadDocument("document", stringUpload, bitmap, capturedImageURI);
            } else if (extension.equals("ppt") || extension.equals("pptx")) {
                uploadDocument("power-point", stringUpload, bitmap, capturedImageURI);
            } else if (extension.equals("xls") || extension.equals("xlsx")) {
                uploadDocument("excel", stringUpload, bitmap, capturedImageURI);
            } else if (extension.equals("zip") || extension.equals("rar")) {
                uploadDocument("zip", stringUpload, bitmap, capturedImageURI);
            } else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), capturedImageURI);
                    uploadDocument("image", stringUpload, bitmap, capturedImageURI);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "File tidak didukung", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadDocument(String path, String stringUpload, Bitmap bitmap, Uri capturedImageURI) {
        final StorageReference strg = storage.child("media").child(path).child(stringUpload);
        UploadTask uploadTask;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
            byte[] dataUpload = byteArrayOutputStream.toByteArray();

            uploadTask = strg.putBytes(dataUpload);
            uploadNewDataDodument(uploadTask, strg, path,stringUpload);
        } else {
            uploadTask = strg.putFile(capturedImageURI);
            uploadNewDataDodument(uploadTask, strg, path,stringUpload);
        }

    }

    private void uploadNewDataDodument(UploadTask uploadTask, final StorageReference strg, final String s, final String stringUpload) {
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                strg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        insertData(uri, s,stringUpload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void insertData(Uri uri, String path, String stringUpload) {
        database.child("communication").push().setValue(
                new dataCommunication(
                        preferences.getId(context),
                        preferences.getKey(context),
                        uri.toString(),
                        simpleDateFormat.format(System.currentTimeMillis()),
                        System.currentTimeMillis(),
                        path,
                        stringUpload

                )
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                Toast.makeText(context, "Pesan berhasil terkirim", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 65) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 65);
        }
    }

    private void inputMessageData() {
        String send = inputMessage.getText().toString();
        if (send.isEmpty()) {
            inputMessage.setError("Masukan pesan");
            inputMessage.requestFocus();
        } else {
            String id = preferences.getId(context);
            String name = preferences.getKey(context);

            database.child("communication")
                    .push()
                    .setValue(new dataCommunication(
                            id,
                            name,
                            send,
                            simpleDateFormat.format(System.currentTimeMillis()),
                            System.currentTimeMillis(),
                            "text"))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            inputMessage.setText(null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendDataMessageShow() {
        database.child("communication").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCommunication.clear();
                listData.clear();
                listData.add("");
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    dataCommunication communication = item.getValue(dataCommunication.class);
                    listData.add(communication != null ? communication.getTanggal() : null);
                    if (communication != null) {
                        communication.setPush(item.getKey());
                    }
                    listCommunication.add(communication);
                }
                communicationRecyclerAdapter = new CommunicationRecyclerAdapter(context, listCommunication, listData, toolbar);
                recyclerView.setAdapter(communicationRecyclerAdapter);
//                recyclerView.scrollToPosition(listCommunication.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public File createImageFile() {
        File imageStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES)
                , "Die-Cut");

        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }

        return new File(
                imageStorageDir + File.separator + "IMG_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");
    }


    private String filePathStorage(String extension) {
        String hasil = extension.toUpperCase() + "_" +
                String.valueOf(System.currentTimeMillis()) +
                "." + extension;

        return hasil;
    }


    private String clipString(Uri uri) {
        String clip = uri.toString().substring(
                uri.toString().length() - 4,
                uri.toString().length());
        String take = clip.replace(".", "");

        return take;
    }


}
