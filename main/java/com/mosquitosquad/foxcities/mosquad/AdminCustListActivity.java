package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 3/30/2016.
 */
public class AdminCustListActivity extends AppCompatActivity
{
    final Context context = this;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.customer_list);
        final Button custBtn = (Button) findViewById(R.id.custAddButton);
        final List<String> custList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, custList);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com");
        Query query = firebase.child("users").child("clients");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString().substring(9);
                String[] values = value.split("=");
                for (int i = 0; i < values.length; i++) {
                    if (values[i].substring(values[i].indexOf(",")+ 2).equals("fullName")) {
                        String name = values[i + 1].substring(0, values[i + 1].length() - 15);
                        if(!name.replaceAll("\\s", "").isEmpty())
                        {
                            custList.add(name);
                        }

                    }
                }
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                listView = (ListView) findViewById(R.id.custList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, AdminUpdateCustActivity.class);
                        intent.putExtra("name", listView.getItemAtPosition(position).toString());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        custBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean userIsAdmin = true;
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("userIsAdmin", userIsAdmin);
                startActivity(intent);
            }
        });
    }
}