package com.example.sohbetet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetet.Models.MessageModel;
import com.example.sohbetet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<String> userKeysList;
    private List<MessageModel> messageModelList;
    private Context mContex;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;
    private Boolean state;
    private int view_type_sent = 1, view_type_recieved = 2;


    public MessageAdapter(List<String> userKeysList, Context mContex, Activity activity, List<MessageModel> messageModelList) {
        this.userKeysList = userKeysList;
        this.mContex = mContex;
        this.activity = activity;
        this.messageModelList = messageModelList;


        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        state = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;


        ViewHolder(View itemView) {
            super(itemView);
            if (state == true)
            {
                messageText = itemView.findViewById(R.id.message_send_text);
            }
            else
            {
                messageText = itemView.findViewById(R.id.message_received_text);
            }


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getFrom().equals(userId))
        {
            state = true;
            return view_type_sent;
        }
        else
        {
            state = false;
            return view_type_recieved;
        }
    }

    // Layout tanımlaması yapılacak.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == view_type_sent)
        {
            view = LayoutInflater.from(mContex).inflate(R.layout.message_sent_layout, parent, false);
            return new ViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(mContex).inflate(R.layout.message_recieved_layout, parent, false);
            return new ViewHolder(view);
        }


    }

    // View'lara set lemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.messageText.setText(messageModelList.get(position).getText());

    }


    // listenin size
    @Override
    public int getItemCount() {
        return messageModelList.size();
    }




}
