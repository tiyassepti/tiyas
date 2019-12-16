package com.example.loginactivity.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.AngsuranDiprosesAdapter;
import com.example.loginactivity.admin.DetailAngsuranAdmin;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.UserPinjaman;
import com.example.loginactivity.user.UserDetailAngsuranActivity;
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
public class AngsuranDiprosesFragment extends Fragment implements AngsuranDiprosesAdapter.SedangDiprosesAdapterOnClick {
    private static final String TAG = "AngsuranDiprosesFragmen";

    String email, mode;
    private List<UserPinjaman> userPinjamanList;
    private List<Angsuran> angsuranList;
    AngsuranDiprosesAdapter sedangDiprosesAdapter;
    RecyclerView recyclerView;
    TextView tvkosong;


    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;
    String idPinjaman;


    public AngsuranDiprosesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_angsuran_diproses, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        //Prepare for Recyclerview
        angsuranList = new ArrayList<>();
        userPinjamanList = new ArrayList<>();

tvkosong=view.findViewById(R.id.angsuran_diproses_kosong);
        recyclerView = view.findViewById(R.id.rv_sedang_diproses);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Get Data From Activity
        assert this.getArguments() != null;
        email = this.getArguments().getString("EMAIL");
        //mode = this.getArguments().getString("MODE");
        mode="admin";

        Log.d(TAG, "onCreateView: "+mode);

        //Get Data From Firebase Pinjaman Where email = email
        if (mode.equals("peternak")){
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
                        if (pinjamanSnapshot.child("status").getValue().equals("Proses")) {
                            Log.d(TAG, "onDataChange: Jumlah Data Angsuran " + dataSnapshot.getChildrenCount());
                            Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                            angsuranList.add(ang);
                            idPinjaman = angsuranList.get(0).getKodePinjaman();
                            Log.d(TAG, "onDataChange kode pinjaman: " + idPinjaman);
                        } else {
                            Log.d(TAG, "onDataChange: Data Kosong");
                        }
                    }

                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Pinjaman")
                            .orderByChild("id")
                            .equalTo(idPinjaman);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange Peminjaman: " + dataSnapshot.getChildrenCount());
                            userPinjamanList.clear();
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                                userPinjamanList.add(ka);
                            }
                         //   Log.d(TAG, "onDataChange Sisa Pinjaman: " + userPinjamanList.get(0).getSisaPinjam());
                            sedangDiprosesAdapter = new AngsuranDiprosesAdapter(angsuranList,
                                    AngsuranDiprosesFragment.this,
                                    userPinjamanList.get(0).getSisaPinjam(),
                                    userPinjamanList.get(0).getNamarek());
                            recyclerView.setAdapter(sedangDiprosesAdapter);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
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
                        if (pinjamanSnapshot.child("status").getValue().equals("diProses")) {
                            Log.d(TAG, "onDataChange: Jumlah Data Angsuran " + dataSnapshot.getChildrenCount());
                            Angsuran ang = pinjamanSnapshot.getValue(Angsuran.class);
                            angsuranList.add(ang);
                            idPinjaman = angsuranList.get(0).getKodePinjaman();
                            Log.d(TAG, "onDataChange kode pinjaman: " + idPinjaman);
                        } else {
                            Log.d(TAG, "onDataChange: Data Kosong");
                        }
                    }

                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Pinjaman")
                            .orderByChild("id")
                            .equalTo(idPinjaman);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){                         Log.d(TAG, "onDataChange Peminjaman: " + dataSnapshot.getChildrenCount());
                            userPinjamanList.clear();
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()) {
                                UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                                userPinjamanList.add(ka);
                                //sisa = pinjamanSnapshot.child("");
                            }
                            //Log.d(TAG, "onDataChange Sisa Pinjaman: " + userPinjamanList.get(0).getSisaPinjam());
                            sedangDiprosesAdapter = new AngsuranDiprosesAdapter(angsuranList, AngsuranDiprosesFragment.this, userPinjamanList.get(0).getSisaPinjam(),userPinjamanList.get(0).getNamarek());
                            recyclerView.setAdapter(sedangDiprosesAdapter);
                                tvkosong.setVisibility(View.GONE);
                            dialog.dismiss();}
                            else {
                            Log.d(TAG, "instance initializer: data Kosong");
                            tvkosong.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void angsuranClicked(View v, int position, String mode, String idAngsuran, String idPinjam, String mail) {

        Log.d(TAG, "angsuranClicked: "+this.mode);
        if(this.mode.equals("peternak")){
            Log.d(TAG, "angsuranClicked: "+this.mode);
            Intent intent = new Intent(getActivity(), UserDetailAngsuranActivity.class);
            intent.putExtra("IDA", idAngsuran);
            intent.putExtra("IDP", idPinjam);
            intent.putExtra("EMAIL", mail);
            startActivity(intent);
        }else{
            Log.d(TAG, "angsuranClicked: "+mode);
        Intent intent = new Intent(getActivity(), DetailAngsuranAdmin.class);
        intent.putExtra("IDA", idAngsuran);
        intent.putExtra("IDP", idPinjam);
        intent.putExtra("EMAIL", mail);
        startActivity(intent);}

    }
}
