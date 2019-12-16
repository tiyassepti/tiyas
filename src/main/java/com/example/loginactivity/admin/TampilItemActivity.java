package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.ItemMakananAdapter;
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.model.MakananSapi;
import com.example.loginactivity.model.PeternakRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TampilItemActivity extends AppCompatActivity {
    private static final String TAG = "ItemMakanan";

    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
   ItemMakananAdapter rvAdapter;

    private List<MakananSapi> listMakanan;
    List<String> uid;

    String id;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_item);
        Query ref = FirebaseDatabase.getInstance().getReference().child("ItemMakanan");


       ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMakanan.clear();
                uid.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                Log.d(TAG, "onDataChange Key 1: " + dataSnapshot.getKey());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                    MakananSapi ka = pinjamanSnapshot.getValue(MakananSapi.class);
                    uid.add(pinjamanSnapshot.getKey());
                    listMakanan.add(ka);
                    id = ka.getId();

                    Log.d(TAG, "onDataChange: " + listMakanan);
                    rvAdapter = new ItemMakananAdapter(TampilItemActivity.this, listMakanan);
                    recyclerView.setAdapter(rvAdapter);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog = new ProgressDialog(TampilItemActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        listMakanan = new ArrayList<MakananSapi>(); uid = new ArrayList<String>();

        recyclerView = findViewById(R.id.rv_tampildata);
        linearLayoutManager = new LinearLayoutManager(TampilItemActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    } private void fetch() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("ItemMakanan");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMakanan.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot peternakSnapshot : dataSnapshot.getChildren()){
                    MakananSapi ka = peternakSnapshot.getValue(MakananSapi.class);
                    listMakanan.add(ka);
                    uid.add(dataSnapshot.getKey());
                }
                rvAdapter = new ItemMakananAdapter(TampilItemActivity.this, listMakanan);
                recyclerView.setAdapter(rvAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
