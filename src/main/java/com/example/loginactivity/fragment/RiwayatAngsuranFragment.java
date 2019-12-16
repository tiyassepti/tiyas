package com.example.loginactivity.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.AngsuranAdapter;
import com.example.loginactivity.adapter.RiwayatAngsuranAdapter;
import com.example.loginactivity.model.Angsuran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatAngsuranFragment extends Fragment implements AngsuranAdapter.AngsuranAdapterOnClick {
    private static final String TAG = "RiwayatAngsuranFragment";

    String email, pengguna;

    private List<Angsuran> angsuranList;
    RiwayatAngsuranAdapter riwayatAngsuranAdapter;
    RecyclerView recyclerView;


    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;
    TextView tvKosong;


    public RiwayatAngsuranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_angsuran, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        assert this.getArguments() != null;
        email = this.getArguments().getString("EMAIL");
        pengguna = this.getArguments().getString("MODE");
        Log.d(TAG, "onCreateView: "+email);

        angsuranList = new ArrayList<>();
        tvKosong = view.findViewById(R.id.Angsuran_diproses_kosong);

        recyclerView = view.findViewById(R.id.rv_riwayat_angsuran);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (pengguna.equals("peternak")){
            Log.d(TAG, "onCreateView: " + pengguna);
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("angsuran")
                    .orderByChild("email")
                    .equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: 1");
                    angsuranList.clear();
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: 2");
                        //Check if user have sisa pinjaman
                        if (!pinjamanSnapshot.child("status").getValue().equals("Proses")) {
                            Log.d(TAG, "onDataChange: Jumlah Data Angsuran " + dataSnapshot.getChildrenCount());
                            Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                            angsuranList.add(ang);
                           // tvKosong.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onDataChange: Data Kosong");
                            recyclerView.setVisibility(View.VISIBLE);
                            tvKosong.setVisibility(View.GONE);
                        }
                    }
                    riwayatAngsuranAdapter = new RiwayatAngsuranAdapter(angsuranList, RiwayatAngsuranFragment.this);
                    recyclerView.setAdapter(riwayatAngsuranAdapter);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.d(TAG, "onCreateView: " + pengguna);
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("angsuran")
                    .orderByChild("kodeAngsuran");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: 1");
                    angsuranList.clear();
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: 2");
                        //Check if user have sisa pinjaman
                        if(dataSnapshot.exists()){
                        if (!pinjamanSnapshot.child("status").getValue().equals("Proses") & !pinjamanSnapshot.child("status").getValue().equals("diProses")) {
                            Log.d(TAG, "onDataChange: Jumlah Data Angsuran " + dataSnapshot.getChildrenCount());
                            Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                            angsuranList.add(ang);
                            tvKosong.setVisibility(View.GONE);
                        }
                        else {
                            Log.d(TAG, "onDataChange: Data Kosong");
                            //tvKosong.setVisibility(View.VISIBLE);
                        }} else {
                            Log.d(TAG, "onDataChange: Data Kosong");
                            tvKosong.setVisibility(View.VISIBLE);
                        }
                    }
                    riwayatAngsuranAdapter = new RiwayatAngsuranAdapter(angsuranList, RiwayatAngsuranFragment.this);
                    recyclerView.setAdapter(riwayatAngsuranAdapter);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    @Override
    public void angsuranClicked(View v, int position, String mode, String idAngsuran, String idPinjam, String mail) {
        if (pengguna.equals("admin")){
            if (mode.equals("setuju")){

            }

        } else {

        }
    }
}
