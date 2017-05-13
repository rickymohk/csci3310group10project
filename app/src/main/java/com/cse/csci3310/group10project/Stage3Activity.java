package com.cse.csci3310.group10project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Stage3Activity extends AppCompatActivity {
    Intent caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3);
        caller = getIntent();

        Button btnStage3Clear = (Button) findViewById(R.id.btnStage3Clear);
        btnStage3Clear.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(Stage3Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),3);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.putExtra(getString(R.string.key_cash),caller.getIntExtra(getString(R.string.key_cash),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage3Activity.this,Stage0Activity.class);
        intent.putExtra(getString(R.string.key_fromStage),3);
        intent.putExtra(getString(R.string.key_currentStage),caller.getIntExtra(getString(R.string.key_currentStage),0));
        intent.putExtra(getString(R.string.key_cash),caller.getIntExtra(getString(R.string.key_cash),0));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
