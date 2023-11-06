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

public class must_intent extends AppCompatActivity {
    //info
    Button must_info_but;
    //*
    //must_ekle
    EditText must_ad_ekle_edit, must_ekle_edit;
    Button must_ekle_but;
    Map<String, Object> ekle_must;
    ArrayList<String> must_list;
    String musteri;
    //*
    //must_sil
    TextView must_sil_text;
    Dialog dialog;
    EditText intent_edit_text;
    ListView intent_list_view;
    Button must_sil_but;

    //*
    //Firebase
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    //*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_must_intent);
        init();
        must_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(must_intent.this);
                builder.setCancelable(true);
                String message = "/*/Müşteri Ekle/*/ Müşteri adı giriniz ksımına müşteri ismi yazarak" +
                        " Müşteri Ekleyebilrisiniz\n" +
                        "\n/*/Müşteri Sil/*/Müşteri Seçiniz Kısmından seçtiğiniz müşteriyi silebilirsiniz\n" +
                        " \n!!!Silmek İstediğiniz müşterinin siparişi olmadığını Emin Olunuz!!!";
                builder.setTitle("     " + "Müşteri Ekranına Hoşgeldiniz \n" + "               ");
                builder.setMessage(message.toUpperCase());
                builder.show();
            }
        });

        must_ekle_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_must = must_ekle_edit.getText().toString();
                create_must(new_must);
                must_ekle_edit.setText(" ");
            }
        });
        get_must_db();
        must_sil_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_must_db();
            }
        });
        must_sil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String del_must = must_sil_text.getText().toString();
                delete_must(del_must);

            }
        });
    }
    public void init(){
        must_sil_text = findViewById(R.id.must_text_view);
        must_sil_but = findViewById(R.id.must_sil_but);
        must_ekle_edit = findViewById(R.id.must_ad_ekle_edit);
        must_ekle_but = findViewById(R.id.must_ekle_but);
        must_info_but = findViewById(R.id.ur_info_but);

    }
    private void create_must(String newMust) {
        ekle_must = new HashMap<>();
        Object o = " ";
        ekle_must.put(" ", o);
        firestore.collection("musteriler").document(newMust)
                .set(ekle_must)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(must_intent.this, "Müşteri Eklendi", Toast.LENGTH_SHORT).show();
                    }
                });
        get_must_db();
    }
    private void get_must_db(){
        must_list = new ArrayList<>();
        firestore.collection("musteriler")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                musteri = doc.getId();
                                must_list.add(musteri);

                            }
                        }
                    }
                });
    }
    private void set_must_db(){
            dialog = new Dialog(must_intent.this);
            dialog.setContentView(R.layout.must_crud_search_spin); // bakacuk
            dialog.getWindow().setLayout(650, 900);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            intent_edit_text = dialog.findViewById(R.id.intent_edit_text);
            intent_list_view = dialog.findViewById(R.id.intent_list_view);
            ArrayAdapter<String> xml_adapt = new ArrayAdapter<>(must_intent.this,
                    android.R.layout.simple_list_item_1, must_list);
            intent_list_view.setAdapter(xml_adapt);
            intent_edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    xml_adapt.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            intent_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    must_sil_text.setText(xml_adapt.getItem(i));
                    dialog.dismiss();
                }
            });
    }// müşteriler spine atandı
    public void delete_must(String get_del_must){
        firestore.collection("musteriler").document(get_del_must)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(must_intent.this, "Müşteri Kaydı Silindi", Toast.LENGTH_LONG).show();
                    }
                });
        get_must_db();
    } // d


}