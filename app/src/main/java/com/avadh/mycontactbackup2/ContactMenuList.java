package com.avadh.mycontactbackup2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactMenuList extends AppCompatActivity implements View.OnClickListener, OnContactsRead {

    private Button mBackup;
    private Button mRestore;
    private Button mDeleteBackup;
    private Button mLogout;
    private ListView lv_BackupList;
    private ArrayList<String> mContactList;
    ArrayAdapter<String> mArrayAdapter;
    private ProgressBar mSignUpPBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    RetrieveContactList retrieveContactList;
    ValueEventListener valueEventListenerDb;
    private String userName;
    private String mUserId;

    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_menu_list);

        mBackup = findViewById(R.id.bt_backup);
        mRestore = findViewById(R.id.bt_restore);
        mDeleteBackup = findViewById(R.id.bt_deletebackup);
        mLogout = findViewById(R.id.bt_logout);
        lv_BackupList = findViewById(R.id.lv_contactlist);
        mSignUpPBar = findViewById(R.id.pb_listpb);

        getSupportActionBar().setTitle("Backup And Restore");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mUserId = mAuth.getCurrentUser().getUid();
        Log.d("UID ..... ####### ", mUserId);

        EnableRunTimePermission();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userName = null;
                mUserId = null;
            } else {
                userName = extras.getString("UserName");
                mUserId = extras.getString("UserId");
                getSupportActionBar().setSubtitle("Hello " + mUserId);
            }
        } else {
            getSupportActionBar().setSubtitle("Hello " + mUserId);
            userName = (String) savedInstanceState.getSerializable("UserName");
            mUserId = (String) savedInstanceState.getSerializable("UserId");
        }
        String FirebaseUserId = mAuth.getCurrentUser().getDisplayName();
        getSupportActionBar().setSubtitle("Hello " + FirebaseUserId);

        mBackup.setOnClickListener(this);
        mRestore.setOnClickListener(this);
        mDeleteBackup.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    public void EnableButtons(Boolean vlaue) {
        mBackup.setEnabled(vlaue);
        mRestore.setEnabled(vlaue);
        mDeleteBackup.setEnabled(vlaue);
        mLogout.setEnabled(vlaue);

    }


    public void EnableRunTimePermission() {
        if (ContextCompat.checkSelfPermission(ContactMenuList.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    ContactMenuList.this,
                    android.Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(ContactMenuList.this, "Contacts permission allows us to access CONTACTS app", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ContactMenuList.this, new String[]{
                        android.Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
            }
        } else {

        }
    }

    public void GetUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            boolean emailVerified = user.isEmailVerified();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactMenuList.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactMenuList.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int btnClkId = view.getId();

        mSignUpPBar.setVisibility(View.VISIBLE);
        EnableButtons(false);

        if (btnClkId == R.id.bt_backup) {
            CreateContactList createContactList = new CreateContactList();
            ArrayList<String> StoredContacts = createContactList.GetContactsIntoArrayList(this);

            mArrayAdapter = new ArrayAdapter<String>(
                    ContactMenuList.this,
                    R.layout.contact_item_listview,
                    R.id.textView, StoredContacts);
            lv_BackupList.setAdapter(mArrayAdapter);

//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String FirebaseUserId = mAuth.getCurrentUser().getUid();
//            String FirebaseUserName = mAuth.getCurrentUser().getDisplayName();
//            databaseReference.child("Users").child(FirebaseUserId).child("a").child("contacts").setValue(StoredContacts);
//            databaseReference.child("Users").child(FirebaseUserId).child(FirebaseUserName).child("contacts").setValue(StoredContacts);
/*
            if (userName.isEmpty()) {
                Toast.makeText(this,"User name is empty",Toast.LENGTH_SHORT).show();
            }
*/
//            databaseReference.child("Users").child(FirebaseUserId).child(userName).child("contacts").setValue(StoredContacts);
            databaseReference.child("Users").child(FirebaseUserId).child("contacts").setValue(StoredContacts);
//            databaseReference.child("aaaaa").child("contacts").setValue(StoredContacts);
            valueEventListenerDb = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSignUpPBar.setVisibility(View.GONE);
                    EnableButtons(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mSignUpPBar.setVisibility(View.GONE);
                    EnableButtons(true);

                    int message = databaseError.getCode();
                    Log.d("Error message : -  ", "" + message);
                    String details = databaseError.getDetails();
                    Log.d("Error details : -  ", "" + details);

                }
            });

        } else if (btnClkId == R.id.bt_restore) {
            retrieveContactList = new RetrieveContactList();
            retrieveContactList.setOnContactsReadListner(this);
            ArrayList<String> contactList = retrieveContactList.RetrieveContact();

        } else if (btnClkId == R.id.bt_deletebackup) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//            databaseReference.child("Users").child(mUserId).child(userName).child("contacts").removeValue();
            String FirebaseUserId = mAuth.getCurrentUser().getUid();
            databaseReference.child("Users").child(FirebaseUserId).child("contacts").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mSignUpPBar.setVisibility(View.GONE);
                    EnableButtons(true);
                }
            });

        } else if (btnClkId == R.id.bt_logout) {
            mAuth.signOut();
            SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("com.avadh.mycontactbackup2", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            mSignUpPBar.setVisibility(View.GONE);
            EnableButtons(true);
            startActivity(new Intent(ContactMenuList.this, LoginActivity.class));
            finish();
        }
    }

    public static boolean isTheNumberExistsinContacts(Context ctx,
                                                      String phoneNumber) {
        Cursor cur = null;
        ContentResolver cr = null;

        try {
            cr = ctx.getContentResolver();

        } catch (Exception ex) {
            Log.d("Exception..........", ex.getMessage());
        }

        try {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                    null, null);
        } catch (Exception ex) {
            Log.i("Exception..........", ex.getMessage());
        }

        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // Log.i("Names", name);
                    if (Integer
                            .parseInt(cur.getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // Query phone here. Covered next
                        Cursor phones = ctx
                                .getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + id, null, null);
                        while (phones.moveToNext()) {
                            String phoneNumberX = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // Log.i("Number", phoneNumber);

                            phoneNumberX = phoneNumberX.replace(" ", "");
                            phoneNumberX = phoneNumberX.replace("(", "");
                            phoneNumberX = phoneNumberX.replace(")", "");
                            if (phoneNumberX.contains(phoneNumber)) {
                                phones.close();
                                return true;

                            }

                        }
                        phones.close();
                    }

                }
            }
        } catch (Exception ex) {
            Log.i("Exception..........", ex.getMessage());

        }

        return false;
    }


    @Override
    public void onCompleted(ArrayList<String> stringArrayList) {
        retrieveContactList.mDatabaseReference.removeEventListener(retrieveContactList.valueEventListener);
        retrieveContactList = null;
        mArrayAdapter = new ArrayAdapter<String>(
                ContactMenuList.this,
                R.layout.contact_item_listview,
                R.id.textView, stringArrayList);
        lv_BackupList.setAdapter(mArrayAdapter);

        mContactList = stringArrayList;
//        RestoreContacts restoreContacts = new RestoreContacts();
//        restoreContacts.StoreContacts(this);
        // Logic to check and store contact...............
        for (String contact :
                mContactList) {
            String[] contactToken = contact.split(":\\s+");
            if (contactToken.length == 3) {
                boolean theNumberExistsinContacts = isTheNumberExistsinContacts(this, contactToken[2]);
                if (theNumberExistsinContacts) {
                    Log.d("Contact already exists", contactToken[1]);
                    continue;
                }
                RestoreContacts.Insert2Contacts(this, contactToken[1], contactToken[2]);
            }
            for (int i = 0; i < contactToken.length; i++) {
                Log.d("Splited token ... ", contactToken[i].trim());
            }
        }
        RestoreContacts.Insert2Contacts(this, "qwertyui", "1230456789");
        boolean theNumberExistsinContacts = isTheNumberExistsinContacts(this, "1230456789");
        if (!theNumberExistsinContacts) {
            Log.d("Contact does not exists", "1230456789");
            RestoreContacts.Insert2Contacts(this, "qwertyui", "1230456789");
        } else {
            Log.d("Contact already exists", "1230456789");
        }
        mSignUpPBar.setVisibility(View.GONE);
        EnableButtons(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListenerDb != null)
            databaseReference.removeEventListener(valueEventListenerDb);
    }
}
