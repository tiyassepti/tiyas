package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginactivity.admin.AdminMainActivity;
import com.example.loginactivity.model.Peternak;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

private static final String TAG="LoginActivity";
private DatabaseReference mDatabase;
private FirebaseAuth mAuth ;
private EditText edtEmail;
private EditText edtPass;
private Button btnMasuk;
private Button btnDaftar;
private  Button btnforget;
 private ProgressDialog dialog;
 private CheckBox showpass;
private static final int perlogin=1000;

    private String emaill="EMAIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog=new ProgressDialog(MainActivity.this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        showpass = findViewById(R.id.showpass);
        edtEmail = findViewById(R.id.txemail);
        edtPass =  findViewById(R.id.txpass);
        btnMasuk = findViewById(R.id.btnmasuk);
        btnDaftar = findViewById(R.id.btn_daftar);
        btnforget = findViewById(R.id.btnforgot);

        edtEmail.setText("tiyasssepti3@gmail.com");
        edtPass.setText("123455");

        int hasil=3;

        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnforget.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
        btnMasuk.setOnClickListener(this);



    }private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return ;
        }
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {


                            if (mAuth.getCurrentUser().isEmailVerified()) {

                                Log.d(TAG, "onComplete: success");
                                onAuthSuccess(task.getResult().getUser());

                                dialog.dismiss();

                            } else {
                                Toast.makeText(MainActivity.this, "Please Verify Email Address", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });
        edtEmail.setText("");
        edtPass.setText("");

    }



    private void onAuthSuccess(FirebaseUser user) {
        final String username=usernameFromEmail(user.getEmail());
        // writeNewAdmin(user.getUid(),username,user.getEmail());
        Log.d(TAG, "onDataChange: "+user.getEmail());

        String nama=edtEmail.getText().toString();
        if(nama!=null && nama!=""){
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("peternak")
                    .orderByChild("email")
                    .equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        if (pinjamanSnapshot.child("userType").getValue().equals("admin")){


                            Intent y=new Intent(MainActivity.this, AdminMainActivity.class);
                            y.putExtra(emaill, username);
                            startActivity(y);
                        } else {
                            Intent y=new Intent(MainActivity.this,LoginActivity.class);
                            y.putExtra(emaill, username);
                            startActivity(y);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            finish();}
    }

    private void writeNewAdmin(String uid, String username, String email) {
        Peternak peternak=new Peternak(username,email);
        mDatabase.child("peternak").child(uid).setValue(peternak);
    }

    private String usernameFromEmail(String email) {
        if(email.contains("@")) {

            return email.split("@")[0];

        }else {
            return email;
        }
    }
    private boolean validateForm() {
        boolean result=true;
        if(TextUtils.isEmpty(edtEmail.getText().toString())){
            edtEmail.setError("Required");
            result=false;
        }else {
            edtEmail.setError(null);
        }
        if(TextUtils.isEmpty(edtPass.getText().toString())){
            edtPass.setError("Required");
            result=false;
        }else {
            edtPass.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        int i=view.getId();
        if(i==R.id.btnmasuk) {

            dialog.setMessage("Loading");
            dialog.show();

            signIn();


        }else if(i==R.id.btn_daftar){
            signUp();
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }else if(i==R.id.btnforgot){

            startActivity(new Intent(MainActivity.this,ForgotpassActivity.class));
        }
    }

    private void signUp() {
        Log.d(TAG,"signUp");
        if(!validateForm()){
            return;
        }
    }}

