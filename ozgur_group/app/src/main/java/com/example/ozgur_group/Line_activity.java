package com.example.ozgur_group;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Line_activity extends AppCompatActivity {
    //init
    Button sip_intent_but, must_intent_but, ur_intent_but, line_info_but;
    //*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        init();
        sip_intent_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sip_intent_init = new Intent(Line_activity.this, sip_ekle.class);
                Line_activity.this.startActivity(sip_intent_init);
            }
        });
        must_intent_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent must_intent_init = new Intent(Line_activity.this, must_intent.class);
                Line_activity.this.startActivity(must_intent_init);
            }
        });
        ur_intent_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ur_intent_init = new Intent(Line_activity.this, ur_intent.class);
                Line_activity.this.startActivity(ur_intent_init);
            }
        });
        line_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Line_activity.this);
                builder.setCancelable(true);
                builder.setTitle("****");
                String message = "SİPARİŞLER butonunu kullanarak sipariş işlemlerini\n" +
                        "sipariş ekleme, silme, görüntüleme\n" +
                        "işlemlerini yapabilirsiniz\n"+
                        "\nMüşteriler butonunu kullanarak müşteri işlemlerini\n" +
                        "\nÜrünler butonunu kullanarak ürün işlemlerini\n" +
                        "GERÇEKLEŞTİREBİLİRSİNİZ";
                builder.setMessage(message.toUpperCase());
                builder.show();
            }
        });
    }
    public void init(){
        sip_intent_but = findViewById(R.id.sip_intent_but);
        must_intent_but = findViewById(R.id.must_intent_but);
        ur_intent_but = findViewById(R.id.ur_intent_but);
        line_info_but = findViewById(R.id.line_info_but);

    }

}