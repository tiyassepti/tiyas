package com.example.loginactivity.user;

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
import com.example.loginactivity.fragment.PencarianPinjamanFragment;
import com.example.loginactivity.fragment.PencarianUserPinjaman;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RiwayatPinjamanActivity extends AppCompatActivity {

    private static final String TAG = "AdminPinjaman";


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String email;
    Date d;
    int mYear,mMonth,mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pinjaman);
        //email = getIntent().getStringExtra("EMAIL");
        email = "tiyasana28@gmail.com";

        Log.d(TAG, "onCreate: " + email);




        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Date k=new Date();
        Log.d(TAG, "onCreate: "+k);
        isiBundlePencarian();


    }

    private void isiBundleRiwayat(){
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);

        PencarianPinjamanFragment myFragment = new PencarianPinjamanFragment();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_pinjaman_user, myFragment).commit();
    }
    private void isiBundlePencarian(){

        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);

        PencarianUserPinjaman myFragment = new PencarianUserPinjaman();
        myFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_pinjaman_user, myFragment).commit();
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


}
