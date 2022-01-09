package com.example.edspireptss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Semester extends AppCompatActivity {

    TextView dept;
    String[] empty={"Empty List","Empty List"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        Intent intent = getIntent();
        String[] JTMKsemesterList=intent.getStringArrayExtra("JTMKkey");
        String[] JTMKCode=intent.getStringArrayExtra("JTMKnum");
        String[] JRKVsemesterList=intent.getStringArrayExtra("JRKVkey");
        String[] JRKVCode=intent.getStringArrayExtra("JRKVnum");
        String[] JKMsemesterList=intent.getStringArrayExtra("JKMkey");
        String[] JKMCode=intent.getStringArrayExtra("JKMnum");
        String[] JKEsemesterList=intent.getStringArrayExtra("JKEkey");
        String[] JKECode=intent.getStringArrayExtra("JKEnum");
        String[] JPsemesterList=intent.getStringArrayExtra("JPkey");
        String[] JPCode=intent.getStringArrayExtra("JPnum");
        String[] JPHsemesterList=intent.getStringArrayExtra("JPHkey");
        String[] JPHCode=intent.getStringArrayExtra("JPHnum");
        String[] CommonsemesterList=intent.getStringArrayExtra("Commonkey");
        String[] CommonCode=intent.getStringArrayExtra("Commonnum");
        String[] JSKKWsemesterList=intent.getStringArrayExtra("JSKKWkey");
        String[] JSKKWCode=intent.getStringArrayExtra("JSKKWnum");
        dept=findViewById(R.id.title_dept);

        RecyclerView Subjects = findViewById(R.id.list_subs);
        Subjects.setLayoutManager(new LinearLayoutManager(this));
        if(JTMKsemesterList!=null) {
            Subjects.setAdapter(new SubjectsAdapter(JTMKsemesterList,JTMKCode,"JTMK_Department",this));
            dept.setText("Jabatan Teknologi Maklumat dan Komunikasi");
        }
        else if(JRKVsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JRKVsemesterList,JRKVCode,"JRKV_Department",this));
            dept.setText("Jabatan Rekabentuk dan Komunikasi Visual");
        }
        else if(JKMsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JKMsemesterList,JKMCode,"JKM_Department",this));
            dept.setText("Jabatan Kejuruteraan Mekanikal");
        }
        else if(JKEsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JKEsemesterList,JKECode,"JKE_Department",this));
            dept.setText("Jabatan Kejuruteraan Elektrikal");
        }
        else if(JPsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JPsemesterList,JPCode,"JP_Department",this));
            dept.setText("Jabatan Perdagangan");
        }
        else if(JPHsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JPHsemesterList,JPHCode,"JPH_Department",this));
            dept.setText("Jabatan Pelancongan dan Hospitality");
        }
        else if(CommonsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(CommonsemesterList,CommonCode,"JPA_Department",this));
            dept.setText("Common Subject");
        }
        else if(JSKKWsemesterList!=null){
            Subjects.setAdapter(new SubjectsAdapter(JSKKWsemesterList,JSKKWCode,"JSKKW_Department",this));
            dept.setText("Jabatan Sukan, Kokokurikulum, Kebudayaan dan Warisan");
        }
        else {
            Subjects.setAdapter(new SubjectsAdapter(empty,empty,"empty",this));
        }
    }

}