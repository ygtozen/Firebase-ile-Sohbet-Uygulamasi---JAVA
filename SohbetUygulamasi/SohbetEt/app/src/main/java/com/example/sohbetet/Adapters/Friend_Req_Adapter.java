package com.example.sohbetet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetet.Fragments.OtherProfileFragment;
import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_Req_Adapter extends RecyclerView.Adapter<Friend_Req_Adapter.ViewHolder> {

    private List<String> userKeysList;
    private Context mContex;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;


    public Friend_Req_Adapter(List<String> userKeysList, Context mContex, Activity activity) {
        this.userKeysList = userKeysList;
        this.mContex = mContex;
        this.activity = activity;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friend_req_text;
        Button btn_ekle, btn_red;
        CircleImageView friend_req_image;

        ViewHolder(View itemView) {
            super(itemView);
            friend_req_text = itemView.findViewById(R.id.friend_req_text);
            btn_ekle = itemView.findViewById(R.id.btn_ekle);
            btn_red = itemView.findViewById(R.id.btn_red);
            friend_req_image = itemView.findViewById(R.id.friend_req_image);

        }
    }

    // Layout tanımlaması yapılacak.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContex).inflate(R.layout.friend_req_layout, parent, false);

        return new ViewHolder(view);
    }

    // View'lara set lemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.txt_username.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                Picasso.get().load(kl.getResim()).into(holder.friend_req_image);
                holder.friend_req_text.setText(kl.getIsim());

                holder.btn_ekle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulEt(userId, userKeysList.get(position));
                    }
                });

                holder.btn_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redEt(userId, userKeysList.get(position));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    // listenin size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }


    public void kabulEt(String userId, String otherId) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        reference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue(reportDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(reportDate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContex, "Arkadaşlık isteği kabul edildi."
                                                    , Toast.LENGTH_LONG).show();
                                            // Arkdaşlık isteği kabul edildikten sonra istek tablosundan kayı siler
                                            reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    public void redEt(String userId, String otherId) {
        reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContex, "Arkadaşlık isteğini reddettiniz.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }

}
