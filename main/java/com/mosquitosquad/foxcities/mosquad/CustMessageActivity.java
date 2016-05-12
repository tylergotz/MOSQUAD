package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/6/2016.
 */
public class CustMessageActivity extends Activity
{
    private static final String FIREBASE_URL = "https://mosquito-squad.firebaseio.com/messages";

    FirebaseListAdapter<ChatMessage> listAdapter;
    Firebase firebase;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_messages);
        Firebase.setAndroidContext(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String uid = bundle.getString("uid");
            author = bundle.getString("author");
            firebase = new Firebase(FIREBASE_URL).child("custToManager").child(uid);
        }


        final EditText textEdit = (EditText) this.findViewById(R.id.messageInput);
        ImageButton sendBtn = (ImageButton) this.findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                Map<String, String> values = new HashMap<String, String>();
                values.put("name", author);
                values.put("message", text);
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date date = Calendar.getInstance().getTime();
                String timeSent = dateFormat.format(date);
                values.put("date", timeSent);
                firebase.push().setValue(values);
                textEdit.setText("");
            }
        });



        final ListView listView = (ListView) this.findViewById(R.id.messagelist);
        listAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, android.R.layout.two_line_list_item, firebase) {
            @Override
            protected void populateView(View v, ChatMessage model, int i) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getMessage());
            }
        };

        listView.setAdapter(listAdapter);


    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        listAdapter.cleanup();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}


