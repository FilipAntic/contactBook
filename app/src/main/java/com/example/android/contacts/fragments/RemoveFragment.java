package com.example.android.contacts.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.contacts.R;
import com.example.android.contacts.data.ContactContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private TextView nameTextView;
    private TextView numberTextView;
    private Uri mCurrentContactUri;
    private Button removeButton;
    public RemoveFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mCurrentContactUri = Uri.parse(getArguments().getString("uri"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_remove, container, false);
        nameTextView = view.findViewById(R.id.remove_name_value);
        numberTextView = view.findViewById(R.id.remove_number_value);
        removeButton = view.findViewById(R.id.remove_button);


        if (mCurrentContactUri != null) {

            getLoaderManager().initLoader(0, null, this);
        }
        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            int numberColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int number = cursor.getInt(numberColumnIndex);

            nameTextView.setText(String.valueOf(name));
            numberTextView.setText(String.valueOf(number));
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteContact();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteContact() {
        if (mCurrentContactUri != null) {
            int rowsDeleted = getActivity().getContentResolver().delete(mCurrentContactUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(getActivity(), getString(R.string.editor_delete_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.editor_delete_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        getActivity().finish();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        nameTextView.setText("");
        numberTextView.setText("");
    }
}
