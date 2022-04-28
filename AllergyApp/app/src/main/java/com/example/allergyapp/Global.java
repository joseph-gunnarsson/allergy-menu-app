package com.example.allergyapp;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

//class that can be used in any activity
public class Global extends Application{
    //create a dialog popup with message
    public static AlertDialog dialog(View view, String[] Messages, Context a, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setTitle(title).setItems(Messages, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("OK", null);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


}