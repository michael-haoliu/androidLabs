package com.example.hliu.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {

    protected static final String ACTIVITY_NAME = ListItemsActivity.class.getSimpleName();
    private ImageButton imageButton;
    private static final int REQUEST_IMAGE_CAPTURE =20;
    private Switch aSwitch;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME,"In onCreate");

        //------------------
        aSwitch = findViewById(R.id.switchID_listItem);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CharSequence charSequence;
                int duration;
                if(isChecked){
                    charSequence = "Switch is ON";
                    duration = Toast.LENGTH_LONG;
                    Log.i("listItemsActivity", "switch is ON");
                }else{
                    charSequence = "Switch is OFF";
                    duration = Toast.LENGTH_SHORT;
                    Log.i("listItemsActivity", "switch is OFF");
                }
//                Toast toast = Toast.makeText(getApplicationContext(), charSequence, duration);
                Toast toast = Toast.makeText(ListItemsActivity.this, charSequence, duration);
                toast.show();
            }
        });

        //-------------------
        checkBox = findViewById(R.id.checkBoxID_ListItem);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);

                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent_result = new Intent();
                                intent_result.putExtra("Response","Here is my response");
                                setResult(Activity.RESULT_OK, intent_result);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


            }
        });

        //-------------------
        imageButton = findViewById(R.id.imageButtonID_listItem);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent_takePic.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(intent_takePic, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
//            Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 1024, 1024, true);
            imageButton.setImageBitmap(imageBitmap );
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
