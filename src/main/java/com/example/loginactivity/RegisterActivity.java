package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loginactivity.model.PeternakRegister;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private final int CHOOSE_IMAGE = 1;
    FirebaseDatabase data;
    DatabaseReference ref;
    private long hitungId = 0;
    private String id;
    FirebaseAuth auth;
    Button register, cancel, upload,kamera;
    EditText nama, email, pass, nohp, ktp, kelu;
    ImageView gambar;
    private Uri ImageURL;
    private StorageReference msstorage;
    private StorageTask upluadtask;
    int kodeKamera = 99;
    private ProgressDialog dialog;
    ProgressBar progressBar;

    long maxId;
    String idBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dialog = new ProgressDialog(RegisterActivity.this);
        data = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ref = data.getReference().child("peternak");

        register = findViewById(R.id.btnregis);
        cancel = findViewById(R.id.btncancel);
        nama = findViewById(R.id.txtnama);
        email = findViewById(R.id.txtemail);
        pass = findViewById(R.id.txtpass);
        nohp = findViewById(R.id.txtnohp);
        ktp = findViewById(R.id.txtktp);
        kelu = findViewById(R.id.txttps);
        upload = findViewById(R.id.btnupload);
        gambar = findViewById(R.id.view);
        kamera = findViewById(R.id.btnkamera);
        progressBar = findViewById(R.id.progressbar);

        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
        upload.setOnClickListener(this);
        kamera.setOnClickListener(this);

        isiData();

        msstorage = FirebaseStorage.getInstance().getReference("Peternak");

        //IKI NGGO KODE PETERNAK
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("peternak");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxId = dataSnapshot.getChildrenCount() + 1;
                idBaru = "PTN-" + maxId;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Showchooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageURL = data.getData();
            Picasso.with(this).load(ImageURL).into(gambar);
            Log.d(TAG, "onActivityResult: " + ImageURL);
        }else if(requestCode == kodeKamera && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            gambar.setImageBitmap(bitmap);}
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(final String uid) {
        if (ImageURL != null) {
            final StorageReference fileReference = msstorage.child(System.currentTimeMillis() + "." + getFileExtension(ImageURL));
            Log.d(TAG, "uploadImage: " + fileReference);
            fileReference.putFile(ImageURL).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Toast.makeText(RegisterActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Email : " + email.getText().toString());
                    PeternakRegister regis = new PeternakRegister(
                            idBaru,
                            email.getText().toString(),
                            nama.getText().toString(),
                            pass.getText().toString(),
                            nohp.getText().toString(),
                            ktp.getText().toString(),
                            kelu.getText().toString(),
                            task.getResult().toString(),
                            "peternak");
                    Log.d(TAG, "onComplete: " + task.getResult());
                    ref.child(uid).setValue(regis);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(RegisterActivity.this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void inputData(String mail, String passw) {
        auth.createUserWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String userid = task.getResult().getUser().getUid();
                    DatabaseReference refc = FirebaseDatabase.getInstance().getReference().child("Peternak").child(userid);
                    refc.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(RegisterActivity.this, "Email sudah ada", Toast.LENGTH_LONG).show();
                            } else {


                                uploadImage(userid);
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registerd Succesfully,please check email for verification"
                                                    , Toast.LENGTH_LONG).show();
//                                    email.setText("");
//                                    pass.setText("");
                                            dialog.dismiss();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                            dialog.dismiss();

                                        }

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    Toast.makeText(RegisterActivity.this, "Email sudah ada", Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public void isiData() {
        nama.setText("Sinyo");
        email.setText("gym.adhipa@gmail.com");
        pass.setText("123455");
        nohp.setText("0825621563");
        ktp.setText("331214556");
        kelu.setText("Sinyo");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnregis):
                dialog.setMessage("loading....");
                dialog.show();
                validasi();
                break;
            case (R.id.btncancel):
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                break;
            case (R.id.btnupload):
                Showchooseimage();
                break;
            case (R.id.btnkamera):
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, kodeKamera);;
                break;
        }
    }

    public void validasi() {
        if (!email.getText().toString().isEmpty()) {
        } else {
            email.setError("email tidak boleh kosong");
        }


        if (!pass.getText().toString().isEmpty()) {
        } else {
            pass.setError("pass tidak boleh kosong");

        }

        if (!nama.getText().toString().isEmpty()) {
        } else {
            nama.setError("nama tidak boleh kosong");
        }

        if (!nohp.getText().toString().isEmpty()) {
        } else {
            nohp.setError("nohp tidak boleh kosong");
        }
        if (!ktp.getText().toString().isEmpty()) {
        } else {
            ktp.setError("ktp tidak boleh kosong");
        }


        if (!kelu.getText().toString().isEmpty()) {
        } else {
            kelu.setError("kelurahan tidak boleh kosong");
        }


        if (pass.length() >= 6) {

            inputData(email.getText().toString(), pass.getText().toString());

        } else {
            pass.setError("password lebih dari 6");
        }

    }
}




