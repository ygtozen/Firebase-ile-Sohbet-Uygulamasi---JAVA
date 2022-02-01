package com.example.sohbetet.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sohbetet.Adapters.Friend_Req_Adapter;
import com.example.sohbetet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class BildirimFragment extends Fragment {

    private View view;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String userId;
    private List<String> friend_req_key_list;
    private RecyclerView bildirim_rv;
    private Friend_Req_Adapter friend_req_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bildirim, container, false);

        tanimla();
        istekler();

        return view;
    }

    public void tanimla()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Arkadaslik_Istek");
        friend_req_key_list = new ArrayList<>();
        bildirim_rv = view.findViewById(R.id.bildirim_rv);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        bildirim_rv.setLayoutManager(manager);
        friend_req_adapter = new Friend_Req_Adapter(friend_req_key_list,getContext(),getActivity());
        bildirim_rv.setAdapter(friend_req_adapter);
    }

    public void istekler()
    {
        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    String kontrol = snapshot.child("tip").getValue().toString();
                    Log.i("kontroldeger",kontrol);
                    if (kontrol.equals("aldi")) {
                        //Log.i("istekler",snapshot.getKey());
                        if (friend_req_key_list.indexOf(snapshot.getKey()) == -1)
                        {
                            friend_req_key_list.add(snapshot.getKey());
                        }

                        friend_req_adapter.notifyDataSetChanged(); // Adapter güncelleme
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friend_req_key_list.remove(snapshot.getKey());
                friend_req_adapter.notifyDataSetChanged(); // Adapter güncelleme
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