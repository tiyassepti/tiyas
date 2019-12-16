package com.example.loginactivity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.user.UserAngsuranActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDataPeternak, btnAngsuran, btnPinjaman, btnSimpanan, btnLaporan, btnPemesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnDataPeternak = findViewById(R.id.admin_peternak);
        btnAngsuran = findViewById(R.id.admin_angsuran);
        btnPinjaman = findViewById(R.id.admin_pinjaman);
        btnSimpanan = findViewById(R.id.admin_Ubahharga);
        btnLaporan = findViewById(R.id.admin_laporan);
        btnPemesanan = findViewById(R.id.admin_Pemesanan);
        btnDataPeternak.setOnClickListener(this);
        btnAngsuran.setOnClickListener(this);
        btnPinjaman.setOnClickListener(this);
        btnSimpanan.setOnClickListener(this);
        btnLaporan.setOnClickListener(this);
        btnPemesanan.setOnClickListener(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnDataPeternak.getLayoutParams();
        params.width = (getResources().getDisplayMetrics().widthPixels)/3;
        params.height = ((getResources().getDisplayMetrics().widthPixels)/4);
        btnDataPeternak.setLayoutParams(params);
        btnAngsuran.setLayoutParams(params);
        btnPinjaman.setLayoutParams(params);
        btnSimpanan.setLayoutParams(params);
        btnLaporan.setLayoutParams(params);
        btnPemesanan.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.admin_peternak:
                //WES BERES CUK
                startActivity(new Intent(AdminMainActivity.this, RecyclerViewPeternakActivity.class));
                break;
            case R.id.admin_angsuran:
                startActivity(new Intent(AdminMainActivity.this, AdminAngsuran.class));
                break;
            case R.id.admin_pinjaman:
                startActivity(new Intent(AdminMainActivity.this, AdminPinjaman.class));
                break;
            case R.id.admin_Ubahharga:
                startActivity(new Intent(AdminMainActivity.this, TampilItemActivity.class));
                break;
            case R.id.admin_laporan:
                startActivity(new Intent(AdminMainActivity.this, AdminLaporanActivity.class));
//                finish();
                break;
            case R.id.admin_Pemesanan:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminMainActivity.this,AdminPemesanan.class));
                finish();
                break;
        }
    }
}
