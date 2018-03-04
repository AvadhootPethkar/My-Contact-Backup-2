package com.avadh.mycontactbackup2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mLoginView;
    private TextView mSignupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginView = findViewById(R.id.tv_login);
        mSignupView = findViewById(R.id.tv_signup);

        mLoginView.setOnClickListener(this);
        mSignupView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_login) {
            Toast.makeText(this,"Loging in ...",Toast.LENGTH_SHORT);
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == R.id.tv_signup) {
            Toast.makeText(this,"Signing up ...",Toast.LENGTH_SHORT);
            Intent signupIntent = new Intent(this,SignupActivity.class);
            startActivity(signupIntent);
        }
    }
}
