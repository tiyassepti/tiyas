package com.example.loginactivity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.admin.RecyclerViewPeternakActivity;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class PemesananUser extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "pemesananUserActivity";

    MaterialEditText id, tgl,jumlah,harga,total;
    String email;
    Button pesan,batal;
    Spinner namaMakanan;
    DatabaseReference database;
    TextView nama;
    long maxid;
    String tanggalSekarang;
    double mkn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan_user);
        id=findViewById(R.id.idpemesanan);
        tgl=findViewById(R.id.tgl);
        jumlah=findViewById(R.id.jml);
        harga=findViewById(R.id.harga);
        namaMakanan=findViewById(R.id.namaMakanan);
        total=findViewById(R.id.total);
        pesan=findViewById(R.id.btnpesan);
        batal=findViewById(R.id.btnbatal);
        //
        // email = getIntent().getStringExtra("EMAIL");
        email="tiyassepti3@gmail.com";


        pesan.setOnClickListener(this);
        batal.setOnClickListener(this);
       // tgl.setText(tanggalSekarang);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String tanggalSekarang = format.format(new Date());
        tgl.setText(tanggalSekarang);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Pemesanan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid = dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange: " + maxid);
                id.setText("PSN-" + (maxid+1));

                // final pemesananM pemesananuser=new PemesananMakanan(id.getText().toString(),namaMakanan.getSelectedItem().toString(), Integer.valueOf(jumlah.getText().toString()), Double.valueOf(harga.getText().toString()) ,tanggalSekarang,email);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PemesananUser.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        namaMakanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Query query = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("ItemMakanan")
                        .orderByChild("nama")
                        .equalTo(namaMakanan.getSelectedItem().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                        Log.d(TAG, "onDataChange Key 1: " + dataSnapshot.getKey());
                        for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                          MakananSapi ka = pinjamanSnapshot.getValue(MakananSapi.class);
                          mkn=ka.getHarga();
                          harga.setText(String.valueOf(mkn));

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });}


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        database = FirebaseDatabase.getInstance().getReference();


        jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(jumlah.getText().toString().equals("")){
                    total.setText("0");
                }else{
                double jml=Double.valueOf(harga.getText().toString())*Double.valueOf(jumlah.getText().toString());
                total.setText(String.valueOf(jml));}

            }
        });


    }
    public void editdata(PemesananMakanan user){

        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("Pemesanan") //akses parent index, ibaratnya seperti nama tabel
                .child(String.valueOf(maxid+1))
                .setValue(user) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener
                        <Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /**
                         * Baris kode yang akan dipanggil apabila proses update barang sukses
                         */
                        Toast.makeText(PemesananUser.this, "pesanan berhasil", Toast.LENGTH_SHORT).show();
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        };
                    }
                });}

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnpesan:
                Log.d(TAG, "onClick: btnpesan");
PemesananMakanan pemesananmakanan=new PemesananMakanan(id.getText().toString(),namaMakanan.getSelectedItem().toString(), Integer.valueOf(jumlah.getText().toString()), Double.valueOf(harga.getText().toString()) ,tgl.getText().toString(),"tiyassepti3@gmail.com","diproses");
                editdata(pemesananmakanan);
                break;
            case R.id.btnbatal:
                Intent l=new Intent(PemesananUser.this, RiwayatPemesanan.class);
                l.putExtra("EMAIL",email);
                startActivity(l);
                break;
        }
    }
}
