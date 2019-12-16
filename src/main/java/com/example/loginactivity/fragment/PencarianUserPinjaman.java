package com.example.loginactivity.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.PinjamAdapter;
import com.example.loginactivity.admin.DetailPinjamanActivity;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PencarianUserPinjaman extends Fragment implements PinjamAdapter.PinjamAdapterOnClick {
    private static final String TAG = "PencarianPinjamanuserFragment";
    String email;
    private List<String> idKeyList;
    private List<UserPinjaman> userPinjamanList;
    PinjamAdapter pinjamAdapter;
    RecyclerView recyclerView;
    String tanggal;
TextView kosong;
String tgl;

    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;



    public PencarianUserPinjaman() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_pencarian_user_pinjaman, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        userPinjamanList = new ArrayList<>();
        idKeyList = new ArrayList<>();


        kosong = view.findViewById(R.id.diproses_kosong);
        recyclerView = view.findViewById(R.id.rv_riwayat_pinjama);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        email = this.getArguments().getString("EMAIL");
        tanggal= this.getArguments().getString("TGL");
       fetch();

        return view;


}
    private void fetch() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman").orderByChild("email").equalTo(email).limitToLast(3);
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                userPinjamanList.clear();
                idKeyList.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                        idKeyList.add(pinjamanSnapshot.getKey());
                        userPinjamanList.add(ka);
                        kosong.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);







              }
                pinjamAdapter = new PinjamAdapter(PencarianUserPinjaman.this, userPinjamanList, idKeyList);
                recyclerView.setAdapter(pinjamAdapter);
                dialog.dismiss();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetch2() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman").orderByChild("email").equalTo(email);
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+tgl);


                    userPinjamanList.clear();
                    idKeyList.clear();
                    Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){

                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                        idKeyList.add(pinjamanSnapshot.getKey());
                        userPinjamanList.add(ka);

                    }

                    pinjamAdapter = new PinjamAdapter(PencarianUserPinjaman.this, userPinjamanList, idKeyList);
                    recyclerView.setAdapter(pinjamAdapter);
                    dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void pinjamanOnClick(View v, int position, String idPinjam) {
        Intent intent = new Intent(getActivity(), DetailPinjamanActivity.class);
        intent.putExtra("KEY", idPinjam);
        Log.d(TAG, "onClick: " + idPinjam);
    }}