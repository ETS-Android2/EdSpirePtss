package com.example.edspireptss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class NotificationActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);

        drawerLayout = findViewById(R.id.drawer_layout);
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
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.AddReminder.class);
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