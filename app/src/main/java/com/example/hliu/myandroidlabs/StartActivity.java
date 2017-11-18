package com.example.hliu.myandroidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    private Button button;
    private Button button_startChat;
    private static final int REQUEST_CODE =10;

    protected static final String ACTIVITY_NAME = StartActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME,"In onCreate");

        button = (Button)findViewById(R.id.buttonWelcome_startPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        button_startChat =(Button) findViewById(R.id.button_startChat);
        button_startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");

                Intent intent = new Intent(StartActivity.this, ChatWindowActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            Log.i(ACTIVITY_NAME, "Returned to "+ACTIVITY_NAME+" onActivityResult");
        }
        if(resultCode == Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Returned to "+ACTIVITY_NAME+" result ok");
            String messagePassed = data.getStringExtra("Response");
            Toast.makeText(this, messagePassed, Toast.LENGTH_LONG)
                    .show();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy");
    }
}
