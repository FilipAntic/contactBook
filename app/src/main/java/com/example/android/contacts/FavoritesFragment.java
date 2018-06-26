package com.example.android.contacts;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.contacts.data.ContactContract;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MyListCursorAdapter mMyListCursorAdapter;
    //    private CursorRecyclerViewAdapter mCursorRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Contact> contactList;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        contactList = new ArrayList<>();
        mMyListCursorAdapter = new MyListCursorAdapter(getContext(), null);

        recyclerView = view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mMyListCursorAdapter);

//        Cursor cursor = mCursorRecyclerViewAdapter.getCursor();

//        prepareList(view, cursor);

        return view;
    }

    private Cursor prepareList(View view, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView numberTextView = (TextView) view.findViewById(R.id.number);

        int nameColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
        int numberColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER);

        String contactName = cursor.getString(nameColumnIndex);
        String contactNumber = cursor.getString(numberColumnIndex);

        nameTextView.setText(contactName);
        numberTextView.setText(contactNumber);

        return cursor;

        //mMyListCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER,
                ContactContract.ContactEntry.COLUMN_FAVORITES};
        String query =  ContactContract.ContactEntry.COLUMN_FAVORITES + " = 1";
        return new CursorLoader(getContext(), ContactContract.ContactEntry.CONTENT_URI, projection, query, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMyListCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMyListCursorAdapter.swapCursor(null);
    }
}
