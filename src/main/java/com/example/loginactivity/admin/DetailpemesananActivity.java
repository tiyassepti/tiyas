package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.example.loginactivity.model.PemesananMakanan;
import com.example.loginactivity.model.PeternakRegister;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class DetailpemesananActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "DetailPinjamanActivity";

    private List<PemesananMakanan> userMakananList;
    private List<PeternakRegister> listpeternak;

    TextView id;
    MaterialEditText tvNama, tvemail, tvTanggal, tvmakanan, tvjumlaha, tvharga, tvtotal;
    Button btnSetuju, btnTolak;
    String email, keterangan, keyid, jam, key;
    long maxid;
  String hp,nma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpemesanan);
        id = findViewById(R.id.dp_id);
        tvNama = findViewById(R.id.dp_nama);
        tvemail = findViewById(R.id.dp_email);
        tvTanggal = findViewById(R.id.dp_tanggal);
        tvmakanan= findViewById(R.id.dp_makanan);
        tvjumlaha = findViewById(R.id.dp_jml);
        tvharga = findViewById(R.id.dp_harga);
        tvtotal = findViewById(R.id.total);
        btnSetuju = findViewById(R.id.dp_btn_setuju);
        btnSetuju.setOnClickListener(this);
        btnTolak = findViewById(R.id.dp_btn_tolak);
        btnTolak.setOnClickListener(this);
        userMakananList = new ArrayList<>();
        listpeternak=new ArrayList<>();
        keyid = getIntent().getStringExtra("KEY");

        Log.d(TAG, "onCreate: " + keyid);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Pemesanan")
                .orderByChild("id")
                .equalTo(keyid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                    if (pinjamanSnapshot.child("status").getValue().equals("diproses")){
                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        PemesananMakanan ka = pinjamanSnapshot.getValue(PemesananMakanan.class);
                        userMakananList.add(ka);
                        key = pinjamanSnapshot.getKey();
                        email=ka.getEmail();
                        id.setText(ka.getId());

                        tvemail.setText(email);
                        tvTanggal.setText(ka.getTgl());
                        tvjumlaha.setText(String.valueOf(ka.getJml()));
                        tvharga.setText(String.valueOf(ka.getHarga()));
                        tvmakanan.setText(ka.getMakanan());
                        Double y=ka.getHarga()*Double.valueOf(ka.getJml());
                        tvtotal.setText(String.valueOf(y));
                    }
                }
                Log.d(TAG, "onDataChange: "+email);
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
                                PeternakRegister  user = pinjamanSnapshot.getValue(PeternakRegister.class);
                                listpeternak.add(user);
                                hp=user.getNohp();
                                nma=user.getNama();
                                tvNama.setText(user.getNama());

                        }

                        Log.d(TAG, "onDataChange: "+hp);
                        Log.d(TAG, "onDataChange: "+nma);
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
    private void inputData(){
        // public PemesananMakanan(String id, String makanan, int jml, Double harga, String tgl, String email, String status)
      PemesananMakanan userPinjaman = new PemesananMakanan(
                id.getText().toString(),
                tvmakanan.getText().toString(),
                Integer.valueOf(tvjumlaha.getText().toString()),
                Double.valueOf(tvharga.getText().toString()),
              tvTanggal.getText().toString(),
              tvemail.getText().toString(),


                keterangan
        );

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Pemesanan");
        ref.child(key).setValue(userPinjaman);
        Toast.makeText(DetailpemesananActivity.this, "Pesanan "+ keterangan, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(DetailpemesananActivity.this, AdminMainActivity.class);
        intent.putExtra("EMAIL", tvemail.getText().toString());
        startActivity(intent);
    }
    public void sendSmsByVIntent( ) {

        SmsManager smsManager=SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: "+hp);
        String pesan="Selamat pesanan anda disetujui dari KUD Cepogo boyolali dengan pesanan  "+tvmakanan.getText().toString()
                +"jumlah pesanan Rp."+tvjumlaha.getText().toString()+
                "Total pembayaran RP."+tvtotal.getText().toString()
                +" atas nama  "+tvNama.getText().toString()+" akan dikirim besok pagi di TPS ";      try{
            smsManager.sendTextMessage("082242571096","085640521383","Dari kud Cepogo Boyolali : pesanaan Makanan Ternak akan dikirim besok pagi di tps terdekat",null,null);
            Toast.makeText(DetailpemesananActivity.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailpemesananActivity.this, "Pengiriman SMS Gagal..."+ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
    public void sendSmsDitolak( ) {

        SmsManager smsManager = SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: " + hp);
        String pesan = "mohon maaf transaksi pemesanan anda  dari kud cepogo boyolali tidak bisa diproses karena barang tidak tersedia da ";
        try {
            smsManager.sendTextMessage(hp, "085640521383", pesan, null, null);
            Toast.makeText(DetailpemesananActivity.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailpemesananActivity.this, "Pengiriman SMS Gagal..." + ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dp_btn_setuju:
                keterangan = "Setuju";

               // inputData();
                sendSmsByVIntent();
                break;
            case R.id.dp_btn_tolak:
                keterangan = "Tolak";
                inputData();
                sendSmsDitolak();
                break;
        }
    }
}
