package com.example.hliu.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//import static android.content.ContentValues.TAG;

public class ChatWindow extends Activity implements MessageFragment.OnItemSelectedListener {

    private Button button_send;
    private EditText editText_msg;
    private ListView listView_chat;
    private ArrayList<String> messageList;

    private ChartAdapter messageAdapter;
    private ChatDatabaseHelper chatDatabaseHelper;

    private final static String TAG_chatWindow = ChatWindow.class.getSimpleName();

    private boolean toListFromTop = true;


    //---------lab7
    private FrameLayout frameLayout_chat;
    private boolean isPhoneMode;

    private Cursor cursor;

    public final static int RequestCode_messageActivity = 20;
    public final static int ResultCode_messageActivity = 22;

    //--------inner classs
    class ChartAdapter extends ArrayAdapter<String> {

        public ChartAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public String getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
//            int index = cursor.getColumnIndex(chatDatabaseHelper.getStr_MESSAGE_COL());
//            String str = cursor.getString(index);
            int indexID = cursor.getColumnIndex(ChatDatabaseHelper.getStr_ID_COL());

            if (indexID != -1) {
                long idNum = cursor.getLong(indexID);
                return idNum;
            } else {
                Log.i(TAG_chatWindow, "database index returned -1");
            }
            return -1;
        }
    }//chartAdapter

    //--------------outter class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // lab 7
        frameLayout_chat = findViewById(R.id.framelayout_layoutChat);
        //If it returns null, then it wasn’t loaded and you are using the phone layout
        isPhoneMode = frameLayout_chat == null;


/*        if(savedInstanceState != null){

            Fragment fragment1 = getFragmentManager().findFragmentById(R.id.framelayout_layoutChat);
            Fragment fragment2 = getFragmentManager().findFragmentById(R.id.framelayout_layout1);

            if(fragment1 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment1);
                fragmentTransaction.commit();
            }

            if(fragment2 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment2);
                fragmentTransaction.commit();
            }
            setContentView(R.layout.activity_chat_window);

        }*/


        //-------lab 5
//        setContentView(R.layout.activity_chat_window);
        button_send = findViewById(R.id.button_sentChat);
        editText_msg = findViewById(R.id.chatMsg);
        listView_chat = findViewById(R.id.listView_chatWin);
        messageList = new ArrayList<>();


        //-------
        messageAdapter = new ChartAdapter(this);
        listView_chat.setAdapter(messageAdapter);

        // -- sql
        chatDatabaseHelper = new ChatDatabaseHelper(this); // this ? context
        //added for lab5
        chatDatabaseHelper.openDatabase();

//        chatDatabaseHelper.getWritableDatabase();
//        registerForContextMenu(listView_chat);

        lab5funciton_displaySQL();

        // ------------
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_msg.getText().toString().equals("") == false) {
                    // lab4 code
                    messageList.add(editText_msg.getText().toString());
                    messageAdapter.notifyDataSetChanged();
                    moveItem_toLast();
                    chatDatabaseHelper.insertEntry(editText_msg.getText().toString());
                    editText_msg.setText("");
                }
            }
        });
        //----------------------
        listView_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (toListFromTop) {
                    moveItem_toFirst();
                    toListFromTop = false;
                } else {
                    moveItem_toLast();
                    toListFromTop = true;
                }
            }
        });

        listView_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //---lab 5
//                Log.i(TAG_chatWindow, "database delete the first entry ");
//                chatDatabaseHelper.deleteEntry_mydb_firstEntry();
//                lab5funciton_displaySQL();
//                moveItem_toFirst();
//                return true;

                //---Lab 7
                String message = parent.getItemAtPosition(position).toString();
                Long messageID = parent.getItemIdAtPosition(position);
                if (isPhoneMode) {
                    Intent intent = new Intent(ChatWindow.this, MessageDetailsActivity.class);
                    intent.putExtra("message", message);
                    intent.putExtra("messageID", messageID);
                    startActivityForResult(intent, RequestCode_messageActivity);

                } else {
                    Fragment fragment = new MessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    bundle.putLong("messageID", messageID);

                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout_layoutChat, fragment);
                    fragmentTransaction.commit();
                }

                return true;
            }// end of long click

        });
    }//end of method

    //------------------Lab 7
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCode_messageActivity) {
            Long id = data.getLongExtra("messageID", -2); // default value;
            Log.i(TAG_chatWindow, "chat window returned value is " + id);

            chatDatabaseHelper.deleteEntry_mydb_IDNum(id);
            lab5funciton_displaySQL();
        }
    }

    //------------------Lab 7
    @Override
    public void onMsgItemSelected(Long IDnum) {
        chatDatabaseHelper.deleteEntry_mydb_IDNum(IDnum);
        lab5funciton_displaySQL();
    }


    //-------------------------------------

    //-----------lab 5

    private void moveItem_toLast() {
        //set to the last row -1;
        listView_chat.post(new Runnable() {
            @Override
            public void run() {
                listView_chat.setSelection(listView_chat.getCount() - 1);
            }
        });
    }

    private void moveItem_toFirst() {
        //set to the last row -1;
        listView_chat.post(new Runnable() {
            @Override
            public void run() {
                listView_chat.setSelection(0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatDatabaseHelper.closeDatabase();
    }

    private void lab5funciton_displaySQL() {
//        Cursor cursor = null;
        cursor = chatDatabaseHelper.getCursor_RecordList();
//        for(cursor.moveToFirst(); (cursor.isAfterLast() ==false) ; cursor.moveToNext()){
//        }
        messageList.clear();
        messageAdapter.notifyDataSetChanged();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //String fName = c.getString( c.getColumnIndex(”FirstName”) );
                int index = cursor.getColumnIndex(chatDatabaseHelper.getStr_MESSAGE_COL());
                String str = cursor.getString(index);

                messageList.add(str);
                Log.i(TAG_chatWindow, "SQL MESSAGE: " + str);
                cursor.moveToNext();
            }
            messageAdapter.notifyDataSetChanged();
        }


        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String str = cursor.getColumnName(i);
            Log.i(TAG_chatWindow, "Cursor’s  column name = " + str + " element of " + i);
        }

    }

/*    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Fragment fragment1 = getFragmentManager().findFragmentById(R.id.framelayout_layoutChat);

            if(fragment1 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment1);
                fragmentTransaction.commit();
            }
        }else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mGridViewPortrait.setVisibility(View.GONE);
//            mGridViewLandscape.setVisibility(View.VISIBLE);

            Fragment fragment2 = getFragmentManager().findFragmentById(R.id.framelayout_layout1);
            if(fragment2 != null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment2);
                fragmentTransaction.commit();
            }

        }

    }*/

}




/*    protected void onCreate(Bundle savedInstanceState) {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.aportrait);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.alandscape);
                break;
        }

/////..............
    }*/
