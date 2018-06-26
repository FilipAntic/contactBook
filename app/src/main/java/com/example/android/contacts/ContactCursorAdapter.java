package com.example.android.contacts;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contacts.data.ContactContract;
import com.example.android.contacts.data.ContactContract.ContactEntry;

public class ContactCursorAdapter extends CursorAdapter {

    public ContactCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
//        String summaryTextView =  "/storage/emulated/0/Pictures/Messenger/received_10208900976821051.png";
        ImageView imageView = (ImageView) view.findViewById(R.id.list_target_image);
//        imageView.setImageBitmap(BitmapFactory.decodeFile(summaryTextView));
    //    imageView.setImageBitmap(BitmapFactory.decodeFile("pathToPicture"));
    //    imageView.setImageBitmap(BitmapFactory.decodeResource(view.getResources(), R.drawable.ic_no_contact));

        int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_NAME);
        int companyColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_COMPANY);

        String contactName = cursor.getString(nameColumnIndex);
        String contactCompany = cursor.getString(companyColumnIndex);

        if (TextUtils.isEmpty(contactCompany)) {
            contactCompany = context.getString(R.string.no_company);
        }

        nameTextView.setText(contactName);
        //summaryTextView.setText(contactCompany);
    }
}
