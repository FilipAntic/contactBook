package com.example.android.contacts.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

public final class ContactContract {

    private ContactContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.contacts";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATA_CONTACTS = "contacts";

    public static final class ContactEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, DATA_CONTACTS);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATA_CONTACTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATA_CONTACTS;

        public final static String TABLE_NAME = "contacts";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_CONTACT_NAME = "name";

        public final static String COLUMN_COMPANY = "company";

        public final static String COLUMN_FAVORITES = "favorites";

        public final static String COLUMN_CONTACT_NUMBER = "number";

        public final static String COLUMN_CONTACT_LOCATION = "location";

        public final static String COLUMN_CONTACT_IMAGE = "image";

        public static final int FAVORITES_TRUE = 0;

        public static final int FAVORITES_FALSE = 1;

        public static boolean isValidGroup(int group) {
            if (group == FAVORITES_TRUE || group == FAVORITES_FALSE) {
                return true;
            }
            return false;
        }
    }

}