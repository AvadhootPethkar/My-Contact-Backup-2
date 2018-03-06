package com.avadh.mycontactbackup2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by avadh on 3/6/2018.
 */

public class RestoreContacts {
    private ArrayList<String> mContactList;
    private String mKey;
    private String mContactName;
    private String mContactNumber;

    public RestoreContacts(ArrayList<String> mContactList) {
        this.mContactList = mContactList;
    }
    
    public void StoreContacts(Context context) {

        for (String contactList :
                mContactList) {
            String[] Contact = contactList.split(":\\s+");
            int i = 0;
            for (String Token :
                    Contact) {
                if (i == 1) {
/*
                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.Data.RAW_CONTACT_ID, 001);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1-800-GOOG-411");
                    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
                    values.put(ContactsContract.CommonDataKinds.Phone.LABEL, "Nirav");

                    Uri dataUri = context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
*/

//                    ContactsContract.CommonDataKinds.Phone phoneNo ;
                }
                Log.d("Splited token ... ", Token.trim());
                i++;
            }
            i = 0;
        }
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, 001);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1-800-GOOG-411");
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(ContactsContract.CommonDataKinds.Phone.LABEL, "free directory assistance");

        Uri dataUri = context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

    }
}
