package com.mosquitosquad.foxcities.mosquad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tyler Gotz on 3/22/2016.
 */
public class AdminAppListActivity extends AppCompatActivity
{
    final Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicator_list);
        final Button addApp = (Button) findViewById(R.id.addButton);
        addApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminAddAppActivity.class);
                startActivity(intent);
            }
        });
        final List<String> appList = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appList);
        final Firebase fire = new Firebase("https://mosquito-squad.firebaseio.com/");
        final Query ref = fire.orderByChild("admin/applicators");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue().toString();
                final String[] values = value.split("=");
                for (int i = 0; i < values.length; i++) //gets name
                {
                    if (values[i].equals("{fullName"))
                    {
                        String name = values[i + 1].replace('}', ' ').replace(',', ' ').replaceAll("[0-9]", "");
                        if(!name.replaceAll("\\s", "").isEmpty())
                        {
                            appList.add(name);
                        }
                    }
                }
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                listView = (ListView) findViewById(R.id.appList);
                listView.setAdapter(adapter);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        alertDialogBuilder.setTitle("Mosquito Squad").setMessage("Do you want to remove Applicator?").setCancelable(true).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < values.length; i++) {
                                    if (values[i].equals("{fullName")) //gets license num
                                    {
                                        String license = values[i - 1].replace('{', ' ').replace('}', ' ').replaceAll("\\s", "").replaceAll("[^\\d.]", "");
                                        fire.child("admin/applicators/" + license + "/fullName").setValue(" ");
                                    }
                                }
                                appList.remove(listView.getItemAtPosition(position));
                                adapter.notifyDataSetChanged();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
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

}
