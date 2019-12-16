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
import com.example.loginactivity.adapter.PinjamAdapter;
import com.example.loginactivity.admin.DetailPinjamanActivity;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinjamanDiprosesFragment extends Fragment implements PinjamAdapter.PinjamAdapterOnClick {
    private static final String TAG = "PinjamanDiprosesFrag";

    private List<String> idKeyList;
    private List<UserPinjaman> userPinjamanList;
    PinjamAdapter pinjamAdapter;
    RecyclerView recyclerView;
    TextView tvKosong;


    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;


    public PinjamanDiprosesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pinjaman_diproses, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        userPinjamanList = new ArrayList<>();
        idKeyList = new ArrayList<>();

        tvKosong = view.findViewById(R.id.pinjaman_diproses_kosong);

        recyclerView = view.findViewById(R.id.rv_perlu_diproses);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetch();


        return view;
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Pinjaman")
                .orderByChild("keterangan")
                .equalTo("Proses");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userPinjamanList.clear();
                    idKeyList.clear();
                    Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        if (pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                            Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                            UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                            idKeyList.add(pinjamanSnapshot.getKey());
                            userPinjamanList.add(ka);
                            recyclerView.setVisibility(View.VISIBLE);
                            tvKosong.setVisibility(View.GONE);
                        }
                    }
                    pinjamAdapter = new PinjamAdapter(PinjamanDiprosesFragment.this, userPinjamanList, idKeyList);
                    recyclerView.setAdapter(pinjamAdapter);
                    dialog.dismiss();
                } else {
                    tvKosong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userPinjamanList.clear();
                    idKeyList.clear();
                    Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                        if (pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                            Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                            UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                            idKeyList.add(pinjamanSnapshot.getKey());
                            userPinjamanList.add(ka);
                            recyclerView.setVisibility(View.VISIBLE);
                            tvKosong.setVisibility(View.GONE);
                        }
                    }
                    pinjamAdapter = new PinjamAdapter(PinjamanDiprosesFragment.this, userPinjamanList, idKeyList);
                    recyclerView.setAdapter(pinjamAdapter);
                    dialog.dismiss();
                } else {
                    tvKosong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void pinjamanOnClick(View v, int position, String idPinjam) {
        Intent intent = new Intent(getActivity(), DetailPinjamanActivity.class);
        intent.putExtra("KEY", idPinjam);
        Log.d(TAG, "onClick: " + idPinjam);
        startActivity(intent);
    }
}
