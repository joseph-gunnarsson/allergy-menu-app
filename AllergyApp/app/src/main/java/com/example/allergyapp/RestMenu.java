package com.example.allergyapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestMenu {
    //class varible declared
    private static requests RequestsStat=new requests();
    private requests Requests =new requests();
    private String RestName;
    private int id;
    private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>( );
    public RestMenu(int id) throws JSONException {
        JSONArray StoreMenuItemsInfo;
        //sets id
        this.id=id;
        //creates json object
        JSONObject restinfo = new JSONObject();
        restinfo.put("restid",id);
        //get resturant menu using post requst with the id
        JSONObject Data=Requests.post(restinfo,"https://www.doc.gold.ac.uk/usr/391/getRest/");
        //sets the resturant name
        this.RestName=Data.getString("RestName");
        //gets the menu items
        StoreMenuItemsInfo=Data.getJSONArray("MenuItems");
        //creates an arraylist of menu items
        this.menuItems=JsonArrayToArraylistOfMenuitems(StoreMenuItemsInfo);
    }
    public ArrayList<MenuItem> filtermenu(ArrayList<String> Allergies,ArrayList<String> AllergyGroups){
        //filters the menu items removing items that contain any of String is in allergies or allergygroups
        ArrayList<MenuItem> Filtered = new ArrayList<MenuItem>( );
        Filtered=fliter(Allergies,AllergyGroups,menuItems);
        return Filtered;
    }
     //getters and setters
    public String getRestName() {
        return RestName;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public int getId() {
        return id;
    }
    //takes a jsonarray of menuitems and converts them into an arraylist of menuitem objects
    public static ArrayList<MenuItem> JsonArrayToArraylistOfMenuitems(JSONArray MenuItemsJson) throws JSONException {
        ArrayList<MenuItem> menuItemsAsList=new ArrayList<MenuItem>( );
        //llops through each menu item
        if (MenuItemsJson != null) {
            for (int i=0;i<MenuItemsJson.length();i++){
                //adds menu item to arraylist
                menuItemsAsList.add(new MenuItem(MenuItemsJson.getJSONObject(i).getString("Name"),MenuItemsJson.getJSONObject(i).getString("Section"),RequestsStat.JsonArraytoList("ingredients",MenuItemsJson.getJSONObject(i)),RequestsStat.JsonArraytoList("allergygroups",MenuItemsJson.getJSONObject(i)),MenuItemsJson.getJSONObject(i).getInt("id"),MenuItemsJson.getJSONObject(i).getString("price")));
            }
        }
        return menuItemsAsList;
    }
    public ArrayList<MenuItem> fliter(ArrayList<String> i,ArrayList<String> g,ArrayList<MenuItem> arrlist2){
        //filters the menu items removing items that contain any of String is in i or g
        ArrayList<MenuItem>flitered=new ArrayList<MenuItem>();
        //loops through each menu item
            for(MenuItem item: arrlist2){
                boolean contains =false;
                for(String ing:i){
                    //checks if contains ingredient
                    if(item.containsIng(ing)){
                        //if so sets contains true
                        contains=true;
                        break;
                    }
                }
                for(String group:g){
                    //checks if contains allergygroup
                    if(item.containsAllergen(group)){
                        contains=true;
                        break;
                    }
                }
                //if does not contain either i or g adds menu items to arraylist of filter menu items
                if(!contains)
                    flitered.add(item);
             }
        return flitered;
    }


}
