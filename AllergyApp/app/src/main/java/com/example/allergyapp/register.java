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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class register extends setup {
    private Button   Signupbtn,Backbtn;
    private EditText  name,pass,email,com_pass;
    public requests request=new requests();
    private RequestQueue Queue;
    private String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_register);
        //gets the ref for the button and input boxes
        Signupbtn=(Button)findViewById(R.id.signupbtn);
        Backbtn=(Button)findViewById((R.id.backbtn)) ;
        name = (EditText)findViewById(R.id.nametxt);
        email = (EditText)findViewById(R.id.emailtxt);
        pass = (EditText)findViewById(R.id.passtxt);
        com_pass = (EditText)findViewById(R.id.comtxt);
        View.OnClickListener signupcheck = new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(register.this, login.class);
                Boolean isValid=true;
                List<String> Error = new ArrayList<String>();
                String Fullname=name.getText().toString();
                String PassStr= pass.getText().toString();
                String EmailStr=email.getText().toString();
                String comStr=com_pass.getText().toString();
                //uses the regex method to check if Fullname varible is of name format
                if(Fullname(Fullname)==true)
                    name.setBackgroundResource(R.drawable.correct);
                else{
                    //if nots adds an error message to that error array
                    name.setBackgroundResource(R.drawable.incorrect);
                    isValid=false;
                    Error.add("Invalided Full name, Please make sure to use only characters and spaces.");
                }
                //uses regex to check if password is of a certain strength and if the password and confirmed password match
                if(Password(PassStr)==true && PassStr.equals(comStr))
                { pass.setBackgroundResource(R.drawable.correct);
                    com_pass.setBackgroundResource(R.drawable.correct);}
                else{
                    //if nots adds an error message to that error array
                    pass.setBackgroundResource(R.drawable.incorrect);
                    com_pass.setBackgroundResource(R.drawable.incorrect);
                    isValid=false;
                    Error.add("Invalided Password, Please make sure minimum eight characters, at least one letter and one number and Passwords match.");
                }
                //check if email is of email format using regex method
                if(Email(EmailStr.toLowerCase()))
                 email.setBackgroundResource(R.drawable.correct);
                else{
                    //if nots adds an error message to that error array
                    email.setBackgroundResource(R.drawable.incorrect);
                    isValid=false;
                    Error.add("Invalided Email, Please enter a valid email or the email is already taken.");

                }
                //checks if there are no incorrect input
                if(isValid){
                    JSONObject x = null;
                    try {
                        //makes a post request that adds the user to the database

                        //creates a json object with the user info
                        JSONObject userinfo = new JSONObject();
                        userinfo.put("password", PassStr);
                        userinfo.put("email", EmailStr.toLowerCase());
                        userinfo.put("first_name", Fullname.toLowerCase());
                        //makes a request to the server to add the user
                        x=request.post(userinfo,"https://www.doc.gold.ac.uk/usr/391/signup/");
                        //if the user was added start new activity
                        if(x.getString("email").equals("Sucsses")){
                            startActivity(myIntent);
                        }else{
                            //else display the error message returned by the server
                            Error.add(x.getString("email"));
                            String[] Errorarr = new String[Error.size()];
                            Errorarr=Error.toArray(Errorarr);
                            AlertDialog a=Global.dialog(view,Errorarr,register.this,"Invalid");
                            a.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    //if  some of the inputted was incorrect displays error message contained in the error array
                    String[] Errorarr = new String[Error.size()];
                    Errorarr=Error.toArray(Errorarr);
                    isValid=true;
                    AlertDialog a=Global.dialog(view,Errorarr,register.this,"Invalid");
                    a.show();
                }
            }
        };
        //button takes back to home activity
        View.OnClickListener back = new View.OnClickListener() {
            public void onClick(View view) {
              Intent myIntent = new Intent(register.this, MainActivity.class);
                startActivity(myIntent);
            }
        };
        Signupbtn.setOnClickListener(signupcheck);
        Backbtn.setOnClickListener(back);

    }

      //regex that check if email fulname passwrod are valid inputs
    public boolean Fullname(String str) {
        String reg = "^[a-zA-Z\\s]+";
        return str.matches(reg);
    }
    public boolean Password(String str) {
        String reg ="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return str.matches(reg);
    }
    public boolean Email(String str) {
        String reg ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return str.matches(reg);
    }



}
