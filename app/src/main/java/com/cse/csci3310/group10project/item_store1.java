package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class item_store1 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences settings;

    int money;

    //UI
    TextView moneyTextView;
    TextView[] quanTextView = new TextView[3];
    ImageView[] itemImgView = new ImageView[3];
    ImageView[] playerItemImgView = new ImageView[3];

    //arr tem storage
    int[] slotQuan = new int [3];
    int[] slotID = new int[3];

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_store1, container, false);
        //Storage sections
        settings = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();
        //
        LoadData();
        SetUpUI();

        InitUI();
        SetUpListener();

        return rootView;
    }
    Boolean hadBeenInvisible = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && hadBeenInvisible && settings != null){

            settings = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), 0);
            editor = settings.edit();

            money = settings.getInt("MONEY", 0);
            moneyTextView.setText("Money:"+money);

        }
        else{
            hadBeenInvisible = true;
        }
    }

    public void LoadData(){
        money = settings.getInt("MONEY", -1);
        //preset the money amount , for TESTING ONLY, this line should be deleted
        if(money == -1){money = 100;}

        slotQuan[0] = settings.getInt("SLOT0NO", 0);
        slotQuan[1] = settings.getInt("SLOT1NO", 0);
        slotQuan[2] = settings.getInt("SLOT2NO", 0);

        slotID[0] = settings.getInt("SLOT0", 0);
        slotID[1] = settings.getInt("SLOT1", 1);
        slotID[2] = settings.getInt("SLOT2", 2);

    }

    public void InitUI(){
        moneyTextView.setText("Money:"+money);

        for(int i = 0; i < 3; i++) {
            if(slotID[i] == 0){playerItemImgView[i].setImageResource(R.drawable.empty);}
            else if(slotID[i] == 1){playerItemImgView[i].setImageResource(R.drawable.item_0);}
            else if(slotID[i] == 2){playerItemImgView[i].setImageResource(R.drawable.item_1);}
            else if(slotID[i] == 3){playerItemImgView[i].setImageResource(R.drawable.poison);}
        }

        for(int i = 0; i < 3; i++){
            quanTextView[i].setText(Integer.toString(slotQuan[i]));
        }
    }
    public void SetUpUI(){
        moneyTextView = (TextView)rootView.findViewById(R.id.MoneyTextView);

        quanTextView[0] = (TextView)rootView.findViewById(R.id.QuanTextView0);
        quanTextView[1]= (TextView)rootView.findViewById(R.id.QuanTextView1);
        quanTextView[2] = (TextView)rootView.findViewById(R.id.QuanTextView2);

        itemImgView[0] = (ImageView)rootView.findViewById(R.id.HPImageView);
        itemImgView[1] = (ImageView)rootView.findViewById(R.id.GrenadeImageView);
        itemImgView[2] = (ImageView)rootView.findViewById(R.id.PoisonImageView);

        playerItemImgView[0] = (ImageView)rootView.findViewById(R.id.PlayerItemImageView0);
        playerItemImgView[1] = (ImageView)rootView.findViewById(R.id.PlayerItemImageView1);
        playerItemImgView[2] = (ImageView)rootView.findViewById(R.id.PlayerItemImageView2);
    }


    public void SetUpListener(){

        itemImgView[0].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int res = CheckIfAnyEmptySlot(1);
                if(res != -1 && money >= 50){
                    money-=50;
                    moneyTextView.setText("Money:"+money);
                    slotID[res] = 1;
                    playerItemImgView[res].setImageResource(R.drawable.item_0);
                    slotQuan[res]++;
                    quanTextView[res].setText(Integer.toString(slotQuan[res]));
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+res, slotID[res]);
                    editor.putInt("SLOT"+res+"NO", slotQuan[res]);
                    editor.commit();
                }

            }
        });
        itemImgView[1].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int res = CheckIfAnyEmptySlot(2);
                if(res != -1 && money >= 30){
                    money-=30;
                    moneyTextView.setText("Money:"+money);
                    slotID[res] = 2;
                    playerItemImgView[res].setImageResource(R.drawable.item_1);
                    slotQuan[res]++;
                    quanTextView[res].setText(Integer.toString(slotQuan[res]));
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+res, slotID[res]);
                    editor.putInt("SLOT"+res+"NO", slotQuan[res]);
                    editor.commit();
                }
            }
        });
        itemImgView[2].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int res = CheckIfAnyEmptySlot(3);
                if(res != -1 && money >= 100){
                    money-=100;
                    moneyTextView.setText("Money:"+money);
                    slotID[res] = 3;
                    playerItemImgView[res].setImageResource(R.drawable.poison);
                    slotQuan[res]++;
                    quanTextView[res].setText(Integer.toString(slotQuan[res]));
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+res, slotID[res]);
                    editor.putInt("SLOT"+res+"NO", slotQuan[res]);
                    editor.commit();
                }
            }
        });


        playerItemImgView[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotID[0] >= 1) {
                    money+=(slotID[0] == 1) ? 50 : (slotID[0] == 2) ? 30 : 100;
                    moneyTextView.setText("Money:" + money);
                    slotQuan[0]--;
                    quanTextView[0].setText(Integer.toString(slotQuan[0]));
                    if (slotQuan[0] <= 0) {
                        playerItemImgView[0].setImageResource(R.drawable.empty);
                        slotID[0] = 0;
                        slotQuan[0] = 0;
                    }
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+0, slotID[0]);
                    editor.putInt("SLOT0NO", slotQuan[0]);
                    editor.commit();
                }
            }
        });

        playerItemImgView[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotID[1] >= 1) {
                    money+=(slotID[1] == 1) ? 50 : (slotID[1] == 2) ? 30 : 100;
                    moneyTextView.setText("Money:" + money);
                    slotQuan[1]--;
                    quanTextView[1].setText(Integer.toString(slotQuan[1]));
                    if (slotQuan[1] <= 0) {
                        playerItemImgView[1].setImageResource(R.drawable.empty);
                        slotID[1] = 0;
                        slotQuan[1] = 0;
                    }
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+1, slotID[1]);
                    editor.putInt("SLOT1NO", slotQuan[1]);
                    editor.commit();
                }
            }
        });
        playerItemImgView[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotID[2] >= 1) {
                    money+=(slotID[2] == 1) ? 50 : (slotID[2] == 2) ? 30 : 100;
                    moneyTextView.setText("Money:" + money);
                    slotQuan[2]--;
                    quanTextView[2].setText(Integer.toString(slotQuan[2]));
                    if (slotQuan[2] <= 0) {
                        playerItemImgView[2].setImageResource(R.drawable.empty);
                        slotID[2] = 0;
                        slotQuan[2] = 0;
                    }
                    editor.putInt("MONEY", money);
                    editor.putInt("SLOT"+2, slotID[2]);
                    editor.putInt("SLOT2NO", slotQuan[2]);
                    editor.commit();
                }
            }
        });
    }


    //itemID:0 == empty slot, 1== healthpotion, 2==gernade, 3==poison
    //check if and only if there are empty slots in player's inventory
    //if no empty slot found, then return -1
    public int CheckIfAnyEmptySlot(int itemID){
        //check if all item slot is empty
        int emptySlotIndex = -1;
        for(int i = 0; i < 3; i++){
            if(slotID[i] == itemID){
                return i;
            }
            else if(slotID[i]==0 && emptySlotIndex == -1){ //storing the earliest found slot if any.
                emptySlotIndex = i;
            }
        }
        return emptySlotIndex;
    }

}
