package com.avadh.mycontactbackup2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mName;
    private EditText mSurname;
    private EditText mMailId;
    private EditText mPassword;
    private EditText mConfurmPassword;
    private EditText mPhoneNo;
    private Button mSignup;
    private NewUser newUser;

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

/*        mName.setOnClickListener(this);
        mSurname.setOnClickListener(this);
        mMailId.setOnClickListener(this);
        mPassword.setOnClickListener(this);
        mConfurmPassword.setOnClickListener(this);
        mPhoneNo.setOnClickListener(this);*/
        mSignup.setOnClickListener(this);
    }

    void InsertDataToDatabase() {
        String mail = mMailId.getText().toString().trim();
        DatabaseReference CBackupReference = FirebaseDatabase.getInstance().getReference();
        CBackupReference.child(mail).setValue(newUser);
    }

    void validateData() {
        String name = mName.getText().toString().trim();
        String surname = mSurname.getText().toString().trim();
        String mail = mMailId.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confpassword = mConfurmPassword.getText().toString().trim();
        String phoneno = mPhoneNo.getText().toString().trim();
//            mName.setText("Wrong");
        if (TextUtils.isEmpty(name)) {

        } else if (TextUtils.isEmpty(surname) ) {

        } else if (TextUtils.isEmpty(mail) ) {

        } else if (TextUtils.isEmpty(password) ) {

        } else if (TextUtils.isEmpty(confpassword) ) {

        } else if (TextUtils.isEmpty(confpassword) ) {

        } else if (TextUtils.isEmpty(phoneno) ) {
            Toast.makeText(SignupActivity.this,"Something left",Toast.LENGTH_LONG);
        } else {
            newUser = new NewUser(name,surname,mail,password,phoneno);
            InsertDataToDatabase();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_signup) {
            validateData();
            Toast.makeText(SignupActivity.this,"Matched",Toast.LENGTH_LONG);
        } else {
            Toast.makeText(SignupActivity.this,"UnMatched",Toast.LENGTH_LONG);
        }
    }

}
