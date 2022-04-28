package com.example.allergyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ingredient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ingredient extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ingredient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ingredient.
     */
    // TODO: Rename and change types and number of parameters
    public static ingredient newInstance(String param1, String param2) {
        ingredient fragment = new ingredient();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private AutoCompleteTextView Auto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }






    }
    //declaring varibles
    ArrayList<TextView> Text=new ArrayList<TextView>( );
    Button Add,binbtn,update;
    LinearLayout Lin;
    boolean bin=false;
    Intent intent;
    requests requests=new requests();
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        Auto= (AutoCompleteTextView)view.findViewById(R.id.ingredientsAuto);
        Text=((allergyinput) getActivity()).getStoreIngredientList();


         //set the ref for lin and add button
        Add=view.findViewById(R.id.add);
        Lin=view.findViewById(R.id.lin);

        //removes the parent of the Textview so that they can be reloaded on tab change
        if(Text!=null){
            Lin.removeView(view);
            for(final TextView i:Text) {
                ((ViewGroup)i.getParent()).removeView(i);
                i.setPadding(50,0,0,0);
                Lin.addView(i);
                //set new onclicklistener
                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if bin is selected
                        if(bin & Text!=null) {
                            //remove the Textview from activity and Arraylist
                            i.setVisibility(View.GONE);
                            Text.remove(i);
                            ((allergyinput) getActivity()).setIngredients(Text);
                        }
                    }
                });
            }
        }


        //set click event for adding a new ingredient
        View.OnClickListener AddToList = new View.OnClickListener() {
            public void onClick(View view) {
                //creates a new Textview
              TextView newIngredient=new TextView(getActivity());
              //checks if the ingredient to be add has no text or the linear layout already contains a textview with the text to be added
              if( !Auto.getText().toString().equals("") && !textviewcon(Text,Auto.getText().toString())){
                //sets up the textview settings
                newIngredient.setText(Auto.getText().toString());
                newIngredient.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                newIngredient.setTextSize(50);
                newIngredient.setTextColor(Color.parseColor("#707070"));
                newIngredient.setBackgroundResource(R.drawable.seachbar);
                newIngredient.setPadding(50,0,0,0);
                //adds the textview the arraylist
                Text.add(newIngredient);
                 //updates the user allergylist
                ((allergyinput) getActivity()).setIngredients(Text);
                //adds the textview to the linear layout lin
              Lin.addView( Text.get(Text.size() - 1));
              final TextView FIng=newIngredient;
              //sets on click event
                Text.get(Text.size() - 1).setOnClickListener(new View.OnClickListener() {
                  public void onClick(View view) {
                      //if bin is selected
                      if(bin & Text!=null) {
                          //remove the textview from linear layout and from arraylist
                          FIng.setVisibility(View.GONE);
                          Text.remove(FIng);
                          //set allergylist
                          ((allergyinput) getActivity()).setIngredients(Text);
                      }
                  }
              });}else{
                  Global.dialog(getView(),new String[]{"Already exists"},getContext(),"Error");
              }


            }
        };
        //set on click listener
        View.OnClickListener binclick = new View.OnClickListener() {
            public void onClick(View view) {
                //nots the bin current value
                bin=!bin;
                //set the bin background to show that the bin mode is active or not
                if(bin){
                    binbtn.setBackgroundResource(R.drawable.inuse);
                }else{
                    binbtn.setBackgroundResource(R.drawable.delete);
                }

            }
        };
        //updates the user allergy information
        View.OnClickListener update = new View.OnClickListener() {
                public void onClick(View view) {
                    ((allergyinput) getActivity()).setIngredients();
                    ((allergyinput) getActivity()).setAllergygroups();
                    try {
                        ((allergyinput) getActivity()).update();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
        //sets the on click listners
        Button updater=view.findViewById(R.id.update);
        binbtn=view.findViewById(R.id.bins);
        binbtn.setOnClickListener(binclick);
        ArrayList<String> ing=new ArrayList<String>();
        updater.setOnClickListener(update);
        Add.setOnClickListener(AddToList);
        return view;
    }
    //checks if string already is set to a textview in array
    public boolean textviewcon(ArrayList<TextView> t,String s){
        for(TextView b:t){
            if(b.getText().toString().equals(s)){
                return true;
            }

        }
       return false;
    }







}
