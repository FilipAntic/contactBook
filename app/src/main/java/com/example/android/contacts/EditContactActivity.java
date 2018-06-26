package com.example.android.contacts;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.contacts.data.ContactContract;
import com.example.android.contacts.fragments.CameraFragment;
import com.example.android.contacts.fragments.EditFragment;
import com.example.android.contacts.fragments.InfoFragment;
import com.example.android.contacts.fragments.RemoveFragment;

public class EditContactActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private Uri mCurrentContactUri;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        intent = getIntent();

        viewPager = findViewById(R.id.editorViewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.editorTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        {
            InfoFragment infoFragment = new InfoFragment();
            RemoveFragment removeFragment = new RemoveFragment();
            EditFragment editFragment = new EditFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uri",intent.getData().toString());
            infoFragment.setArguments(bundle);
            removeFragment.setArguments(bundle);
            editFragment.setArguments(bundle);
            ContactPagerAdapter adapter = new ContactPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(infoFragment, "Info");
            adapter.addFragment(removeFragment, "Brisanje");
            adapter.addFragment(editFragment, "Izmena");
            adapter.addFragment(new CameraFragment(), "Kamera");
            viewPager.setAdapter(adapter);
        }
    }


}
