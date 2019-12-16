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

import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.PeternakRegister;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailPinjamanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailPinjamanActivity";

    private List<UserPinjaman> userPinjamanList;
    private List<PeternakRegister> listpeternak;

    TextView id;
    MaterialEditText tvNama, tvNorek, tvTanggal, tvBesar, tvLama, tvAngsuran, tvAlasan;
    Button btnSetuju, btnTolak;
    String email, keterangan, keyid, jam, key;
    long maxid;
    String hp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pinjaman);

        id = findViewById(R.id.dp_id);
        tvNama = findViewById(R.id.dp_nama);
        tvNorek = findViewById(R.id.dp_norek);
        tvTanggal = findViewById(R.id.dp_tanggal);
        tvBesar = findViewById(R.id.dp_besarPinjam);
        tvLama = findViewById(R.id.dp_lamaPinjam);
        tvAngsuran = findViewById(R.id.dp_angsuran);
        tvAlasan = findViewById(R.id.dp_alasan);
        btnSetuju = findViewById(R.id.dp_btn_setuju);
        btnSetuju.setOnClickListener(this);
        btnTolak = findViewById(R.id.dp_btn_tolak);
        btnTolak.setOnClickListener(this);

        userPinjamanList = new ArrayList<>();
        keyid = getIntent().getStringExtra("KEY");

        Log.d(TAG, "onCreate: " + keyid);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Pinjaman")
                .orderByChild("id")
                .equalTo(keyid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                    if (pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                        userPinjamanList.add(ka);
                        key = pinjamanSnapshot.getKey();
                    }
                }

                Query query = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("peternak")
                        .orderByChild("email")
                        .equalTo(userPinjamanList.get(0).getEmail());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                            if (pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                                Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                                PeternakRegister  user = pinjamanSnapshot.getValue(PeternakRegister.class);
                               listpeternak.add(user);
                                hp=user.getNohp();
                            }
                        }

                        Log.d(TAG, "onDataChange: "+hp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                id.setText(userPinjamanList.get(0).getId());
                tvNama.setText(userPinjamanList.get(0).getNamarek());
                tvNorek.setText(userPinjamanList.get(0).getNorek());
                tvTanggal.setText(userPinjamanList.get(0).getTglPinjam());
                tvBesar.setText(String.valueOf(userPinjamanList.get(0).getBesarPinjam()));
                tvLama.setText(String.valueOf(userPinjamanList.get(0).getLamaPinjam()));
                tvAngsuran.setText(String.valueOf(userPinjamanList.get(0).getAngsuran()));
                tvAlasan.setText(userPinjamanList.get(0).getAlasan());
                email = userPinjamanList.get(0).getEmail();
                jam = userPinjamanList.get(0).getJamPinjam();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dp_btn_setuju:
                keterangan = "Setuju";
                inputAngsuran();
                inputData();
                sendSmsByVIntent();
                break;
            case R.id.dp_btn_tolak:
                keterangan = "Tolak";
                inputData();
                sendSmsDitolak();
                break;
        }
    }

    private void inputData(){
        UserPinjaman userPinjaman = new UserPinjaman(
                id.getText().toString(),
                email,
                tvTanggal.getText().toString(),
                jam,
                tvAlasan.getText().toString(),
                tvNama.getText().toString(),
                tvNorek.getText().toString(),
                Long.valueOf(tvBesar.getText().toString()),
                Long.valueOf(tvLama.getText().toString()),
                Long.valueOf(tvAngsuran.getText().toString()),
                Long.valueOf(tvBesar.getText().toString()),
                keterangan
        );

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
        ref.child(key).setValue(userPinjaman);
        Toast.makeText(DetailPinjamanActivity.this, "Pinjaman Berhasil Diajukan", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(DetailPinjamanActivity.this, AdminMainActivity.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }

    private void inputAngsuran(){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMMM yyyy");
        LocalDate localDate = LocalDate.now().plusMonths(1);
        for (int i=1; i<=Integer.valueOf(tvLama.getText().toString()); i++){
            String idAng = "ANG-"+(maxid+1);
            Log.d(TAG, "inputData Bulan Depan " + idAng + " : " + dtf.print(localDate));
            Angsuran angsuran = new Angsuran(idAng,
                    id.getText().toString(),
                    email,
                    Long.valueOf(String.valueOf(i)),
                    Long.valueOf(tvAngsuran.getText().toString()),
                    dtf.print(localDate),
                    "Proses",
                    ""
                    );
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("angsuran");
            ref.child(String.valueOf((maxid+1))).setValue(angsuran);
            localDate = localDate.plusMonths(1);
            maxid = maxid + 1;
        }
    }
    public void sendSmsByVIntent( ) {

        SmsManager smsManager=SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: "+hp);
        String pesan="Selamat pinjaman anda disetujui dari KUD Cepogo boyolali dengan nominal "+tvBesar.getText().toString()+" atas nama  "+tvNama.getText().toString()+" silahkan cek sekitar 15 menit dari sms ini";      try{
            smsManager.sendTextMessage(hp,"null","Selamat pinjaman anda disetujui , silahkan cek rekening anda sekitarr 15 menit",null,null);
            Toast.makeText(DetailPinjamanActivity.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailPinjamanActivity.this, "Pengiriman SMS Gagal..."+ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
    public void sendSmsDitolak( ) {

        SmsManager smsManager=SmsManager.getDefault();
        Log.d(TAG, "sendSmsByVIntent: "+hp);
        String pesan="mohon maaf transaksi pinjaman anda tidak bisa diproses karena data tidak lengkap/atau masih memiliki transaksi yang belum selsai ";
                try{
            smsManager.sendTextMessage(hp,"null","Selamat pinjaman anda disetujui , silahkan cek rekening anda sekitarr 15 menit",null,null);
            Toast.makeText(DetailPinjamanActivity.this, "Pengiriman SMS berhasil...",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(DetailPinjamanActivity.this, "Pengiriman SMS Gagal..."+ex,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
}
