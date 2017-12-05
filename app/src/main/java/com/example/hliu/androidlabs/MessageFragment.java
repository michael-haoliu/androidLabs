package com.example.hliu.androidlabs;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private View view;
    private Button buttonDel;
    private Button buttonCancel;
    private TextView textView_ID, textView_msg;

    //--------interface  Lab 7
    OnItemSelectedListener onItemSelectedListener;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_message_detail, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView_ID = view.findViewById(R.id.messageID_inFragment);
        textView_msg = view.findViewById(R.id.message_inFragement);

        buttonDel = view.findViewById(R.id.button_deleteMessage_framelayout);
        buttonCancel = view.findViewById(R.id.button_cancel_framelayout);

        final Long msgID = getArguments().getLong("messageID");
        String msg = getArguments().getString("message");

        textView_msg.setText(msg);
        textView_ID.setText(String.valueOf(msgID));

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Toast.makeText(getActivity(), "Deleting the item selected", Toast.LENGTH_LONG).show();

                    //--------interface pholimophism  Lab 7
                    onItemSelectedListener.onMsgItemSelected(msgID);


                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("messageID", msgID);
                    getActivity().setResult(ChatWindow.ResultCode_messageActivity, resultIntent);
                    getActivity().finish();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MessageFragment", "MessageFragment the clas name of the paraent activity" + getActivity().getLocalClassName());
                if (getActivity().getLocalClassName().equalsIgnoreCase("MessageDetailsActivity")) {
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "can not use cancel due to it is not phone view", Toast.LENGTH_LONG).show();
                    Log.i("MessageFragment", "MessageFragment can not use cancel due to it is not phone view");
                }
            }
        });
    }// on create

    //--------interface  Lab 7
//    private OnItemSelectedListener onItemSelectedListener;

    //--OnItemSelectedListener listener;
    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.

    @Override
    public void onAttach(Activity context) { // can not use context
//    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("MessageFragment ", "Message Fragment context check before" + context.toString());


        try{
            onItemSelectedListener = (OnItemSelectedListener) context;
            Log.i("MessageFragment ", "Message Fragment context check " + context.toString());
        }catch (Exception e){
            Log.i("MessageFragment ", " must implement MessageFragment.OnItemSelectedListener" + context.toString());

        }



/*        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            onItemSelectedListener = (OnItemSelectedListener) context;
//            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
            Log.i("MessageFragment ", "Message Fragment context check " + context.toString());
        } else {
            Log.i("MessageFragment ", "Message Fragment exception " + context.toString());

            throw new ClassCastException(context.toString()
                    + " must implement MessageFragment.OnItemSelectedListener");
        }*/
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onMsgItemSelected(Long IDnum);
    }

}// outer class
