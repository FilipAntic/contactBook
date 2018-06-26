package com.example.android.contacts;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //tabLayout = findViewById(R.id.mainTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ContactPagerAdapter adapter = new ContactPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactsFragment(),"PRVI");
        //adapter.addFragment(new TabFragment2(), "DRUGI");
        viewPager.setAdapter(adapter);
    }
}

