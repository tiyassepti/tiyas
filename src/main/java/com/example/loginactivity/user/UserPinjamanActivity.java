package com.example.loginactivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserPinjamanActivity extends AppCompatActivity {
    private static final String TAG = "UserPinjamanActivity";

    MaterialEditText id, email, tanggal, alasan, namaRek, noRek, besarPinjaman, lamaPinjaman, angsuranan;
    Button ajukan,riwayat;
    long maxid;
    String mail;
    List<UserPinjaman> userPinjamanList;

String nama;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pinjaman);
        dialog = new ProgressDialog(UserPinjamanActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        id = findViewById(R.id.up_id);
        email = findViewById(R.id.up_email);
        tanggal = findViewById(R.id.up_tanggal);
        alasan = findViewById(R.id.up_alasan);
        namaRek = findViewById(R.id.up_namaRek);
        noRek = findViewById(R.id.up_noRek);
        besarPinjaman = findViewById(R.id.up_besarPinjam);
        lamaPinjaman = findViewById(R.id.up_lamaPinjam);
        angsuranan = findViewById(R.id.up_angsuran);
        ajukan = findViewById(R.id.up_ajukan);
        riwayat=findViewById(R.id.btnRiwayat);
        riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y=new Intent(UserPinjamanActivity.this, RiwayatPinjamanActivity.class);
                y.putExtra("EMAIL",email.getText().toString());
                startActivity(y);
            }
        });
        ajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPinjaman();
            }
        });

     lamaPinjaman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (besarPinjaman.getText()!=null && lamaPinjaman.getText()!=null){
                    Double hasil = Double.valueOf(besarPinjaman.getText().toString());
                    Double lama = Double.valueOf(lamaPinjaman.getText().toString());
                    DecimalFormat format = new DecimalFormat("0.#");
                    Double persen = hasil/lama;
                    angsuranan.setText(String.valueOf(format.format(persen)));
                }
            }
        });

        mail = getIntent().getStringExtra("EMAIL");
        //mail="tiyasana28@gmail.com";
        email.setText(mail);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid = dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange: " + maxid);
                id.setText("PJN-" + (maxid+1));
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String tanggalSekarang = format.format(new Date());
                tanggal.setText(tanggalSekarang);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserPinjamanActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private void cekPinjaman(){
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Pinjaman");
        
        ref.orderByChild("email")
                .equalTo(mail)
            //.orderByKey()
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //UserPinjaman userPinjaman = dataSnapshot.getValue();
                            Log.d(TAG, "onDataChange: ADA");
                            Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                            for (DataSnapshot dataUser : dataSnapshot.getChildren()){
                                UserPinjaman up = dataUser.getValue(UserPinjaman.class);
                                Log.d(TAG, "onDataChange: " + up.getSisaPinjam());
                                if (up.getSisaPinjam()>0){
                                    Toast.makeText(UserPinjamanActivity.this, "Anda Punya Pinjaman Yang Belum Lunas", Toast.LENGTH_LONG).show();
                                } else {
                                    inputDataPinjaman();
                                }
                            }
                        } else {
                            Log.d(TAG, "onDataChange: KOSONG");
                            inputDataPinjaman();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void inputDataPinjaman(){
        SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
        String jamSekarang = format.format(new Date());
        UserPinjaman userPinjaman = new UserPinjaman(
                id.getText().toString(),
                email.getText().toString(),
                tanggal.getText().toString(),
                jamSekarang,
                alasan.getText().toString(),
                namaRek.getText().toString(),
                noRek.getText().toString(),
                Long.valueOf(besarPinjaman.getText().toString()),
                Long.valueOf(lamaPinjaman.getText().toString()),
                Long.valueOf(angsuranan.getText().toString()),
                Long.valueOf(besarPinjaman.getText().toString()),
                "diajukan"
        );
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
        ref.child(String.valueOf(maxid+1)).setValue(userPinjaman);
        Toast.makeText(UserPinjamanActivity.this, "Pinjaman Berhasil Diajukan,Mohon Tunggu Beberapa saat Pinjaman akan diproses", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(UserPinjamanActivity.this, LoginActivity.class);
        intent.putExtra("EMAIL", mail);
        startActivity(intent);
    }

}
