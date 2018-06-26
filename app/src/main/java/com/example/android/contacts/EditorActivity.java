package com.example.android.contacts;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.contacts.data.ContactContract;
import com.example.android.contacts.data.ContactContract.ContactEntry;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_CONTACT_LOADER = 0;

    private Uri mCurrentContactUri;

    private EditText mNameEditText;


    String textTargetUri = "/storage/emulated/0/Pictures/Messenger/received_10208900976821051.png";

    private ImageView targetImage;

    public TextView mCompanyEditText;

    private EditText mNumberEditText;

    private Spinner mGroupSpinner;

    private int mGroup = ContactEntry.FAVORITES_TRUE;

    private boolean mContactHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mContactHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentContactUri = intent.getData();

        if (mCurrentContactUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_contact));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_contact));

            getLoaderManager().initLoader(EXISTING_CONTACT_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_contact_name);
        //imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_contact));

        mNumberEditText = (EditText) findViewById(R.id.edit_number);
        mGroupSpinner = (Spinner) findViewById(R.id.spinner_group);

        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        mCompanyEditText = (TextView)findViewById(R.id.edit_company);
        targetImage = (ImageView)findViewById(R.id.targetimage);

        mNameEditText.setOnTouchListener(mTouchListener);
        mNumberEditText.setOnTouchListener(mTouchListener);
        mGroupSpinner.setOnTouchListener(mTouchListener);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter groupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_group_options, android.R.layout.simple_spinner_item);

        groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGroupSpinner.setAdapter(groupSpinnerAdapter);

        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.group_favorites))) {
                        mGroup = ContactEntry.FAVORITES_FALSE;
                    } else {
                        mGroup = ContactEntry.FAVORITES_TRUE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGroup = ContactEntry.FAVORITES_TRUE;
            }
        });
    }

    private void saveContact() {
        String nameString = mNameEditText.getText().toString().trim();
        String companyString = mCompanyEditText.getText().toString().trim();;
        String numberString = mNumberEditText.getText().toString().trim();

        if (mCurrentContactUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(companyString) &&
                TextUtils.isEmpty(numberString) && mGroup == ContactEntry.FAVORITES_TRUE) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, nameString);
        values.put(ContactEntry.COLUMN_COMPANY, companyString);
        values.put(ContactEntry.COLUMN_FAVORITES, mGroup);

        int number = 0;
        if (!TextUtils.isEmpty(numberString)) {
            number = Integer.parseInt(numberString);
        }
        values.put(ContactEntry.COLUMN_CONTACT_NUMBER, number);

        if (mCurrentContactUri == null) {
            Uri newUri = null;
            try{
                newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, values);
            }catch (IllegalArgumentException e){

            }
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected=-1;
            try{
                rowsAffected = getContentResolver().update(mCurrentContactUri, values, null, null);

            }catch (IllegalArgumentException e){
                rowsAffected = 0;
            }
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentContactUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveContact();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mContactHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mContactHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_CONTACT_NAME,
                ContactEntry.COLUMN_COMPANY,
                ContactEntry.COLUMN_FAVORITES,
                ContactEntry.COLUMN_CONTACT_NUMBER};

        return new CursorLoader(this,
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
            int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_NAME);
            int companyColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_COMPANY);
            int groupColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_FAVORITES);
            int numberColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            String company = cursor.getString(companyColumnIndex);
            int group = cursor.getInt(groupColumnIndex);
            int number = cursor.getInt(numberColumnIndex);

            mNameEditText.setText(name);
            mCompanyEditText.setText(company);
            mNumberEditText.setText(Integer.toString(number));

            switch (group) {
                case ContactEntry.FAVORITES_FALSE:
                    mGroupSpinner.setSelection(1);
                    break;
                default:
                    mGroupSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mCompanyEditText.setText("");
        mNumberEditText.setText("");
        mGroupSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            int rowsDeleted = getContentResolver().delete(mCurrentContactUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}