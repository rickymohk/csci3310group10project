package com.cse.csci3310.group10project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Stage1Activity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    Intent caller;
    RelativeLayout bucket,main,dropDummy;
    float mainHeight,mainWidth;
    TextView timerText,cashText,hintText;
    float dX;
    float bucketX,bucketY;
    int bucketW,bucketH;
    Thread dispenserThread,timerThread,loopThread;
    int countDown,cash;
    ArrayList<Dropping> droppings,dropsToBeAdd;
    boolean pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage1);
        caller = getIntent();
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

        pause = true;
        countDown = 60;
        cash = settings.getInt("MONEY",0);
        droppings = new ArrayList<Dropping>();
        dropsToBeAdd = new ArrayList<Dropping>();

        hintText = (TextView) findViewById(R.id.hintText);
        timerText = (TextView) findViewById(R.id.timerText);
        cashText = (TextView) findViewById(R.id.cashText);
        bucket = (RelativeLayout) findViewById(R.id.bucket);
        main = (RelativeLayout) findViewById(R.id.main);
        dropDummy = (RelativeLayout) findViewById(R.id.dropDummy);
        LinearLayout startDialog = (LinearLayout) findViewById(R.id.startDialog);
        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                dispenserThread = new Thread(dispenser);
                timerThread = new Thread(timer);
                loopThread = new Thread(gameLoop);
                dispenserThread.start();
                timerThread.start();
                loopThread.start();
            }
        });

        cashText.setText(getString(R.string.cash_counter,cash));





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
                threadWait(300+(int)(Math.random()*1200));
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
                bucketX = bucket.getX();
                bucketY = bucket.getY();
                bucketW = bucket.getWidth();
                bucketH = bucket.getHeight();

                Iterator<Dropping> iter = droppings.iterator();
                while(iter.hasNext())
                {
                    Dropping d = iter.next();
                    d.update();
                    if(d.x>bucketX && d.y>bucketY && d.x+d.view.getWidth()<bucketX+bucketW && d.y+d.view.getHeight()<bucketY+bucketH)
                    {
                        //get
                        int amount = d.type== DropType.COIN?10:(d.type==DropType.GOLD?50:-20);
                        if(cash+amount<0) amount = -cash;
                        final int value = amount;
                        cash += amount;

                        final RelativeLayout rl = d.view;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                main.removeView(rl);
                                cashText.setText(getString(R.string.cash_counter,cash));
                                final TextView ht = new TextView(Stage1Activity.this);
                                ht.setVisibility(View.INVISIBLE);
                                ht.setText((value>0?"+":"")+Integer.toString(value));
                                ht.setTextSize(25);
                                ht.setTextColor(value>0? Color.YELLOW:Color.RED);
                                ht.setShadowLayer((float)1.6,(float)1.5,(float)1.3,Color.BLACK);
                                main.addView(ht);
                                ht.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ht.setX(bucketX+bucketW/2-ht.getWidth()/2);
                                        ht.setY(bucketY-ht.getHeight());
                                        ht.setVisibility(View.VISIBLE);
                                        ht.animate().y(ht.getY()-150).setDuration(1000).start();
                                    }
                                });


                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                main.removeView(ht);
                                            }
                                        });

                                    }
                                },1000);
                            }
                        });
                        iter.remove();

                    }
                    else if(d.y>mainHeight)
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

        while(!droppings.isEmpty())
        {
            final Iterator<Dropping> iter =  droppings.iterator();
            while(iter.hasNext())
            {
                Dropping d = iter.next();
                final RelativeLayout rl = d.view;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main.removeView(rl);
                    }
                });
                iter.remove();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerText.setText("Time's up");
                LinearLayout endDialog = (LinearLayout) findViewById(R.id.endDialog);
                endDialog.setVisibility(View.VISIBLE);
                TextView endText = (TextView) findViewById(R.id.endText);
                endText.setText(getString(R.string.stage_1_end,cash));
                endDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stageClear();
                    }
                });
            }
        });

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
        editor.putInt(getString(R.string.key_fromStage),1);
        editor.putInt("MONEY",settings.getInt("MONEY",0));
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage1Activity.this,Stage0Activity.class);
        editor.putInt(getString(R.string.key_fromStage),1);
        editor.putInt("MONEY",cash);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private static class DropType
    {
        static final int COIN=0,GOLD=1,BOMB=2;
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
                    view.setBackgroundResource(type==0?R.drawable.coin:(type==1?R.drawable.gold:R.drawable.bomb));
                    x = (float)Math.random()*(mainWidth-lp.width);
                    y = -lp.height;
                    view.setX(x);
                    view.setY(y);
                    view.setVisibility(View.VISIBLE);
                    main.addView(view);
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

