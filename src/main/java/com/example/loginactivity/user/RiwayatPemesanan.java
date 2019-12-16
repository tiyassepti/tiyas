package com.example.loginactivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.loginactivity.R;
import com.example.loginactivity.fragment.PencarianPemesananFragment;
import com.example.loginactivity.fragment.PencarianUserPinjaman;

import java.util.Date;

public class RiwayatPemesanan extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String email;
    private static final String TAG = "Riwayat pinjaman";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pemesanan);
        email = "tiyassepti3@gmail.com";

        Log.d(TAG, "onCreate: " + email);




        Log.d(TAG, "onCreate: fragment");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Date k=new Date();
        Log.d(TAG, "onCreate: "+k);
        isiBundlePencarian();}
        private void isiBundlePencarian(){

            Bundle bundle = new Bundle();
            bundle.putString("EMAIL", email);

            PencarianPemesananFragment myFragment = new PencarianPemesananFragment();
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
