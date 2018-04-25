package com.avadh.mycontactbackup2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mLoginText;
    private EditText mPasswordText;
    private TextView mSignUp;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar mSignInPBar;

    private static final String USERNAME = "admin@gmail.com";
    private static final String PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mLoginText = findViewById(R.id.et_loginTxt);
        mPasswordText = findViewById(R.id.et_passwordTxt);
        mSignUp = findViewById(R.id.tv_signup);
        mLoginButton = findViewById(R.id.bt_loginCheck);
        mSignInPBar = findViewById(R.id.pb_signin);

        getSupportActionBar().setTitle("Backup And Restore");
        getSupportActionBar().setSubtitle("Login page");

        mSignUp.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    public void EnableButtons(Boolean vlaue) {
        mLoginButton.setEnabled(vlaue);
    }

    private void userLogin() {
        final String LOGINID = mLoginText.getText().toString().trim();
        final String PASSWORD = mPasswordText.getText().toString().trim();

        if (TextUtils.isEmpty(LOGINID) ) {
            mLoginText.setError("Email is required");
            mLoginText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(LOGINID).matches()) {
            mLoginText.setError("Email is required");
            mLoginText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(PASSWORD) ) {
            mPasswordText.setError("Password is required");
            mPasswordText.requestFocus();
            return;
        }
        if (mPasswordText.length() < 6) {
            mPasswordText.setError("Minimum length of password required is 6");
            mPasswordText.requestFocus();
            return;
        }

        mSignInPBar.setVisibility(View.VISIBLE);
        EnableButtons(false);


        mAuth.signInWithEmailAndPassword(LOGINID,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mSignInPBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    SharedPreferences userNamePassword = getSharedPreferences("com.avadh.mycontactbackup2",MODE_PRIVATE);
                    SharedPreferences.Editor editor = userNamePassword.edit();
                    editor.putString("LOGINID",LOGINID);
                    editor.putString("PASSWORD",PASSWORD);
                    editor.commit();

/*
                    authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName("Avadhoot").build();
                                currentUser.updateProfile(updateProfile);
                                Intent intent = new Intent(LoginActivity.this,ContactMenuList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                String currentUserDisplayName = currentUser.getDisplayName();
                                intent.putExtra("UserName",currentUserDisplayName);
                                Toast.makeText(LoginActivity.this,"Logged in...",Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this,"Errror.......",Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    mAuth.addAuthStateListener(authStateListener);
*/

                    Intent intent = new Intent(LoginActivity.this,ContactMenuList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        Log.d("Current User ID : - ",uid);
                        String currentUserDisplayName = currentUser.getDisplayName();
/*
                        if (currentUserDisplayName.equals(""))
                            intent.putExtra("UserName","user");
                        else
*/
                            intent.putExtra("UserName",currentUserDisplayName);

                        intent.putExtra("UserId",uid);
                    } else {
//                        String uid = currentUser.getUid();
                    }
                    Toast.makeText(LoginActivity.this,"Logged in...",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                    EnableButtons(true);
                } else {
                    EnableButtons(true);
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_loginCheck) {
            userLogin();
            Toast.makeText(this,"Logged in...",Toast.LENGTH_SHORT);
/*
            Intent intent = new Intent(LoginActivity.this,ContactMenuList.class);
            startActivity(intent);
*/
        } else if (id == R.id.tv_signup) {
            Intent intent = new Intent(this,SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
