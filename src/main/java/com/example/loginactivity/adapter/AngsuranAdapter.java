package com.example.loginactivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;

import java.util.List;

public class AngsuranAdapter extends RecyclerView.Adapter<AngsuranAdapter.ViewHolder>{
    private static final String TAG = "AngsuranAdapter";
    View mView;
    List<String> keyId;
    List <Angsuran> angsuranList;
    private AngsuranAdapterOnClick listener;
    public interface AngsuranAdapterOnClick{
        void angsuranClicked(View v, int position, String mode, String idAngsuran, String idPinjam, String mail);
    }
    long sisaPinjaman;

    public AngsuranAdapter(List<Angsuran> angsuranList, AngsuranAdapterOnClick listener, long sisaPinjaman) {
        this.angsuranList = angsuranList;
        this.listener = listener;
        this.sisaPinjaman = sisaPinjaman;
    }
    public AngsuranAdapter(List<Angsuran> angsuranList, AngsuranAdapterOnClick listener, List<String> keyId ) {
        this.angsuranList = angsuranList;
        this.listener = listener;
        this.keyId = keyId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_angsuran_diproses, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cv_sisa.setText(String.valueOf(sisaPinjaman));
        holder.cv_angsuran.setText(String.valueOf(angsuranList.get(position).getJumlah()));
        holder.cv_tagihan_sebelumnya.setText("0");
        holder.cv_tanggal.setText(angsuranList.get(position).getJatuhTempo());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.angsuranClicked(view,position,"detail", angsuranList.get(position).getKodeAngsuran(), angsuranList.get(position).getKodePinjaman(), angsuranList.get(position).getEmail());
            }
        });

    }

    @Override
    public int getItemCount() {
        return angsuranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;

        public TextView cv_sisa;
        public TextView cv_angsuran;
        public TextView cv_tagihan_sebelumnya;
        public TextView cv_tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.sp_card_view);

            cv_sisa = itemView.findViewById(R.id.cv_ang_sisa_pinjaman);
            cv_angsuran = itemView.findViewById(R.id.cv_ang_tag_saat_ini);
            cv_tagihan_sebelumnya = itemView.findViewById(R.id.cv_ang_tag_sebelumnya);
            cv_tanggal = itemView.findViewById(R.id.cv_ang_tanggal);


            mView = itemView;

        }
    }
}
