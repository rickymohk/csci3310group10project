package com.cse.csci3310.group10project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Stage0Activity extends AppCompatActivity {

    Button btnFound;
    int currentStage = 0;
    Intent caller;
    ImageView map,poi;
    boolean[] nearBeacon;
    Spinner beaconCheater;

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


        btnFound = (Button) findViewById(R.id.btnFound);
        btnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextStage = currentStage +1;
                if(nextStage<5)
                    goToStage(nextStage);
            }
        });




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
        stageClasses[2] = Stage2Activity.class;
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
