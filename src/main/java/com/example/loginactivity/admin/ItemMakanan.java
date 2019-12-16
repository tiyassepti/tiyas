package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginactivity.R;

import com.example.loginactivity.model.MakananSapi;
import com.example.loginactivity.model.PemesananMakanan;

import com.example.loginactivity.model.PeternakRegister;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.loginactivity.R.id.namaMakanan;

public class ItemMakanan extends AppCompatActivity {
    private static final String TAG = "pemesananUserActivity";
    MaterialEditText id, nama,harga;
    String email;
    Button tambah,batal;
    long maxid;
    DatabaseReference database;
    String key,uid,namas,hargas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_makanan);
        id=findViewById(R.id.iditem);
        nama=findViewById(R.id.Namamakanan);
        harga=findViewById(R.id.hargamakanan);
        tambah=findViewById(R.id.btnTambah);
        database = FirebaseDatabase.getInstance().getReference();
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasi();

            }
        });
        uid = getIntent().getStringExtra("ID");

        namas = getIntent().getStringExtra("NAMA");
        hargas = getIntent().getStringExtra("Harga");

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ItemMakanan")
                .orderByChild("id")
                .equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"OnDataChange:JumlahData"+dataSnapshot.getChildrenCount());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                    key=pinjamanSnapshot.getKey();
                    Log.d(TAG,"onDataChange "+key);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        id.setText(uid);
        nama.setText(namas);
        harga.setText(hargas);

    }
    public void editdata(MakananSapi user){

        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("ItemMakanan") //akses parent index, ibaratnya seperti nama tabel
                .child(String.valueOf(key))
                .setValue(user) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener
                        <Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /**
                         * Baris kode yang akan dipanggil apabila proses update barang sukses
                         */
                        Toast.makeText(ItemMakanan.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        };
                    }
                });
    id.setText("");
    nama.setText("");
    harga.setText("");}
    public void validasi() {



        if (harga.getText().toString().equals("")) {
            harga.setError("Harga tidak boleh kosong");
        } else {
            MakananSapi sp= new MakananSapi(id.getText().toString(),nama.getText().toString(),Double.valueOf(harga.getText().toString()));
            editdata(sp);

        }

    }

}
