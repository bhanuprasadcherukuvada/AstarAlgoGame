package com.example.dp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {


    EditText vname,vemail,vpassword,vphoneno;
    Button vregister;
    TextView vloginhere;
    FirebaseAuth fAuth;
    ProgressBar vprogressbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        vname=findViewById(R.id.et_name);
        vemail=findViewById(R.id.et_email);
        vpassword=findViewById(R.id.et_password);
        vphoneno=findViewById(R.id.et_phone);

        vregister=findViewById(R.id.btn_register);

        vloginhere=findViewById(R.id.tv_loginhere);

        fAuth=FirebaseAuth.getInstance();

        vprogressbar=findViewById(R.id.progressbarid);

        if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }



        vregister.setOnClickListener(new View.OnClickListener() {

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

               vprogressbar.setVisibility(View.VISIBLE);

                //register the user in firebase

                fAuth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this,"User Creation Failed\n"+ "Error is :"+task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });





            }
        });





    }

    public void backslidefun(View view)
    {

        new PreferenceManager(this).clearPreference();
        startActivity(new Intent(this,WelcomeActivity.class));
        finish();

    }

    public void registertologin(View view)
    {
        startActivity(new Intent(this,Login.class));
    }

}
