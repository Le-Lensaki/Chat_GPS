package com.example.chat_gps2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.chat_gps2.Service.SenLocation;
import com.example.chat_gps2.fragment.GroupFragment;
import com.example.chat_gps2.fragment.MapFragment;
import com.example.chat_gps2.fragment.ProfileFragment;
import com.example.chat_gps2.fragment.SettingFragment;
import com.example.chat_gps2.fragment.spclass.SPMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNV;
    SettingFragment settingFragment = new SettingFragment();
    MapFragment mapFragment = new MapFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    GroupFragment GroupFragment = new GroupFragment();


    Switch aSwitch;
    Intent intentSv;


    //Tham chiếu
    private void finID(){
        bottomNV = findViewById(R.id.navigation);
        aSwitch = findViewById(R.id.switch2);
        intentSv = new Intent(MainActivity.this, SenLocation.class);
    }


    //Tạo và sự kiện click item bottomNavigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_People:
                    loadFragment(mapFragment);
                    return true;

                case R.id.menu_Location_tag:
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                    return false;
                case R.id.menu_group:
                    loadFragment(GroupFragment);

                    return true;
                case R.id.menu_profile:
                    loadFragment(profileFragment);
                    return true;
                case R.id.menu_setting:
                    loadFragment(settingFragment);
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();
        loadFragment(mapFragment);
        finID();
        bottomNV.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SPMap.requires_GPS(this);
        swith();
    }

    //Sự kiện tắt mở share vị trí
    private void swith(){
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    startService(intentSv);

                }
                else {
                    stopService(intentSv);
                }
            }
        });
        aSwitch.setChecked(true);

    }
}