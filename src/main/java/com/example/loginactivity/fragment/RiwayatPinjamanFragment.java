package com.example.loginactivity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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


public class RiwayatPinjamanFragment extends Fragment implements PinjamAdapter.PinjamAdapterOnClick{
    private static final String TAG = "RiwayatPinjamanFragment";
    String email;
    private List<String> idKeyList;
    private List<UserPinjaman> userPinjamanList;
    PinjamAdapter pinjamAdapter;
    RecyclerView recyclerView;


    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;


    public RiwayatPinjamanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_pinjaman, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        userPinjamanList = new ArrayList<>();
        idKeyList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rv_riwayat_pinjaman);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetch();

        return view;
    }

    private void fetch() {
       Query ref = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPinjamanList.clear();
                idKeyList.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                    if (!pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                        Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                        UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                        idKeyList.add(pinjamanSnapshot.getKey());
                        userPinjamanList.add(ka);
                    }
                }
                pinjamAdapter = new PinjamAdapter(RiwayatPinjamanFragment.this, userPinjamanList, idKeyList);
                recyclerView.setAdapter(pinjamAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void pinjamanOnClick(View v, int position, String idPinjaman) {
        Intent intent = new Intent(getActivity(), DetailPinjamanActivity.class);
                intent.putExtra("KEY", idPinjaman);
                Log.d(TAG, "onClick: " + idPinjaman);
    }
}
