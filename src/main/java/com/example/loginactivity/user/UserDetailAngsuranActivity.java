package com.example.loginactivity.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.example.loginactivity.model.UserPinjaman;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDetailAngsuranActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserDetailAngsuranActiv";
    private final int CHOOSE_IMAGE = 1;

    String idPinjaman, idAngsuran, email;
    private List<UserPinjaman> userPinjamanList;
    private List<Angsuran> angsuranList;


    TextView tvTagihan, tvSisa, tvJumlahDilunasi, tvAngsuran, tvTanggal;
    Button btnBayar, btnGetImage,btnkamera;
    String tanggalSekarang;
    ImageView gambar;
    private Uri ImageURL;
    FirebaseAuth auth;
    int kodeKamera = 99;

    private StorageReference msstorage;
    private StorageTask upluadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_angsuran);
        idPinjaman = getIntent().getStringExtra("IDP");
        idAngsuran = getIntent().getStringExtra("IDA");
        email = getIntent().getStringExtra("EMAIL");

        angsuranList = new ArrayList<>();
        userPinjamanList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        tanggalSekarang = format.format(new Date());
        tvTagihan = findViewById(R.id.ang_sisa);
        tvTanggal = findViewById(R.id.ang_tanggal_pembayaran);
        tvJumlahDilunasi = findViewById(R.id.ang_sisa_pinjaman);
        tvAngsuran = findViewById(R.id.ang_tag_sebelumnya);
        tvSisa = findViewById(R.id.ang_sisa2);
        btnBayar = findViewById(R.id.btn_bayar_sekarang);
        btnkamera=findViewById(R.id.btnkamera);
        btnBayar.setOnClickListener(this);
        btnkamera.setOnClickListener(this);
        btnGetImage = findViewById(R.id.btnupload);
        btnGetImage.setOnClickListener(this);
        gambar = findViewById(R.id.img_bukti);


        msstorage = FirebaseStorage.getInstance().getReference("Bukti Angsuran");
        tampilkanData();
    }

    private void tampilkanData(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("angsuran")
                .orderByChild("kodeAngsuran")
                .equalTo(idAngsuran);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange Data Angsuran: " + dataSnapshot.getChildrenCount());
                angsuranList.clear();
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                    //Check if user have sisa pinjaman
                    Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                    angsuranList.add(ang);
                }
                Query q2 = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Pinjaman")
                        .orderByChild("id")
                        .equalTo(idPinjaman);
                q2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange Data Pinjaman: " + dataSnapshot.getChildrenCount());
                        userPinjamanList.clear();
                        for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                            //Check if user have sisa pinjaman
                            UserPinjaman ang = pinjamanSnapshot.getValue(UserPinjaman.class);
                            userPinjamanList.add(ang);
                        }
                        tvTagihan.setText(String.valueOf(angsuranList.get(0).getJumlah()));
                        tvTanggal.setText("Tanggal Pembayaran " + tanggalSekarang);
                        tvJumlahDilunasi.setText(String.valueOf(userPinjamanList.get(0).getSisaPinjam()));
                        tvAngsuran.setText(String.valueOf(angsuranList.get(0).getJumlah()));
                        tvSisa.setText(String.valueOf(userPinjamanList.get(0).getSisaPinjam() - angsuranList.get(0).getJumlah()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageURL = data.getData();
            Picasso.with(this).load(ImageURL).into(gambar);




            }else if(requestCode == kodeKamera && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                gambar.setImageBitmap(bitmap);


        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (ImageURL != null) {
            final StorageReference fileReference = msstorage.child(System.currentTimeMillis() + "." + getFileExtension(ImageURL));
            Log.d(TAG, "uploadImage: " + fileReference);
            fileReference.putFile(ImageURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull final Task<Uri> task) {
                    Toast.makeText(UserDetailAngsuranActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    Query q2 = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Pinjaman")
                            .orderByChild("id")
                            .equalTo(idPinjaman);
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
                                        reference.child("bukti").setValue(task.getResult().toString());
                                        reference.child("status").setValue("diProses");
                                        Intent intent = new Intent(UserDetailAngsuranActivity.this, LoginActivity.class);
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserDetailAngsuranActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });



        } else {
            Toast.makeText(UserDetailAngsuranActivity.this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onStart: User Not Null");
        } else {
            signInAnonymously();
            Log.d(TAG, "onStart: User Null");
        }
    }

    private void signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_bayar_sekarang:
                uploadImage();
                break;
            case R.id.btnupload:
                Showchooseimage();
                break;
            case R.id.btnkamera:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, kodeKamera);
                break;
        }
    }
}
