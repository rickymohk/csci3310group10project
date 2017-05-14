package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Stage3Activity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences settings;
    Intent caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3);
        caller = getIntent();
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();

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
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.putInt(getString(R.string.key_currentStage),settings.getInt(getString(R.string.key_currentStage),0));
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void stageClear()
    {
        Intent intent = new Intent(Stage3Activity.this,Stage0Activity.class);
        editor.putInt(getString(R.string.key_fromStage),3);
        editor.putInt(getString(R.string.key_currentStage),settings.getInt(getString(R.string.key_currentStage),0));
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
