package com.example.allergyapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
@SuppressWarnings("serial")
public class User implements Serializable {
    private ArrayList<String> AllergyList = new ArrayList<String>();
    private ArrayList<String> AllergyGroup = new ArrayList<String>();
    private String email;
    private String name;
    int id;
    public User(){

    }
    public User(int id,String email,String name) throws JSONException {
        requests Requests=new requests();
        //sets varbiles
      this.email=email;
      this.id=id;
      this.name=name;
          //creates a json varible for the post request that contains the users id
          JSONObject userid = new JSONObject();
          userid.put("user_id",id);
          //makes a post request to the API to retrive the users allergy information
          JSONObject Data=Requests.post(userid,"https://www.doc.gold.ac.uk/usr/391/GetAll/");
          //Coverts the json array to an arraylist using and sets allergy information
          AllergyList=Requests.JsonArraytoList("Allergylist",Data);
          AllergyGroup=Requests.JsonArraytoList("AllergyGroup",Data);
    }
    //getter and setters
    public String getEmail() {
        return email;
    }
    public ArrayList<String> getAllergyGroup() {
        return AllergyGroup;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<String> getAllergyList() {
        return AllergyList;
    }
    public ArrayList GetAllergyList(){
        return AllergyList;
    }
    public ArrayList GetAllergyGroups(){
        return AllergyGroup;
    }
    //updates Allergy information in datebase
    public void UpdateAllergy() throws JSONException {
       //creates request object
        requests Requests=new requests();
        //creates new json object
        JSONObject newAllergylist=new JSONObject();
        //adds the user id to object
        newAllergylist.put("User_id",id);
        //adds allergylist to object
        newAllergylist.put("AllergyList",new JSONArray(AllergyList));
        //adds table to object which decides which table should be updated
        newAllergylist.put("Table","AllergyIngredientsList");
        //makes request to web servver
        Requests.post(newAllergylist,"https://www.doc.gold.ac.uk/usr/391/update/");
        //repeats process for allergy groups
        JSONObject newAllergyGroup=new JSONObject();
        newAllergyGroup.put("AllergyList",new JSONArray(AllergyGroup));
        newAllergyGroup.put("User_id",id);
        newAllergyGroup.put("Table","AllergyGroupList");
        Requests.post(newAllergyGroup,"https://www.doc.gold.ac.uk/usr/391/update/");
    }
    public void setAllergyGroup(ArrayList<String> A)  {
        AllergyGroup=A;
    }
    public void setAllergyList(ArrayList<String> A)  {
        AllergyList=A;
    }

}
