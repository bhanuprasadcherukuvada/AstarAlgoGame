package com.example.dp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {


    EditText vemail,vpassword;

    Button vlogin;

    TextView vregister;

    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        vemail=findViewById(R.id.et_email1);
        vpassword=findViewById(R.id.et_password1);

        vlogin=findViewById(R.id.btn_login);

        vregister=findViewById(R.id.tv_registerhere);

        progressBar=findViewById(R.id.progressbarid1);

        vlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                String semail=vemail.getText().toString().trim();
                String spassword=vpassword.getText().toString().trim();

                if(TextUtils.isEmpty(semail))
                {
                    vemail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(spassword))
                {
                    vpassword.setError("Password is required");
                    return;
                }

                if(spassword.length() < 6)
                {
                    vpassword.setError("Minimum 6 numbers is required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                //authenticate the user;

                firebaseAuth.signInWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Login Unsuccessful\n"+ "Error is :"+task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }
    public void logintoregister(View view)
    {
        startActivity(new Intent(this,Register.class));
    }
}
