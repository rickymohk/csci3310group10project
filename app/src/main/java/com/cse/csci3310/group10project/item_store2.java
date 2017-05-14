package com.cse.csci3310.group10project;


import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class item_store2 extends Fragment {
    SharedPreferences.Editor editor;
    SharedPreferences settings;


    //data
    int money;
    int[] stat = new int [4];
    int[] slotID = new int[4];

    int[] equipArr;

    //UI
    TextView moneyTextView;
    TextView[] statTextView = new TextView[4];
    ImageView[] equipImgView = new ImageView[12];
    ImageView[] playerEuqipImgView = new ImageView[4];


    View rootView;

    Button resetBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_store2, container, false);
        //Storage sections
        settings = this.getActivity().getSharedPreferences("PLAYERITEM", 0);
        editor = settings.edit();
        //

        resetBtn = (Button) rootView.findViewById(R.id.resetbtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = 1000;
                editor.putInt("ESLOT0", 0);
                editor.putInt("ESLOT1", 0);
                editor.putInt("ESLOT2", 0);
                editor.putInt("ESLOT3", 0);

                editor.putInt("HP", 0);
                editor.putInt("ATK", 0);
                editor.putInt("DFS", 0);
                editor.putInt("SPD", 0);

                editor.putInt("MONEY", 1000);
                editor.commit();
            }
        });


        InitEquipArr();

        LoadData();
        SetUpUI();

        InitUI();
        SetUpListener();


        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            settings = this.getActivity().getSharedPreferences("PLAYERITEM", 0);
            editor = settings.edit();

            money = settings.getInt("MONEY", 0);
            moneyTextView.setText("Money:"+money);
        }
    }

    public void InitEquipArr(){
        equipArr = new int[12];
        for(int i = 0; i < 12; i++){
            switch(i+1){
                case 1: equipArr[i] = R.drawable.sword;break;
                case 2: equipArr[i] = R.drawable.sword1;break;
                case 3: equipArr[i] = R.drawable.sword2;break;
                case 4: equipArr[i] = R.drawable.helmet;break;
                case 5: equipArr[i] = R.drawable.helmet1;break;
                case 6: equipArr[i] = R.drawable.helmet2;break;
                case 7: equipArr[i] = R.drawable.armor;break;
                case 8: equipArr[i] = R.drawable.armor1;break;
                case 9: equipArr[i] = R.drawable.armor2;break;
                case 10: equipArr[i] = R.drawable.boots;break;
                case 11: equipArr[i] = R.drawable.boots1;break;
                case 12: equipArr[i] = R.drawable.boots2;break;
                default: equipArr[i] = R.drawable.empty;break;
            }
        }
    }

    public void LoadData(){
        money = settings.getInt("MONEY", -1);
        //preset the money amount , for TESTING ONLY, this line should be deleted
        if(money == -1){money = 100;}
        money = 100;
        stat[0] = settings.getInt("HP", 0);
        stat[1] = settings.getInt("ATK", 0);
        stat[2] = settings.getInt("DFS", 0);
        stat[3] = settings.getInt("SPD", 0);

        slotID[0] = settings.getInt("ESLOT0", 0);
        slotID[1] = settings.getInt("ESLOT1", 0);
        slotID[2] = settings.getInt("ESLOT2", 0);
        slotID[3] = settings.getInt("ESLOT3", 0);


    }

    public void SetUpUI(){
        moneyTextView = (TextView)rootView.findViewById(R.id.MoneyTextView);

        statTextView[0] = (TextView)rootView.findViewById(R.id.HP);
        statTextView[1] = (TextView)rootView.findViewById(R.id.ATK);
        statTextView[2] = (TextView)rootView.findViewById(R.id.DFS);
        statTextView[3] = (TextView)rootView.findViewById(R.id.SPD);

        equipImgView[0] = (ImageView)rootView.findViewById(R.id.sword0ImageView);
        equipImgView[1] = (ImageView)rootView.findViewById(R.id.sword1ImageView);
        equipImgView[2] = (ImageView)rootView.findViewById(R.id.sword2ImageView);
        equipImgView[3] = (ImageView)rootView.findViewById(R.id.helmet0ImageView);
        equipImgView[4] = (ImageView)rootView.findViewById(R.id.helmet1ImageView);
        equipImgView[5] = (ImageView)rootView.findViewById(R.id.helmet2ImageView);
        equipImgView[6] = (ImageView)rootView.findViewById(R.id.armor0ImageView);
        equipImgView[7] = (ImageView)rootView.findViewById(R.id.armor1ImageView);
        equipImgView[8] = (ImageView)rootView.findViewById(R.id.armor2ImageView);
        equipImgView[9] = (ImageView)rootView.findViewById(R.id.boots0ImageView);
        equipImgView[10] = (ImageView)rootView.findViewById(R.id.boots1ImageView);
        equipImgView[11] = (ImageView)rootView.findViewById(R.id.boots2ImageView);

        playerEuqipImgView[0] = (ImageView) rootView.findViewById(R.id.WeaponImageView);
        playerEuqipImgView[1] = (ImageView) rootView.findViewById(R.id.HelmetImageView);
        playerEuqipImgView[2] = (ImageView) rootView.findViewById(R.id.ArmorImageView);
        playerEuqipImgView[3] = (ImageView) rootView.findViewById(R.id.BootsImageView);
    }

    public void InitUI() {
        moneyTextView.setText("Money:" + money);
        for (int i = 0; i < 4; i++) {
            int para = 0;
            switch (slotID[i]) {
                case 1:
                    para = R.drawable.sword;
                    break;
                case 2:
                    para = R.drawable.sword1;
                    break;
                case 3:
                    para = R.drawable.sword2;
                    break;
                case 4:
                    para = R.drawable.helmet;
                    break;
                case 5:
                    para = R.drawable.helmet1;
                    break;
                case 6:
                    para = R.drawable.helmet2;
                    break;
                case 7:
                    para = R.drawable.armor;
                    break;
                case 8:
                    para = R.drawable.armor1;
                    break;
                case 9:
                    para = R.drawable.armor2;
                    break;
                case 10:
                    para = R.drawable.boots;
                    break;
                case 11:
                    para = R.drawable.boots1;
                    break;
                case 12:
                    para = R.drawable.boots2;
                    break;
                default:
                    para = R.drawable.empty;
                    break; //case 0
            }
            if (para == R.drawable.empty) {
                switch (i) {
                    case 0:
                        para = R.drawable.weapon;
                        break;
                    case 1:
                        para = R.drawable.helmetslot;
                        break;
                    case 2:
                        para = R.drawable.armorslot;
                        break;
                    case 3:
                        para = R.drawable.bootsslot;
                        break;
                }
            }
            playerEuqipImgView[i].setImageResource(para);
        }

        statTextView[0].setText("HP:" + Integer.toString(stat[0]));
        statTextView[1].setText("ATK:" + Integer.toString(stat[1]));
        statTextView[2].setText("DFS:" + Integer.toString(stat[2]));
        statTextView[3].setText("SPD:" + Integer.toString(stat[3]));

    }
    public void SetUpListener(){
        //weapon
        equipImgView[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[0] == 0 && money >= 50){
                    money -= 50;
                    moneyTextView.setText("Money:"+money);
                    slotID[0] = 1;
                    stat[1] += 2;
                    statTextView[1].setText("ATK:"+stat[1]);
                    playerEuqipImgView[0].setImageResource(equipArr[0]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT0", slotID[0]);
                    editor.putInt("ATK", stat[1]);
                    editor.commit();
                }
            }
        });
        equipImgView[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[0] == 0 && money >= 100){
                    money -= 100;
                    moneyTextView.setText("Money:"+money);
                    slotID[0] = 2;
                    stat[1] += 10;
                    statTextView[1].setText("ATK:"+stat[1]);
                    playerEuqipImgView[0].setImageResource(equipArr[1]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT0", slotID[0]);
                    editor.putInt("ATK", stat[1]);
                    editor.commit();
                }
            }
        });
        equipImgView[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[0] == 0 && money >= 200){
                    money -= 200;
                    moneyTextView.setText("Money:"+money);
                    slotID[0] = 3;
                    stat[1] += 15;
                    statTextView[1].setText("ATK:"+stat[1]);
                    playerEuqipImgView[0].setImageResource(equipArr[2]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT0", slotID[0]);
                    editor.putInt("ATK", stat[1]);
                    editor.commit();
                }
            }
        });
        //helmet
        equipImgView[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[1] == 0 && money >= 50){
                    money -= 50;
                    moneyTextView.setText("Money:"+money);
                    slotID[1] = 4;
                    stat[0] += 10;
                    statTextView[0].setText("HP:"+stat[0]);
                    playerEuqipImgView[1].setImageResource(equipArr[3]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT1", slotID[1]);
                    editor.putInt("HP", stat[0]);
                    editor.commit();
                }
            }
        });
        equipImgView[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[1] == 0 && money >= 100){
                    money -= 100;
                    moneyTextView.setText("Money:"+money);
                    slotID[1] = 5;
                    stat[0] += 30;
                    statTextView[0].setText("HP:"+stat[0]);
                    playerEuqipImgView[1].setImageResource(equipArr[4]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT1", slotID[1]);
                    editor.putInt("HP", stat[0]);
                    editor.commit();
                }
            }
        });
        equipImgView[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[1] == 0 && money >= 200){
                    money -= 200;
                    moneyTextView.setText("Money:"+money);
                    slotID[1] = 6;
                    stat[0] += 50;
                    statTextView[0].setText("HP:"+stat[0]);
                    playerEuqipImgView[1].setImageResource(equipArr[5]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT1", slotID[1]);
                    editor.putInt("HP", stat[0]);
                    editor.commit();
                }
            }
        });
        //armor
        equipImgView[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[2] == 0 && money >= 50){
                    money -= 50;
                    moneyTextView.setText("Money:"+money);
                    slotID[2] = 7;
                    stat[2] += 2;
                    statTextView[2].setText("DFS:"+stat[2]);
                    playerEuqipImgView[2].setImageResource(equipArr[6]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT2", slotID[2]);
                    editor.putInt("DFS", stat[2]);
                    editor.commit();
                }
            }
        });
        equipImgView[7].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[2] == 0 && money >= 100){
                    money -= 100;
                    moneyTextView.setText("Money:"+money);
                    slotID[2] = 8;
                    stat[2] += 5;
                    statTextView[2].setText("DFS:"+stat[2]);
                    playerEuqipImgView[2].setImageResource(equipArr[7]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT2", slotID[2]);
                    editor.putInt("DFS", stat[2]);
                    editor.commit();
                }
            }
        });
        equipImgView[8].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[2] == 0 && money >= 200){
                    money -= 200;
                    moneyTextView.setText("Money:"+money);
                    slotID[2] = 9;
                    stat[2] += 8;
                    statTextView[2].setText("DFS:"+stat[2]);
                    playerEuqipImgView[2].setImageResource(equipArr[8]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT2", slotID[2]);
                    editor.putInt("DFS", stat[2]);
                    editor.commit();
                }
            }
        });
        //boots
        equipImgView[9].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[3] == 0 && money >= 50){
                    money -= 50;
                    moneyTextView.setText("Money:"+money);
                    slotID[3] = 10;
                    stat[3] += 1;
                    statTextView[3].setText("SPD:"+stat[3]);
                    playerEuqipImgView[3].setImageResource(equipArr[9]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT3", slotID[3]);
                    editor.putInt("SPD", stat[3]);
                    editor.commit();
                }
            }
        });
        equipImgView[10].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[3] == 0 && money >= 100){
                    money -= 100;
                    moneyTextView.setText("Money:"+money);
                    slotID[3] = 11;
                    stat[3] += 2;
                    statTextView[3].setText("SPD:"+stat[3]);
                    playerEuqipImgView[3].setImageResource(equipArr[10]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT3", slotID[3]);
                    editor.putInt("SPD", stat[3]);
                    editor.commit();
                }
            }
        });
        equipImgView[11].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[3] == 0 && money >= 200){
                    money -= 200;
                    moneyTextView.setText("Money:"+money);
                    slotID[3] = 12;
                    stat[3] += 3;
                    statTextView[3].setText("SPD:"+stat[3]);
                    playerEuqipImgView[3].setImageResource(equipArr[11]);
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT3", slotID[3]);
                    editor.putInt("SPD", stat[3]);
                    editor.commit();
                }
            }
        });
        //weapon
        playerEuqipImgView[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[0] != 0){ //not empty slot
                    money+= ((slotID[0]-1) % 3 == 0) ? 50 : ((slotID[0] - 2)% 3 == 0) ? 100 : 200;
                    moneyTextView.setText("Money:"+money);
                    stat[1] -= (slotID[0] == 1) ? 2 : (slotID[0] == 2) ? 10 : 15;
                    statTextView[1].setText("ATK:"+stat[1]);
                    playerEuqipImgView[0].setImageResource(R.drawable.weapon);
                    slotID[0] = 0;
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT0", slotID[0]);
                    editor.putInt("ATK", stat[1]);
                    editor.commit();
                }
            }
        });
        playerEuqipImgView[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[1] != 0){ //not empty slot
                    money+= ((slotID[1]-1) % 3 == 0) ? 50 : ((slotID[1] - 2)% 3 == 0) ? 100 : 200;
                    moneyTextView.setText("Money:"+money);
                    stat[0] -= (slotID[1] == 4) ? 10 : (slotID[1] == 5) ? 30 : 50;
                    statTextView[0].setText("HP:"+stat[0]);
                    playerEuqipImgView[1].setImageResource(R.drawable.helmetslot);
                    slotID[1] = 0;
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT1", slotID[1]);
                    editor.putInt("HP", stat[0]);
                    editor.commit();
                }
            }
        });
        playerEuqipImgView[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[2] != 0){ //not empty slot
                    money+= ((slotID[2]-1) % 3 == 0) ? 50 : ((slotID[2] - 2)% 3 == 0) ? 100 : 200;
                    moneyTextView.setText("Money:"+money);
                    stat[2] -= (slotID[2] == 7) ? 2 : (slotID[2] == 8) ? 5 : 8;
                    statTextView[2].setText("DFS:"+stat[2]);
                    playerEuqipImgView[2].setImageResource(R.drawable.armorslot);
                    slotID[2] = 0;
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT2", slotID[2]);
                    editor.putInt("DFS", stat[2]);
                    editor.commit();
                }
            }
        });
        playerEuqipImgView[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(slotID[3] != 0){ //not empty slot
                    money+= ((slotID[3]-1) % 3 == 0) ? 50 : ((slotID[3] - 2)% 3 == 0) ? 100 : 200;
                    moneyTextView.setText("Money:"+money);
                    stat[3] -= (slotID[3] == 10) ? 1 : (slotID[3] == 11) ? 2 : 3;
                    statTextView[3].setText("SPD:"+stat[3]);
                    playerEuqipImgView[3].setImageResource(R.drawable.bootsslot);
                    slotID[3] = 0;
                    editor.putInt("MONEY",money);
                    editor.putInt("ESLOT3", slotID[3]);
                    editor.putInt("SPD", stat[3]);
                    editor.commit();
                }
            }
        });
    }

}
