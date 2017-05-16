package com.cse.csci3310.group10project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        editor = settings.edit();
        if(settings.getBoolean("LOSE",false))
        {
            TextView textView20 = (TextView) findViewById(R.id.textView20);
            textView20.setText(getString(R.string.end_lose));
        }
        editor.clear();
        editor.apply();

        Button btnRepaly = (Button) findViewById(R.id.btnReplay);
        btnRepaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
