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
import com.example.loginactivity.model.PemesananMakanan;
import com.example.loginactivity.model.UserPinjaman;

import java.util.List;

public class PemesananAdapter extends RecyclerView.Adapter<PemesananAdapter.ViewHolder>{

    private static final String TAG = "PemesananAdapter";
    View mView;
    private PemesananAdapterOnClick listener;
    public interface PemesananAdapterOnClick{
        void PemesananOnClick(View v, int position, String idPinjam);
    }

    @NonNull
    @Override
    public PemesananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_file_pemesanan, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PemesananAdapter.ViewHolder holder,  final int position) {
        holder.cv_tanggal.setText(listMakanan.get(position).getTgl());
        holder.cv_makanan.setText(String.valueOf(listMakanan.get(position).getMakanan()));
        holder.cv_keterangan.setText(listMakanan.get(position).getStatus());
        holder.cv_jml.setText(String.valueOf(listMakanan.get(position).getJml()));
        holder.cv_harga.setText(String.valueOf(listMakanan.get(position).getHarga()));
        holder.cv_email.setText(listMakanan.get(position).getEmail());

        if (listMakanan.get(position).getStatus().equals("Setuju")){
            holder.cv_gambar.setImageResource(R.drawable.ic_tick_inside_circle_green);
            holder.cv_keterangan.setTextColor(R.color.hijau);
        } else if (listMakanan.get(position).getStatus().equals("diproses")){
            holder.cv_gambar.setImageResource(R.drawable.ic_autorenew_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.diproses);
        } else {
            holder.cv_gambar.setImageResource(R.drawable.ic_highlight_off_black_24dp);
            holder.cv_keterangan.setTextColor(R.color.ditolak);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             listener.PemesananOnClick(view, position, listMakanan.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }


    List<PemesananMakanan> listMakanan;
    List<String> keyId;
    public PemesananAdapter(PemesananAdapterOnClick listener, List<PemesananMakanan> listMakanan, List<String> keyId) {
        this.listener = listener;
        this.listMakanan = listMakanan;
        this.keyId = keyId;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;

        public TextView cv_tanggal;
        public TextView cv_makanan;
        public TextView cv_keterangan;
        public ImageView cv_gambar;

        public TextView cv_email;
        public TextView cv_harga;
        public TextView cv_jml;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.layout_card_view);

            cv_tanggal = itemView.findViewById(R.id.pemesanan_tanggal);
            cv_makanan = itemView.findViewById(R.id.namaMakanan);
            cv_keterangan = itemView.findViewById(R.id.pinjaman_keterangan);
            cv_gambar = itemView.findViewById(R.id.pinjaman_gambar);
            cv_harga = itemView.findViewById(R.id.harga);
            cv_jml = itemView.findViewById(R.id.jumlah);
            cv_email = itemView.findViewById(R.id.email);
           // cv_norek = itemView.findViewById(R.id.pinjaman_norek);

            mView = itemView;

        }

}

}
