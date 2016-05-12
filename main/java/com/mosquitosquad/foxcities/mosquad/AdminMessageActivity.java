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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/6/2016.
 */
public class AdminMessageActivity extends Activity
{
    private static  String FIREBASE_URL = "https://mosquito-squad.firebaseio.com/messages";

    FirebaseListAdapter<ChatMessage> listAdapter;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_messages);

        Firebase.setAndroidContext(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            String uid = bundle.getString("uid");
            String getUID = uid.substring(uid.indexOf(",") + 1);


            firebase = new Firebase(FIREBASE_URL).child("custToManager").child(getUID);
        }


        final EditText editText = (EditText) this.findViewById(R.id.messageInput);
        ImageButton sendBtn = (ImageButton) this.findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", "MOSQUAD Fox Cities");
                map.put("message", text);
                firebase.push().setValue(map);
                editText.setText("");
            }
        });

        final ListView listView = (ListView) this.findViewById(R.id.messagelist);
        listAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, android.R.layout.two_line_list_item, firebase) {
            @Override
            protected void populateView(View v, ChatMessage model, int i) {
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setText(model.getName());
                TextView tv2 = (TextView)v.findViewById(android.R.id.text2);
                tv2.setText(model.getMessage());
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
