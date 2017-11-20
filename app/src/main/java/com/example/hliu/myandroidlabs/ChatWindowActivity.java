package com.example.hliu.myandroidlabs;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindowActivity extends Activity {
    private ListView listView;
    private EditText msg;
    private Button button_send;

    private ChatAdapter messageAdapter;
    private ArrayList<String> arrayList_msg;
    private boolean toListFromTop = true;

    private final static String TAG = "chat window";

    private ChatDatabaseHelper chatDatabaseHelper;

    //--------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(TAG, "In onCreate. ");

        listView = findViewById(R.id.listView_chatWin);
        msg = findViewById(R.id.chatMsg);
        button_send = findViewById(R.id.button_sentChat);
        arrayList_msg = new ArrayList<>();


//        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);

        // get the opened database instance
//        chatDatabaseHelper = ChatDatabaseHelper.getInstance(this);

        chatDatabaseHelper = new ChatDatabaseHelper(this);
        chatDatabaseHelper.openMydb();

        //------------------
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        Lab5_function_displaySQL();

        //--------------on click

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().equals("") == false) {
                    String m = msg.getText().toString();
                    arrayList_msg.add(m);
                    //-----------
                    long i = chatDatabaseHelper.insertEntry_mydb(m);

                    messageAdapter.notifyDataSetChanged();
                    moveItem_toLast();

                    msg.setText("");
                    Log.i(TAG, "database insert : return long " + i);
                }
            }
        });
        //--------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "database delete the first entry ");
                chatDatabaseHelper.deleteEntry_mydb_firstEntry();
                Lab5_function_displaySQL();
                moveItem_toFirst();
                return true;
            }

        });

    }// end of oncreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatDatabaseHelper.closeMydb();
        Log.i(TAG, "database closed");
    }

    private void Lab5_function_displaySQL() {
        //---------------------data base section
        Cursor cursor = chatDatabaseHelper.getMydb_allRecod();
        arrayList_msg.clear();
        messageAdapter.notifyDataSetChanged();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int index = cursor.getColumnIndex(ChatDatabaseHelper.MESSAGE_colName);
                    String str = cursor.getString(index);
                    Log.i(TAG, "SQL message:" + str);

                    //clear any message in teh arraylist
//                    arrayList_msg.clear();
//                    messageAdapter.notifyDataSetChanged();
                    arrayList_msg.add(str);
                    cursor.moveToNext();
                }
                messageAdapter.notifyDataSetChanged();
                moveItem_toLast();

            } else {
                Log.i(TAG, "cursor can not move to first item");
            }

            Log.i(TAG, "cursor's column count = " + cursor.getColumnCount());

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String str = cursor.getColumnName(i);
                Log.i(TAG, "Cursor’s  column name = " + str + " element of " + i);
            }

        } else {
            Log.w(TAG, "database warning : display no record to read or NULL ");
        }
    }

    private void moveItem_toLast() {
        //set to the last row -1;
/*        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(listView.getCount() - 1);
            }
        });*/
        listView.setSelection(listView.getCount() - 1);
    }

    private void moveItem_toFirst() {
        //set to the last row -1;
/*        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(0);
            }
        });*/
        listView.setSelection(0);
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        //        public ChatAdapter(Context context, int resource, String[] objects) {
//            super(context, resource, objects);
//        }
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return arrayList_msg.size();
        }

        @Override
        public String getItem(int position) {

            return arrayList_msg.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view_result = null;
            if (position % 2 == 0) {
                view_result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                view_result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView textView_msg = view_result.findViewById(R.id.message_text);
            textView_msg.setText(getItem(position));
            return view_result;
        }
    }
}
