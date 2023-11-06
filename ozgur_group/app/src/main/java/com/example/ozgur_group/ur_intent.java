package com.example.ozgur_group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ur_intent extends AppCompatActivity {
    //info
    Button ur_info_but;
    //*
    //ur_ekle
    Button ur_ekle_but;
    EditText ur_ad_ekle_edit, ur_ces_ekle_edit;
    Map<String, Object> ur_ad_map, ur_ces_map;
    ArrayList<String> ur_list;
    String urun;
    //*
    //Firebase
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    //*
    //ur_sil
    TextView ur_text_view;
    Dialog ur_ad_dialog;
    EditText ur_intent_edit;
    ListView ur_ad_list_view;
    Button ur_sil_but;

    //*



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ur_intent);
        init();
        ur_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ur_intent.this);
                builder.setCancelable(true);
                builder.setTitle("     " + "Ürün Ekranına Hoşgeldiniz \n" + "               ");
                String message = "/*/Ürün Ekle/*/ Ürün adı giriniz ksımına Ürün Adı ve" +
                        " Çeşit kısmına Ürünün ilk çeşidini girebilirsiniz" +
                        " Aynı ürün için yeni çeşit girmek istediğinizde " +
                        " Ürün adını yazıp çeşit eklemeye çeşidini yazıp yeni ürün ve ürün çeşitleri ekleyebilirsiniz\n" +
                        " \n/*/Ürün Sil/*/Ürün Seçiniz Kısmından seçtiğiniz ürünü ve çeşitlerini \n" +
                        " \n!!!Silmek İstediğiniz ürüne ait sipariş olmadığına Emin Olunuz!!!";
                builder.setMessage(message.toUpperCase());
                builder.show();
            }
        });
        ur_ekle_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ur_ad = ur_ad_ekle_edit.getText().toString();
                String ur_ces = ur_ces_ekle_edit.getText().toString();
                create_ur_ad(ur_ad);
                create_ur_ces(ur_ces);
            }
        });
        search_ur_ad();
        ur_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_ur_dialog();
            }
        });
        ur_sil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String del_ur = ur_text_view.getText().toString();
                delete_ur(del_ur);
            }
        });



    }
    private void init(){
        ur_ad_ekle_edit = findViewById(R.id.ur_ad_ekle_edit);
        ur_ces_ekle_edit = findViewById(R.id.ur_ces_ekle_edit);
        ur_ekle_but = findViewById(R.id.ur_ekle_but);
        ur_text_view = findViewById(R.id.ur_text_view);
        ur_sil_but = findViewById(R.id.ur_sil_but);
        ur_info_but = findViewById(R.id.ur_info_but);


    }
    private void create_ur_ad(String ad){
        ur_ad_map = new HashMap<>();
        ur_ad_map.put(" ", " ");

        firestore.collection("urunler").document(ad)
                .set(ur_ad_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ur_intent.this, "Ürün Adı eklendi", Toast.LENGTH_SHORT).show();
                    }
                });


        search_ur_ad();

    } // yeni ürün ekleme
    private void search_ur_ad(){
        ur_list = new ArrayList<>();
        firestore.collection("urunler")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot ur_doc : task.getResult()){
                                urun = ur_doc.getId();
                                ur_list.add(urun);

                            }
                        }
                    }
                });

    } // ürün spin dolduruldu
    private void create_ur_ces(String ces){
        ur_ces_map = new HashMap<>();
        ur_ces_map.put(" ", " ");
        String ur_ad = ur_ad_ekle_edit.getText().toString();
        firestore.collection("urunler").document(ur_ad).collection("cesit").document(ces)
                .set(ur_ces_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ur_intent.this, "Ürün Çeşidi eklendi", Toast.LENGTH_SHORT).show();
                        ur_ces_ekle_edit.setText(" ");
                    }
                });
    } // yeni çeşit ekleme
    private void delete_ur_dialog(){
        ur_ad_dialog = new Dialog(ur_intent.this);
        ur_ad_dialog.setContentView(R.layout.ur_crud_search_spin);
        ur_ad_dialog.getWindow().setLayout(650, 800);
        ur_ad_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ur_ad_dialog.show();
        ur_intent_edit = ur_ad_dialog.findViewById(R.id.xml_edit_text);
        ur_ad_list_view = ur_ad_dialog.findViewById(R.id.xml_list_view);
        ArrayAdapter<String> ur_ad_adapt = new ArrayAdapter<>(ur_intent.this,
                android.R.layout.simple_list_item_1, ur_list);
        ur_ad_list_view.setAdapter(ur_ad_adapt);
        ur_intent_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ur_ad_adapt.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ur_ad_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ur_text_view.setText(ur_ad_adapt.getItem(i));
                ur_ad_dialog.dismiss();
            }
        });
    } // ürün silme dialogu
    public void delete_ur(String get_del_ur){
        firestore.collection("urunler").document(get_del_ur)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ur_intent.this, "Ürün Kaydı Silindi", Toast.LENGTH_LONG).show();
                        ur_text_view.setText(" ");
                    }
                });

        search_ur_ad();
    }



}