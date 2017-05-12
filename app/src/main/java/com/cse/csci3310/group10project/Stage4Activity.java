package com.cse.csci3310.group10project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Stage4Activity extends AppCompatActivity {

    private enum State {INIT,WIN,LOSE};
    Button btnAtk,btnItm,btnEsc;
    LinearLayout actionBar,gameoverDialog,itemBarLL;
    TextView messageBar, textGameover;
    HorizontalScrollView itemBar;
    RelativeLayout[] items;
    TextView[] itemCounters;
    Boss theBoss;
    Player thePlayer;
    State state = State.INIT;
    String msg;
    Intent caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage4);
        caller = getIntent();


        actionBar = (LinearLayout) findViewById(R.id.actionBar);
        itemBar = (HorizontalScrollView) findViewById(R.id.itemBar);
        itemBarLL = (LinearLayout) findViewById(R.id.itemBarLL);
        messageBar = (TextView) findViewById(R.id.messageBar);
        gameoverDialog = (LinearLayout) findViewById(R.id.gameoverDialog);
        textGameover = (TextView) findViewById(R.id.textGameover);
        btnAtk = (Button) findViewById(R.id.btnAtk);
        btnItm = (Button) findViewById(R.id.btnItm);
        btnEsc = (Button) findViewById(R.id.btnEsc);

        theBoss = new Boss(10,0,10);
        thePlayer = new Player(10,0,10);

        //Initialize item
        items = new RelativeLayout[10];
        itemCounters = new TextView[10];
        thePlayer.item[0] = 3;         //HP potion
        thePlayer.item[1] = 2;          //grenade

        RelativeLayout itemb = (RelativeLayout) findViewById(R.id.itemb);
      //  TextView itembCounter = (TextView) findViewById(R.id.itembCounter);



        for(int i = 0;i<thePlayer.item.length;i++)
        {
            if(thePlayer.item[i]>0)
            {

                RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                tlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                tlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                RelativeLayout item = new RelativeLayout(this);
                ViewGroup.LayoutParams rlp = itemb.getLayoutParams();
                item.setLayoutParams(rlp);
                item.setBackgroundResource(getResources().getIdentifier("item_"+i,"drawable",getPackageName()));
                items[i] = item;
                itemBarLL.addView(item,rlp);
                TextView counter = new TextView(this);
                counter.setText(getString(R.string.item_counter,thePlayer.item[i]));
                itemCounters[i] = counter;
                item.addView(counter,tlp);
                final int itemIndex = i;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(thePlayer.item[itemIndex]>0)
                        {
                            thePlayer.item[itemIndex]--;
                            thePlayer.usingItem = itemIndex;
                            //Log.d("debug","itemIndex="+itemIndex);
                            //Log.d("debug","thePlayer.item["+itemIndex+"]="+thePlayer.item[itemIndex]);
                            //Log.d("debug","counter get text="+itemCounters[itemIndex].getText().toString());
                            itemCounters[itemIndex].setText(getString(R.string.item_counter,thePlayer.item[itemIndex]));
                            if(thePlayer.item[itemIndex]==0)
                            {
                                items[itemIndex].setVisibility(View.GONE);
                            }
                            Thread battleThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    battle(Action.ITM);
                                }
                            });
                            battleThread.start();
                        }

                    }
                });

            }
        }
        switchBar(Bar.ACT);
        itemb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBar(Bar.ACT);
            }
        });

        gameoverDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stageClear();
            }
        });
        btnAtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread battleThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        battle(Action.ATK);
                    }
                });
                battleThread.start();

            }
        });
        btnItm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBar(Bar.ITM);
            }
        });
        btnEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stageBack();
            }
        });

    }

    private enum Action{ATK,ITM,ESC};
    private void battle(Action playerAct)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchBar(Bar.MSG);
            }
        });

        Fighter first,last;
        if(thePlayer.spd>theBoss.spd)
        {
            first = thePlayer;
            last = theBoss;
        }
        else if(theBoss.spd>thePlayer.spd)
        {
            first = theBoss;
            last = thePlayer;
        }
        else
        {
            double r = Math.random();
            if(r<0.5)
            {
                first = theBoss;
                last = thePlayer;
            }
            else
            {
                first = thePlayer;
                last = theBoss;
            }
        }
        if(playerAct == Action.ATK)
        {
            if(state==State.INIT)
                attack(first,last);
            if(state==State.INIT)
                attack(last,first);
        }
        else if(playerAct == Action.ITM)
        {
            if(state==State.INIT  && first == theBoss )
                attack(first,last);
            if(state==State.INIT)
            {
                switch(thePlayer.usingItem)
                {
                    case 0: //hp potion
                        writeMsg("You use a HP potion.");
                        battleWait(1000);
                        writeMsg("You regenerate 20 HP");
                        thePlayer.heal(20);
                        battleWait(1000);
                        break;
                    case 1: //grenade
                        writeMsg("You use a grenade.");
                        battleWait(1000);
                        int dmg = 20;
                        writeMsg(theBoss.name + " gets "+dmg+" points of damage.");
                        theBoss.damage(dmg);
                        battleWait(1000);
                        break;
                }
                thePlayer.usingItem = -1;
            }
            if(state==State.INIT && last == theBoss)
                attack(last,first);

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state==State.INIT)
                {
                    switchBar(Bar.ACT);
                }
                else if(state==State.WIN)
                {
                    switchBar(Bar.MSG);
                    gameoverDialog.setVisibility(View.VISIBLE);
                }
                else if(state==State.LOSE)
                {
                    switchBar(Bar.MSG);
                    textGameover.setText("You lose....");
                    gameoverDialog.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void battleWait(int n)
    {
        try{Thread.sleep(n);}catch (Exception e){Log.e("Exception",e.toString());}
    }

    private void attack(Fighter from, Fighter to)
    {
        String s1 = from==theBoss?"s":"";
        String s2 = from==theBoss?"":"s";
        writeMsg(from.name + " attack"+s1+ " "+to.name +".");
        battleWait(1000);
        double margin = 0.5;
        int dmg = (int)Math.round((from.atk * ((1+margin)-(2*margin)*Math.random()) - to.def));
        if(dmg<1) dmg=1;
        writeMsg(to.name + " get"+s2+" "+dmg+" points of damage.");
        to.damage(10);
        battleWait(1000);

        if(state==State.INIT && to.hp==0)
        {
            if(to==theBoss)
            {
                writeMsg(to.name + " is fainted.");
                state = State.WIN;
            }
            else
            {
                writeMsg("You are fainted.");
                state = State.LOSE;
            }
        }
    }

    private void writeMsg(String str)
    {
        msg = str;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageBar.setText(msg);
            }
        });
    }

    private enum Bar{ACT,ITM,MSG}
    private void switchBar(Bar barType)
    {
        actionBar.setVisibility(View.GONE);
        itemBar.setVisibility(View.GONE);
        messageBar.setVisibility(View.GONE);
        switch(barType)
        {
            case ACT:
                actionBar.setVisibility(View.VISIBLE);
                break;
            case ITM:
                itemBar.setVisibility(View.VISIBLE);
                break;
            case MSG:
                messageBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        stageBack();
    }

    private void stageBack()
    {
        Intent intent = new Intent(Stage4Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),4);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent;
        if(state==State.WIN)
        {
            intent = new Intent(Stage4Activity.this,EndActivity.class);
        }
        else
        {
            intent = new Intent(Stage4Activity.this,MainActivity.class);
        }
        intent.putExtra(getString(R.string.key_fromStage),4);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class Player extends Fighter
    {
        int[] item;
        int usingItem;

        Player(int a, int d, int s)
        {
            item = new int[10];
            usingItem = -1;
            name = "You";
            atk = a;
            def = d;
            spd = s;
            hp = 100;
            hpbar = (RelativeLayout) findViewById(R.id.playerHPbar);
            hpbarbg = (RelativeLayout) findViewById(R.id.playerHPbarbg);
            hpbarShape = (GradientDrawable)hpbar.getBackground();
            hpbarbg.post(new Runnable() {
                @Override
                public void run() {
                    updateHPbar();
                }
            });
        }

        void heal(int n)
        {
            hp += n;
            if(hp>100) hp = 100;
            updateHPbar();
        }

    }

    private class Boss extends Fighter
    {
        Boss(int a, int d, int s)
        {
            name = "Boss";
            atk = a;
            def = d;
            spd = s;
            hp = 100;
            hpbar = (RelativeLayout) findViewById(R.id.bossHPbar);
            hpbarbg = (RelativeLayout) findViewById(R.id.bossHPbarbg);
            hpbarShape = (GradientDrawable)hpbar.getBackground();
            hpbarbg.post(new Runnable() {
                @Override
                public void run() {
                    updateHPbar();
                }
            });

        }

    }

    private class Fighter
    {
        int hp,atk,def,spd;
        RelativeLayout hpbar;
        RelativeLayout hpbarbg;
        GradientDrawable hpbarShape;
        String name;

        void damage(int n)
        {
            hp -= n;
            if(hp<0) hp=0;
            updateHPbar();
            if(hp==0)
            {
                die();
            }
        }

         void updateHPbar()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int width = hpbarbg.getWidth();
                    //Log.d("debug","width="+width);
                    ViewGroup.LayoutParams lp = hpbar.getLayoutParams();
                    lp.width = width * hp/100;
                    hpbar.setLayoutParams(lp);
                    if(hp<=20)
                    {
                        hpbarShape.setColor(Color.RED);
                    }
                    else if(hp<=50)
                    {
                        hpbarShape.setColor(Color.YELLOW);
                    }
                    else
                    {
                        hpbarShape.setColor(Color.GREEN);
                    }
                }
            });

        }

        private void die()
        {

        }
    }

}
