package com.example.allergyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class menu extends setup {
     ArrayList<LinearLayout> menuItems=new ArrayList<LinearLayout>();
     Intent intent=new Intent();
     TextView title;
     RestMenu Restmenu;
     User user=new User();
     LinearLayout lin;
     ArrayList<String> order=new ArrayList<String>();

    public menu() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_menu);
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Allergyinfo);
        //gets user from last intent
        user=(User)getIntent().getSerializableExtra("user");
        //alert that explains how to use the activity
        Global.dialog(this.getWindow().getDecorView().findViewById(android.R.id.content), new String[]{"Click on menu item to add and remove from basket, then order by pressing the order button"},menu.this,"Message").show();
       //sets up nav bar
        nav.setOnNavigationItemSelectedListener(bn(user));


        try {
            //setups restmenu with rest id
          Restmenu=new RestMenu(6);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set title of page to restaurants name
        title=findViewById(R.id.title);
        title.setText(Restmenu.getRestName());
        //removes the menu items that contain ingredients or allergen that the user cannot consume
        ArrayList<MenuItem> a=Restmenu.filtermenu(user.getAllergyList(),user.getAllergyGroup());
        //list to store the added section
        ArrayList<String> addedSection=new ArrayList<String>();
        ArrayList<LinearLayout> linear=new ArrayList<LinearLayout>();
        final View view = new View(this);
        //gets the linear layout of page
        lin=findViewById(R.id.lin);
        //loops through each menu item that
        for(MenuItem item: a){
            //checks if section for menu item has already been added to the linear list
            if(!addedSection.contains(item.getSection())){
                //creates a new linear list to contain the menu section
                LinearLayout parent = new LinearLayout(this);
                //textview to label the section
                final TextView section_name=new TextView(this);
                //set linear layour to match the width of screen and to warp content in height
                parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //set linear layout so views are added vertically
                parent.setOrientation(LinearLayout.VERTICAL);
                //set the tag to the name of section
                parent.setTag(item.getSection()+"Parent");
                parent.setPadding(0,0,0,30);
                //adds the linear layout for the section to the linear layout of the page
                lin.addView(parent);
                //set up the text view
                section_name.setText(item.getSection());
                section_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                section_name.setTextSize(50);
                section_name.setTextColor(Color.parseColor("#707070"));
                section_name.setBackgroundResource(R.drawable.show);
                parent.addView(section_name);
                section_name.setPadding(90,0,0,0);
                //creates a second linear layout that will show the menu items inforamtion
                final LinearLayout parent2 = new LinearLayout(this);
                //sets up second linear layout t
                parent2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                parent2.setOrientation(LinearLayout.VERTICAL);
                parent2.setTag(item.getSection()+"Parent2");
                parent.addView(parent2);
                //adds the section name to the arraylist so another will not be created
                addedSection.add(item.getSection());
                //loops through all of the menu items to added them to the menu item linear layout
                for(final MenuItem test: a){
                    //checks if menu item is part of the menu section
                    if(test.getSection().toLowerCase().equals(item.getSection().toLowerCase())){
                        //creates another linear layout to store a menu item info
                        final LinearLayout itemsection = new LinearLayout(this);
                        //sets up the item section linear layout
                        itemsection.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        itemsection.setOrientation(LinearLayout.VERTICAL);
                        itemsection.setTag(item.getSection()+"Parent");
                        itemsection.setBackgroundResource(R.drawable.seachbar);
                        itemsection.setPadding(0,0,0,30);
                        //creates two textview that will show the price and name of menu item
                        final TextView name=new TextView(this);
                        final TextView price=new TextView(this);
                        //sets up the name and price textview
                        name.setText("Item name:"+test.getName());
                        name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        name.setTextSize(30);
                        name.setPadding(80,0,0,0);
                        name.setTextColor(Color.parseColor("#707070"));
                        price.setText("Price:"+test.price);
                        price.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        price.setTextSize(30);
                        price.setPadding(80,0,0,0);
                        price.setTextColor(Color.parseColor("#707070"));
                        //adds both price and name textview to the item section linear layout
                        itemsection.addView(name);
                        itemsection.addView(price);
                        //adds the item section linear layout to the linear layout for the menu section
                        parent2.addView(itemsection);
                        //sets on click event to add a menu item to basket
                        itemsection.setOnClickListener(new View.OnClickListener() {
                            //creates a textview that will show a message when a menu item is added to basket
                            TextView ordered=new TextView(menu.this);
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(View v) {
                                //checks if menu item has been added to basket or not
                                if(!order.contains(name.getText()+" For "+user.getName())){
                                    //adds the menu item to oreder contain the users name and name of menu item
                                    order.add(name.getText()+" For "+user.getName());
                                    //displays text in itemsection linear layout to show menu items been added to baskest
                                    ordered.setText("Added to basket");
                                    //sets up textview
                                    ordered.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    ordered.setTextSize(30);
                                    ordered.setPadding(80,0,0,0);
                                    ordered.setTextColor(R.color.red);
                                    itemsection.addView(ordered);
                                }else{
                                    //if menu item was already added to basket and clicked again the item is remove from the baskest
                                    order.remove(name.getText()+" For "+user.getName());
                                    ((ViewGroup)ordered.getParent()).removeView(ordered);
                                }
                            }
                        });
                    }
                }

                linear.add(parent);
                //set on click event that either hides menu section content or shows when clicked
                View.OnClickListener hid = new View.OnClickListener() {
                    public void onClick(View view) {
                        //if clicked either show or hide menu items of section or hide.
                        if(parent2.getVisibility() == View.VISIBLE){
                            section_name.setBackgroundResource(R.drawable.show);
                            parent2.setVisibility(View.INVISIBLE);
                            parent2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                        }else{
                            section_name.setBackgroundResource(R.drawable.hide);
                            parent2.setVisibility(View.VISIBLE);
                            parent2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        }
                    }
                };
                parent2.setVisibility(View.INVISIBLE);
                parent2.setPadding(150,10,30,0);
                parent2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                section_name.setOnClickListener(hid);
            }

        }
        //button that when clicks orders items
        Button orderbtn=findViewById(R.id.update);
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup that show menu item has been ordered
                Global.dialog(v, new String[]{"Order has been sent"},menu.this,"Message").show();
                //adds order to a json object to be sent to server
                requests r=new requests();
                JSONArray items=new JSONArray(order);
                JSONObject ob =new JSONObject();
                try {
                    //
                    ob.put("items",items);
                    ob.put("id",user.getId());
                    ob.put("restid",Restmenu.getId());
                    //makes post request to order the item and add it to database
                    ob=r.post(ob,"https://www.doc.gold.ac.uk/usr/391/order/");
                    String message=ob.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });












    }
}
