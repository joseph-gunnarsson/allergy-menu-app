package com.example.allergyapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class map extends fragsetup implements OnMapReadyCallback {
    GoogleMap map;
    BottomNavigationView nav;
    Intent intent;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //creates the googlemap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Allergyinfo);
        user = (User) getIntent().getSerializableExtra("user");
        nav.setOnNavigationItemSelectedListener(bn(user));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        requests request=new requests();
        //set the googlemap
        map=googleMap;
        //creates a json array
        JSONArray DataArray = new JSONArray();
        //creates a arraylist to store the resturants name and addresss
        ArrayList<JSONObject> info=new ArrayList<JSONObject>();
        try {
            //post reqequest to get all of the resturants name and addresss
            JSONObject r=request.post(new JSONObject(),"https://www.doc.gold.ac.uk/usr/391/address/");
            DataArray=r.getJSONArray("info");
            //loops the json array to get all jsonobject in an arraylist
            if (DataArray != null) {
                for (int i=0;i<DataArray.length();i++){
                    info.add(DataArray.getJSONObject(i));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //loop through each jsonobject contains the address and name of eah resturant
        for(JSONObject loc: info){
            try {
            //gets the latlng for the address of resturant
            LatLng lat= getLocationFromAddress(loc.getString("address"),map.this);
            //adds a marker to the map with the resturant name as label
            map.addMarker(new MarkerOptions().position(lat).title(loc.getString("restaurant_name")));
            } catch (Exception b) {
                Log.d("namer",loc.toString());
                b.printStackTrace();
            }
        }



    }
    //converts an address to latlng value
    public LatLng getLocationFromAddress(String strAddress,Context context){
        //create a geocoder object
        Geocoder geocoder = new Geocoder(context);
        //list to store results
        List<Address> address;
        //stores the latlng location
        LatLng lat = null;
        try {
            //get the address of the strAdress
            address = geocoder.getFromLocationName(strAddress,1);
            //checks if getFromLocationName found any addresses
            if (address!=null) {
            //gets first address
            Address location=address.get(0);
            //get the lat and lng of the address and creates a latlng object
            lat = new LatLng (location.getLatitude() , location.getLongitude() );
            return lat;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }





}
