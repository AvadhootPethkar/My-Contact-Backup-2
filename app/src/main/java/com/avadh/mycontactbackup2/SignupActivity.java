package com.avadh.mycontactbackup2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mName;
    private EditText mSurname;
    private EditText mMailId;
    private EditText mPassword;
    private EditText mConfurmPassword;
    private EditText mPhoneNo;
    private Button mSignup;
    private NewUser newUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar mSignUpPBar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName = findViewById(R.id.et_name);
        mSurname = findViewById(R.id.et_surname);
        mMailId = findViewById(R.id.et_mailid);
        mPassword = findViewById(R.id.et_password);
        mConfurmPassword = findViewById(R.id.et_confpassowrd);
        mPhoneNo = findViewById(R.id.et_phoneno);
        mSignup = findViewById(R.id.bt_signup);
        mSignUpPBar = findViewById(R.id.pb_signup);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Backup And Restore");

        getSupportActionBar().setSubtitle("Sign up form");

/*        mName.setOnClickListener(this);
        mSurname.setOnClickListener(this);
        mMailId.setOnClickListener(this);
        mPassword.setOnClickListener(this);
        mConfurmPassword.setOnClickListener(this);
        mPhoneNo.setOnClickListener(this);*/
        mSignup.setOnClickListener(this);
    }

    void InsertDataToDatabase() {
        String Username = mName.getText().toString().trim();
        DatabaseReference CBackupReference = FirebaseDatabase.getInstance().getReference();
        String firebaseUserId = mAuth.getCurrentUser().getUid();
//        CBackupReference.child("Users").child(firebaseUserId).child(Username).setValue(newUser);
        Toast.makeText(this,firebaseUserId,Toast.LENGTH_SHORT).show();
        CBackupReference.child("Users").child(firebaseUserId).setValue(newUser);
        CBackupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSignUpPBar.setVisibility(View.GONE);
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    void validateData() {
        final String name = mName.getText().toString().trim();
        String surname = mSurname.getText().toString().trim();
        String mail = mMailId.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confpassword = mConfurmPassword.getText().toString().trim();
        String phoneno = mPhoneNo.getText().toString().trim();
//            mName.setText("Wrong");

        if (TextUtils.isEmpty(name)) {
            mName.setError("User name is required");
            mName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(surname) ) {
            mSurname.setError("Surname is required");
            mName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail) ) {
            mMailId.setError("Email is required");
            mMailId.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mMailId.setError("Email is required");
            mMailId.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) ) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }
        if (mPassword.length() < 6) {
            mPassword.setError("Minimum length of password required is 6");
            mPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confpassword) ) {
            mConfurmPassword.setError("Reenter same password");
            mConfurmPassword.requestFocus();
            return;
        }
/*
        if (!mConfurmPassword.equals(mPassword) ) {
            mConfurmPassword.setError("Password does not match");
            mConfurmPassword.requestFocus();
            return;
        }
*/
        if (TextUtils.isEmpty(phoneno) ) {
            mPhoneNo.setError("Phone No is required");
            mPhoneNo.requestFocus();
            return;
        }

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(name)) {
                        mName.setError("User name is required");
                        mName.requestFocus();
                        return;
                    } else {
                        String namefound = mName.getText().toString().trim();
                        if (namefound.equals("@vd")) {
                            mName.setText("Error");
                            return;
                        }
                    }
                }
            }
        });

        mSignUpPBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                mSignUpPBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String uid = mAuth.getCurrentUser().getUid();
                    userId = uid;
                    Toast.makeText(SignupActivity.this,uid,Toast.LENGTH_SHORT).show();
                    authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String displayName = mName.getText().toString().trim();
                                UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName).build();
                                Toast.makeText(getApplicationContext(),"Display name set",Toast.LENGTH_LONG).show();
                                currentUser.updateProfile(updateProfile);
                                Toast.makeText(getApplicationContext(),mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),mName.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                                Log.d("Displa name added","nncncnc");
                            }
                            userId = currentUser.getUid();

                            }
                    };

                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_LONG).show();
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String name1 = mName.getText().toString().trim();
                String surname1 = mSurname.getText().toString().trim();
                String mail1 = mMailId.getText().toString().trim();
                String password1 = mPassword.getText().toString().trim();
                String phoneno1 = mPhoneNo.getText().toString().trim();
                newUser = new NewUser(name1, surname1, mail1, password1, phoneno1);
                mAuth.addAuthStateListener(authStateListener);
                InsertDataToDatabase();

            }
        });
/*
        newUser = new NewUser(name,surname,mail,password,phoneno);
        InsertDataToDatabase();
*/
    }

/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
*/

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this,mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_SHORT).show();
        if(authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_signup) {
            validateData();
            Toast.makeText(SignupActivity.this,"Matched",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SignupActivity.this,"UnMatched",Toast.LENGTH_LONG).show();
        }
    }

}
