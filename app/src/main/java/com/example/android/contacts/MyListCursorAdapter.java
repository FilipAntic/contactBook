package com.example.android.contacts;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.contacts.data.ContactContract;


public class MyListCursorAdapter extends RecyclerView.Adapter<MyListCursorAdapter.MyViewHolder> {


    Cursor dataCursor;
    Context context;

    public MyListCursorAdapter(Context context, Cursor cursor) {
        dataCursor = cursor;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTitle;
        public TextView salaryNum;
        public ImageView picture;
        public ImageView overflow;
        public int id = 1;

        public MyViewHolder(View view) {
            super(view);
            nameTitle = view.findViewById(R.id.nameTitle);
            salaryNum = view.findViewById(R.id.number);
            picture = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        int nameColumnIndex = dataCursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
        int numberColumnIndex = dataCursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER);
        //int idColumnIndex = dataCursor.getColumnIndex(ContactContract.ContactEntry._ID );


        String contactName = dataCursor.getString(nameColumnIndex);
        String contactNumber = dataCursor.getString(numberColumnIndex);
        //int contactId = dataCursor.getInt(idColumnIndex);


        holder.nameTitle.setText(contactName);
        holder.salaryNum.setText(contactNumber);

        Glide.with(context).load(R.drawable.ic_no_contact).into(holder.picture);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(holder.overflow);
            }
        });

    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_contact, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopUpListener());
        popupMenu.show();
    }

    class PopUpListener implements PopupMenu.OnMenuItemClickListener {

        public PopUpListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int idColumnIndex = dataCursor.getColumnIndex(ContactContract.ContactEntry._ID );
            int contactId = dataCursor.getInt(idColumnIndex);
            switch (item.getItemId()) {
                case R.id.add_favourite:
                    Toast.makeText(context, "Information", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.detail_employe:
                    Uri currentContactUri = ContentUris.withAppendedId(ContactContract.ContactEntry.CONTENT_URI,contactId);
                    if (currentContactUri != null) {
                        int rowsDeleted = context.getContentResolver().delete(currentContactUri, null, null);

                        if (rowsDeleted == 0) {
                            Toast.makeText(context, context.getString(R.string.editor_delete_contact_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.editor_delete_contact_successful),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    }

    
    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}
