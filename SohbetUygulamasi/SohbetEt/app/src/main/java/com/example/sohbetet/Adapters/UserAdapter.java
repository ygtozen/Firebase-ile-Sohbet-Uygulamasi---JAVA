package com.example.sohbetet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetet.Fragments.OtherProfileFragment;
import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.ChangeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<String> userKeysList;
    private Context mContex;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public UserAdapter(List<String> userKeysList, Context mContex, Activity activity) {
        this.userKeysList = userKeysList;
        this.mContex = mContex;
        this.activity = activity;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_username;
        CircleImageView user_image, user_state_image;
        LinearLayout userLayout;

        ViewHolder(View itemView)
        {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            user_image = itemView.findViewById(R.id.user_image);
            userLayout = itemView.findViewById(R.id.userLayout);
            user_state_image = itemView.findViewById(R.id.user_state_image);
        }
    }

    // Layout tanımlaması yapılacak.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContex).inflate(R.layout.user_layout,parent,false);

        return new ViewHolder(view);
    }

    // View'lara set lemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.txt_username.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                Boolean userState = Boolean.parseBoolean(snapshot.child("state").getValue().toString());

                Picasso.get().load(kl.getResim()).into(holder.user_image);
                holder.txt_username.setText(kl.getIsim());

                if (userState == true)
                {
                    holder.user_state_image.setImageResource(R.drawable.online_icon);
                }
                else
                {
                    holder.user_state_image.setImageResource(R.drawable.offline_icon);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(mContex);
                changeFragment.changeWithParamter(new OtherProfileFragment(), userKeysList.get(position));
            }
        });

    }


    // listenin size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }
}
