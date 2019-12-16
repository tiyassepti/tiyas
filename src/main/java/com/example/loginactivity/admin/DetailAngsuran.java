package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.PeternakRegister;
import com.example.loginactivity.model.UserPinjaman;
import com.example.loginactivity.user.UserDetailAngsuranActivity;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailAngsuran extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailAngsuranActivity";
    TextView id;
    MaterialEditText kodePinjam,nama,Tanggal,jumlah,kali,emaill;
    ImageView gambar;
    Button setuju,tolak;
    private List<Angsuran> userAngsuranList;
    private List<PeternakRegister> listpeternak;
    String email, keterangan, keyid,naman , key,gmbr;
    long maxid;
    String hp;
    private final int CHOOSE_IMAGE = 1;
    private StorageReference msstorage;
    private StorageTask upluadtask;
    private Uri ImageURL;
    String idangsur,idpinjam,tgl,jml,kkl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_angsuran);
        id=findViewById(R.id.dp_id);
        kodePinjam=findViewById(R.id.id_pinjaman);
        nama=findViewById(R.id.nama);
        Tanggal=findViewById(R.id.dp_jatuhtempo);
        jumlah=findViewById(R.id.jumlah);
        kali=findViewById(R.id.kali);
        gambar=findViewById(R.id.img_bukti);
        setuju=findViewById(R.id.dp_btn_setuju);
        tolak=findViewById(R.id.dp_btn_tolak);
        emaill=findViewById(R.id.email);
        setuju.setOnClickListener(this);
        tolak.setOnClickListener(this);
        userAngsuranList = new ArrayList<>();
       listpeternak = new ArrayList<>();
        keyid = getIntent().getStringExtra("IDA");
        msstorage = FirebaseStorage.getInstance().getReference();
        Log.d(TAG, "onCreate: " + keyid);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("angsuran")
                .orderByChild("kodeAngsuran")
                .equalTo(keyid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                    if (pinjamanSnapshot.child("status").getValue().equals("diProses")){
                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        Angsuran ka = pinjamanSnapshot.getValue(Angsuran.class);
                       userAngsuranList.add(ka);
                        key = ka.getEmail();
                        idangsur=ka.getKodeAngsuran();
                        idpinjam=ka.getKodePinjaman();
                        tgl=ka.getJatuhTempo();
                        jml=String.valueOf(ka.getJumlah());
                        kkl=String.valueOf(ka.getAngsuranKe());
                        gmbr=ka.getBukti();

                        Log.d(TAG, "onDataChange: "+gmbr);
                    }
                }

                Query query = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("peternak")
                        .orderByChild("email")
                        .equalTo(key);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                                Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                                PeternakRegister  user = pinjamanSnapshot.getValue(PeternakRegister.class);
                                listpeternak.add(user);
                                hp=user.getNohp();
                                naman=user.getNama();
                            Log.d(TAG, "onDataChange: "+nama);


                        }
                        nama.setText(naman);
                        Log.d(TAG, "onDataChange: "+naman);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Log.d(TAG, "onDataChange: "+gmbr);
                Picasso.with(DetailAngsuran.this)
                        .load(gmbr)
                        .centerCrop()
                        .resize(200,200)
                        .into(gambar);

                id.setText(idangsur);
                kodePinjam.setText(idpinjam);
                emaill.setText(key);
                Tanggal.setText(tgl);
                jumlah.setText(jml);
                kali.setText(kkl);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void Showchooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage() {
        if (ImageURL != null) {
            StorageReference fileReference = msstorage.child(System.currentTimeMillis() + "." + getFileExtension(ImageURL));
            Log.d(TAG, "uploadImage: " + fileReference);
            upluadtask = fileReference.putFile(ImageURL)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            Toast.makeText(DetailAngsuran.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                            Query q2 = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("angsuran")
                                    .orderByChild("id")
                                    .equalTo(id.getText().toString());
                            q2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "onDataChange Data Pinjaman: " + dataSnapshot.getChildrenCount());
                                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                                        final DatabaseReference reference = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("angsuran")
                                                .child(pinjamanSnapshot.getKey());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                reference.child("bukti").setValue(taskSnapshot.getUploadSessionUri().toString());
                                                reference.child("status").setValue("Tolak");
                                                Intent intent = new Intent(DetailAngsuran.this, LoginActivity.class);
                                                intent.putExtra("EMAIL", email);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.d(TAG, "onCancelled bukti: " + databaseError);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, "onCancelled input: " + databaseError);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailAngsuran.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        }
                    });
        } else {
            Toast.makeText(DetailAngsuran.this, "No File Selected", Toast.LENGTH_SHORT).show();
        }}
    private void uploadImage2() {
        if (ImageURL != null) {
            StorageReference fileReference = msstorage.child(System.currentTimeMillis() + "." + getFileExtension(ImageURL));
            Log.d(TAG, "uploadImage: " + fileReference);
            upluadtask = fileReference.putFile(ImageURL)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            Toast.makeText(DetailAngsuran.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                            Query q2 = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("angsuran")
                                    .orderByChild("id")
                                    .equalTo(id.getText().toString());
                            q2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "onDataChange Data Pinjaman: " + dataSnapshot.getChildrenCount());
                                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                                        final DatabaseReference reference = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("angsuran")
                                                .child(pinjamanSnapshot.getKey());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                reference.child("bukti").setValue(taskSnapshot.getUploadSessionUri().toString());
                                                reference.child("status").setValue("Setuju");
                                                Intent intent = new Intent(DetailAngsuran.this, LoginActivity.class);
                                                intent.putExtra("EMAIL", email);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.d(TAG, "onCancelled bukti: " + databaseError);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, "onCancelled input: " + databaseError);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailAngsuran.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        }
                    });
        } else {
            Toast.makeText(DetailAngsuran.this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

            switch (v.getId()){
                case R.id.dp_btn_setuju:
                    keterangan = "Setuju";
                    uploadImage();
                    uploadImage();
                    //sendSmsByVIntent();
                    break;
                case R.id.dp_btn_tolak:
                    keterangan = "Tolak";
                    uploadImage2();
                    break;
            }
    }
}
