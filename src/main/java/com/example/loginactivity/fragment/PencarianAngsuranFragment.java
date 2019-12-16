package com.example.loginactivity.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.example.loginactivity.adapter.PemesananAdapter;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.PemesananMakanan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PencarianAngsuranFragment extends Fragment implements AngsuranAdapter.AngsuranAdapterOnClick {
    private static final String TAG = "PencarianAngsuranFragment";
    String email;
    private List<String> idKeyList;
    private List<Angsuran> userAngsuranList;
    AngsuranAdapter pinjamAdapter;
    RecyclerView recyclerView;
    String tgl;
    TextView tvKosong;
    Long sisa;


    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;

    public PencarianAngsuranFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pencarian_pemesanan, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

       userAngsuranList = new ArrayList<>();
        idKeyList = new ArrayList<>();
        Long sisa;

        tvKosong =view.findViewById(R.id.tvkosong);

        recyclerView = view.findViewById(R.id.rv_riwayat_pinjaman);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        email = this.getArguments().getString("EMAIL");
        tgl = this.getArguments().getString("TGL");
        fetch();


        return view;
    }
    private void fetch() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("angsuran").orderByChild("email").equalTo(email);
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    userAngsuranList.clear();
                    idKeyList.clear();
                    Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        if (!pinjamanSnapshot.child("status").getValue().equals("Proses")) {
                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        Angsuran ka = pinjamanSnapshot.getValue(Angsuran.class);
                        idKeyList.add(pinjamanSnapshot.getKey());
                        userAngsuranList.add(ka);
                        recyclerView.setVisibility(View.VISIBLE);
                        tvKosong.setVisibility(View.GONE);

                    }}
                    pinjamAdapter = new AngsuranAdapter(userAngsuranList, PencarianAngsuranFragment.this, idKeyList);
                    recyclerView.setAdapter(pinjamAdapter);
                    dialog.dismiss();}


                else{
                    tvKosong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    dialog.dismiss();


                }}



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void angsuranClicked(View v, int position, String mode, String idAngsuran, String idPinjam, String mail) {

    }
}
