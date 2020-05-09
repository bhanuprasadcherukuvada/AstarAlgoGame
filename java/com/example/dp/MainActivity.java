package com.example.dp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logoutfun(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,Login.class));
        finish();

    }

    public void letsplayfun(View view)
    {

        startActivity(new Intent(MainActivity.this,GameActivity.class));
        finish();


    }




}
