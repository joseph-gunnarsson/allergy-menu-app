package com.example.allergyapp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import android.os.Build;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;


public class requests {
    public boolean x=false;
    public requests() {

    }


    public JSONObject get(final String urlstring) throws Exception {
        final String[] x=new String[1];;
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(urlstring);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    x[0] = result.toString();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while(x[0]==null){
            Log.d("asd","x[0]");
        }

        JSONObject jsonObject;
        jsonObject = new JSONObject(x[0]);

        return jsonObject;
    }
     //methods preforms post request, data is the data to be sent to web service and urlstring is the url to send request to.
    public JSONObject post(final JSONObject data, final String urlstring) throws JSONException {
        final String[] x = new String[1];
        x[0]=null;
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    //the url to read from
                    URL url = new URL(urlstring);
                    //opens a connection to the designated url
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //decalares request type
                    connection.setRequestMethod("POST");
                    //declares the type of datd being sent
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setRequestProperty("Accept","application/json");
                    //declares sending output data
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    //add data in the jsonobject in string format to outPutstream
                    outputStream.writeBytes(data.toString());
                    //closes the output stream
                    outputStream.flush();
                    outputStream.close();
                    //gets the response data
                    try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        //reads each lines of response and adds to StringBuilder
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        x[0] = response.toString();
                    }
                    //stop connection
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Starts thread
        thread.start();
        //waits until response data has been recived
        while(x[0]==null){
            Log.d("Message","Fetching data");
        }
            JSONObject jsonObject;
        //parse json string to json object
            jsonObject = new JSONObject(x[0]);
        return jsonObject;
    }


    public ArrayList<String> JsonArraytoList(String name, JSONObject info) throws JSONException {
        ArrayList<String> AsString=new ArrayList<String>();
        JSONArray DataArray=info.getJSONArray(name);
        if (DataArray != null) {
            for (int i=0;i<DataArray.length();i++){
                AsString.add(DataArray.getString(i));
            }
        }
        return AsString;
    }


}
