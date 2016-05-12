package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 4/6/2016.
 */
public class AdminMessagesListActivity extends AppCompatActivity
{
    private static final String FIREBASE_URL = "https://mosquito-squad.firebaseio.com/messages/";
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.admin_messages_list);
        getSupportActionBar().setTitle("Messages");
        Firebase.setAndroidContext(this);
        final Context context = this;
        final ListView messagesList = (ListView) findViewById(R.id.adminMessagesListView);
        final List<String> names = new ArrayList<>();
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        firebase = new Firebase(FIREBASE_URL);
        Query query = firebase;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue().toString();
                final String[] values = value.split("=");
                for(int i = 0; i < values.length; i++)
                {
                    if(values[i].equals("{name") && !values[i+1].substring(0, values[i+1].indexOf(",")).equals("MOSQUAD Fox Cities"))
                    {
                        if(!names.contains(values[i+1].substring(0, values[i+1].indexOf(","))))
                        {
                            names.add(0, values[i+1].substring(0, values[i + 1].indexOf(",")));
                        }

                    }
                }

                listAdapter.notifyDataSetChanged();
                messagesList.setAdapter(listAdapter);

                messagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(int i = 0; i < values.length; i++)
                        {
                            if(values[i].equals("{name") && !values[i+1].substring(0, values[i+1].indexOf(",")).equals("MOSQUAD Fox Cities"))
                            {
                                if(values[i+1].substring(0, values[i+1].indexOf(",")).equals(messagesList.getItemAtPosition(position)))
                                {
                                    Intent intent = new Intent(context, AdminMessageActivity.class);
                                    intent.putExtra("uid", "junk".concat(values[i-2].replace("{", ",").replace("}", "").replace(" ","")));
                                    startActivity(intent);
                                }

                            }
                        }

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
