package com.example.sohbetet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<String> userKeysList;
    private Context mContex;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;


    public FriendAdapter(List<String> userKeysList, Context mContex, Activity activity) {
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
        TextView friend_text;
        CircleImageView friend_image, friend_state_image;

        ViewHolder(View itemView) {
            super(itemView);
            friend_text = itemView.findViewById(R.id.friend_text);
            friend_state_image = itemView.findViewById(R.id.friend_state_image);
            friend_image = itemView.findViewById(R.id.friend_image);

        }
    }

    // Layout tanımlaması yapılacak.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContex).inflate(R.layout.friend_layout, parent, false);

        return new ViewHolder(view);
    }

    // View'lara set lemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.txt_username.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("isim").getValue().toString();
                String userImage = snapshot.child("resim").getValue().toString();

                Boolean stateUser = Boolean.parseBoolean(snapshot.child("state").getValue().toString());
                if (stateUser == true)
                {
                    holder.friend_state_image.setImageResource(R.drawable.online_icon);
                }
                else
                {
                    holder.friend_state_image.setImageResource(R.drawable.offline_icon);
                }

                Picasso.get().load(userImage).into(holder.friend_image);
                holder.friend_text.setText(userName);



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




}
