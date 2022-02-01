package com.example.sohbetet.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.ChangeFragment;
import com.example.sohbetet.Utils.RandomName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KullaniciProfilFragment extends Fragment {

    private View view;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private EditText kullaniciIsmi, input_egitim, input_dogumTarihi, input_hakkimda;
    private CircleImageView profile_image;
    private Button bilgiGuncelle, bilgiArkadas, bilgiIstek;
    private String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);

        tanimla();
        bilgileriGetir();

        return view;
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar").child(user.getUid());
        // Resim ekleme için
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        kullaniciIsmi = view.findViewById(R.id.kullaniciIsmi);
        input_egitim = view.findViewById(R.id.input_egitim);
        input_dogumTarihi = view.findViewById(R.id.input_dogumTarihi);
        input_hakkimda = view.findViewById(R.id.input_hakimda);
        profile_image = view.findViewById(R.id.profile_image);
        bilgiGuncelle = view.findViewById(R.id.bilgiGuncelle);
        bilgiArkadas = view.findViewById(R.id.bilgiArkadas);
        bilgiIstek = view.findViewById(R.id.bilgiIstek);

        bilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });

        bilgiArkadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });

        bilgiIstek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        });
    }

    public void bilgileriGetir() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);

                kullaniciIsmi.setText(kl.getIsim());

                input_egitim.setText(kl.getEgitim());
                input_dogumTarihi.setText(kl.getDogumTarih());
                input_hakkimda.setText(kl.getHakkimda());
                imageUrl = kl.getResim();

                if(!kl.getResim().equals("null"))
                {
                    Picasso.get().load(kl.getResim()).into(profile_image);
                }



                /*
                String adi = dataSnapshot.child("isim").getValue().toString();
                String egitim = dataSnapshot.child("egitim").getValue().toString();
                String hakkimda = dataSnapshot.child("hakkimda").getValue().toString();
                String dogumtarih = dataSnapshot.child("dogumtarih").getValue().toString();
                String resim = dataSnapshot.child("resim").getValue().toString();
                */


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void guncelle() {
        String isim = kullaniciIsmi.getText().toString();
        String egitim = input_egitim.getText().toString();
        String dogum = input_dogumTarihi.getText().toString();
        String hakkimda = input_hakkimda.getText().toString();

        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
        Map map = new HashMap();

        map.put("isim", isim);
        map.put("egitim", egitim);
        map.put("dogumTarih", dogum);
        map.put("hakkimda", hakkimda);

        if (imageUrl.equals("null")) {
            map.put("resim", "null");
        } else {
            map.put("resim", imageUrl);
        }

        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ChangeFragment changeFragment = new ChangeFragment(getContext());
                    changeFragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi."
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Bilgiler güncellenemedi."
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void galeriAc() // Galeriyi açıp resim seçebilmek için bu kod kullanılır.
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==5 && resultCode==Activity.RESULT_OK)
        {
            Uri filePath =  data.getData();
            StorageReference ref = storageReference.child("kullaniciresimleri").child(RandomName.getSaltString()+".jpg");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Resim Güncellendi...", Toast.LENGTH_LONG).show();
                        String isim = kullaniciIsmi.getText().toString();
                        String egitim= input_egitim.getText().toString();
                        String dogum = input_dogumTarihi.getText().toString();
                        String hakkimda = input_hakkimda.getText().toString();
                        imageUrl = task.getResult().getStorage().getDownloadUrl().toString();


                        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map = new HashMap();

                        map.put("isim",isim);
                        map.put("egitim",egitim);
                        map.put("dogumTarih",dogum);
                        map.put("hakkimda",hakkimda);
                        //map.put("resim",task.getResult().getStorage().getDownloadUrl().toString());
                        map.put("resim",imageUrl);


                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    ChangeFragment fragment = new ChangeFragment(getContext());
                                    fragment.change(new KullaniciProfilFragment());
                                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                                }else
                                {
                                    Toast.makeText(getContext(), "Bilgiler güncellenmedi...", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }else
                    {
                        Toast.makeText(getContext(), "Resim güncellenmedi...", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }


    /*public void onActivityResult(int requestCode, int resultCode, Intent data) // Seçtiğimiz resme ulaşmak için bu kod kullanılır
    {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            Uri filePath = data.getData();
            StorageReference ref = storageReference.child("kullaniciresimleri").child(RandomName.getSaltString()+".jpg");

            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Resim güncellendi...", Toast.LENGTH_LONG).show();
                        String isim = kullaniciIsmi.getText().toString();
                        String egitim = input_egitim.getText().toString();
                        String dogum = input_dogumTarihi.getText().toString();
                        String hakkimda = input_hakkimda.getText().toString();

                        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map = new HashMap();

                        map.put("isim", isim);
                        map.put("egitim", egitim);
                        map.put("dogumTarih", dogum);
                        map.put("hakkimda", hakkimda);
                        map.put("resim",task.getResult().getStorage().getDownloadUrl());


                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ChangeFragment changeFragment = new ChangeFragment(getContext());
                                    changeFragment.change(new KullaniciProfilFragment());
                                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi."
                                            , Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Bilgiler güncellenemedi."
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Resim güncellenmedi.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }*/
}