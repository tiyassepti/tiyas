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
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.fragment.AngsuranDiprosesFragment;
import com.example.loginactivity.fragment.PencarianAngsuranFragment;
import com.example.loginactivity.fragment.PencarianPemesananFragment;
import com.example.loginactivity.fragment.RiwayatAngsuranFragment;

public class AdminAngsuran extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdminAngsuran";

    Button btnDiproses, btnRiwayat;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

TextView cari;
Button btncari;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_angsuran);

        //email = getIntent().getStringExtra("EMAIL");
        email="tiyasana28@gmail.com";
        Log.d(TAG, "onCreate: " + email);

        btnDiproses = findViewById(R.id.aa_sedang_diproses);
        btnRiwayat = findViewById(R.id.aa_riwayat);
        cari=findViewById(R.id.peternak_search);
        btncari=findViewById(R.id.btncari);
        btncari.setOnClickListener(this);
        btnDiproses.setOnClickListener(this);
        btnRiwayat.setOnClickListener(this);

        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        isiBundleSedangDiproses();
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

    }

    private void isiBundleSedangDiproses(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "isiBundleSedangDiproses: " + email);
        bundle.putString("EMAIL", email);
        bundle.putString("MODE", "admin");
        AngsuranDiprosesFragment myFragment = new AngsuranDiprosesFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }

    private void isiBundleRiwayat(){
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);
        bundle.putString("MODE", "admin");
        RiwayatAngsuranFragment myFragment = new RiwayatAngsuranFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }
    private void isiBundlecariDiproses(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "isiBundleSedangDiproses: " + cari.getText().toString());
        bundle.putString("EMAIL", cari.getText().toString());
        PencarianAngsuranFragment myFragment = new PencarianAngsuranFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_angsuran_admin, myFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aa_sedang_diproses:
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
                btnRiwayat.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnDiproses.setBackgroundResource(R.color.holo_orange);
                Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragment_angsuran_admin);
                if (fragment1 != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment1).commit();
                }
                isiBundleRiwayat();
                break;
        }
    }
}
