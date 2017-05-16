package com.cse.csci3310.group10project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Stage0Activity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences settings;

    Button btnFound;
    int currentStage = 0;
    Intent caller;
    ImageView map, poi, nearPoint1, nearPoint2, nearPoint3, nearPoint4, point1, point2, point3, point4;
    boolean[] nearBeacon;
    Spinner beaconCheater;

    MyReceiver myReceiver;

    String uuid1, uuid2, uuid3, uuid4;
    double distance1, distance2, distance3, distance4;
    int rssi1, rssi2, rssi3, rssi4;
    int minutes, seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage0);



        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

        caller = getIntent();
        int from =  settings.getInt(getString(R.string.key_fromStage),0);
        currentStage = settings.getInt(getString(R.string.key_currentStage),0);
        if(from>currentStage) currentStage = from;

        map = (ImageView) findViewById(R.id.imgMap);

        nearBeacon = new boolean[] {false,false,false,false};
        point1 = (ImageView) findViewById(R.id.point1);
        pointToStage(point1);
/*
        //================To be replaced by beacon logic================================//
        beaconCheater = (Spinner) findViewById(R.id.beaconCheater);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.beacons_array,android.R.layout.simple_spinner_dropdown_item);
        beaconCheater.setAdapter(adapter);
        beaconCheater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nearBeacon[0] = nearBeacon[1] = nearBeacon[2] = nearBeacon[3] = false;
                if(position>0)
                {
                    nearBeacon[position-1] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //=======================================================================//

*/


        btnFound = (Button) findViewById(R.id.btnFound);
        btnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextStage = currentStage +1;
                if(nextStage<5)
                    goToStage(nextStage);
            }
        });

        displayBeaconInfo();
        myReceiver = new MyReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(MyService.beacon);
        registerReceiver(myReceiver, intentfilter);

        startService();

  //      pointChecker();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            seconds = intent.getIntExtra("seconds", 0);
            minutes = intent.getIntExtra("minutes", 0);
            uuid1 = intent.getStringExtra("uuid1");
            distance1 = intent.getDoubleExtra("distance1", 0);
            rssi1 = intent.getIntExtra("rssi1", 0);
            uuid2 = intent.getStringExtra("uuid2");
            distance2 = intent.getDoubleExtra("distance2", 0);
            rssi2 = intent.getIntExtra("rssi2", 0);
            uuid3 = intent.getStringExtra("uuid3");
            distance3 = intent.getDoubleExtra("distance3", 0);
            rssi3 = intent.getIntExtra("rssi3", 0);
            uuid4 = intent.getStringExtra("uuid4");
            distance4 = intent.getDoubleExtra("distance4", 0);
            rssi4 = intent.getIntExtra("rssi4", 0);
        }
    }

    public void pointToStage(final View poi) {
        poi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                poi.setDrawingCacheEnabled(true);
                Bitmap hotspots = Bitmap.createBitmap(poi.getDrawingCache());
                poi.setDrawingCacheEnabled(false);
                int touchColor = hotspots.getPixel(x,y);
                if(touchColor== Color.RED && nearBeacon[0])
                {
                    if(currentStage>=0)
                        goToStage(1);
                }
                else if(touchColor==Color.GREEN && nearBeacon[1])
                {
                    if(currentStage>=1)
                        goToStage(2);
                }
                else if(touchColor==Color.BLUE && nearBeacon[2])
                {
                    if(currentStage>=2)
                        goToStage(3);
                }
                else if(touchColor==Color.YELLOW && nearBeacon[3])
                {
                    if(currentStage>=3)
                        goToStage(4);
                }

                return true;
            }
        });
    }

    public void displayBeaconInfo() {
        final TextView time = (TextView) findViewById(R.id.time);
        final TextView textBeacon1 = (TextView) findViewById(R.id.beacon1);
        final TextView textBeacon2 = (TextView) findViewById(R.id.beacon2);
        final TextView textBeacon3 = (TextView) findViewById(R.id.beacon3);
        final TextView textBeacon4 = (TextView) findViewById(R.id.beacon4);
        nearPoint1 = (ImageView) findViewById(R.id.nearPoint1);
        nearPoint2 = (ImageView) findViewById(R.id.nearPoint2);
        nearPoint3 = (ImageView) findViewById(R.id.nearPoint3);
        nearPoint4 = (ImageView) findViewById(R.id.nearPoint4);
        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        point4 = (ImageView) findViewById(R.id.point4);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                time.setText("Time: " + minutes + ":" + seconds );
                                textBeacon1.setText("58 - UUID: " + uuid1 + ", Distance: " + distance1 + ", RSSI: " + rssi1);
                                textBeacon2.setText("CC - UUID: " + uuid2 + ", Distance: " + distance2 + ", RSSI: " + rssi2);
                                textBeacon3.setText("iPad(E8) - UUID: " + uuid3 + ", Distance: " + distance3 + ", RSSI: " + rssi3);
                                textBeacon4.setText("Mobile - UUID: " + uuid4 + ", Distance: " + distance4 + ", RSSI: " + rssi4);

                                if((distance1 > 2.0 && distance1 >= 1.0) && currentStage == 0) {
                                    nearPoint1.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    nearPoint1.setVisibility(View.VISIBLE);
                                }

                                if(distance1 > 1.0 && currentStage == 0) {
                                    nearBeacon[0] = false;
                                    point1.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    nearBeacon[0] = true;
                                    point1.setVisibility(View.VISIBLE);
                                }

                                if((distance2 > 1.0 && currentStage == 1) || currentStage < 1) {
                                    nearBeacon[1] = false;
                                    point2.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    nearBeacon[1] = true;
                                    point1.setVisibility(View.VISIBLE);
                                }

                                if((distance3 > 1.0 && currentStage == 2) || currentStage < 2) {
                                    nearBeacon[2] = false;
                                    point3.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    nearBeacon[2] = true;
                                    point1.setVisibility(View.VISIBLE);
                                }

                                if((distance4 > 1.0 && currentStage == 3) || currentStage < 3) {
                                    nearBeacon[3] = false;
                                    point4.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    nearBeacon[3] = true;
                                    point4.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public void pointChecker() {
        Thread t = new Thread() {
            @Override
            public void run() {

                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                nearPoint1 = (ImageView) findViewById(R.id.nearPoint1);
                                nearPoint2 = (ImageView) findViewById(R.id.nearPoint2);
                                nearPoint3 = (ImageView) findViewById(R.id.nearPoint3);
                                nearPoint4 = (ImageView) findViewById(R.id.nearPoint4);
                                point1 = (ImageView) findViewById(R.id.point1);
                                point2 = (ImageView) findViewById(R.id.point2);
                                point3 = (ImageView) findViewById(R.id.point3);
                                point4 = (ImageView) findViewById(R.id.point4);

                                if(rssi1 > -75 && rssi1 < -1 && currentStage == 0) {
                                    nearBeacon[0] = true;
                                    point1.setVisibility(View.VISIBLE);
                                }

                                if(rssi2 > -50 && rssi2< -1 && currentStage >= 0) {
                                    nearBeacon[1] = true;
                                    point2.setVisibility(View.VISIBLE);
                                }

                                if(rssi3 > -50 && rssi3< -1 && currentStage >= 1) {
                                    nearBeacon[2] = true;
                                    point3.setVisibility(View.VISIBLE);
                                }

                                if(rssi4 > -50 && rssi4< -1 && currentStage >= 2) {
                                    nearBeacon[3] = true;
                                    point4.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        t.start();
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Quiting game")
                .setMessage("Are you sure you want to quit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        stopService();
                        Toast.makeText(Stage0Activity.this, "Bye!", Toast.LENGTH_SHORT).show();
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void goToStage(int n)
    {

        Log.d("Stage transit","Current stage:" + currentStage);
        Intent intent;
        Class[] stageClasses = new Class[6];
        stageClasses[0] = Stage0Activity.class;
        stageClasses[1] = Stage1Activity.class;
        stageClasses[2] = itemstore.class;
        stageClasses[3] = Stage3Activity.class;
        stageClasses[4] = Stage4Activity.class;
        intent = new Intent(Stage0Activity.this,stageClasses[n]);
        editor.putInt(getString(R.string.key_fromStage),0);
        editor.putInt(getString(R.string.key_currentStage),currentStage);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
