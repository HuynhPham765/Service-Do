package com.example.servicedo.Pages.HomePage.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.servicedo.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
    }
}
