package com.example.android.contacts;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.contacts.data.ContactContract;
import com.example.android.contacts.data.ContactContract.ContactEntry;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACT_LOADER = 0;

    ContactCursorAdapter mCursorAdapter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.mainViewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.mainTabs);
        tabLayout.setupWithViewPager(viewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
//                startActivity(intent);
//            }
//        });

        ListView contactListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
//        contactListView.setEmptyView(emptyView);

        mCursorAdapter = new ContactCursorAdapter(this, null);
//        contactListView.setAdapter(mCursorAdapter);

//        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
//
//                Uri currentContactUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI, id);
//
//                intent.setData(currentContactUri);
//
//                startActivity(intent);
//            }
//        });

        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
    }

    private void setupViewPager(ViewPager viewPager) {
        {
            ContactPagerAdapter adapter = new ContactPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new ContactsFragment(), "CONTACTS");
            adapter.addFragment(new FavoritesFragment(), "FAVORITES");
            viewPager.setAdapter(adapter);
        }
    }

    private void insertContact() {
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, "Ivana");
        values.put(ContactContract.ContactEntry.COLUMN_COMPANY, "RAF");
        values.put(ContactEntry.COLUMN_FAVORITES, ContactEntry.FAVORITES_FALSE);
        values.put(ContactEntry.COLUMN_CONTACT_NUMBER, 064123456);

        Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, values);
    }

    private void deleteAllContacts() {
        int rowsDeleted = getContentResolver().delete(ContactEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from contact database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_data:
                insertContact();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllContacts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_CONTACT_NAME,
                ContactEntry.COLUMN_COMPANY};

        return new CursorLoader(this,
                ContactEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
