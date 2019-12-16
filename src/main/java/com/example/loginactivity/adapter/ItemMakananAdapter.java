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
import com.example.loginactivity.admin.ItemMakanan;
import com.example.loginactivity.model.MakananSapi;
import com.example.loginactivity.model.PeternakRegister;

import java.util.List;

public class ItemMakananAdapter extends RecyclerView.Adapter<ItemMakananAdapter.ViewHolder>{
    private static final String TAG = "ItemMakananAdapter";
    View mView;

    private Context context;
    List<MakananSapi> listMakanan;
    String[] listUid;
    public ItemMakananAdapter(Context context, List<MakananSapi> listPeternak) {
        this.context = context;
        this.listMakanan = listPeternak;
    }

    @NonNull
    @Override

    public ItemMakananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemMakananAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_file_item, parent, false));
    }

    @Override



    public void onBindViewHolder(@NonNull ItemMakananAdapter.ViewHolder holder, final int position) {
        holder.cv_nama.setText(listMakanan.get(position).getId());
        holder.cv_kelurahan.setText(listMakanan.get(position).getNama());
        holder.cv_no_hp.setText(String.valueOf(listMakanan.get(position).getHarga()));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + listMakanan.get(position).getId());
                Intent intent = new Intent(context, ItemMakanan.class);
                intent.putExtra("ID", listMakanan.get(position).getId());
                intent.putExtra("NAMA", listMakanan.get(position).getNama());
                intent.putExtra("Harga", listMakanan.get(position).getHarga());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout root;

        public TextView cv_nama;
        public TextView cv_kelurahan;
        public TextView cv_no_hp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.layout_card_view);

            cv_nama = itemView.findViewById(R.id.cv_nama);
            cv_kelurahan = itemView.findViewById(R.id.cv_kelurahan);
            cv_no_hp = itemView.findViewById(R.id.cv_no_hp);

            mView = itemView;

        }
    }
}
