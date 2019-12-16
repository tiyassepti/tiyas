package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.fragment.RecyclerViewFragment;
import com.example.loginactivity.model.PeternakRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPeternakActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewPeternakAct";

    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
    PeternakAdapter rvAdapter;

    private List<PeternakRegister> listPeternak;
    List<String> uid;
    FirebaseDatabase database;
    EditText edtSearch;
    Button btnSearch;
    TextView txt;
    String id;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_peternak);
        txt=findViewById(R.id.txtpesan);
        edtSearch = findViewById(R.id.peternak_search);
        btnSearch = findViewById(R.id.btn_Keluar);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtSearch.getText().toString().equals("")){
                    edtSearch.setError("Data belum diinputkan");
                }else{
                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("peternak")
                            .orderByChild("email")
                            .equalTo(edtSearch.getText().toString());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listPeternak.clear();
                            uid.clear();
                            Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                            Log.d(TAG, "onDataChange Key 1: " + dataSnapshot.getKey());
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                PeternakRegister ka = pinjamanSnapshot.getValue(PeternakRegister.class);
                                uid.add(pinjamanSnapshot.getKey());
                                listPeternak.add(ka);
                                id=ka.getId();
                            }
                            Log.d(TAG, "onDataChange: "+id);

                            rvAdapter = new PeternakAdapter(RecyclerViewPeternakActivity.this, listPeternak);
                            recyclerView.setAdapter(rvAdapter);
                            dialog.dismiss();}


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });}if(edtSearch.getText().toString().equals("")){
                    edtSearch.setError("Data belum diinputkan");
                }else{
                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("peternak")
                            .orderByChild("email")
                            .equalTo(edtSearch.getText().toString());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listPeternak.clear();
                            uid.clear();
                            Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                            Log.d(TAG, "onDataChange Key 1: " + dataSnapshot.getKey());
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                PeternakRegister ka = pinjamanSnapshot.getValue(PeternakRegister.class);
                                uid.add(pinjamanSnapshot.getKey());
                                listPeternak.add(ka);
                                id=ka.getId();
                            }
                            Log.d(TAG, "onDataChange: "+id);

                            rvAdapter = new PeternakAdapter(RecyclerViewPeternakActivity.this, listPeternak);
                            recyclerView.setAdapter(rvAdapter);
                            dialog.dismiss();}


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });}
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RecyclerViewPeternakActivity.this, AdminMainActivity.class));
            }
        });

        dialog = new ProgressDialog(RecyclerViewPeternakActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        listPeternak = new ArrayList<PeternakRegister>();
        uid = new ArrayList<String>();

        recyclerView = findViewById(R.id.rv_peternak);
        linearLayoutManager = new LinearLayoutManager(RecyclerViewPeternakActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetch();


    } private void fetch() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("peternak");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPeternak.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot peternakSnapshot : dataSnapshot.getChildren()){
                    PeternakRegister ka = peternakSnapshot.getValue(PeternakRegister.class);
                    listPeternak.add(ka);
                    uid.add(dataSnapshot.getKey());
                }
                rvAdapter = new PeternakAdapter(RecyclerViewPeternakActivity.this, listPeternak);
                recyclerView.setAdapter(rvAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}