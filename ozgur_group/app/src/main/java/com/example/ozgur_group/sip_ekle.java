package com.example.ozgur_group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class sip_ekle extends AppCompatActivity {
    //info
    Button sip_ekle_info_but;
    //*
    //+sip_ekle
    Dialog sip_ekle_dialog, ur_ad_dialog, ur_cesit_dialog, ur_olcu_dialog;
        //musteri
    TextView set_musteri_text;
    EditText musteri_intent_edit;
    ListView musteri_list_view;
    ArrayList<String> musteri_list;
    String musteri;
    //*
        //urun
    TextView set_urun_text, set_cesit_text, set_olcu_text;
    EditText urun_ad_intent_edit, urun_cesit_intent_edit, urun_olcu_intent_edit, urun_adet_mik_edit;
    ListView urun_ad_list_view, urun_cesit_list_view, urun_olcu_list_view;
    ArrayList<String> urun_ad_list, urun_cesit_list, urun_olcu_list;
    String urun_cesit, urun;
    CheckBox ucret_check, teslim_check;
    //*
        //ekle
    Button ekle_but;
    String sip_musteri, sip_urun, sip_cesit, sip_olcu, sip_adet, user;
    HashMap<String, Object> set_sip_db;
    Integer i = 0;
    //*

    //Firestore-Database
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser logcheck = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase realtime; DatabaseReference dbref;
    //*
    //sip_gör
    Button sip_intent_gor_sil_pdf_but;
    //*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_ekle);
        init();
        sip_intent_gor_sil_pdf_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sip_other_init = new Intent(sip_ekle.this, sip_gor_pdf_sil.class);
                sip_ekle.this.startActivity(sip_other_init);
            }
        });
        sip_ekle_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(sip_ekle.this);
                builder.setCancelable(true);
                builder.setTitle("     " + "Siparişler Ekranına Hoşgeldiniz \n" + "               ");
                builder.setMessage("/*/Sipariş Ekle/*/ Kısmında Müşteri Adı, Ürün Adı, Ürün Çeşidi, Ürün Ölçüsü "
                        + "ve Adet Mikatırını Girebilir Teslimat ve Ücret Bilgilerini seçerek" +
                        " Siparişinizi Ekleyebilirsiniz.");
                builder.show();

            }
        });
        //sip_ekle
        get_musteri_db(); set_musteri_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_musteri_db();
            }
        }); // musteri_ekle
        get_urun_ad_db(); set_urun_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_urun_ad_db();

            }
        });
        set_cesit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_urun_cesit_db();
            }
        });
        ur_olcu_spin_fill();
        set_olcu_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_urun_olcu_adapt();
            }
        });
        ucret_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ucret_check.isChecked()) ucret_check.setText("Ücret Alındı");
                else ucret_check.setText("Ücret Alınmadı");
            }
        });
        teslim_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teslim_check.isChecked()) teslim_check.setText("Teslim Edildi");
                else teslim_check.setText("Teslim Edilmedi");
            }
        });
        ekle_but.setOnClickListener(new View.OnClickListener() {
            //pdf kontrol edilecek ben yatar  04:24
            @Override
            public void onClick(View view) {
                set_sip_db = new HashMap<>();


                if (set_musteri_text.getText().length() == 0) Toast.makeText(sip_ekle.this, "Müşteri Seçiniz Kısmını Doldurunuz", Toast.LENGTH_LONG).show();
                else {sip_musteri = String.valueOf(set_musteri_text.getText()); i++;
                    set_sip_db.put("Müşteri Adı", sip_musteri);
                }

                if (set_urun_text.getText().length() == 0) Toast.makeText(sip_ekle.this, "Ürün Seçiniz Kısmını Doldurunuz", Toast.LENGTH_LONG).show();
                else {sip_urun = String.valueOf(set_urun_text.getText()); i++;
                    set_sip_db.put("Ürün Adı", sip_urun);
                }

                if (set_cesit_text.getText().length() == 0) Toast.makeText(sip_ekle.this, "Ürün Çeşit Seçiniz Kısmını Doldurunuz", Toast.LENGTH_LONG).show();
                else {sip_cesit = String.valueOf(set_cesit_text.getText()); i++;
                    set_sip_db.put("Ürün Çeşidi", sip_cesit);
                }

                if (set_olcu_text == null) sip_olcu = String.valueOf(0);
                else sip_olcu = String.valueOf(set_olcu_text.getText());
                set_sip_db.put("Ürün Ölçüsü", sip_olcu);


                if (urun_adet_mik_edit.getText().length() == 0) Toast.makeText(sip_ekle.this, "Adet Miktarını Giriniz Kısmını Doldurunuz", Toast.LENGTH_LONG).show();
                else {sip_adet = String.valueOf(urun_adet_mik_edit.getText()); i++;
                    set_sip_db.put("Ürün Miktarı", sip_adet);
                }


                set_sip_db.put("Sipariş Verildiği Tarih", get_date());
                set_sip_db.put("Sipariş Veren Kullanıcı", get_user_email());
                if (ucret_check.isChecked()) set_sip_db.put("Ücret", "Alındı");
                else set_sip_db.put("Ücret", "Alınmadı");
                if (teslim_check.isChecked()) set_sip_db.put("Teslim", "Edildi");
                else set_sip_db.put("Teslim", "Edilmedi");

                if (i == 4) {
                    realtimedb();
                    i = 0;

                }


            }
        });
        ucret_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ucret_check.isChecked()) ucret_check.setText("Ücret Alındı");
                else ucret_check.setText("Ücret Alınmadı");
            }
        });
        teslim_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teslim_check.isChecked()) teslim_check.setText("Teslim Edildi");
                else teslim_check.setText("Teslim Edilmedi");
            }
        });



    }
    public void init(){
        set_musteri_text = findViewById(R.id.set_musteri_text);
        set_urun_text = findViewById(R.id.set_urun_text);
        set_cesit_text = findViewById(R.id.set_cesit_text);
        set_olcu_text = findViewById(R.id.set_olcu_text);
        urun_adet_mik_edit = findViewById(R.id.urun_adet_mik_edit);
        ekle_but = findViewById(R.id.ekle_but);
        ucret_check = findViewById(R.id.ucret_check);
        teslim_check = findViewById(R.id.teslim_check);
        sip_ekle_info_but = findViewById(R.id.sip_ekle_info_but);
        sip_intent_gor_sil_pdf_but = findViewById(R.id.sip_intent_gor_sil_pdf_but);


    }
    private void get_musteri_db(){
        musteri_list = new ArrayList<>();
        firestore.collection("musteriler")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                musteri = doc.getId();
                                musteri_list.add(musteri);

                            }
                        }
                    }
                });
    } // get musteri collection on firestore
    private void set_musteri_db() {
        sip_ekle_dialog = new Dialog(sip_ekle.this);
        sip_ekle_dialog.setContentView(R.layout.must_search_spin);
        sip_ekle_dialog.getWindow().setLayout(650, 950);
        sip_ekle_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sip_ekle_dialog.show();
        musteri_intent_edit = sip_ekle_dialog.findViewById(R.id.musteri_edit);
        musteri_list_view = sip_ekle_dialog.findViewById(R.id.musteri_list_view);
        ArrayAdapter<String> must_adapt = new ArrayAdapter<>(sip_ekle.this, android.R.layout.simple_list_item_1, musteri_list);
        musteri_list_view.setAdapter(must_adapt);
        musteri_intent_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                must_adapt.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        musteri_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                set_musteri_text.setText(must_adapt.getItem(i));
                sip_ekle_dialog.dismiss();
            }
        });
    }
    private void get_urun_ad_db(){
        urun_ad_list = new ArrayList<>();
        firestore.collection("urunler")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot ur_doc : task.getResult()){
                                urun = ur_doc.getId();
                                urun_ad_list.add(urun);

                            }
                        }
                    }
                });
    } // get urun collection on firestore
    private void set_urun_ad_db(){
        ur_ad_dialog = new Dialog(sip_ekle.this);
        ur_ad_dialog.setContentView(R.layout.ur_search_spin);
        ur_ad_dialog.getWindow().setLayout(650, 950);
        ur_ad_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ur_ad_dialog.show();
        urun_ad_intent_edit = ur_ad_dialog.findViewById(R.id.urun_ad_edit);
        urun_ad_list_view = ur_ad_dialog.findViewById(R.id.urun_list_view);
        ArrayAdapter<String> ur_ad_adapt = new ArrayAdapter<>(sip_ekle.this, android.R.layout.simple_list_item_1, urun_ad_list);
        urun_ad_list_view.setAdapter(ur_ad_adapt);
        urun_ad_intent_edit.addTextChangedListener(new TextWatcher() {
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
        urun_ad_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                set_urun_text.setText(ur_ad_adapt.getItem(i));
                get_urun_cesit_db();
                ur_ad_dialog.dismiss();

            }
        });


    } // set urun adapter
    private void get_urun_cesit_db(){
        String urun_ad_cesit = String.valueOf(set_urun_text.getText());
        urun_cesit_list = new ArrayList<>();
        firestore.collection("urunler").document(urun_ad_cesit).collection("cesit")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                urun_cesit = doc.getId();
                                urun_cesit_list.add(urun_cesit);

                            }


                        }
                    }
                });

    } // get urun cesit collection on firestore
    private void set_urun_cesit_db(){
        ur_cesit_dialog = new Dialog(sip_ekle.this);
        ur_cesit_dialog.setContentView(R.layout.ur_cesit_search_spin);
        ur_cesit_dialog.getWindow().setLayout(650, 800);
        ur_cesit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ur_cesit_dialog.show();
        urun_cesit_intent_edit = ur_cesit_dialog.findViewById(R.id.urun_cesit_edit);
        urun_cesit_list_view = ur_cesit_dialog.findViewById(R.id.urun_cesit_list_view);
        ArrayAdapter<String> ur_cesit_adapt = new ArrayAdapter<>(sip_ekle.this, android.R.layout.simple_list_item_1, urun_cesit_list);
        urun_cesit_list_view.setAdapter(ur_cesit_adapt);
        urun_cesit_intent_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ur_cesit_adapt.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        urun_cesit_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                set_cesit_text.setText(ur_cesit_adapt.getItem(i));
                ur_cesit_dialog.dismiss();
            }
        });
    } // set urun cesit adapter
    private void ur_olcu_spin_fill(){
        urun_olcu_list = new ArrayList<>();
        for (int j = 0; j <= 20000; j++) {
            urun_olcu_list.add(String.valueOf(j - 0.5) + " cm");
            urun_olcu_list.add(String.valueOf(j) + " cm");
        }



    } // filling the olcu spinner // bakacaz
    private void set_urun_olcu_adapt(){
        ur_olcu_dialog = new Dialog(sip_ekle.this);
        ur_olcu_dialog.setContentView(R.layout.ur_olcu_search_spin);
        ur_olcu_dialog.getWindow().setLayout(650, 800);
        ur_olcu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ur_olcu_dialog.show();
        urun_olcu_intent_edit = ur_olcu_dialog.findViewById(R.id.urun_olcu_edit);
        urun_olcu_list_view = ur_olcu_dialog.findViewById(R.id.urun_olcu_list_view);
        ArrayAdapter<String> ur_olcu_adapt = new ArrayAdapter<>(sip_ekle.this, android.R.layout.simple_list_item_1, urun_olcu_list);
        urun_olcu_list_view.setAdapter(ur_olcu_adapt);
        urun_olcu_intent_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ur_olcu_adapt.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        urun_olcu_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                set_olcu_text.setText(ur_olcu_adapt.getItem(i));
                ur_olcu_dialog.dismiss();
            }
        });
    } // set urun olcu adapter
    private String get_date(){
        Calendar takvim = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        String tarih = date_format.format(takvim.getTime());
        return tarih;
    }
    private String get_user_email(){
        if (logcheck != null) {
            for (UserInfo profile : logcheck.getProviderData())
                user = profile.getEmail();
        } else Toast.makeText(sip_ekle.this, "İsim Alınamadı.", Toast.LENGTH_SHORT).show();
        return user;
    }
    private void realtimedb() {
        realtime = FirebaseDatabase.getInstance();
        dbref = realtime.getReference().child("siparisler");
        dbref.push().setValue(set_sip_db).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(sip_ekle.this, "Sipariş Eklendi", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sip_ekle.this, "Sipariş Eklemede Hata Oldu" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });


    } // bakılacak

}