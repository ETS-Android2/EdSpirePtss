package com.example.edspireptss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.edspireptss.ui.MainActivity2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    private DatabaseReference UsersRef;
    TextView tvProfile, tvName;
    Button button;
    ImageView NavProfileImg, btnReminder, btnForum, btnNotification;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        tvProfile = findViewById(R.id.tvProfile);
        tvName = findViewById(R.id.homeUserName);
        NavProfileImg=findViewById(R.id.nav_profileimg);
        btnReminder=findViewById(R.id.btnReminder);
        btnForum=findViewById(R.id.btnForum);
        btnNotification=findViewById(R.id.btnNotification);

        //Quick Button Access dekat Home
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Department.class);
                startActivity(intent);
            }
        });
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,NotificationFragment.class);
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
                        Toast.makeText(getApplicationContext(),"Your email was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("Username")) {
                        String username = dataSnapshot.child("Username").getValue().toString();
                        tvName.setText(username);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your email was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("profileImage")) {
                        String profilePic = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.with(Home.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please add a profile picture",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            tvProfile.setText(user.getEmail());
        }
        else {
            startActivity(new Intent(Home.this, LoginActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }

    public void ClickMenu(View view){
        com.example.edspireptss.MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        com.example.edspireptss.MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickProfile(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.Profile.class);
    }

    public void ClickReminder(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.AssignmentList.class);
    }

    public void ClickForum(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.Department.class);
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