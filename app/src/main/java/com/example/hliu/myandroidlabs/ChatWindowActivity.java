package com.example.hliu.myandroidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<String> arrayList_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i("chat window", "In onCreate. ");

        listView = (ListView)findViewById(R.id.listView_chatWin);
        msg = (EditText)findViewById(R.id.chatMsg);
        button_send = (Button)findViewById(R.id.button_sentChat);
        arrayList_msg = new ArrayList<>();


        //------------------
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg.getText().toString().equals("") == false){
                    String m = msg.getText().toString();
                    arrayList_msg.add(m);
                    messageAdapter.notifyDataSetChanged();
                    msg.setText("");

                    //set to the last row -1;
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(listView.getCount() -1);
                        }
                    });
                }
            }
        });

    }// end of oncreate

    private class ChatAdapter extends ArrayAdapter<String>{
//        public ChatAdapter(Context context, int resource, String[] objects) {
//            super(context, resource, objects);
//        }
        public ChatAdapter(Context context){
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
            View view_result =null;
            if(position%2 == 0){
                view_result = inflater.inflate(R.layout.chat_row_incoming, null);
            }else{
                view_result = inflater.inflate(R.layout.chat_row_outgoing,null);
            }
            TextView textView_msg = (TextView)view_result.findViewById(R.id.message_text);
            textView_msg.setText(getItem(position));
            return view_result;
        }
    }
}
