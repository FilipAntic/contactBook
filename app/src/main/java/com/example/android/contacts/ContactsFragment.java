package com.example.android.contacts;

import android.content.ContentUris;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.Cursor;
import com.example.android.contacts.data.ContactContract.ContactEntry;

import com.example.android.contacts.data.ContactContract;


public class ContactsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int CONTACT_LOADER = 0;
    private ContactCursorAdapter mCursorAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCursorAdapter = new ContactCursorAdapter(getContext(), null);
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView contactListView = (ListView) view.findViewById(R.id.list);
        contactListView.setAdapter(mCursorAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                startActivity(intent);
            }
        });

        View emptyView = view.findViewById(R.id.empty_view);
        contactListView.setEmptyView(emptyView);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditContactActivity.class);

                Uri currentContactUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI, id);

                intent.setData(currentContactUri);

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public  android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_COMPANY};


        return new CursorLoader(getActivity(), ContactContract.ContactEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished( android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset( android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
