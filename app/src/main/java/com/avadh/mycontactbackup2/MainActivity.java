package com.avadh.mycontactbackup2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{


    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("com.avadh.mycontactbackup2",MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        String loginid = sharedPreferences.getString("LOGINID", "");
        String password = sharedPreferences.getString("PASSWORD", "");
        boolean empty = loginid.isEmpty();
        if (!empty) {
            mAuth.signInWithEmailAndPassword(loginid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Intent intent = new Intent(MainActivity.this,ContactMenuList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        Log.d("Current User ID : - ", uid);
                        String currentUserDisplayName = currentUser.getDisplayName();
                    }
                    startActivity(intent);

                    finish();
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*
    private TextView mLoginView;
    private TextView mSignupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginView = findViewById(R.id.tv_login);
        mSignupView = findViewById(R.id.tv_signup);

        getSupportActionBar().setTitle("Backup And Restore");

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
*/
}
