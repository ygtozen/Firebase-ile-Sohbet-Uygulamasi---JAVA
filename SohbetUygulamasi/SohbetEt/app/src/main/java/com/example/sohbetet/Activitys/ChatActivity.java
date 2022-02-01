package com.example.sohbetet.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sohbetet.Adapters.MessageAdapter;
import com.example.sohbetet.Models.MessageModel;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.GetDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private TextView txt_username_chat;
    private FloatingActionButton btn_sendMessage;
    private EditText mesajEditText;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<MessageModel> messageModelList;
    private RecyclerView chat_rv;
    private MessageAdapter messageAdapter;
    private List<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();
        loadMessage();

    }

    public void tanimla()
    {
        txt_username_chat = findViewById(R.id.txt_username_chat);
        txt_username_chat.setText(getUserName());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        btn_sendMessage = findViewById(R.id.btn_sendMessage);
        mesajEditText = findViewById(R.id.mesajEditText);
        messageModelList = new ArrayList<>();
        keyList = new ArrayList<>();

        chat_rv = findViewById(R.id.chat_rv);
        RecyclerView.LayoutManager manager = new GridLayoutManager(ChatActivity.this,1);
        chat_rv.setLayoutManager(manager);
        messageAdapter = new MessageAdapter(keyList,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_rv.setAdapter(messageAdapter);
    }

    public void action()
    {
        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mesajEditText.getText().toString();
                if (!message.equals(""))
                {
                    sendMessage(user.getUid(), getId(), "text", GetDate.getDate(), false, message);
                    mesajEditText.setText("");
                }
            }
        });
    }

    public String getId()
    {
        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }

    public String getUserName()
    {
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");
        return userName;
    }

    public void sendMessage(String userId, String otherId, String textType, String date, Boolean seen, String messageText)
    {
        String mesajId = reference.child("Mesajlar").child(userId).child(otherId).push().getKey();
        Map messageMap = new HashMap();
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",messageText);
        messageMap.put("from",userId);

        reference.child("Mesajlar").child(userId).child(otherId).child(mesajId).setValue(messageMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Mesajlar").child(otherId).child(userId).child(mesajId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    public void loadMessage()
    {
        reference.child("Mesajlar").child(user.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged(); // Güncelleme
                keyList.add(snapshot.getKey());
                chat_rv.scrollToPosition(messageModelList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}