package com.avadh.mycontactbackup2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by avadh on 2/26/2018.
 */

public class CreateContactList  {
    ArrayList<String> StoreContacts;
    Cursor mCursor;
    String name;
    String PhoneNumber;

    public CreateContactList() {
    }

    public ArrayList<String> GetContactsIntoArrayList(Context context) {
        StoreContacts = new ArrayList<String>();

        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String displayName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        String phoneNumber = ContactsContract.CommonDataKinds.Phone.NUMBER;
        mCursor = context.getContentResolver().query(contentUri,
                null,null,null,null);

        while (mCursor.moveToNext()) {
            name = mCursor.getString(mCursor.getColumnIndex(displayName));
            PhoneNumber = mCursor.getString(mCursor.getColumnIndex(phoneNumber));

            StoreContacts.add(name + " " + ":" + " " + PhoneNumber);
        }
        mCursor.close();
        return StoreContacts;
    }

}
