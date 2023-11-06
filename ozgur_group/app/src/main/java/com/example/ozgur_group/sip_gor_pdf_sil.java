package com.example.ozgur_group;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DecimalFormat;



public class sip_gor_pdf_sil extends AppCompatActivity {
    //sip_info
    Button sip_gor_pdf_sil_info_but;
    Integer s = 0;

    //*
    //sip_gor
    Spinner view_sip_spin, view_sip_spes_spin;
    String view_sip_get, view_sip_spes_get, musteri;
    ArrayList<String> get_sip_must_list, get_sip_ur_list, get_sip_tar_list, get_sip_user_list,
            get_sip_teslim_list, view_sip_list, musteri_list,
            view_sip_exist_list, view_sip_spes_list;
    ArrayList<String>builder_list, builder_must_list, builder_tar_user_list, builder_ur_list, builder_tar_list,
            builder_user_list, builder_teslim_list;
    Button viewer_but;
    //*
    //sip_sil
    Spinner sip_sil_spin;
    Button sip_sil_but;
    ArrayList<String> sip_sil_list;
    //*
    //sip_pdf
    Button pdf_data_save_but, create_pdf_but;
    EditText pdf_name_edit;
    ArrayList<String> pdf_ur_list, pdf_olcu_list, pdf_uc_tes_list, pdf_adet_list;
    //*
    //Firestore-Database
    FirebaseDatabase realtime; DatabaseReference dbref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_gor_sil_pdf);
        init();
        sip_gor_pdf_sil_info_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(sip_gor_pdf_sil.this);
                builder.setCancelable(true);
                if ( s == 0){
                    builder.setMessage("/*/Sipariş Görüntüle/*/ Kısmında Müşteri Adı, Ürün Adı, Sipariş Tarihi" +
                            " veya Sipariş Ekleyen Kullanıcılara göre seçim yaptıktan sonra istediğiniz" +
                            " görüntüleme süzgecini seçerek Sipariş Görüntüleme yapabilirsiniz\n"
                           + "\n SONRAKİ SAYFA İÇİN PENCEREYİ KAPATIP TEKRAR i SİMGESİNE BASABİLİRSİNİZ");
                    s++;
                }
                else if (s == 1){
                    builder.setMessage("/*/PDF Oluşturma/*/ Kısmında Oluşturacağını PDF'e isim vererek" +
                            " sipariş görüntüleme kısmında seçmiş olduğunuz müşteriye ait siparişleri pdf" +
                            " pdf verileri kaydet kısmından kaydedebilir ve oluştur kısmından oluşturabilirsiniz\n"
                    + "\n SONRAKİ SAYFA İÇİN PENCEREYİ KAPATIP TEKRAR i SİMGESİNE BASABİLİRSİNİZ");
                    s++;
                }
                else if (s == 2) {

                    builder.setMessage("/*/Sipariş Silme/*/ Kısmında Müşteri Adı seçerek" +
                            " seçmiş olduğunuz Müşterinin tüm siparişlerinin silebilirsiniz\n"
                    +  "\n SONRAKİ SAYFA İÇİN PENCEREYİ KAPATIP TEKRAR i SİMGESİNE BASABİLİRSİNİZ");
                    s = 0;
                }
                builder.show();

            }
        });
        view_sip_spin_fill();
        get_musteri_db();
        get_sip_key();
        get_sip_value();
        view_sip_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //0-1-2
                if (view_sip_spin.getSelectedItemId() == 0){
                    view_sip_get = view_sip_spin.getSelectedItem().toString();// orderbychild == müşteri adı
                    ArrayAdapter<String> sip_spes_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, get_sip_must_list); // sadece müşteri list değişecek
                    view_sip_spes_spin.setAdapter(sip_spes_adapt);
                    view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    viewer_but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realtime_view_attempt(view_sip_get, view_sip_spes_get);

                        }
                    });


                } // müşteri adı seçildiğinde görüntüleme yapıyor, ancak sadece siparişi olan müşterileri görüntülemesi lazım
                else if (view_sip_spin.getSelectedItemId() == 1){
                    view_sip_get = view_sip_spin.getSelectedItem().toString();
                    ArrayAdapter<String> sip_spes_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, get_sip_ur_list);
                    view_sip_spes_spin.setAdapter(sip_spes_adapt);
                    view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    viewer_but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realtime_view_attempt(view_sip_get, view_sip_spes_get);
                        }
                    });


                } // ürün adı seçildiğinde görüntüleme yapacak
                else if (view_sip_spin.getSelectedItemId() == 2){
                    view_sip_get = view_sip_spin.getSelectedItem().toString();
                    ArrayAdapter<String> sip_spes_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, get_sip_tar_list);
                    view_sip_spes_spin.setAdapter(sip_spes_adapt);
                    view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    viewer_but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realtime_view_attempt(view_sip_get, view_sip_spes_get);
                        }
                    });
                }
                else if (view_sip_spin.getSelectedItemId() == 3){
                    view_sip_get = view_sip_spin.getSelectedItem().toString();
                    ArrayAdapter<String> sip_spes_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, get_sip_user_list);
                    view_sip_spes_spin.setAdapter(sip_spes_adapt);
                    view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    viewer_but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realtime_view_attempt(view_sip_get, view_sip_spes_get);
                        }
                    });

                }
                else {
                    view_sip_get = view_sip_spin.getSelectedItem().toString();
                    ArrayAdapter<String> sip_spes_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, get_sip_teslim_list);
                    view_sip_spes_spin.setAdapter(sip_spes_adapt);
                    view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    viewer_but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realtime_view_attempt(view_sip_get, view_sip_spes_get);
                        }
                    });
                }
                //else ile tarihler eklenecek

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        view_sip_exist_list = new ArrayList<>();
        pdf_data_save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdf_data();
            }
        });
        create_pdf_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_pdf();
            }
        });
        sip_sil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String del_sip = sip_sil_spin.getSelectedItem().toString();
                realtime_delete_sip(del_sip);

            }
        });

    }
    private void init(){
        viewer_but = findViewById(R.id.viewer_but);
        view_sip_spin = findViewById(R.id.view_sip_sipin);
        view_sip_spes_spin = findViewById(R.id.view_sip_spes_spin);
        sip_sil_but = findViewById(R.id.sip_sil_but);
        sip_sil_spin = findViewById(R.id.sip_sil_spin);
        pdf_data_save_but = findViewById(R.id.pdf_data_save_but);
        create_pdf_but = findViewById(R.id.create_pdf_but);
        pdf_name_edit = findViewById(R.id.pdf_name_edit);
        sip_gor_pdf_sil_info_but = findViewById(R.id.sip_info_gor_sil_pdf_but);
    }
    private void view_sip_spin_fill(){
        view_sip_list = new ArrayList<>();
        view_sip_list.add("Müşteri Adı");
        view_sip_list.add("Ürün Adı");
        view_sip_list.add("Sipariş Verildiği Tarih");
        view_sip_list.add("Sipariş Veren Kullanıcı");
        view_sip_list.add("Teslim");
        ArrayAdapter<String> view_sip_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_spinner_dropdown_item, view_sip_list);
        view_sip_spin.setAdapter(view_sip_adapt);

    } // sip görüntülemenin adını seçiyor müşteri, ürün, tarih gibi ve spinnera atıyor
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
    private void get_sip_key(){
        dbref = FirebaseDatabase.getInstance().getReference();
        int m = musteri_list.size();
        m = m-1;
        for (int k = 0; k <= m; k++){
            String must = musteri_list.get(k);
            Query attempt_query = dbref.child("siparisler").orderByChild("Müşteri Adı").equalTo(must);
            if (attempt_query != null) {
                attempt_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot attempt : snapshot.getChildren()){
                            String key_deneme = attempt.getRef().getKey(); // klasör key-i alındı
                            view_sip_exist_list.add(key_deneme); // -ler listeye eklendi
                            int l = view_sip_exist_list.size();
                            l = l - 1;
                            for (int o = 0; o <= l; o++){
                                String key = view_sip_exist_list.get(o);
                                FirebaseDatabase.getInstance().getReference("siparisler/" + key + "/Müşteri Adı")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                view_sip_spes_list = new ArrayList<>();
                                                for (DataSnapshot view : snapshot.getChildren()){
                                                    String view_attempt = view.getValue().toString();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }
    private void get_sip_value(){
        get_sip_must_list = new ArrayList<>();
        get_sip_ur_list = new ArrayList<>();
        get_sip_tar_list = new ArrayList<>();
        get_sip_user_list = new ArrayList<>();
        get_sip_teslim_list = new ArrayList<>();
        sip_sil_list = new ArrayList<>();
        sip_sil_list.clear();
        realtime = FirebaseDatabase.getInstance();
        dbref = realtime.getReference().child("siparisler");
        dbref.orderByChild("Müşteri Adı").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snp : snapshot.getChildren()){
                    String sip_must = snp.child("Müşteri Adı").getValue().toString();
                    if (!get_sip_must_list.contains(sip_must)) get_sip_must_list.add(sip_must);
                    sip_sil_list.add(sip_must);
                    String sip_ur = snp.child("Ürün Adı").getValue().toString();
                    if (!get_sip_ur_list.contains(sip_ur)) get_sip_ur_list.add(sip_ur);
                    String sip_tar = snp.child("Sipariş Verildiği Tarih").getValue().toString();
                    if (!get_sip_tar_list.contains(sip_tar)) get_sip_tar_list.add(sip_tar);
                    String sip_user = snp.child("Sipariş Veren Kullanıcı").getValue().toString();
                    if (!get_sip_user_list.contains(sip_user)) get_sip_user_list.add(sip_user);
                    String sip_teslim = snp.child("Teslim").getValue().toString();
                    if (!get_sip_teslim_list.contains(sip_teslim)) get_sip_teslim_list.add(sip_teslim);

                }
                ArrayAdapter<String> sip_sil_adapt = new ArrayAdapter<>(sip_gor_pdf_sil.this, android.R.layout.simple_list_item_1, sip_sil_list);
                sip_sil_spin.setAdapter(sip_sil_adapt);
                sip_sil_adapt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void realtime_view_attempt(String get_one, String get_two){
        builder_list = new ArrayList<>();
        builder_must_list = new ArrayList<>();
        builder_tar_user_list = new ArrayList<>();
        builder_ur_list = new ArrayList<>();
        builder_tar_list = new ArrayList<>();
        builder_user_list = new ArrayList<>();
        builder_teslim_list = new ArrayList<>();
        realtime = FirebaseDatabase.getInstance();
        dbref = realtime.getReference().child("siparisler");
        dbref.orderByChild(get_one).equalTo(get_two).addListenerForSingleValueEvent(new ValueEventListener() {
            // türkçesi: orderbychild = müşteri adı, tarihi veya ürün adına göre key veriyor Müşteri Adı = Omeksan gibi burdaki omeksan da equalto oluyor
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snp : snapshot.getChildren()){
                    builder_must_list.clear();
                    builder_tar_user_list.clear();

                    String must_ad = snp.child("Müşteri Adı").getValue().toString();
                    String ur_ad = snp.child("Ürün Adı").getValue().toString();
                    String ur_ces = snp.child("Ürün Çeşidi").getValue().toString();
                    String ur_olcu = snp.child("Ürün Ölçüsü").getValue().toString();
                    String ur_adet = snp.child("Ürün Miktarı").getValue().toString();
                    String ucret = snp.child("Ücret").getValue().toString();
                    String teslim = snp.child("Teslim").getValue().toString();
                    String sip_tar = snp.child("Sipariş Verildiği Tarih").getValue().toString();
                    String sip_user = snp.child("Sipariş Veren Kullanıcı").getValue().toString();

                    if (builder_must_list != null) {
                        builder_must_list.add(must_ad);
                        builder_tar_user_list.add(sip_tar + "\n" + sip_user);
                    }
                    for (int i = 1; i <= builder_must_list.size(); i++){
                        if (builder_must_list.get(i-1) != must_ad) {
                            builder_must_list.add(must_ad);

                        }
                    }
                    builder_list.add("\n" + "Ürün: " + ur_ad + ", " + ur_ces + ", " + ur_olcu + ", " + ur_adet + " Adet" + "\n" + "Ücret: " + ucret + "\n" + "Teslim: " + teslim + "/*/" + "\n");

                    builder_ur_list.add(must_ad + "\n" +sip_tar + " " + sip_user + "\n" + "Ürün: " + ur_ad + ", " + ur_ces + ", " + ur_olcu + ", " + ur_adet + " Adet" + "\n" + "Ücret: " + ucret + "\n" + "Teslim: " + teslim + "/*/" + "\n");
                    builder_tar_list.add(must_ad + "\n" + sip_user + "\n" + "Ürün: " + ur_ad + ", " + ur_ces + ", " + ur_olcu + ", " + ur_adet + " Adet" + "\n" + "Ücret: " + ucret + "\n" + "Teslim: " + teslim + "/*/" + "\n");
                    builder_user_list.add(must_ad + "\n" + sip_tar + "\n" + "Ürün: " + ur_ad + ", " + ur_ces + ", " + ur_olcu + ", " + ur_adet + " Adet" + "\n" + "Ücret: " + ucret + "\n" + "Teslim: " + teslim + "/*/" + "\n");
                    builder_teslim_list.add(must_ad + "\n" + sip_tar + " " + sip_user +  "\n" + "Ürün: " + ur_ad + ", " + ur_ces + ", " + ur_olcu + ", " + ur_adet + " Adet" + "\n" + "Ücret: " + ucret + "/*/" + "\n");
                }
                Integer item_id = (int) view_sip_spin.getSelectedItemId();
                AlertDialog.Builder builder = new AlertDialog.Builder(sip_gor_pdf_sil.this);
                builder.setCancelable(true);
                builder.setTitle(view_sip_spes_spin.getSelectedItem().toString() + " Siparişleri");
                switch (item_id) {
                    case 0:
                        builder.setMessage(builder_must_list.toString() + "\n" + builder_tar_user_list.toString() + "\n" + builder_list.toString());
                        break;
                    case 1:
                        builder.setMessage(builder_ur_list.toString());
                        break;
                    case 2:
                        builder.setMessage(builder_tar_list.toString());
                        break;
                    case 3:
                        builder.setMessage(builder_user_list.toString());
                        break;
                    case 4:
                        builder.setMessage(builder_teslim_list.toString());
                        break;

                }

                builder.show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });// çalışıyor
    } // geliştirilecek // string verideki ilk ve son parantezler ve ilk 12 harf silinecek ondan sonra listeye eklenecek
    private void pdf_data(){
        pdf_ur_list = new ArrayList<>();
        pdf_olcu_list = new ArrayList<>();
        pdf_uc_tes_list = new ArrayList<>();
        pdf_adet_list = new ArrayList<>();
        String must = view_sip_spes_spin.getSelectedItem().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("siparisler");
        reference.orderByChild("Müşteri Adı").equalTo(must).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    String pdf_ur_ad = snapshot.child("Ürün Adı").getValue().toString();
                    String pdf_ur_ces = snapshot.child("Ürün Çeşidi").getValue().toString();
                    pdf_ur_list.add(pdf_ur_ad + "  " + pdf_ur_ces);
                    String pdf_ur_olcu = snapshot.child("Ürün Ölçüsü").getValue().toString();
                    pdf_olcu_list.add(pdf_ur_olcu);
                    String pdf_ur_adet = snapshot.child("Ürün Miktarı").getValue().toString();
                    pdf_adet_list.add(pdf_ur_adet);
                    String pdf_ucr = snapshot.child("Ücret").getValue().toString();
                    String pdf_tes = snapshot.child("Teslim").getValue().toString();
                    pdf_uc_tes_list.add(pdf_tes + " / " + pdf_ucr);

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(sip_gor_pdf_sil.this, "Seçmiş olduğunuz müşteriye ait siparişler kaydedildi, Pdf Oluşturabilirsiniz", Toast.LENGTH_SHORT).show();

    }
    private void create_pdf(){
        view_sip_get = view_sip_spin.getSelectedItem().toString();
        view_sip_spes_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                view_sip_spes_get = view_sip_spes_spin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        int y = 65;
        int y_to = 65;
        int pdf_ur_list_lenght = pdf_ur_list.size();
        PdfDocument new_pdf = new PdfDocument();

        Paint paint = new Paint();
        Paint new_line_paint = new Paint();
        PdfDocument.PageInfo new_info = new PdfDocument.PageInfo.Builder(260, 360, 1).create();
        PdfDocument.Page new_page = new_pdf.startPage(new_info);
        Canvas canvas = new_page.getCanvas();
        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0, 50, 250));

        canvas.drawText("Özgür Metal Sipariş Formu", 40, 20, paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Horozluhan mah. Tahıl sok. NO/58, 42100 Selçuklu/Konya", 15, 30, paint);
        new_line_paint.setStyle(Paint.Style.STROKE);
        new_line_paint.setTextSize(10f);


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(15, 35, 240, 250, paint);

        Paint tar_paint = new Paint();
        tar_paint.setStyle(Paint.Style.FILL);
        tar_paint.setTextSize(10);

        canvas.drawText(get_date(), 180, 47, tar_paint);
        canvas.drawText(view_sip_spes_get, 20, 47, tar_paint);
        canvas.drawLine(20, 50, 235, 50, new_line_paint);


        Paint text_paint = new Paint();
        text_paint.setStyle(Paint.Style.FILL);
        text_paint.setTextSize(8f);

        canvas.drawText("Ürün Adı", 20, 60, text_paint);
        canvas.drawText("Birim", 100, 60, text_paint);
        canvas.drawText("Adet", 130, 60, text_paint);
        canvas.drawText("Teslim / Ücret", 160, 60, text_paint);
//        canvas.drawText("Fiyat", 210, 60, text_paint);
        canvas.drawLine(20, 65, 235, 65, new_line_paint);

        canvas.drawLine(95, 50, 95, 240, new_line_paint); // ürün
        canvas.drawLine(125, 50, 125, 240, new_line_paint); // birim
        canvas.drawLine(155, 50, 155, 240, new_line_paint); // adet
//        canvas.drawLine(210, 50, 210, 240, new_line_paint); // fiyat

        canvas.drawText("Bakiye: ", 20, 270, text_paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);

        Paint text_in_paint = new Paint();
        text_in_paint.setStyle(Paint.Style.FILL);
        text_in_paint.setTextSize(5f);

        if (pdf_ur_list_lenght == 1){
            canvas.drawText(pdf_ur_list.toString(), 20, y + 8, text_in_paint);
            canvas.drawText(pdf_olcu_list.toString(), 105, y + 8, text_in_paint);
            canvas.drawText(pdf_adet_list.toString(), 130, y + 8, text_in_paint);
            canvas.drawText(pdf_uc_tes_list.toString(), 160, y + 8, text_in_paint);
            canvas.drawLine(20, 90, 235, 90, new_line_paint);
        }
        else{
            for (int i = 0; i + 1 <= pdf_ur_list_lenght; i++){
                y_to = y_to + 15;
                canvas.drawText(pdf_ur_list.get(i), 20, y + 8, text_in_paint);
                canvas.drawText(pdf_olcu_list.get(i), 105, y + 8, text_in_paint);
                canvas.drawText(pdf_adet_list.get(i), 130, y + 8, text_in_paint);
                canvas.drawText(pdf_uc_tes_list.get(i), 160, y + 8, text_in_paint);
                canvas.drawLine(20, y_to, 235, y_to, new_line_paint);
                y = y + 18;

            }


        }
        //pdf
        new_pdf.finishPage(new_page);
        if (pdf_name_edit.getText().length() >= 1){
            File file = new File(this.getExternalFilesDir("/"), pdf_name_edit.getText().toString() + ".pdf");
            try {
                new_pdf.writeTo(new FileOutputStream(file));

            }catch (IOException e){
                e.printStackTrace();
            }
            new_pdf.close();

        } else Toast.makeText(sip_gor_pdf_sil.this, "Lütfen PDF'e İsim Veriniz", Toast.LENGTH_LONG).show();
        Toast.makeText(sip_gor_pdf_sil.this, "PDF Oluşturuldu", Toast.LENGTH_LONG).show();

        // pdf

    }
    private String get_date(){
        Calendar takvim = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        String tarih = date_format.format(takvim.getTime());
        return tarih;
    }
    private void realtime_delete_sip(String del_sip){
        dbref = FirebaseDatabase.getInstance().getReference();
        Query delete_siparis = dbref.child("siparisler").orderByChild("Müşteri Adı").equalTo(del_sip); // hem müşteri özeli hem ürün eklenecek dinamik olacak
        delete_siparis.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot delete_snap : snapshot.getChildren()){
                    delete_snap.getRef().removeValue();
                }
                Toast.makeText(sip_gor_pdf_sil.this, del_sip + "Siparişi Silindi", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());

            }
        });

    }



}