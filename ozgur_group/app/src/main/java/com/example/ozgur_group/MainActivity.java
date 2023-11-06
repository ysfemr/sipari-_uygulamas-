package com.example.ozgur_group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    // Firebase
    public FirebaseAuth sign;
    //*
    // init
    EditText reg_mail_edit, reg_pass_edit, sign_mail_edit, sign_pass_edit;
    String reg_mail, sign_mail, reg_pass, sign_pass;
    Integer i = 0;
    Button reg_but, sign_but, reg_lay_viewer, sign_lay_viewer, main_info_but;
    LinearLayout reg_lay, sign_lay;
    //*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign = FirebaseAuth.getInstance();
        init();
        reg_lay_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_lay.setVisibility(View.INVISIBLE);
                reg_lay.setVisibility(View.VISIBLE);
            }
        });
        sign_lay_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_lay.setVisibility(View.INVISIBLE);
                sign_lay.setVisibility(View.VISIBLE);
            }
        });
        reg_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_mail = reg_mail_edit.getText().toString();
                reg_pass = reg_pass_edit.getText().toString();
                if (reg_mail.isEmpty()) reg_mail_edit.setError("E-mail Boş Olamaz");
                if (reg_pass.isEmpty()) reg_pass_edit.setError("Parola Kısmını Tekrar Kontrol Ediniz");
                else sign.createUserWithEmailAndPassword(reg_mail, reg_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            Toast.makeText(MainActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(MainActivity.this, "Kayıt Başarısız" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        sign_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_mail = sign_mail_edit.getText().toString();
                sign_pass = sign_pass_edit.getText().toString();
                if (sign_mail.isEmpty()) sign_mail_edit.setError("E-mail Boş Olamaz");
                if (sign_pass.isEmpty()) sign_pass_edit.setError("Parola Kısmını Tekrar Kontrol Ediniz");
                else sign.signInWithEmailAndPassword(sign_mail, sign_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                        Intent line_intent_init = new Intent(MainActivity.this, Line_activity.class);
                        MainActivity.this.startActivity(line_intent_init);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Giriş Başarısız" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        main_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "developed by -ysfemr- ";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("" + "zgr Sipariş Uygulamasına Hoşgeldiniz \n" + "                 " + title);
                String message = "Kayıt ol butonunu kullanarak uygulamaya kaydınızı yapabilir ve kullanıma başlayabilirsiniz.\n" +
                        "\nKayıt yaparken e-mail'inizin aktif olduğundan emin olunuz,\n" +
                        "!!e-mail'inizi yazarken boşluk bırakmayınız!!\n"+
                        "şifrenizi unuttuğunuz zaman şifre yenileme e-posta'sı o adrese gönderilecektir.\n" +
                        "\n!!Şifreniz en az 6 haneli ve rakamlardan oluşmak zorundadır!!\n" +
                        "Giriş yap butonunu kullanarak uygulamaya giriş yapabilirsiniz";

                builder.setMessage(message.toUpperCase());
                builder.show();
            }
        });
    }
    public void init(){
        reg_mail_edit = findViewById(R.id.reg_mail_edit);
        reg_pass_edit = findViewById(R.id.reg_pass_edit);
        sign_mail_edit = findViewById(R.id.sign_mail_edit);
        sign_pass_edit = findViewById(R.id.sign_pass_edit);
        reg_but = findViewById(R.id.reg_but);
        reg_lay_viewer = findViewById(R.id.reg_lay_viewer);
        sign_but = findViewById(R.id.sign_but);
        sign_lay_viewer = findViewById(R.id.sign_lay_viewer);
        reg_lay = findViewById(R.id.reg_lay);
        sign_lay = findViewById(R.id.sign_lay);
        main_info_but = findViewById(R.id.main_info_but);

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = sign.getCurrentUser();
        if (currentUser != null) reload();
    }
    private void reload(){}

}