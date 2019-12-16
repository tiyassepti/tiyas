package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.PeternakRegister;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailAngsuranAdmin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ADetailAngsuran";

    String idPinjaman, idAngsuran, email, keyid;
    private List<UserPinjaman> userPinjamanList;
    private List<Angsuran> angsuranList;


    TextView tvTagihan, tvSisa, tvJumlahDilunasi, tvAngsuran, tvTanggal, tvNama, tvNorek,Tvnama,Tvemail;
    Button btnSetuju, btnTolak;
    ImageView gambar;
    String hp;
    private List<PeternakRegister> listpeternak;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_angsuran_admin);


        idPinjaman = getIntent().getStringExtra("IDP");
        idAngsuran = getIntent().getStringExtra("IDA");
        email = getIntent().getStringExtra("EMAIL");

        angsuranList = new ArrayList<>();
        userPinjamanList = new ArrayList<>();
        listpeternak= new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        tvTagihan = findViewById(R.id.ada_sisa);
        tvTanggal = findViewById(R.id.ada_tanggal_pembayaran);
        tvNama = findViewById(R.id.ada_nama);
        tvNorek = findViewById(R.id.ada_norek);
        tvJumlahDilunasi = findViewById(R.id.ada_sisa_pinjaman);
        tvAngsuran = findViewById(R.id.ada_tag_sebelumnya);
        Tvemail=findViewById(R.id.ada_email);
                Tvnama=findViewById(R.id.namapeternak);
        tvSisa = findViewById(R.id.ada_sisa2);
        btnSetuju = findViewById(R.id.ada_setuju);
        btnSetuju.setOnClickListener(this);
        btnTolak = findViewById(R.id.ada_tolak);
        btnTolak.setOnClickListener(this);
        gambar = findViewById(R.id.ada_img_bukti);
        Log.d(TAG, "onCreate id Angsuran: " + idAngsuran);
        Log.d(TAG, "onCreate id Pinjaman: " + idPinjaman);

        tampilkanData();
    }

    private void tampilkanData() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("angsuran")
                .orderByChild("kodeAngsuran")
                .equalTo(idAngsuran);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange Jumlah Data Id Angsuran: " + dataSnapshot.getChildrenCount());
                angsuranList.clear();
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                    //Check if user have sisa pinjaman
                    Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                    angsuranList.add(ang);
                    keyid = pinjamanSnapshot.getKey();
                    Log.d(TAG, "onDataChange: key id angsuran " + keyid);
                }

                Query q2 = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Pinjaman")
                        .orderByChild("id")
                        .equalTo(idPinjaman);
                q2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange Jumlah Data Id Pinjaman: " + dataSnapshot.getChildrenCount());
                        userPinjamanList.clear();
                        for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                            //Check if user have sisa pinjaman
                            UserPinjaman ang = pinjamanSnapshot.getValue(UserPinjaman.class);
                            userPinjamanList.add(ang);
                        }
                        Query query = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("peternak")
                                .orderByChild("email")
                                .equalTo(email);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                                    Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                                    PeternakRegister user = pinjamanSnapshot.getValue(PeternakRegister.class);
                                    listpeternak.add(user);
                                    hp=user.getNohp();
                                    //nma=user.getNama();
                                    Tvnama.setText(user.getNama());

                                }

                                Log.d(TAG, "onDataChange: "+hp);
                                //Log.d(TAG, "onDataChange: "+nma);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        tvNama.setText(userPinjamanList.get(0).getNamarek());
                        Tvemail.setText(email);
                        tvNorek.setText(userPinjamanList.get(0).getNorek());
                        tvTagihan.setText(String.valueOf(angsuranList.get(0).getJumlah()));
                        tvTanggal.setText("Tanggal Pembayaran " + angsuranList.get(0).getJatuhTempo());
                        tvJumlahDilunasi.setText(String.valueOf(userPinjamanList.get(0).getSisaPinjam()));
                        tvAngsuran.setText(String.valueOf(angsuranList.get(0).getJumlah()));
                        long b = userPinjamanList.get(0).getSisaPinjam() - angsuranList.get(0).getJumlah();
                        Log.d(TAG, "onDataChange: " + b);
                        tvSisa.setText(String.valueOf(b));
                        Picasso.with(DetailAngsuranAdmin.this)
                                .load(angsuranList.get(0).getBukti())
                                .into(gambar);
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

    private void updateAngsuran(String keterangan) {
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("angsuran");
        reference.child(keyid).child("status").setValue(keterangan);

        if (keterangan.equals("Setuju")){
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Pinjaman");
            ref.child(idPinjaman).child("sisaPinjam").setValue(Long.valueOf(tvSisa.getText().toString()));
        }

        Intent intent = new Intent(DetailAngsuranAdmin.this, AdminAngsuran.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }
    public void sendSmsByVIntent( ) {

        SmsManager smsManager=SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: "+hp);
        String pesan="DARI KUD BOYOLALI:" +
                "Selamat Angsuran Anda disetuji kud boyolali sebesar"+tvAngsuran.getText().toString();      try{
            smsManager.sendTextMessage(hp,"085640521383",pesan,null,null);
            Toast.makeText(DetailAngsuranAdmin.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailAngsuranAdmin.this, "Pengiriman SMS Gagal..."+ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
    public void sendSmsDitolak( ) {

        SmsManager smsManager = SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: " + hp);
        String pesan = "mohon maaf transaksi pemesanan anda  dari kud cepogo boyolali tidak bisa diproses karena data kurang lengkap ";
        try {
            smsManager.sendTextMessage(hp, "085640521383", pesan, null, null);
            Toast.makeText(DetailAngsuranAdmin.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailAngsuranAdmin.this, "Pengiriman SMS Gagal..." + ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ada_setuju:
                updateAngsuran("Setuju");
                Log.d(TAG, "onClick: Setuju");
                sendSmsByVIntent();
//                pinjaman(key);
                break;
            case R.id.ada_tolak:
                updateAngsuran("Tolak");
                sendSmsDitolak();
                break;
        }
    }

}

