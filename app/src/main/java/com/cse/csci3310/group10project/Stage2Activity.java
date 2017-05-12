package com.cse.csci3310.group10project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Stage2Activity extends AppCompatActivity {
    Intent caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage2);
        caller = getIntent();

        Button btnStage2Clear = (Button) findViewById(R.id.btnStage2Clear);
        btnStage2Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            stageClear();
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
        Intent intent = new Intent(Stage2Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),2);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage2Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),2);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}

