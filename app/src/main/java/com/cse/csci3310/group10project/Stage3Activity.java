package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Stage3Activity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences settings;
    Intent caller;

    ImageView blackCir, redCir, blueCir, greenCir, img, imgMask, img1, imgMask1;

    Boolean[] fourCir = new Boolean[]{false, false, false, false};

    LinearLayout lLayout, lLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3);
        caller = getIntent();
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

        lLayout = (LinearLayout)findViewById(R.id.stage3startpage);
        lLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                lLayout.setVisibility(View.GONE);

                fourCir = new Boolean[]{false, false, false, false};

                img = (ImageView)findViewById(R.id.FindDiff);
                imgMask = (ImageView)findViewById(R.id.FindDiffMask);
                blackCir = (ImageView)findViewById(R.id.BlackCircle);
                redCir = (ImageView)findViewById(R.id.RedCircle);
                blueCir = (ImageView)findViewById(R.id.BlueCircle);
                greenCir = (ImageView)findViewById(R.id.GreenCircle);

                imgMask.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        imgMask.setDrawingCacheEnabled(true);
                        Bitmap hotspots = Bitmap.createBitmap(imgMask.getDrawingCache());
                        imgMask.setDrawingCacheEnabled(false);
                        int touchColor = hotspots.getPixel(x,y);
                        if(touchColor== Color.RED && redCir.getVisibility() == View.INVISIBLE)
                        {
                            redCir.setVisibility(View.VISIBLE);
                            fourCir[0] = true;
                        }
                        else if(touchColor==Color.GREEN && greenCir.getVisibility() == View.INVISIBLE)
                        {
                            greenCir.setVisibility(View.VISIBLE);
                            fourCir[1] = true;
                        }
                        else if(touchColor==Color.BLUE && blueCir.getVisibility() == View.INVISIBLE)
                        {
                            blueCir.setVisibility(View.VISIBLE);
                            fourCir[2] = true;
                        }
                        else if(touchColor==Color.BLACK && blackCir.getVisibility() == View.INVISIBLE)
                        {
                            blackCir.setVisibility(View.VISIBLE);
                            fourCir[3] = true;
                        }

                        if(fourCir[0] && fourCir[1] && fourCir[2] && fourCir[3]) {
                            stageClear();


                        }

                        return true;
                    }
                });
            }
        });



    }

    @Override
    public void onBackPressed()
    {
        stageBack();
    }

    private void stageBack()
    {
        Intent intent = new Intent(Stage3Activity.this,Stage0Activity.class);
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage3Activity.this,Stage3FindKeyActivity.class);
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
