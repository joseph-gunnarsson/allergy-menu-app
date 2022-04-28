package com.example.allergyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.provider.Settings.Secure;


public class login extends setup {
    //declares varibles
    private Button button;
    private EditText pass,email;
    private requests request=new requests();
    private String id;
    private Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen


        setContentView(R.layout.activity_login);
        //sets ref for buttons and edittext
        button = (Button)findViewById(R.id.loginbtn);
        pass=(EditText)findViewById(R.id.passtxt);
        email=(EditText)findViewById((R.id.emailtxt)) ;
        JSONObject token=new JSONObject();
        //get the unique identifier of the device
        id=Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        try {
            //set the token to the unique id of android devices
            token.put("token", id);
            //post to check if android devices is linked to account
            JSONObject tokens=request.post(token,"https://www.doc.gold.ac.uk/usr/391/tokencheck/");
            if(tokens.getString("message").equals("Sucsses")){
                //sets Intent to qrscan activity
                 myIntent = new Intent(login.this, qrscan.class);
                 //and the user will be set to the user data returned
                User user=new User(tokens.getInt("id"),tokens.getString("email"),tokens.getString("name"));
                //adds the user object to intent so it can be retrived on next activity
                myIntent.putExtra("user",user);
                //start the new activity
                startActivity(myIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //chceks for login button click
        View.OnClickListener Tologin = new View.OnClickListener() {
            public void onClick(View view) {
                //shows button clicked by changing background
                button.setBackgroundResource(R.drawable.clickedbutton);
                //gets the current text of the input feilds
                String emails=email.getText().toString();
                String passw=pass.getText().toString();
                //sets Intent to qrscan activity
                 myIntent = new Intent(login.this, qrscan.class);
                JSONObject x;
                try {
                    JSONObject data = new JSONObject();
                    //adds the login information to json object
                    data.put("password", passw);
                    data.put("email", emails);
                    data.put("token", id);
                    //send request to api to check if user login details are correct
                    x = request.post(data, "https://www.doc.gold.ac.uk/usr/391/login/");
                    //checks if the response data return a Sucsses message
                    if(x.getString("message").toString().equals("Sucsses")){
                        //creates a user with the response data
                        User user=new User(x.getInt("id"),x.getString("email"),x.getString("name"));
                        //sets new intent to qr scan
                       myIntent = new Intent(login.this, qrscan.class);
                        //adds the user object to intent so it can be retrived on next activity
                         myIntent.putExtra("user",user);
                        startActivity(myIntent);}
                    else{
                        //else it changes the background of the epass and email input box to show there was incorrect information added
                        Log.d("as","as");
                        pass.setBackgroundResource(R.drawable.incorrect);
                        email.setBackgroundResource(R.drawable.incorrect);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
        //set listener
          button.setOnClickListener(Tologin);
    }
}
