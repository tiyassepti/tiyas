package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.admin.DetailPinjamanActivity;
import com.example.loginactivity.model.PeternakRegister;
import com.example.loginactivity.model.Pinjaman;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class DetailPeternakActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailPeternakActivity";
    String uid, nama, kelurahan, email, ktp, nohp, gambar,pass,key;
    TextView txtId;
    MaterialEditText txtNama, txtKelurahan, txtEmail, txtKtp, txtNoHp,txtpassword;
    ImageView imgKtp;
    Button ubah,simpan;
    DatabaseReference database;
    private FirebaseAuth auth;
    String GetUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peternak);

        database =  FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        GetUserID= getIntent().getStringExtra("EMAIL");


        txtId = findViewById(R.id.detail_id);
        txtNama = findViewById(R.id.detail_nama);
        txtKelurahan = findViewById(R.id.detail_kelurahan);
        txtEmail = findViewById(R.id.detail_email);
        txtKtp = findViewById(R.id.detail_ktp);
        txtNoHp = findViewById(R.id.detail_no_hp);
        imgKtp = findViewById(R.id.detail_img_ktp);
        txtpassword=findViewById(R.id.detail_password);
        ubah=findViewById(R.id.btnubah);
        simpan=findViewById(R.id.btnsimpan);
        ubah.setOnClickListener(this);
        simpan.setOnClickListener(this);


        uid = getIntent().getStringExtra("ID");
        nama = getIntent().getStringExtra("NAMA");
        email = getIntent().getStringExtra("EMAIL");
        kelurahan = getIntent().getStringExtra("KELURAHAN");
        ktp = getIntent().getStringExtra("KTP");
        nohp = getIntent().getStringExtra("NOHP");
        pass=getIntent().getStringExtra("PASSWORD");

        gambar = getIntent().getStringExtra("GAMBAR");
        Log.d(TAG, "onCreate: " + gambar);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("peternak")
                .orderByChild("id")
                .equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"OnDataChange:JumlahData"+dataSnapshot.getChildrenCount());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                    key=pinjamanSnapshot.getKey();
                    Log.d(TAG,"onDataChange "+key);
                    final PeternakRegister barang= new PeternakRegister();


                    simpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            barang.setKelurahan(txtKelurahan.getText().toString());
                            barang.setPassword(txtpassword.getText().toString());
                            barang.setNohp(txtNoHp.getText().toString());
                            barang.setId(txtId.getText().toString());
                            barang.setNama(txtNama.getText().toString());
                            barang.setKtp(txtKtp.getText().toString());
                            barang.setGambar(gambar);
                            barang.setEmail(txtEmail.getText().toString());

                            editdata(barang);
                            simpandata();
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtId.setText(uid);
        txtNama.setText(nama);
        txtKelurahan.setText(kelurahan);
        txtEmail.setText(email);
        txtKtp.setText(ktp);
        txtNoHp.setText(nohp);
        txtpassword.setText(pass);

        Picasso.with(this)
                .load(gambar)
                .fit()
                .centerCrop()
                .into(imgKtp);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathRef = storageRef.child("Peternak/1576002691671.jpg");
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        });

//        try {
//            File localFile = File.createTempFile("images", "jpg");
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                    .child("Peternak/1576002691671.jpg");
//            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    public void Ubahdata(){
        txtKelurahan.setEnabled(true);
        txtNoHp.setEnabled(true);
        txtpassword.setEnabled(true);
        txtKelurahan.requestFocus();
    }
    public void simpandata(){
        txtKelurahan.setEnabled(false);
        txtNoHp.setEnabled(false);
        txtpassword.setEnabled(false);
        txtKelurahan.requestFocus();
    }
    public void editdata(PeternakRegister peternak){

        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("peternak") //akses parent index, ibaratnya seperti nama tabel
                .child(key) //select barang berdasarkan key
                .setValue(peternak) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener
                        <Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /**
                         * Baris kode yang akan dipanggil apabila proses update barang sukses
                         */
                        Toast.makeText(DetailPeternakActivity.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        };
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnubah:
                Ubahdata();
                break;
        }}
}
