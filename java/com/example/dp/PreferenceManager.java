package com.example.dp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {


    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        this.context = context;
        getSharedPreferences();

    }


    private void getSharedPreferences() {

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.slide2_title), Context.MODE_PRIVATE);

    }

    public void writePreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getString(R.string.slide2_title), "INIT_OK");
        editor.commit();


    }

    public boolean checkPreference() {

        boolean status = false;

        if (sharedPreferences.getString(context.getString(R.string.slide2_title), "null").equals("null"))
        {
             status = false;
        } else {
            status = true;
        }
        return status;

    }

    public void clearPreference()
    {

        sharedPreferences.edit().clear().commit();
    }

}
