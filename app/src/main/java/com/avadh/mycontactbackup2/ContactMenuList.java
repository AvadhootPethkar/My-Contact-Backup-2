package com.avadh.mycontactbackup2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactMenuList extends AppCompatActivity implements View.OnClickListener,OnContactsRead{

    private Button mBackup;
    private Button mRestore;
    private Button mViewBackup;
    private Button mDeleteBackup;
    private ListView lv_BackupList;
    private ArrayList<String> mContactList;
    ArrayAdapter<String> mArrayAdapter;


    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_menu_list);

        mBackup = findViewById(R.id.bt_backup);
        mRestore = findViewById(R.id.bt_restore);
        mViewBackup = findViewById(R.id.bt_viewbackup);
        mDeleteBackup = findViewById(R.id.bt_deletebackup);
        lv_BackupList = findViewById(R.id.lv_contactlist);

        EnableRunTimePermission();

        mBackup.setOnClickListener(this);
        mRestore.setOnClickListener(this);
        mViewBackup.setOnClickListener(this);
        mDeleteBackup.setOnClickListener(this);
    }

    public void EnableRunTimePermission() {
        if (ContextCompat.checkSelfPermission(ContactMenuList.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    ContactMenuList.this,
                    android.Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(ContactMenuList.this,"Contacts permission allows us to access CONTACTS app",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ContactMenuList.this,new String[] {
                        android.Manifest.permission.READ_CONTACTS },RequestPermissionCode);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactMenuList.this,"Permission Granted, Now your application can access CONTACTS.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactMenuList.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int btnClkId = view.getId();

        if (btnClkId == R.id.bt_backup) {
            CreateContactList createContactList = new CreateContactList();
            ArrayList<String> StoredContacts = createContactList.GetContactsIntoArrayList(this);

            mArrayAdapter = new ArrayAdapter<String>(
                    ContactMenuList.this,
                    R.layout.contact_item_listview,
                    R.id.textView,StoredContacts);
            lv_BackupList.setAdapter(mArrayAdapter);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("avd").child("contacts").setValue(StoredContacts);

        } else if (btnClkId == R.id.bt_restore) {
            RetrieveContactList retrieveContactList = new RetrieveContactList();
            retrieveContactList.setOnContactsReadListner(this);
            ArrayList<String> contactList = retrieveContactList.RetrieveContact();

        } else if (btnClkId == R.id.bt_viewbackup) {
        } else if (btnClkId == R.id.bt_deletebackup) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("avd").child("contacts").removeValue();

        }
    }

    @Override
    public void onCompleted(ArrayList<String> stringArrayList) {
        mArrayAdapter = new ArrayAdapter<String>(
                ContactMenuList.this,
                R.layout.contact_item_listview,
                R.id.textView,stringArrayList);
        lv_BackupList.setAdapter(mArrayAdapter);

        mContactList = stringArrayList;
        RestoreContacts restoreContacts = new RestoreContacts(mContactList);
        restoreContacts.StoreContacts(this);
    }
}
