package com.example.loginactivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.loginactivity.R;
import com.example.loginactivity.fragment.AngsuranDiprosesFragment;
import com.example.loginactivity.fragment.RiwayatAngsuranFragment;

public class UserAngsuranActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserAngsuranActivity";

    Button btnDiproses, btnRiwayat;
    AngsuranDiprosesFragment angsuranDiprosesFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    String email,mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_angsuran);

       // email = getIntent().getStringExtra("EMAIL");
       // mode=getIntent().getStringExtra("MODE");
        email="tiyasana28@gmail.com";
        mode="peternak";
        Log.d(TAG, "onCreate: " + mode);

        btnDiproses = findViewById(R.id.btn_sedang_diproses);
        btnRiwayat = findViewById(R.id.btn_riwayat);
        btnDiproses.setOnClickListener(this);
        btnRiwayat.setOnClickListener(this);

        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        isiBundleSedangDiproses();

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_angsuran);
        if (fragment != null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    private void isiBundleSedangDiproses(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "isiBundleSedangDiproses: " + email);
        bundle.putString("EMAIL", email);
        AngsuranDiprosesFragment myFragment = new AngsuranDiprosesFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran, myFragment).commit();
    }

    private void isiBundleRiwayat(){
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);
        bundle.putString("MODE", mode);
        RiwayatAngsuranFragment myFragment = new RiwayatAngsuranFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran, myFragment).commit();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sedang_diproses:
                btnDiproses.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnRiwayat.setBackgroundResource(R.color.holo_orange);
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_angsuran);
                if (fragment != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment).commit();
                }
                isiBundleSedangDiproses();
                break;
            case R.id.btn_riwayat:
                btnRiwayat.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnDiproses.setBackgroundResource(R.color.holo_orange);
                Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragment_angsuran);
                if (fragment1 != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment1).commit();
                }
                isiBundleRiwayat();
                break;
        }
    }
}
