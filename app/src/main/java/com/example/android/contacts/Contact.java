package com.example.android.contacts;

import android.database.Cursor;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.android.contacts.data.ContactContract;

public class Contact {

    private String name;
    private int number;
    private int picture;

    public Contact()
    {

    }
    public static Contact fromCursor(Cursor cursor) {
        //TODO return your MyListItem from cursor.
        Contact contact = new Contact();
        return contact;
    }

    public Contact(String name, int number, int picture)
    {
        this.name = name;
        this.number = number;
        this.picture = picture;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public int getPicture()
    {
        return picture;
    }

    public void setPicture(int picture)
    {
        this.picture = picture;
    }
}
