package com.example.loginactivity.fragment;

import android.app.ProgressDialog;
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

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.PeternakAdapter;
import com.example.loginactivity.admin.RecyclerViewPeternakActivity;
import com.example.loginactivity.model.PeternakRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private View view;
    RecyclerView recyclerView;
    PeternakAdapter rvAdapter;
    private List<PeternakRegister> listPeternak;
    private ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;
    String cari;


    public RecyclerViewFragment(String cari) {
        // Required empty public constructor
        this.cari=cari;
    }
    public RecyclerViewFragment() {
        // Required empty public constructor
     fetch();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        // Inflate the layout for this fragment
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        listPeternak = new ArrayList<PeternakRegister>();

        recyclerView = view.findViewById(R.id.rv_peternak);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("peternak")
                .orderByChild("id")
                .equalTo(cari);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPeternak.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot peternakSnapshot : dataSnapshot.getChildren()){
                    PeternakRegister ka = peternakSnapshot.getValue(PeternakRegister.class);
                    listPeternak.add(ka);
                }
                rvAdapter = new PeternakAdapter(getActivity(), listPeternak);
                recyclerView.setAdapter(rvAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog.dismiss();
    }
    private void fetch() {
        Query ref = FirebaseDatabase.getInstance().getReference().child("peternak");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPeternak.clear();
                Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                for (DataSnapshot peternakSnapshot : dataSnapshot.getChildren()){
                    PeternakRegister ka = peternakSnapshot.getValue(PeternakRegister.class);
                    listPeternak.add(ka);

                }
                rvAdapter = new PeternakAdapter(getActivity(), listPeternak);
                recyclerView.setAdapter(rvAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
