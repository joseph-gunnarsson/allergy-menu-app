package com.example.allergyapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class qrscan extends setup {
    //declares varibles
    private SurfaceView cambox;
    private CameraSource cam;
    private BarcodeDetector barcode;
    private TextView text;
    private Intent intent;
    private User user;
    public View view;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_qrscan);
        view=this.getWindow().getDecorView().findViewById(android.R.id.content);
        cambox=(SurfaceView)findViewById(R.id.Qrscanbox);
        barcode = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cam= new CameraSource.Builder(this,barcode).setRequestedPreviewSize(1280,720).build();
        nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Allergyinfo);

        user = (User) getIntent().getSerializableExtra("user");
        text=(TextView)findViewById(R.id.tablayout);
        //sets up nav bar button clicks
        nav.setOnNavigationItemSelectedListener((bn(user)));
        //sets up cambox surfaceview to show preview of the smartphones camera feed
        cambox.getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //chcecks if the users has granted that application camera permission
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //if not displays a pop up that ask the user to enable the camera permission in settiongs
                    String[] one=new String[1];
                    one[0]="Grant cam permission in settings";
                    AlertDialog a=Global.dialog(view,one,qrscan.this,"Invalid");
                    a.show();
                    return;
                }
                try{
                    cam.start(holder);
                }catch(IOException e){
                    //if another error occour display an error occoured
                    String[] one=new String[1];
                    requests r=new requests();
                    one[0]="Some sort of error";
                    AlertDialog a=Global.dialog(view,one,qrscan.this,"Invalid");
                    a.show();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
              cam.stop();
            }});
            barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            //method dectects if barcode scanned
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                //array to store barcodes scanned
                final SparseArray<Barcode> qrcode=detections.getDetectedItems();
                //if qrcode scanned the size of the qrcode array will no longer be 0.
                if(qrcode.size()!=0){
                    text.post(new Runnable(){
                        @Override
                        public void run() {
                            //gets the qrcode scanned
                            Barcode qr = qrcode.get(0);
                            requests r=new requests();
                            String[] one=new String[1];
                            JSONObject data=new JSONObject();
                            try {
                                //does a post request to check if the qr scanned is valid and has resturant linked with the value
                                //decodes the qrcode
                                data.put("id",(qrcode.valueAt(0).displayValue));
                                //makes post request that returns a message to tell if qr is valid
                                data=r.post(data,"https://www.doc.gold.ac.uk/usr/391/checkrest/");
                                String message=data.getString("message");
                                //if qr code is valid
                                if(message.equals("true")){
                                    //stops that camera and starts the new activtiy
                                    intent = new Intent(qrscan.this, menu.class);
                                    intent.putExtra("user",user);
                                    cam.stop();
                                    intent.putExtra("restid",qrcode.valueAt(0).displayValue);
                                    startActivity(intent);
                                }else{
                                    //else displays popup message
                                    one[0]="Resturant does not exist";
                                    AlertDialog a=Global.dialog(view,one,qrscan.this,"Invalid");
                                    a.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            }
        });

    }
}
