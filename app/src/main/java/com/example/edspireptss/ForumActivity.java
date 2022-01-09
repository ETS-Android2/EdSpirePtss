package com.example.edspireptss;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainforum);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new Forum()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.bforum:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new Forum()).commit();
                    break;
                case R.id.bhome:
                    Intent intent=new Intent(getApplicationContext(),Department.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.badd:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new AddButtonQue()).commit();
                    break;
                case R.id.bsearch:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new SearchQuestionFragment()).commit();
                    break;
                case R.id.bfindf:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new FindFriend()).commit();
                    break;
            }
            return true;
        }
    };

}
