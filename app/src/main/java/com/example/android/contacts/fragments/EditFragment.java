package com.example.android.contacts.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.contacts.R;
import com.example.android.contacts.data.ContactContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentContactUri;
    private EditText nameEditText;
    private EditText numberEditText;
    private Button izmeniButton;


    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentContactUri = Uri.parse(getArguments().getString("uri"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        nameEditText = view.findViewById(R.id.edit_name_value);
        numberEditText = view.findViewById(R.id.edit_number_value);
        izmeniButton = view.findViewById(R.id.edit_button);
        izmeniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.editorTabs);
                tabhost.getTabAt(0).select();
            }
        });
        if (mCurrentContactUri != null) {

            getLoaderManager().initLoader(0, null, this);
        }


        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER};

        return new CursorLoader(getActivity(),
                mCurrentContactUri,
                projection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            int numberColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int number = cursor.getInt(numberColumnIndex);

            nameEditText.setText(String.valueOf(name));
            numberEditText.setText(String.valueOf(number));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        numberEditText.setText("");
    }

    private void saveContact() {
        String nameString = nameEditText.getText().toString().trim();
        String numberString = numberEditText.getText().toString().trim();

        if (mCurrentContactUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(numberString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, nameString);


        int number = 0;
        if (!TextUtils.isEmpty(numberString)) {
            number = Integer.parseInt(numberString);
        }
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER, number);

        if (mCurrentContactUri == null) {
            Uri newUri = null;
            try {
                newUri = getActivity().getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI, values);
            } catch (IllegalArgumentException e) {

            }
            if (newUri == null) {
                Toast.makeText(getActivity(), getString(R.string.editor_insert_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.editor_insert_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = -1;
            try {
                rowsAffected = getActivity().getContentResolver().update(mCurrentContactUri, values, null, null);

            } catch (IllegalArgumentException e) {
                rowsAffected = 0;
            }
            if (rowsAffected == 0) {
                Toast.makeText(getActivity(), getString(R.string.editor_update_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.editor_update_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
