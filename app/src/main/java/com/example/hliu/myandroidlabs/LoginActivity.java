package com.example.hliu.myandroidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = LoginActivity.class.getSimpleName();
    private Button button_logIn;
    private EditText email;

    private static final String DEFAULT_EMAIL = "default_email@domain.com";
    private String fileName_sharePref = "fileName";
// can not do this way; define in the code   private String key_emailAddress = getString(R.string.logIn_text);
    private static final String key_emailAddress = "emailStored";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate");

        button_logIn = (Button) findViewById(R.id.button_login);
        email = (EditText) findViewById(R.id.email);
        //-- read email from stored sharepreferences
        SharedPreferences sharedPreferences = getSharedPreferences(fileName_sharePref , Context.MODE_PRIVATE);
        // key and value
        String storedEmail = sharedPreferences.getString(key_emailAddress, DEFAULT_EMAIL);
//        String storedEmail = sharedPreferences.getString(getString(R.string.logIn_text), "default_email@domain.com");
        email.setText(storedEmail);

// the button press and login
        button_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(fileName_sharePref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key_emailAddress, email.getText().toString());
//                editor.apply();
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy");
    }
}
