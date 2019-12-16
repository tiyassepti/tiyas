package com.example.loginactivity.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.model.PeternakRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TampilDataActivity extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;


    private static final String TAG = "DataActivity";
    RecyclerView recyclerView;
    PeternakAdapter rvAdapter;
    DatabaseReference refer;

    private List<PeternakRegister> listPeternak;
    FirebaseDatabase database;
    String email;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);
        email=getIntent().getStringExtra("EMAIL");
        refer=FirebaseDatabase.getInstance().getReference();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("peternak")
                .orderByChild( "email")
                .equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPeternak.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                    PeternakRegister ka = pinjamanSnapshot.getValue(PeternakRegister.class);
                    listPeternak.add(ka);
                }
                rvAdapter = new PeternakAdapter(TampilDataActivity.this, listPeternak);
                recyclerView.setAdapter(rvAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog = new ProgressDialog(TampilDataActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        listPeternak = new ArrayList<PeternakRegister>();

        recyclerView = findViewById(R.id.rv_Datapeternak1);
        linearLayoutManager = new LinearLayoutManager(TampilDataActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
