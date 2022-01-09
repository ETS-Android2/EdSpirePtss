package com.example.edspireptss;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edspireptss.ui.MainActivity2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AssignmentList extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<Model>();                                               //Array list to add reminders and display in recyclerview
    com.example.edspireptss.myAdapter adapter;
    FirebaseAuth firebaseAuth;
    private DatabaseReference UsersRef;
    TextView tvProfile;
    ImageView NavProfileImg;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);

        drawerLayout = findViewById(R.id.drawer_layout);
//        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        tvProfile = findViewById(R.id.tvProfile);
        NavProfileImg=findViewById(R.id.nav_profileimg);

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
                        Picasso.with(AssignmentList.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
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

//        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);                     //Floating action button to change activity
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);                                                              //Starts the new activity to add Reminders
            }
        });

//        Cursor cursor = new dbManager(getApplicationContext()).readallreminders();                  //Cursor To Load data From the database
//        while (cursor.moveToNext()) {
//            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3) ,cursor.getString(4));
//            dataholder.add(model);
//        }
//
//        adapter = new com.example.edspireptss.myAdapter(dataholder);
//        mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview

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
        recreate();
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