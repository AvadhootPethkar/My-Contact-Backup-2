package com.avadh.mycontactbackup2;

import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by avadh on 2/27/2018.
 */

public class RetrieveContactList {
    private ListView mContactListView;
    ArrayList<String> mContactList;
    DatabaseReference mDatabaseReference;
    OnContactsRead onContactsRead;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;
    private String mUserId;


    public RetrieveContactList() {
        onContactsRead = null;
        mContactList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
    }

    public ArrayList<String> RetrieveContact() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String currentUserDisplayName;
        if (currentUser != null) {
            currentUserDisplayName = currentUser.getDisplayName();
        } else {
            currentUserDisplayName = "wrong";
        }

//        String userName = mAuth.getCurrentUser().getDisplayName();
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mUserId).child(currentUserDisplayName).child("contacts");
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId).child(userName).child("contacts");
        mUserId = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId).child("contacts");

        valueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<String> mContactList = new ArrayList<>();
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contactName = contactSnapshot.getKey().trim();
                    String contactNumber = contactSnapshot.getValue(String.class).trim();
                    mContactList.add(contactName + " : " + contactNumber);
                }
                onContactsReadCompleted();
/*
                RetrieveContactList retrieveContactList = new RetrieveContactList();
                retrieveContactList.mContactList = mContactList;
*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String cotactName = contactSnapshot.getKey();
                    String contactNumber = contactSnapshot.getValue(String.class);
                    mContactList.add(cotactName + " : " + contactNumber);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        Log.d("My Tag........",mContactList.toString());
        return mContactList;
    }

    public void setOnContactsReadListner(OnContactsRead onContactsRead) {
        this.onContactsRead = onContactsRead;
    }

    public void onContactsReadCompleted() {
        this.onContactsRead.onCompleted(mContactList);
    }
}
