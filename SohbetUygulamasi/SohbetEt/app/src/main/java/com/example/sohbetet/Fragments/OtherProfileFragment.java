package com.example.sohbetet.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sohbetet.Activitys.ChatActivity;
import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.ShowToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileFragment extends Fragment {

    private View view;
    private String otherId, userId;
    private TextView user_profile_isim, user_profile_egitim, user_profile_dogum, user_profile_hakkimda,
            user_profile_txt_takip, user_profile_txt_arkadas, user_profile_isim2;
    private ImageView user_profile_arkadas_image, user_profile_mesaj_image, user_profile_takip_image;
    private CircleImageView userProfileImage;

    private FirebaseDatabase database;
    private DatabaseReference reference, referenceArkadaslik;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String kontrol = "", begeniKontrol = "";
    private ShowToastMessage showToastMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        tanimla();
        action();
        getBegeniText();
        getArkadasText();

        return view;
    }

    public void tanimla() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        referenceArkadaslik = database.getReference().child("Arkadaslik_Istek");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        otherId = getArguments().getString("userid"); // Göderilen id yi aldık.

        user_profile_isim = view.findViewById(R.id.user_profile_isim);
        user_profile_isim2 = view.findViewById(R.id.user_profile_isim2);
        user_profile_egitim = view.findViewById(R.id.user_profile_egitim);
        user_profile_dogum = view.findViewById(R.id.user_profile_dogum);
        user_profile_hakkimda = view.findViewById(R.id.user_profile_hakkimda);
        user_profile_txt_takip = view.findViewById(R.id.user_profile_txt_takip);
        user_profile_txt_arkadas = view.findViewById(R.id.user_profile_txt_arkadas);
        user_profile_arkadas_image = view.findViewById(R.id.user_profile_arkadas_image);
        user_profile_mesaj_image = view.findViewById(R.id.user_profile_mesaj_image);
        user_profile_takip_image = view.findViewById(R.id.user_profile_takip_image);
        userProfileImage = view.findViewById(R.id.userProfileImage);

        // Arkadaşlık isteği gönderildimi
        referenceArkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    kontrol = "istek";
                    user_profile_arkadas_image.setImageResource(R.drawable.arkadas_ekle_off);
                } else {

                    user_profile_arkadas_image.setImageResource(R.drawable.arkadas_ekle_on);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherId)) {
                    kontrol = "arkadas";
                    user_profile_arkadas_image.setImageResource(R.drawable.deletinguser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId))
                {
                    begeniKontrol = "begendi";
                    user_profile_takip_image.setImageResource(R.drawable.takip_ok);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showToastMessage = new ShowToastMessage(getContext());

    }



    public void action() {
        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                user_profile_isim2.setText(kl.getIsim());

                user_profile_isim.setText("İsim : " + kl.getIsim());
                user_profile_egitim.setText("Eğitim : " + kl.getEgitim());
                user_profile_dogum.setText("Doğum Tarihi : " + kl.getDogumTarih());
                user_profile_hakkimda.setText("Hakkımda : " + kl.getHakkimda());
                if (!kl.getResim().equals("null")) {
                    Picasso.get().load(kl.getResim()).into(userProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        user_profile_arkadas_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol.equals("istek")) {
                    arkdasIptalEt(otherId, userId);
                } else if (kontrol.equals("arkadas")) {
                    arkadasTablosundanCikar(otherId, userId);
                } else {
                    arkdasEkle(otherId, userId);
                }

            }
        });

        user_profile_takip_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begeniKontrol.equals("begendi"))
                {
                    begeniIptal(userId,otherId);
                }
                else
                {
                    begen(userId,otherId);
                }
            }
        });

        user_profile_mesaj_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName",user_profile_isim2.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });
    }


    // Bir istek attığı kişini id'si birde kendi id'si gerekir
    // mantığı bir kişi arkdaşlık isteği göndermiş olacak, diğeri isteği almış olacak.
    public void arkdasEkle(String otherId, String userId) {
        referenceArkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            referenceArkadaslik.child(otherId).child(userId).child("tip").setValue("aldi")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                kontrol = "istek";
                                                Toast.makeText(getContext(), "Arkadaşlık isteği gönderildi.",
                                                        Toast.LENGTH_LONG).show();
                                                user_profile_arkadas_image.setImageResource(R.drawable.arkadas_ekle_off);

                                            } else {
                                                Toast.makeText(getContext(), "Bir problem meydana geldi.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Bir problem meydana geldi.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void arkdasIptalEt(String otherId, String userId) {
        referenceArkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                referenceArkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        user_profile_arkadas_image.setImageResource(R.drawable.arkadas_ekle_on);
                        Toast.makeText(getContext(), "Arkdaşlık isteği iptal edildi.",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void arkadasTablosundanCikar(String otherId, String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        user_profile_arkadas_image.setImageResource(R.drawable.arkadas_ekle_on);
                        Toast.makeText(getContext(), "Arkdaşlıktan çıkarıldı..",
                                Toast.LENGTH_LONG).show();
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void begen(String userId, String otherId)
    {
        reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            showToastMessage.showToast("Profili beğendiniz.");
                            user_profile_takip_image.setImageResource(R.drawable.takip_ok);
                            begeniKontrol = "begendi";
                            getBegeniText();
                        }
                    }
                });
    }

    // addoComplete de olabilir
    public void begeniIptal(String userId,String otherId)
    {
        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user_profile_takip_image.setImageResource(R.drawable.takip_off);
                begeniKontrol = "";
                showToastMessage.showToast("Beğenmekten vazgeçtiniz.");
                getBegeniText();
            }
        });
    }

    public void getBegeniText()
    {
        //List<String> begeniList = new ArrayList<>();
        //user_profile_txt_takip.setText("0 Beğeni");
        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_profile_txt_takip.setText(snapshot.getChildrenCount() + " Beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getArkadasText()
    {
        // getChildrenCount için addListenerForSingleValueEvent önerilir
        //List<String> arkadasList = new ArrayList<>();
        //user_profile_txt_arkadas.setText("0 Arkadaş");
        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_profile_txt_arkadas.setText(snapshot.getChildrenCount() + " Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

