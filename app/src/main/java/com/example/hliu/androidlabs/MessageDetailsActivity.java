package com.example.hliu.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

public class MessageDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }

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

/*    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
*//*            Fragment fragment1 = getFragmentManager().findFragmentById(R.id.framelayout_layoutChat);

            if(fragment1 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment1);
                fragmentTransaction.commit();
            }*//*

            finish();
        }

*//*        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mGridViewPortrait.setVisibility(View.GONE);
//            mGridViewLandscape.setVisibility(View.VISIBLE);

            Fragment fragment2 = getFragmentManager().findFragmentById(R.id.framelayout_layout1);
            if(fragment2 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment2);
                fragmentTransaction.commit();
            }

        }*//*

    }*/

}
