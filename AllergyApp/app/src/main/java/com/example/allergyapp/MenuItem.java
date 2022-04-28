package com.example.allergyapp;

import java.util.ArrayList;

public class MenuItem {
    public String Name,section;
    public ArrayList<String> ingredients,allergygroups;
    public int id;
    public String price;
    public MenuItem(String Name,String section,ArrayList<String> ingredients,ArrayList<String> allergygroups,int id,String price){
        //set all the varibles
        this.Name=Name;
        this.section=section;
        this.ingredients=ingredients;
        this.allergygroups=allergygroups;
        this.id=id;
        this.price=price;
    }
    //getters and setter methods
    public String getSection() {
        return section;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getAllergygroups() {
        return allergygroups;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return Name;
    }
     //checks if allergygroups contains certain allergen
    public boolean containsAllergen(String allergen){
        if(allergygroups.contains(allergen))
            return true;
        return false;
    }
    //checks if ingredients contains certain ingredient
    public boolean containsIng(String Ing){
        if(ingredients.contains(Ing))
            return true;
        return false;
    }


}
