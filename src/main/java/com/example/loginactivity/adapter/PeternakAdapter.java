package com.example.loginactivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.DetailPeternakActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.model.PeternakRegister;

import java.util.List;

public class PeternakAdapter extends RecyclerView.Adapter<PeternakAdapter.ViewHolder>{
    private static final String TAG = "PeternakAdapter";
    View mView;

    private Context context;
    List<PeternakRegister> listPeternak;
    String[] listUid;

    public PeternakAdapter(Context context, List<PeternakRegister> listPeternak) {
        this.context = context;
        this.listPeternak = listPeternak;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_peternak, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cv_email.setText(listPeternak.get(position).getEmail());
        holder.cv_nama.setText(listPeternak.get(position).getNama());
        holder.cv_kelurahan.setText(listPeternak.get(position).getKelurahan());
        holder.cv_no_hp.setText(listPeternak.get(position).getNohp());


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + listPeternak.get(position).getNama());
                Intent intent = new Intent(context, DetailPeternakActivity.class);
                intent.putExtra("NAMA", listPeternak.get(position).getNama());
                intent.putExtra("EMAIL", listPeternak.get(position).getEmail());
                intent.putExtra("GAMBAR", listPeternak.get(position).getGambar());
                intent.putExtra("ID", listPeternak.get(position).getId());
                intent.putExtra("KELURAHAN", listPeternak.get(position).getKelurahan());
                intent.putExtra("KTP", listPeternak.get(position).getKtp());
                intent.putExtra("NOHP", listPeternak.get(position).getNohp());
                Log.d(TAG, "onClick: "+ listPeternak.get(position).getGambar());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPeternak.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout root;

        public TextView cv_nama;
        public TextView cv_kelurahan;
        public TextView cv_no_hp;
        public TextView cv_email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.layout_card_view);

            cv_nama = itemView.findViewById(R.id.cv_nama);
            cv_kelurahan = itemView.findViewById(R.id.cv_kelurahan);
            cv_no_hp = itemView.findViewById(R.id.cv_no_hp);
cv_email=itemView.findViewById(R.id.cv_email);
            mView = itemView;

        }
    }
}
