package com.example.sohbetet.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sohbetet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity {

    private EditText giris_email, giris_password;
    private Button buttonGiris;
    private TextView txt_kayitol;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        tanimla();


    }

    public void tanimla()
    {
        giris_email = findViewById(R.id.giris_email);
        giris_password = findViewById(R.id.giris_password);
        buttonGiris = findViewById(R.id.girisButton);
        txt_kayitol = findViewById(R.id.txt_kayitol);
        auth = FirebaseAuth.getInstance();

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = giris_email.getText().toString();
                String password = giris_password.getText().toString();
                if (!email.equals("") && !password.equals(""))
                {
                    sistemeGiris(email , password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Bu alanlar boş geçilemez",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        txt_kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this,KayitOlActivity.class);
                startActivity(intent);
            }
        });

    }

    public void sistemeGiris(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(GirisActivity.this,AnaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Hatalı bilgi girişi.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}