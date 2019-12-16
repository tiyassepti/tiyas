package com.example.loginactivity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.example.loginactivity.model.Angsuran;
import com.example.loginactivity.model.PemesananMakanan;
import com.example.loginactivity.model.UserPinjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class AdminLaporanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdminLaporanActivity";

    private List<UserPinjaman> userPinjamanList;
    private List<Angsuran> userAngsuranList;
    private List<PemesananMakanan> userMakananList;
    Button btnPinjaman, btnAngsuran,btnpemesanan;
    String tanggalSekarang,tgl;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_laporan);

        dialog = new ProgressDialog(AdminLaporanActivity.this);
        dialog.setMessage("Loading...");

        userPinjamanList = new ArrayList<>();
        userAngsuranList = new ArrayList<>();
        userMakananList=new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy kk:mm:ss");
        tanggalSekarang = format.format(new Date());
        SimpleDateFormat forma = new SimpleDateFormat("dd MMMM yyyy");
        String tanggalSekarang = format.format(new Date());
        tgl=(tanggalSekarang);

        btnPinjaman = findViewById(R.id.laporan_pinjam);
        btnAngsuran = findViewById(R.id.laporan_angsuran);

        btnpemesanan = findViewById(R.id.laporan_Pemesanan);
        btnpemesanan.setOnClickListener(this);
        btnPinjaman.setOnClickListener(this);
        btnAngsuran.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.laporan_pinjam:
                Log.d(TAG, "onClick: Laporan Pinjam");
                dialog.show();
              DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Pinjaman");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: ");
                        if (dataSnapshot.exists()){
                            Log.d(TAG, "onDataChange: Data Exist");
                            userPinjamanList.clear();
                            Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                if (!pinjamanSnapshot.child("keterangan").getValue().equals("Proses")){
                                    Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                                    UserPinjaman ka = pinjamanSnapshot.getValue(UserPinjaman.class);
                                    userPinjamanList.add(ka);
                                }
                            }

                            File ad = Environment.getExternalStorageDirectory();
                            String csvFile = "laporanPinjaman "+tanggalSekarang+".xls";
                            File directory = new File(ad.getAbsolutePath()+"/KUDBoyolali");
                            String fullPath = ".../KUDBoyolali/"+csvFile;
                            if (!directory.isDirectory()){
                                directory.mkdir();
                            }
                            try {
                                File file = new File(directory, csvFile);
                                WorkbookSettings workbookSettings = new WorkbookSettings();
                                workbookSettings.setLocale(new Locale("en", "EN"));
                                WritableWorkbook workbook;
                                workbook = Workbook.createWorkbook(file, workbookSettings);
                                WritableSheet sheet = workbook.createSheet("pinjaman", 0);
                                sheet.mergeCells(0,1,5,1);
                                addHeaderCell(sheet, Border.NONE, BorderLineStyle.NONE, 0, 1, "LAPORAN PINJAMAN");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 0, 3, "NO");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 1, 3, "ID");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 2, 3, "TANGGAL PINJAM");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 3, 3, "NAMA REKENING");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 4, 3, "NOMOR REKENING");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 5, 3, "LAMA PINJAM");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 6, 3, "BESAR PINJAM");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 7, 3, "SISA PINJAM");
                                int i;
                                for (i = 0; i<userPinjamanList.size(); i++){
                                    int j = i+4;
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 0, j, String.valueOf(j-3));
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 1, j, userPinjamanList.get(i).getId());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 2, j, userPinjamanList.get(i).getTglPinjam());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 3, j, userPinjamanList.get(i).getNamarek());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 4, j, userPinjamanList.get(i).getNorek());
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 5, j, String.valueOf(userPinjamanList.get(i).getLamaPinjam()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 6, j, "Rp. " + String.valueOf(userPinjamanList.get(i).getBesarPinjam()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 7, j, "Rp. "+String.valueOf(userPinjamanList.get(i).getSisaPinjam()));
                                }
                                workbook.write();
                                workbook.close();
                                dialog.dismiss();
                                Toast.makeText(AdminLaporanActivity.this,
                                        "Berhasil Export data, Lokasi File :\n" +
                                                fullPath, Toast.LENGTH_LONG).show();

                            } catch (Exception e){
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        } else {
                            dialog.dismiss();
                            Log.d(TAG, "onDataChange: Data Null");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.laporan_angsuran:
                Log.d(TAG, "onClick: Laporan Pinjam");
                dialog.show();
              DatabaseReference referen = FirebaseDatabase.getInstance().getReference().child("angsuran");
                referen.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: ");
                        if (dataSnapshot.exists()){
                            Log.d(TAG, "onDataChange: Data Exist");
                           userAngsuranList.clear();
                            Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                //if (!pinjamanSnapshot.child("status").getValue().equals("Proses")){
                                    Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                                  Angsuran ka = pinjamanSnapshot.getValue(Angsuran.class);
                                   userAngsuranList.add(ka);
                                //}
                            }

                            File ad = Environment.getExternalStorageDirectory();
                            String csvFile = "laporanAngsuran "+tanggalSekarang+".xls";
                            File directory = new File(ad.getAbsolutePath()+"/KUDBoyolali");
                            String fullPath = ".../KUDBoyolali/"+csvFile;
                            if (!directory.isDirectory()){
                                directory.mkdir();
                            }
                            try {
                                File file = new File(directory, csvFile);
                                WorkbookSettings workbookSettings = new WorkbookSettings();
                                workbookSettings.setLocale(new Locale("en", "EN"));
                                WritableWorkbook workbook;
                                workbook = Workbook.createWorkbook(file, workbookSettings);
                                WritableSheet sheet = workbook.createSheet("angsuran", 0);
                                sheet.mergeCells(0,1,5,1);
                                addHeaderCell(sheet, Border.NONE, BorderLineStyle.NONE, 0, 1, "LAPORAN ANGSURAN");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 0, 3, "NO");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 1, 3, "KODE PINJAM");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 2, 3, "KODE ANGSUR");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 3, 3, "EMAIL");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 4, 3, "ANGSURAN KE");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 5, 3, "JATUH TEMPO");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 6, 3, "JUMLAH");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 7, 3, "STATUS");
                                int i;
                                for (i = 0; i<userAngsuranList.size(); i++){
                                    int j = i+4;
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 0, j, String.valueOf(j-3));
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 1, j, userAngsuranList.get(i).getKodePinjaman());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 2, j, userAngsuranList.get(i).getKodeAngsuran());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 3, j, userAngsuranList.get(i).getEmail());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 4, j, String.valueOf(userAngsuranList.get(i).getAngsuranKe()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 5, j, userAngsuranList.get(i).getJatuhTempo());;
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 6, j, "Rp. " + String.valueOf(userAngsuranList.get(i).getJumlah()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 7, j, userAngsuranList.get(i).getStatus());
                                }
                                workbook.write();
                                workbook.close();
                                dialog.dismiss();
                                Toast.makeText(AdminLaporanActivity.this,
                                        "Berhasil di ekspor, Lokasi File :\n" +
                                                fullPath, Toast.LENGTH_LONG).show();

                            } catch (Exception e){
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        } else {
                            dialog.dismiss();
                            Log.d(TAG, "onDataChange: Data Null");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.laporan_Pemesanan:
                Log.d(TAG, "onClick: Laporan Pinjam");
                dialog.show();
                DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Pemesanan");
                refer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: ");
                        if (dataSnapshot.exists()){
                            Log.d(TAG, "onDataChange: Data Exist");
                            userAngsuranList.clear();
                            Log.d(TAG, "onDataChange: Jumlah Data " + dataSnapshot.getChildrenCount());
                            for (DataSnapshot pinjamanSnapshot : dataSnapshot.getChildren()){
                                //if (!pinjamanSnapshot.child("status").getValue().equals("Proses")){
                                Log.d(TAG, "onDataChange Key: " + pinjamanSnapshot.getKey());
                              PemesananMakanan ka = pinjamanSnapshot.getValue(PemesananMakanan.class);
                             userMakananList.add(ka);
                                //}
                            }

                            File ad = Environment.getExternalStorageDirectory();
                            String csvFile = "laporanMakanan "+tanggalSekarang+".xls";
                            File directory = new File(ad.getAbsolutePath()+"/KUDBoyolali");
                            String fullPath = ".../KUDBoyolali/"+csvFile;
                            if (!directory.isDirectory()){
                                directory.mkdir();
                            }
                            try {
                                File file = new File(directory, csvFile);
                                WorkbookSettings workbookSettings = new WorkbookSettings();
                                workbookSettings.setLocale(new Locale("en", "EN"));
                                WritableWorkbook workbook;
                                workbook = Workbook.createWorkbook(file, workbookSettings);
                                WritableSheet sheet = workbook.createSheet("angsuran", 0);
                                sheet.mergeCells(0,1,5,1);
                                addHeaderCell(sheet, Border.NONE, BorderLineStyle.NONE, 0, 1, "LAPORAN ANGSURAN");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 0, 3, "NO");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 1, 3, "KODE PEMESANAN");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 2, 3, "Email");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 3, 3, "NAMA PAKAN");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 4, 3, "JUMLAH");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 5, 3, "HARGA");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 6, 3, "TOTAL");
                                addHeaderCell(sheet, Border.ALL, BorderLineStyle.THIN, 7, 3, "STATUS");
                                int i;
                                for (i = 0; i<userMakananList.size(); i++){
                                    int j = i+4;
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 0, j, String.valueOf(j-3));
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 1, j, userMakananList.get(i).getId());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 2, j, userMakananList.get(i).getEmail());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 3, j, userMakananList.get(i).getMakanan());
                                    addRegulerCell(sheet, Border.ALL, Alignment.LEFT, 4, j, String.valueOf(userMakananList.get(i).getJml()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.CENTRE, 5, j, "Rp." +String.valueOf(userMakananList.get(i).getHarga()));;
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 6, j, "Rp. " + String.valueOf(userMakananList.get(i).getHarga()*userMakananList.get(i).getJml()));
                                    addRegulerCell(sheet, Border.ALL, Alignment.RIGHT, 7, j, userMakananList.get(i).getStatus());
                                }
                                workbook.write();
                                workbook.close();
                                dialog.dismiss();
                                Toast.makeText(AdminLaporanActivity.this,
                                        "Berhasil di ekspor, Lokasi File :\n" +
                                                fullPath, Toast.LENGTH_LONG).show();

                            } catch (Exception e){
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        } else {
                            dialog.dismiss();
                            Log.d(TAG, "onDataChange: Data Null");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
        }
    }

    private static void addHeaderCell(WritableSheet sheet,
                                      Border border,
                                      BorderLineStyle borderLineStyle,
                                      int col, int row, String desc) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.TIMES);
        cellFont.setBoldStyle(WritableFont.BOLD);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBorder(border, borderLineStyle);
        cellFormat.setAlignment(Alignment.CENTRE);
        Label label = new Label(col, row, desc, cellFormat);
        int size = desc.length() + 7;
        sheet.setColumnView(col, size);
        sheet.addCell(label);
    }

    private static void addRegulerCell(WritableSheet sheet,
                                       Border border,
                                       Alignment alignment,
                                       int col, int row, String desc) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.TIMES);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBorder(border, BorderLineStyle.THIN);
        cellFormat.setAlignment(alignment);
        Label label = new Label(col, row, desc, cellFormat);
        sheet.addCell(label);
    }
}
