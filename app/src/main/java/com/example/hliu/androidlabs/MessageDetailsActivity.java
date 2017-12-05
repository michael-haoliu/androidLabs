package com.example.hliu.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        String msg = getIntent().getStringExtra("message");
        Long id = getIntent().getLongExtra("messageID", -2);
//        intent.putExtra("message", message);
//        intent.putExtra("messageID", messageID);

        Bundle bundle = new Bundle();
        bundle.putString("message", msg);
        bundle.putLong("messageID", id);

        Fragment fragment = new MessageFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_layout1, fragment);
        fragmentTransaction.commit();
    }

}
