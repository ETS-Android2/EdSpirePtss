package com.example.edspireptss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NotificationFragment extends AppCompatActivity {
    DrawerLayout drawerLayout;

    //recyclerview
    RecyclerView notificationRv;
    String currentuserID;
    private DatabaseReference UsersRef;
    TextView tvProfile;
    ImageView NavProfileImg;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelNotification> notificationsList;

    private AdapterNotification adapterNotification;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //init recyclerview
        notificationRv = findViewById(R.id.notificationRv);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        currentuserID = firebaseAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        tvProfile = findViewById(R.id.tvProfile);
        NavProfileImg=findViewById(R.id.nav_profileimg);

        getAllNotifications();

        UsersRef.child(currentuserID).addValueEventListener(new ValueEventListener() {
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
                        Picasso.with(NotificationFragment.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
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


    private void getAllNotifications() {
        notificationsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(currentuserID).child("Notifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notificationsList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            //get data
                            ModelNotification model = ds.getValue(ModelNotification.class);

                            //add to list
                            notificationsList.add(model);
                        }

                        //adapter
                        adapterNotification  = new AdapterNotification(NotificationFragment.this,notificationsList);
                        //set to recyclerview
                        notificationRv.setAdapter(adapterNotification);
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
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.AssignmentList.class);
    }

    public void ClickForum(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.Department.class);
    }

    public void ClickNotification(View view){
        recreate();
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
