package com.cse.csci3310.group10project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class Stage1Activity extends AppCompatActivity {
    Intent caller;
    RelativeLayout bucket,main,dropDummy;
    float mainHeight,mainWidth;
    TextView timerText;
    float dX;
    Thread dispenserThread,timerThread,loopThread;
    int countDown;
    ArrayList<Dropping> droppings,dropsToBeAdd;
    boolean pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage1);
        caller = getIntent();

        pause = false;
        countDown = 60;
        droppings = new ArrayList<Dropping>();
        dropsToBeAdd = new ArrayList<Dropping>();


        timerText = (TextView) findViewById(R.id.timerText);
        bucket = (RelativeLayout) findViewById(R.id.bucket);
        main = (RelativeLayout) findViewById(R.id.main);
        dropDummy = (RelativeLayout) findViewById(R.id.dropDummy);

        dispenserThread = new Thread(dispenser);
        timerThread = new Thread(timer);
        loopThread = new Thread(gameLoop);

        dispenserThread.start();
        timerThread.start();
        loopThread.start();

        main.post(new Runnable() {
            @Override
            public void run() {
                mainHeight = main.getHeight();
                mainWidth = main.getWidth();
            }
        });


        bucket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View parent = (View)v.getParent();
                double width = parent.getWidth()-v.getWidth();

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if((event.getRawX() + dX)>0 && (event.getRawX() + dX)<width)
                        {
                            v.animate().x(event.getRawX() + dX).setDuration(0).start();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    Runnable timer = new Runnable() {
        @Override
        public void run() {
            while(countDown>0 && !pause)
            {
                threadWait(1000);
                countDown--;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerText.setText(Integer.toString(countDown));
                    }
                });
            }
            timesup();
        }
    };

    Runnable dispenser = new Runnable() {
        @Override
        public void run() {
            while(countDown>0 && !pause)
            {
                threadWait(700+(int)Math.random()*1300);
                double x = Math.random();
                int type;
                if(x<0.5)
                {
                   type = DropType.COIN;
                }
                else if(x<0.7)
                {
                    type = DropType.GOLD;
                }
                else
                {
                    type = DropType.BOMB;
                }
                Dropping drop = new Dropping(type);
                dropsToBeAdd.add(drop);
            }

        }
    };

    Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            int fps = 60;
            int dtime = 1000/fps;

            while(countDown>0 && !pause)
            {
                if(!dropsToBeAdd.isEmpty())
                {
                    droppings.addAll(dropsToBeAdd);
                    dropsToBeAdd.clear();
                }
                threadWait(dtime);
                Iterator<Dropping> iter = droppings.iterator();
                while(iter.hasNext())
                {
                    Dropping d = iter.next();
                    d.update();
                    if(d.y>mainHeight)
                    {
                        final RelativeLayout rl = d.view;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                main.removeView(rl);
                            }
                        });
                       iter.remove();
                    }
                    else
                    {
                        d.render();
                    }
                }
            }


        }
    };

    private void timesup()
    {

    }

    private void threadWait(int n)
    {
        try{Thread.sleep(n);}catch (Exception e){Log.e("Exception",e.toString());}
    }

    @Override
    public void onPause()
    {
        super.onPause();
        pause = true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        pause = false;
    }

    @Override
    public void onBackPressed()
    {
        stageBack();
    }

    private void stageBack()
    {
        Intent intent = new Intent(Stage1Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),1);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage1Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),1);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private static class DropType
    {
        static int COIN=0,GOLD=1,BOMB=2;
    }

    private class Dropping
    {

        int type;
        float x,y,dx,dy;
        RelativeLayout view;

        Dropping(int t)
        {
            type=t;
            x=0;
            y=0;
            dx=0;
            dy=(float)(7+5*Math.random());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    view = new RelativeLayout(Stage1Activity.this);
                    ViewGroup.LayoutParams lp =  dropDummy.getLayoutParams();
                    view.setLayoutParams(lp);
                    view.setVisibility(View.VISIBLE);
                    view.setBackgroundResource(type==0?R.drawable.coin:(type==1?R.drawable.gold:R.drawable.bomb));
                    main.addView(view);
                    x = (float)Math.random()*(mainWidth-lp.width);
                    view.setX(x);
                }
            });

        }

        void update()
        {
            x += dx;
            y += dy;
        }

        void render()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setX(x);
                    view.setY(y);
                }
            });

        }

    }

}

