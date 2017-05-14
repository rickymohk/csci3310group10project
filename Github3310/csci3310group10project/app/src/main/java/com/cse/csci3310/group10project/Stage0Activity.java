package com.cse.csci3310.group10project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Stage0Activity extends AppCompatActivity {

    Button btnFound;
    int currentStage = 0;
    Intent caller;
    ImageView map,poi;
    boolean[] nearBeacon;
    Spinner beaconCheater;

    MyReceiver myReceiver;

    String uuid1, distance1;
    String uuid2, distance2;
    String uuid3, distance3;
    int rssi1, rssi2, rssi3;

    protected static final String TAG = "BeaconsEverywhere";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage0);

        caller = getIntent();
        if(caller!=null)

        {
            int from =  caller.getIntExtra(getString(R.string.key_fromStage),0);
            currentStage = caller.getIntExtra(getString(R.string.key_currentStage),0);
            if(from>currentStage) currentStage = from;
        }

        nearBeacon = new boolean[] {false,false,false,false};

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


        map = (ImageView) findViewById(R.id.imgMap);
        poi = (ImageView) findViewById(R.id.imgPOI);
        poi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int x = (int)event.getX();
                int y = (int)event.getY();
                poi.setDrawingCacheEnabled(true);
                Bitmap hotspots = Bitmap.createBitmap(poi.getDrawingCache());
                poi.setDrawingCacheEnabled(false);
                int touchColor = hotspots.getPixel(x,y);
                if(touchColor== Color.RED)
                {
                    //if(currentStage>=0)
                        goToStage(1);
                }
                else if(touchColor==Color.GREEN)
                {
                    //if(currentStage>=1)
                        goToStage(2);
                }
                else if(touchColor==Color.BLUE)
                {
                    //if(currentStage>=2)
                        goToStage(3);
                }
                else if(touchColor==Color.YELLOW )
                {
                    //if(currentStage>=3)
                        goToStage(4);
                }

                return true;
            }
        });


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
            uuid1 = intent.getStringExtra("uuid1");
            distance1 = intent.getStringExtra("distance1");
            rssi1 = intent.getIntExtra("rssi1", 0);
            uuid2 = intent.getStringExtra("uuid2");
            distance2 = intent.getStringExtra("distance2");
            rssi2 = intent.getIntExtra("rssi2", 0);
            uuid3 = intent.getStringExtra("uuid3");
            distance3 = intent.getStringExtra("distance3");
            rssi3 = intent.getIntExtra("rssi3", 0);
        }
    }

    public void displayBeaconInfo() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textBeacon1 = (TextView) findViewById(R.id.beacon1);
                                TextView textBeacon2 = (TextView) findViewById(R.id.beacon2);
                                TextView textBeacon3 = (TextView) findViewById(R.id.beacon3);
                                textBeacon1.setText("58 - UUID: " + uuid1 + ", Distance: " + distance1 + ", RSSI: " + rssi1);
                                textBeacon2.setText("CC - UUID: " + uuid2 + ", Distance: " + distance2 + ", RSSI: " + rssi2);
                                textBeacon3.setText("iPad(E8) - UUID: " + uuid3 + ", Distance: " + distance3 + ", RSSI: " + rssi3);
                            }
                        });
                    }
                } catch (InterruptedException e) {
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
        intent.putExtra(getString(R.string.key_fromStage),0);
        intent.putExtra(getString(R.string.key_currentStage),currentStage);
        intent.putExtra(getString(R.string.key_cash),caller.getIntExtra(getString(R.string.key_cash),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
