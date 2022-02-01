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

import com.example.sohbetet.Adapters.UserAdapter;
import com.example.sohbetet.Models.Kullanicilar;
import com.example.sohbetet.R;
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


public class AnaFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<String> userKeysList;
    private RecyclerView user_rv;
    private UserAdapter userAdapter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ana, container, false);

        tanimla();
        kullaniciGetir();
        
        return view;
    }


    public void tanimla()
    {
        userKeysList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        user_rv = view.findViewById(R.id.user_rv);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),2);
        user_rv.setLayoutManager(manager);
        userAdapter = new UserAdapter(userKeysList,getContext(),getActivity());
        user_rv.setAdapter(userAdapter);
    }

    public void kullaniciGetir()
    {
        // Kullanıcıları listelemek için kullanılır.
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.i("keylerrrr",snapshot.getKey());
                reference.child("Kullanicilar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                        // Kullanıcı ismi null değilse ve kendi id'sine eşit değilse ana sayfada gösterir
                        // 1- Kullanıcı ismi girmemiş kişiyi kullanıcılar listesinde kullanmıyoruz.
                        // 2- Kullanıcının kendi hesabını kullanıcı listesinde göstermiyoruz.
                        if (!kl.getIsim().equals("null") && !snapshot.getKey().equals(user.getUid()))
                        {
                            if (userKeysList.indexOf(snapshot.getKey()) == -1)
                            {
                                userKeysList.add(snapshot.getKey());
                            }

                            userAdapter.notifyDataSetChanged(); // adapter güncelleme
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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