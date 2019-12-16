package com.example.loginactivity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.admin.DetailPinjamanActivity;
import com.example.loginactivity.model.Pinjaman;
import com.example.loginactivity.model.UserPinjaman;

import java.util.List;

public class PinjamAdapter extends RecyclerView.Adapter<PinjamAdapter.ViewHolder>{

    private static final String TAG = "PinjamAdapter";
    View mView;

    private PinjamAdapterOnClick listener;
    public interface PinjamAdapterOnClick{
        void pinjamanOnClick(View v, int position, String idPinjam);
    }
    List<UserPinjaman> listPinjaman;
    List<String> keyId;

    public PinjamAdapter(PinjamAdapterOnClick listener, List<UserPinjaman> listPinjaman, List<String> keyId) {
        this.listener = listener;
        this.listPinjaman = listPinjaman;
        this.keyId = keyId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_pinjaman, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.cv_tanggal.setText(listPinjaman.get(position).getTglPinjam());
        holder.cv_besar.setText(String.valueOf(listPinjaman.get(position).getBesarPinjam()));
        holder.cv_keterangan.setText(listPinjaman.get(position).getKeterangan());
        holder.cv_angsuran.setText(String.valueOf(listPinjaman.get(position).getAngsuran()));
        holder.cv_jam.setText(listPinjaman.get(position).getJamPinjam());
        holder.cv_nama.setText(listPinjaman.get(position).getNamarek());
        holder.cv_norek.setText(listPinjaman.get(position).getNorek());
        if (listPinjaman.get(position).getKeterangan().equals("Setuju")){
            holder.cv_gambar.setImageResource(R.drawable.ic_tick_inside_circle_green);
            holder.cv_keterangan.setTextColor(R.color.hijau);
        } else if (listPinjaman.get(position).getKeterangan().equals("Proses")){
            holder.cv_gambar.setImageResource(R.drawable.ic_autorenew_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.diproses);
        } else {
            holder.cv_gambar.setImageResource(R.drawable.ic_highlight_off_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.ditolak);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.pinjamanOnClick(view, position, listPinjaman.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPinjaman.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;

        public TextView cv_tanggal;
        public TextView cv_besar;
        public TextView cv_keterangan;
        public ImageView cv_gambar;
        public TextView cv_angsuran;
        public TextView cv_jam;
        public TextView cv_nama;
        public TextView cv_norek;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.layout_card_view);

            cv_tanggal = itemView.findViewById(R.id.pinjaman_tanggal);
            cv_besar = itemView.findViewById(R.id.pinjaman_total_pinjam);
            cv_keterangan = itemView.findViewById(R.id.pinjaman_keterangan);
            cv_gambar = itemView.findViewById(R.id.pinjaman_gambar);
            cv_angsuran = itemView.findViewById(R.id.pinjaman_angsuran);
            cv_jam = itemView.findViewById(R.id.pinjaman_waktu);
            cv_nama = itemView.findViewById(R.id.pinjaman_nama);
            cv_norek = itemView.findViewById(R.id.pinjaman_norek);

            mView = itemView;

        }
    }
}
