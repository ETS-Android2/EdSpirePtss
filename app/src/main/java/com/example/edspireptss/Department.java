package com.example.edspireptss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Department extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    private DatabaseReference UsersRef;
    TextView tvProfile;
    ImageView NavProfileImg;
    String currentUserID;

    String[] JTMKSem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JTMKSemnum={"1","2","3","4","5"};
    String[] JRKVSem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JRKVSemnum={"1","2","3","4","5"};
    String[] JKMSem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JKMSemnum={"1","2","3","4","5"};
    String[] JKESem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JKESemnum={"1","2","3","4","5"};
    String[] JPSem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JPSemnum={"1","2","3","4","5"};
    String[] JPHSem={"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5"};
    String[] JPHSemnum={"1","2","3","4","5"};
    String[] CommonSem={"Jabatan Pengajian Am","Jabatan Matematik, Sains dan Komputer"};
    String[] Commonnum={"1","2"};
    String[] JSKKWSem={"Semester 1","Semester 2"};
    String[] JSKKWSemnum={"1","2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        tvProfile = findViewById(R.id.tvProfile);
        NavProfileImg=findViewById(R.id.nav_profileimg);

        CardView cdJTMK=findViewById(R.id.itCard);
        CardView cdJRKV=findViewById(R.id.jrkvCard);
        CardView cdJKM=findViewById(R.id.mechanicalCard);
        CardView cdJKE=findViewById(R.id.electricalCard);
        CardView cdJP=findViewById(R.id.jpCard);
        CardView cdJPH=findViewById(R.id.jphCard);
        CardView cdCommon=findViewById(R.id.commonCard);
        CardView cdKoko=findViewById(R.id.kokoCard);

        cdJTMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JTMKkey",JTMKSem);
                intent.putExtra("JTMKnum",JTMKSemnum);
                startActivity(intent);
            }
        });

        cdJRKV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JRKVkey",JRKVSem);
                intent.putExtra("JRKVnum",JRKVSemnum);
                startActivity(intent);
            }
        });

        cdJKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JKMkey",JKMSem);
                intent.putExtra("JKMnum",JKMSemnum);
                startActivity(intent);
            }
        });

        cdJKE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JKEkey",JKESem);
                intent.putExtra("JKEnum",JKESemnum);
                startActivity(intent);
            }
        });

        cdJP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JPkey",JPSem);
                intent.putExtra("JPnum",JPSemnum);
                startActivity(intent);
            }
        });

        cdJPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JPHkey",JPHSem);
                intent.putExtra("JPHnum",JPHSemnum);
                startActivity(intent);
            }
        });

        cdCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("Commonkey",CommonSem);
                intent.putExtra("Commonnum",Commonnum);
                startActivity(intent);
            }
        });

        cdKoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(Department.this,Semester.class);
                intent.putExtra("JSKKWkey",JSKKWSem);
                intent.putExtra("JSKKWnum",JSKKWSemnum);
                startActivity(intent);
            }
        });

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        tvProfile.setText(mail);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your mail was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("profileImage")) {
                        String profilePic = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.with(Department.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your profile was not set up successfully",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void ClickMenu(View view){
        com.example.edspireptss.MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        com.example.edspireptss.MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this,Home.class);
    }

    public void ClickProfile(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this,Profile.class);
    }

    public void ClickReminder(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this,AssignmentList.class);
    }

    public void ClickForum(View view){
        recreate();
    }

    public void ClickNotification(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.NotificationFragment.class);
    }

    public void ClickLogout(View view){
        com.example.edspireptss.MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        com.example.edspireptss.MainActivity.closeDrawer(drawerLayout);
    }
}