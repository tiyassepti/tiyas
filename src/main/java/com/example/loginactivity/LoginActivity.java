package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loginactivity.user.PemesananUser;
import com.example.loginactivity.user.UserAngsuranActivity;
import com.example.loginactivity.user.UserPinjamanActivity;
import com.example.loginactivity.user.TampilDataActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    
    FirebaseUser user;
    FirebaseAuth auth;
    Button btnSignOut, btnPinjaman, btnHistory, btnAngsuran, btnRegister;
    String email="";
    String emailditerima="EMAIL";
    String nama, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //nama=getIntent().getStringExtra("EMAIL");
        nama = "tiyasana28@gmail.com";

        btnSignOut = findViewById(R.id.btn_keluar);
        btnAngsuran = findViewById(R.id.btn_angsuran);
        btnHistory = findViewById(R.id.btn_history);
        btnPinjaman = findViewById(R.id.btn_pinjaman);
        btnRegister = findViewById(R.id.btn_register);

        btnSignOut.setOnClickListener(this);
        btnAngsuran.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnPinjaman.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

/*if (auth!=null && user.isEmailVerified()){

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });


    }*/}

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_register:
                Intent l=new Intent(LoginActivity.this, PemesananUser.class);
                l.putExtra("EMAIL",nama);
                startActivity(l);

                break;
            case R.id.btn_pinjaman:
                Intent m=new Intent(LoginActivity.this, UserPinjamanActivity.class);
                m.putExtra("EMAIL",nama);
                startActivity(m);

                break;
            case R.id.btn_history:
                Intent y=new Intent(LoginActivity.this, TampilDataActivity.class);
                y.putExtra("EMAIL",nama);
                startActivity(y);

                break;
            case R.id.btn_angsuran:
                Intent intent =new Intent(LoginActivity.this, UserAngsuranActivity.class);
                intent.putExtra("EMAIL",nama);
                intent.putExtra("MODE","peternak");


                startActivity(intent);

                break;
        }

    }
}
