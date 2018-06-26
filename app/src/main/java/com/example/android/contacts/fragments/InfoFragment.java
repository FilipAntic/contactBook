package com.example.android.contacts.fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.contacts.CatalogActivity;
import com.example.android.contacts.EditorActivity;
import com.example.android.contacts.MainActivity;
import com.example.android.contacts.MapsActivity;
import com.example.android.contacts.R;
import com.example.android.contacts.data.ContactContract;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private TextView nameTextView;
    private TextView numberTextView;
    private Uri mCurrentContactUri;
    private Button sendSmsButton;
    private Button pickLocationButton;
    private ImageView favoriteImageView;
    private int favorite;
    private String location;

    public InfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        nameTextView = view.findViewById(R.id.info_name_value);
        numberTextView = view.findViewById(R.id.info_number_value);
        sendSmsButton = view.findViewById(R.id.send_sms);
        pickLocationButton = view.findViewById(R.id.pick_location);
        favoriteImageView = view.findViewById(R.id.info_favorite);
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (checkImageResource(getContext(), favoriteImageView, R.drawable.liked)) {
                    favoriteImageView.setImageResource(R.drawable.notliked);
                    values.put(ContactContract.ContactEntry.COLUMN_FAVORITES, 0);
                } else {
                    favoriteImageView.setImageResource(R.drawable.liked);
                    values.put(ContactContract.ContactEntry.COLUMN_FAVORITES, 1);
                }


                int row = getActivity().getContentResolver().update(mCurrentContactUri, values, null, null);
                Log.d("=>", row + "");
            }
        });
        pickLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("uri", mCurrentContactUri.toString());
                intent.putExtra("location", location);

                startActivity(intent);
            }
        });
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_sms, null);
                final EditText number = mView.findViewById(R.id.dialog_number);
                final EditText message = mView.findViewById(R.id.dialog_message);
                final Button loadContactButton = mView.findViewById(R.id.dialog_load_contact);
                loadContactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                        startActivityForResult(intent, 2);
                    }
                });
                Button sendSms = mView.findViewById(R.id.dialog_send_sms);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                sendSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                                    MY_PERMISSIONS_REQUEST_SEND_SMS);
                        } else {
                            SmsManager sms = SmsManager.getDefault();

                            PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent("SMS_SENT"), 0);
                            PendingIntent deliveredIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent("SMS_DELIVERED"), 0);
                            sms.sendTextMessage(number.getText().toString(), null, message.getText().toString(), sentIntent, deliveredIntent);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


        if (mCurrentContactUri != null) {

            getLoaderManager().initLoader(0, null, this);
        }


        return view;
    }

    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_NUMBER,
                ContactContract.ContactEntry.COLUMN_FAVORITES,
                ContactContract.ContactEntry.COLUMN_CONTACT_LOCATION,
        };

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
            int favoritesColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_FAVORITES);
            int locationColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_LOCATION);

            String name = cursor.getString(nameColumnIndex);
            int number = cursor.getInt(numberColumnIndex);
            favorite = cursor.getInt(favoritesColumnIndex);
            location = String.valueOf(cursor.getString(locationColumnIndex));
            nameTextView.setText(String.valueOf(name));
            numberTextView.setText(String.valueOf(number));

            if (favorite == 0) {
                favoriteImageView.setImageResource(R.drawable.notliked);
            } else {
                favoriteImageView.setImageResource(R.drawable.liked);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameTextView.setText("");
        numberTextView.setText("");
    }


}
