package com.example.allergyapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link allergygroups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class allergygroups extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public allergygroups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment allergygroups.
     */
    // TODO: Rename and change types and number of parameters
    public static allergygroups newInstance(String param1, String param2) {
        allergygroups fragment = new allergygroups();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //declares varibles
    LinearLayout linear;
    Button upadatebtn;
    ArrayList<String> store=new ArrayList<String>();
    String[] allergns = {"Celery","Gluten","Crustaceans","Eggs","Fish","Peanuts","Sesame","Lupin","Milk","Molluscs","Mustard","Nuts","Soya","Sulphur dioxide"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_allergygroups, container, false);
        //sets linearlayout ref
        linear=view.findViewById(R.id.lin2);
        //sets update button ref
        upadatebtn=view.findViewById(R.id.update);
        //gets the users allergy groups by calling the method in the activity
         store=((allergyinput) getActivity()).getStoreAllergyGroups();
         //creates checkbox for each allergen
        for(final String allergn:allergns){
            //declares new checkbox
            final CheckBox check= new CheckBox(getActivity());
            //sets text to allergen name
            check.setText(allergn);
            //sets font style
            check.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            //sets text size
            check.setTextSize(50);
            //sets text colour
            check.setTextColor(Color.parseColor("#707070"));
            //sets backgroud image
            check.setBackgroundResource(R.drawable.seachbar);
            //sets tag
            check.setTag(allergn);
            check.setPadding(50,0,0,0);
            check.setHighlightColor(Color.parseColor("#707070"));
            //adds to linear layout
            linear.addView(check);
            //if the allergen is contained in the users allergy groups checks the box
            if(store.contains(allergn))
                check.setChecked(true);
             //set click event
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if checkbox gets checked
                    if(check.isChecked()){
                        //adds the allergn to users allergygroups
                        store.add(allergn);
                        ((allergyinput) getActivity()).setStoreAllergyGroups(store);
                    }else{
                        //else if the boxes is not check and store contains the allergen
                        if(store.contains(allergn)){
                            //removes allergen from the allergy list
                            store.remove(allergn);
                            ((allergyinput) getActivity()).setStoreAllergyGroups(store);
                        }
                    }
                }
            });


        }


        //set onclick event for update button
        View.OnClickListener update = new View.OnClickListener() {
            public void onClick(View view) {
                //sets the users allergylist and allergy groups
                ((allergyinput) getActivity()).setIngredients();
                ((allergyinput) getActivity()).setAllergygroups();
                //calls method in activity to update the users allergy information
                try {
                    ((allergyinput) getActivity()).update();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        Bundle bundle=new Bundle();
        upadatebtn.setOnClickListener(update);
        return view;
    }
}
