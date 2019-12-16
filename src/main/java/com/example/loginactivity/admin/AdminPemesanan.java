package com.example.loginactivity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.loginactivity.R;
import com.example.loginactivity.fragment.PencarianPemesananFragment;
import com.example.loginactivity.fragment.ProsesPemesananFragment;
import com.example.loginactivity.fragment.RiwayatPemesananFragment;

public class AdminPemesanan extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdminPemesanan";
    Button btnDiproses, btnRiwayat,btncari;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    EditText cari;


    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pemesanan);
        email="tiyassepti3@gmail.com";
        Log.d(TAG, "onCreate: " + email);

        btnDiproses = findViewById(R.id.aa_sedang_diproses);
        btnRiwayat = findViewById(R.id.aa_riwayat);
        btnDiproses.setOnClickListener(this);
        btnRiwayat.setOnClickListener(this);
        btncari=findViewById(R.id.btncari);
        btncari.setOnClickListener(this);
        cari=findViewById(R.id.peternak_search);
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnDiproses.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnRiwayat.setBackgroundResource(R.color.holo_orange);
                Fragment fragme = fragmentManager.findFragmentById(R.id.fragment_angsuran_admin);
                if (fragme != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragme).commit();
                }

                isiBundlecariDiproses();

            }
        });

        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        isiBundleSedangDiproses();

    }
    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_angsuran_admin);
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
     ProsesPemesananFragment myFragment = new ProsesPemesananFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }
    private void isiBundleRiwayatDiproses(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "isiBundleSedangDiproses: " + email);
        bundle.putString("EMAIL", email);
        RiwayatPemesananFragment myFragment = new RiwayatPemesananFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }
    private void isiBundlecariDiproses(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "isiBundleSedangDiproses: " + cari.getText().toString());
        bundle.putString("EMAIL", cari.getText().toString());
       PencarianPemesananFragment myFragment = new PencarianPemesananFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aa_sedang_diproses:
                Log.d(TAG, "onClick: ");
                btnDiproses.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnRiwayat.setBackgroundResource(R.color.holo_orange);
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_angsuran_admin);
                if (fragment != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment).commit();
                }
                isiBundleSedangDiproses();
                break;
            case R.id.aa_riwayat:
                Log.d(TAG, "onClick: ");
                btnDiproses.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnRiwayat.setBackgroundResource(R.color.holo_orange);
                Fragment fragmen = fragmentManager.findFragmentById(R.id.fragment_angsuran_admin);
                if (fragmen != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragmen).commit();
                }
                isiBundleRiwayatDiproses();
                break; case R.id.btncari:
                Log.d(TAG, "onClick: ");


                break;

    }}
}
