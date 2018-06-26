package com.example.android.contacts.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.contacts.data.ContactContract.ContactEntry;

public class ContactDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ContactDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "contacts.db";

    private static final int DATABASE_VERSION = 2;

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + ContactEntry.TABLE_NAME + " ("
                + ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactEntry.COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
                + ContactEntry.COLUMN_COMPANY + " TEXT, "
                + ContactEntry.COLUMN_CONTACT_NUMBER + " INTEGER NOT NULL, "
                + ContactEntry.COLUMN_FAVORITES + " INTEGER NOT NULL DEFAULT 1,"
                + ContactEntry.COLUMN_CONTACT_LOCATION + " TEXT,"
                + ContactEntry.COLUMN_CONTACT_IMAGE + " TEXT);";

        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}