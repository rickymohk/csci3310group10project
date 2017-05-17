package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Stage3FindKeyActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    Intent caller;

    ImageView img, mask, yellowCir;
    LinearLayout ll, ll1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3_find_key);
        caller = getIntent();
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

        img = (ImageView)findViewById(R.id.findkeyimg);
        mask = (ImageView)findViewById(R.id.findkeymask);
        yellowCir = (ImageView)findViewById(R.id.yellowCircle);

        ll = (LinearLayout)findViewById(R.id.Message) ;
        ll1 = (LinearLayout)findViewById(R.id.Message1);

        ll.setVisibility(View.VISIBLE);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);

                mask.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        mask.setDrawingCacheEnabled(true);
                        Bitmap maskpt = Bitmap.createBitmap(mask.getDrawingCache());
                        mask.setDrawingCacheEnabled(false);
                        int touchColor = maskpt.getPixel(x,y);
                        if(touchColor == Color.BLUE){
                            ll1.setVisibility(View.VISIBLE);

                            ll1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    stageClear();
                                }
                            });

                        }

                        return false;
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
        Intent intent = new Intent(Stage3FindKeyActivity.this,Stage3Activity.class);
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage3FindKeyActivity.this,Stage0Activity.class);
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
