package com.example.sohbetet.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sohbetet.Adapters.FriendAdapter;
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


public class ArkadaslarFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView friend_rv;
    private FriendAdapter friendAdapter;
    private List<String> keyList;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arkadaslar, container, false);

        tanimla();
        getArkadasList();

        return view;
    }

    public void tanimla()
    {
        keyList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Arkadaslar");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        friend_rv = view.findViewById(R.id.friend_rv);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        friend_rv.setLayoutManager(manager);
        friendAdapter = new FriendAdapter(keyList,getContext(),getActivity());
        friend_rv.setAdapter(friendAdapter);
    }

    public void getArkadasList()
    {
        reference.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (keyList.indexOf(snapshot.getKey()) == -1)
                {
                    keyList.add(snapshot.getKey());
                }

                friendAdapter.notifyDataSetChanged(); // Güncelleme
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