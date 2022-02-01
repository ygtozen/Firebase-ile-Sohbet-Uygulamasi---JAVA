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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {

    private EditText input_email, input_password;
    private Button registerButton;
    private TextView txt_giris;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        tanimla();
    }


    public void tanimla()
    {
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        registerButton = findViewById(R.id.registerButton);
        txt_giris = findViewById(R.id.txt_giris);

        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                if (!email.equals("") && !password.equals(""))
                {
                    kayitOl(email , password);
                    input_email.setText("");
                    input_password.setText("");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Bilgileri boş giremezsiniz.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        txt_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitOlActivity.this,GirisActivity.class);
                startActivity(intent);
            }
        });
    }

    public void kayitOl(String emial , String password) // Kayıt olma işlemi
    {
        auth.createUserWithEmailAndPassword(emial, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map = new HashMap();
                    map.put("resim","null");
                    map.put("isim","null");
                    map.put("egitim","null");
                    map.put("dogumTarih","null");
                    map.put("hakkimda","null");
                    reference.setValue(map);


                    Intent intent = new Intent(KayitOlActivity.this,AnaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Kayıt olma esnasında bir problem yaşandı.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}