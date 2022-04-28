package com.example.allergyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class allergyinput extends setup {
    private TabLayout TabLayout;
    private ViewPager viewPager;
    private TabItem ingredients,allergygroups;
    private PageAdapter pagerAdapter;
    private BottomNavigationView nav;
    private ArrayList<TextView> StoreIngredientList=new ArrayList<TextView>( );
    private ArrayList<String> store=new ArrayList<String>();
    private ArrayList<String> StoreAllergyGroups=new ArrayList<String>();
    private Intent intent;
    private User user;


    public allergyinput() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_allergyinput);
        //sets a bunch of ref for buttons and navbars
        nav=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Allergyinfo);
        TabLayout=(TabLayout) findViewById(R.id.tablayout);
        ingredients=(TabItem) findViewById(R.id.ingredients);
        allergygroups=(TabItem) findViewById(R.id.allergygroups);
        viewPager=findViewById(R.id.viewpager);
        pagerAdapter=new PageAdapter(getSupportFragmentManager(),TabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        nav=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Allergyinfo);
        //gets the user object from last activity
         user = (User) getIntent().getSerializableExtra("user");
         //sets nav bar options
        nav.setOnNavigationItemSelectedListener(bn(user));
        //creates array to store the users allergy information
        StoreAllergyGroups=user.getAllergyGroup();
        store=user.getAllergyList();
        //creates textview for each ingredient in the users allergylist
        for(String store: store){
            LinearLayout l=new LinearLayout(this);
            TextView newIngredient=new TextView(this);
            // set width to size of screen width
               newIngredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
               //set text
                newIngredient.setText(store);
                newIngredient.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                newIngredient.setTextSize(50);
                newIngredient.setTextColor(Color.parseColor("#707070"));
                newIngredient.setBackgroundResource(R.drawable.seachbar);
                l.addView(newIngredient);
                StoreIngredientList.add(newIngredient);

        }

         //check which tab is being selected
        TabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //change fargmet on tab change
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                //check which tab is selected
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition()==1){
                    pagerAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {

            }
        });
        //set the change lister
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabLayout));
    }

    //setter and getter methods for fragmets to use
    public void setIngredients(ArrayList<TextView> x) {
        this.StoreIngredientList = x;
    }
    public ArrayList<String> getStore() {
        return store;
    }
    public ArrayList<TextView> getStoreIngredientList() {
        return StoreIngredientList;
    }

    public void setStoreAllergyGroups(ArrayList<String> storeAllergyGroups) {
        StoreAllergyGroups = storeAllergyGroups;
    }

    public ArrayList<String>  getStoreAllergyGroups() {
        return StoreAllergyGroups;
    }

    //updates user allergygroups
    public void setAllergygroups(){
        user.setAllergyGroup(StoreAllergyGroups);
    }
    //updates allergies
    public void update() throws JSONException {
        user.UpdateAllergy();
    }

   //sets ingrdients
    public void setIngredients(){
        ArrayList<String> update=new ArrayList<String>();
        //gets the current text of each textview and adds to arraylist
        for(TextView s:StoreIngredientList){

            update.add(s.getText().toString().toLowerCase());
        }
        //updates users allergylist
        user.setAllergyList(update);
    }

}
