package com.example.allergyapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.io.*;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLConnection;
import java.security.Permission;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
public class MainActivity extends setup {
    private Button button;
    private TextView  reg;


    private RequestQueue mQueue;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);
         button = (Button)findViewById(R.id.loginbutton);
        reg = (TextView)findViewById(R.id.treg);

        //change page depending on button press
        View.OnClickListener Tologin = new View.OnClickListener() {
            public void onClick(View view) {
                button.setBackgroundResource(R.drawable.clickedbutton);
                Intent myIntent = new Intent(MainActivity.this, login.class);

               startActivity(myIntent);
            }
        };
        View.OnClickListener toReg = new View.OnClickListener() {
            public void onClick(View view) {
                reg.setTextColor(Color.BLUE);
                Intent myIntent = new Intent(MainActivity.this, register.class);
                startActivity(myIntent);
            }
        };

        button.setOnClickListener(Tologin);
        reg.setOnClickListener(toReg);


    }



}
