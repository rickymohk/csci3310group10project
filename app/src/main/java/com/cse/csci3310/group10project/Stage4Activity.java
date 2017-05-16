package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Stage4Activity extends AppCompatActivity {

    private enum State {INIT,WIN,LOSE};
    SharedPreferences.Editor editor;
    SharedPreferences settings;
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
    int[] slotMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage4);
        caller = getIntent();

        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

        if(settings.getBoolean("WIN",false) || settings.getBoolean("LOSE",false))
        {
            stageClear();
        }
        actionBar = (LinearLayout) findViewById(R.id.actionBar);
        itemBar = (HorizontalScrollView) findViewById(R.id.itemBar);
        itemBarLL = (LinearLayout) findViewById(R.id.itemBarLL);
        messageBar = (TextView) findViewById(R.id.messageBar);
        gameoverDialog = (LinearLayout) findViewById(R.id.gameoverDialog);
        textGameover = (TextView) findViewById(R.id.textGameover);
        LinearLayout startDialog = (LinearLayout) findViewById(R.id.startDialog);
        btnAtk = (Button) findViewById(R.id.btnAtk);
        btnItm = (Button) findViewById(R.id.btnItm);
        btnEsc = (Button) findViewById(R.id.btnEsc);

        theBoss = new Boss(200,20,0,1);
        //Equipment
        thePlayer = new Player(100+settings.getInt("HP",0),10+settings.getInt("ATK",0),0+settings.getInt("DFS",0),settings.getInt("SPD",0));
        for(int i=0;i<4;i++)
        {
            int slotID = settings.getInt("ESLOT"+i,0);
            if(slotID>0)
            {
                String[] eqName = new String[]{"sword","helmet","armor","boots"};
                ImageView imgv = (ImageView) findViewById(getResources().getIdentifier("eq"+i+"ImageView","id",getPackageName()));
                imgv.setImageResource(getResources().getIdentifier(eqName[(slotID-1)/3]+(slotID-1)%3,"drawable",getPackageName()));
            }
        }

        //Initialize item
        items = new RelativeLayout[10];
        itemCounters = new TextView[10];
        slotMap = new int[10];
        for(int i=0;i<3;i++)
        {
            int slotQuan = settings.getInt("SLOT"+i+"NO", 0);
            int slotID = settings.getInt("SLOT"+i, 0);
            if(slotID>0)
            {
                thePlayer.item[slotID-1] = slotQuan;
                slotMap[slotID-1] = i;
            }
        }

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
                Log.d("debug","item_"+i);
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
        switchBar(Bar.MSG);
        itemb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBar(Bar.ACT);
            }
        });

        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                Thread startThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeMsg(thePlayer.name + " VS " + theBoss.name +".");
                        battleWait(1000);
                        writeMsg("Battle Start!");
                        battleWait(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchBar(Bar.ACT);
                            }
                        });

                    }
                });
                startThread.start();
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

        //Attack order decision
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

        //Player action execution
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
                    case Item.POTION: //hp potion
                        writeMsg("You use a HP potion.");
                        battleWait(1000);
                        writeMsg("You regenerate 50 HP");
                        thePlayer.heal(50);
                        battleWait(1000);
                        break;
                    case Item.GRENADE: //grenade
                        writeMsg("You use a grenade.");
                        battleWait(1000);
                        int dmg = 30;
                        writeMsg(theBoss.name + " gets "+dmg+" points of damage.");
                        theBoss.damage(dmg);
                        battleWait(1000);
                        break;
                    case Item.POISON:
                        writeMsg("You use poison.");
                        battleWait(1000);
                        if(theBoss.debuf==0)
                        {
                            writeMsg(theBoss.name + " get poisoned");
                            theBoss.debuf = 1;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView bossDebufText = (TextView) findViewById(R.id.bossDebuff);
                                    bossDebufText.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else if(theBoss.debuf==1)
                        {
                            writeMsg(theBoss.name + " is already poisoned");
                        }
                        battleWait(1000);
                        break;
                }
                editor.putInt("SLOT"+slotMap[thePlayer.usingItem]+"NO",thePlayer.item[thePlayer.usingItem]);
                if(thePlayer.item[thePlayer.usingItem]==0)
                    editor.putInt("SLOT"+slotMap[thePlayer.usingItem],0);
                editor.commit();
                thePlayer.usingItem = -1;

            }
            if(state==State.INIT && last == theBoss)
                attack(last,first);

        }

        //Debuff execution
        if(state==State.INIT && theBoss.debuf==1)        //poisoned
        {
            writeMsg(theBoss.name + " affected by poison.");
            battleWait(1000);
            int dmg = 10;
            writeMsg(theBoss.name + " gets "+dmg+" points of damage.");
            theBoss.damage(dmg);
            battleWait(1000);
        }

        //Battle result check
        if(state == State.WIN)
        {
            writeMsg(theBoss.name + " is fainted.");
        }
        else if(state == State.LOSE)
        {
            writeMsg("You are fainted.");
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
                    editor.putBoolean("WIN",true);
                    editor.commit();
                }
                else if(state==State.LOSE)
                {
                    switchBar(Bar.MSG);
                    textGameover.setText("You lose....");
                    gameoverDialog.setVisibility(View.VISIBLE);
                    editor.putBoolean("LOSE",true);
                    editor.commit();
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
        int dmg = (int)Math.round((from.atk-to.def)*(1-0.15*Math.random()));
        if(dmg<1) dmg=1;
        writeMsg(to.name + " get"+s2+" "+dmg+" points of damage.");
        to.damage(10);
        battleWait(1000);
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

    private static class Item
    {
        static final int POTION = 0, GRENADE = 1, POISON = 2;

    }

    @Override
    public void onBackPressed()
    {
        stageBack();
    }

    private void stageBack()
    {
        Intent intent = new Intent(Stage4Activity.this,Stage0Activity.class);
        editor.putInt(getString(R.string.key_fromStage),4);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent;
        intent = new Intent(Stage4Activity.this,EndActivity.class);
        editor.putInt(getString(R.string.key_fromStage),4);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class Player extends Fighter
    {
        int[] item;
        int usingItem;

        Player(int h, int a, int d, int s)
        {
            item = new int[10];
            usingItem = -1;
            name = "You";
            atk = a;
            def = d;
            spd = s;
            hp = maxHp = h;
            debuf = 0;
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
            if(hp>maxHp) hp = maxHp;
            updateHPbar();
        }

        @Override
        protected void die()
        {
            state = State.LOSE;
        }

    }

    private class Boss extends Fighter
    {
        Boss(int h, int a, int d, int s)
        {
            name = "Boss";
            atk = a;
            def = d;
            spd = s;
            hp = maxHp = h;
            debuf = 0;
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

        @Override
        protected void die()
        {
            state = State.WIN;
        }

    }

    private class Fighter
    {

        int maxHp,hp,atk,def,spd,debuf;
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
                    lp.width = width * hp/maxHp;
                    hpbar.setLayoutParams(lp);
                    if(hp<=maxHp*0.2)
                    {
                        hpbarShape.setColor(Color.RED);
                    }
                    else if(hp<=maxHp*0.5)
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


        protected void die()
        {
            return;
        }
    }

}
