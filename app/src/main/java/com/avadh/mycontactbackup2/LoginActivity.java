package com.avadh.mycontactbackup2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mloginText;
    private EditText mpasswordText;
    private TextView mforgetPassword;
    private Button mloginButton;

    private static final String USERNAME = "admin@gmail.com";
    private static final String PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mloginText = findViewById(R.id.et_loginTxt);
        mpasswordText = findViewById(R.id.et_passwordTxt);
        mforgetPassword = findViewById(R.id.tv_fLogin);
        mloginButton = findViewById(R.id.bt_loginCheck);

        mforgetPassword.setOnClickListener(this);
        mloginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_loginCheck) {
            Toast.makeText(this,"Logged in...",Toast.LENGTH_SHORT);
            Intent intent = new Intent(LoginActivity.this,ContactMenuList.class);
            startActivity(intent);
        } else if (id == R.id.tv_fLogin) {

        }
    }
}
