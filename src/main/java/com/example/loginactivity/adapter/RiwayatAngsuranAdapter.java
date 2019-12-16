package com.example.loginactivity.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;

import java.util.List;

public class RiwayatAngsuranAdapter extends RecyclerView.Adapter<RiwayatAngsuranAdapter.ViewHolder>{
    private static final String TAG = "RiwayatAngsuranAdapter";
    View mView;

    List<Angsuran> angsuranList;
    private AngsuranAdapter.AngsuranAdapterOnClick listener;
    public interface AngsuranAdapterOnClick{
        void angsuranClicked(View v, int position, String mode, String idAngsuran, String idPinjam, String mail);
    }

    public RiwayatAngsuranAdapter(List<Angsuran> angsuranList, AngsuranAdapter.AngsuranAdapterOnClick listener) {
        this.angsuranList = angsuranList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_riwayat_angsuran, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cv_tanggal.setText(angsuranList.get(position).getJatuhTempo());
        holder.cv_besar.setText(String.valueOf(angsuranList.get(position).getJumlah()));
        holder.cv_keperluan.setText("Pembayaran Angsuran");
        holder.cv_keterangan.setText(angsuranList.get(position).getStatus());
        if (angsuranList.get(position).getStatus().equals("Setuju")){
            holder.cv_gambar.setImageResource(R.drawable.ic_tick_inside_circle_green);
            holder.cv_keterangan.setTextColor(R.color.hijau);
        } else if (angsuranList.get(position).getStatus().equals("Proses")){
            holder.cv_gambar.setImageResource(R.drawable.ic_autorenew_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.diproses);
        } else {
            holder.cv_gambar.setImageResource(R.drawable.ic_highlight_off_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.ditolak);
        }
    }

    @Override
    public int getItemCount() {
        return angsuranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;

        public TextView cv_tanggal;
        public TextView cv_besar;
        public TextView cv_keperluan;
        public TextView cv_keterangan;
        public ImageView cv_gambar;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.layout_card_view);

            cv_tanggal = itemView.findViewById(R.id.ra_tanggal);
            cv_besar = itemView.findViewById(R.id.ra_total_pinjam);
            cv_keterangan = itemView.findViewById(R.id.ra_keterangan);
            cv_gambar = itemView.findViewById(R.id.ra_gambar);
            cv_keperluan = itemView.findViewById(R.id.ra_keperluan);
            mView = itemView;

        }
    }
}
