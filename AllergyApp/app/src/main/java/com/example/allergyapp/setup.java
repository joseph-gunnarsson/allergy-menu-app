package com.example.allergyapp;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class setup extends AppCompatActivity {
    public BottomNavigationView.OnNavigationItemSelectedListener bn(final User users){
        BottomNavigationView.OnNavigationItemSelectedListener a= new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.Scan:
                        intent = new Intent(getApplicationContext(), qrscan.class);
                        intent.putExtra("user",users);
                        startActivity(intent);
                        break;
                    case R.id.Map:
                        intent = new Intent(getApplicationContext(), map.class);
                        intent.putExtra("user",users);
                        startActivity(intent);
                        break;
                    case R.id.Allergyinfo:
                        intent = new Intent(getApplicationContext(), allergyinput.class);
                        intent.putExtra("user",users);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        intent = new Intent(getApplicationContext(), login.class);
                        requests r=new requests();
                        JSONObject data=new JSONObject();
                        try {
                            data.put("id",users.id);
                            r.post(data,"https://www.doc.gold.ac.uk/usr/391/logouta/");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                        break;
                    default:

                }
                return true;
            }
        };
        return a;
    }
    @Override
    public void onBackPressed()
    {

    }

}
