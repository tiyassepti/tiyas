package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
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
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.fragment.PencarianPinjamanFragment;
import com.example.loginactivity.fragment.PinjamanDiprosesFragment;
import com.example.loginactivity.fragment.RiwayatPinjamanFragment;
import com.example.loginactivity.model.PeternakRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminPinjaman extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AdminPinjaman";

    Button btnDiproses, btnRiwayat,btncari;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    EditText cari;

    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pinjaman);

        email = getIntent().getStringExtra("EMAIL");
        Log.d(TAG, "onCreate: " + email);

        btnDiproses = findViewById(R.id.ap_sedang_diproses);
        btnRiwayat = findViewById(R.id.ap_riwayat);
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
                btnRiwayat.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnDiproses.setBackgroundResource(R.color.holo_orange);
                Fragment fragment2 = fragmentManager.findFragmentById(R.id.fragment_pinjaman);
                if (fragment2 != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment2).commit();
                }
                isiBundlePencarian();

            }
        });

        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        isiBundleSedangDiproses();

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_pinjaman);
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
        PinjamanDiprosesFragment myFragment = new PinjamanDiprosesFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_pinjaman, myFragment).commit();
    }

    private void isiBundleRiwayat(){
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);
        RiwayatPinjamanFragment myFragment = new RiwayatPinjamanFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_pinjaman, myFragment).commit();
    }
    private void isiBundlePencarian(){
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", cari.getText().toString());
        PencarianPinjamanFragment myFragment = new PencarianPinjamanFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_pinjaman, myFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ap_sedang_diproses:
                btnDiproses.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnRiwayat.setBackgroundResource(R.color.holo_orange);
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_pinjaman);
                if (fragment != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment).commit();
                }
                isiBundleSedangDiproses();
                break;
            case R.id.ap_riwayat:
                btnRiwayat.setBackgroundResource(R.drawable.bg_button_orange_putih);
                btnDiproses.setBackgroundResource(R.color.holo_orange);
                Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragment_pinjaman);
                if (fragment1 != null){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment1).commit();
                }
                isiBundleRiwayat();
                break;
            case R.id.btncari:

                break;
        }
    }
}
