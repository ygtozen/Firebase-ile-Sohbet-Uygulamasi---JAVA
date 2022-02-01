package com.example.sohbetet.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sohbetet.Fragments.AnaFragment;
import com.example.sohbetet.Fragments.BildirimFragment;
import com.example.sohbetet.Fragments.KullaniciProfilFragment;
import com.example.sohbetet.R;
import com.example.sohbetet.Utils.ChangeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnaActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private BottomNavigationView bottomNav;
    private ChangeFragment changeFragment;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana);

        tanimla();
        kontrol();
        bottomNavigationGosterme();

        changeFragment = new ChangeFragment(AnaActivity.this);
        changeFragment.change(new AnaFragment());

    }


    public void tanimla()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        bottomNav = findViewById(R.id.bottomNav);

    }

    @Override
    protected void onStop() { // Uygulamadan çıkıldığı zaman offline olur
        super.onStop();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(true);
    }

    public void kontrol()
    {
        // Eğer kullanıcı yoksa KayitOl ekranına gönderilir.
        if (user == null)
        {
            Intent intent = new Intent(AnaActivity.this, GirisActivity.class);
            startActivity(intent);
            finish(); // MainActivty'e geri dönüş sağlanmasın
        }
        else
        {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("Kullanicilar");
            reference.child(user.getUid()).child("state").setValue(true);
        }
    }

    public void bottomNavigationGosterme()
    {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        changeFragment.change(new AnaFragment());
                        return true;

                    case R.id.navigation_bildirim:
                        changeFragment.change(new BildirimFragment());
                        return true;

                    case R.id.navigation_profil:
                        changeFragment.change(new KullaniciProfilFragment());
                        return true;

                    case R.id.navigation_exit:
                        cik();
                        return true;
                }

                return false;
            }
        });
    }

    public void cik()
    {
        auth.signOut();
        Intent intent = new Intent(AnaActivity.this,GirisActivity.class);
        startActivity(intent);
        finish();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar");
        reference.child(user.getUid()).child("state").setValue(false);
    }
}