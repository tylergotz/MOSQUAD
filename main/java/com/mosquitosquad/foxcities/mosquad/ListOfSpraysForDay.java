package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tyler Gotz on 4/26/2016.
 */
public class ListOfSpraysForDay extends AppCompatActivity
{
    private TextView dateTV;
    ArrayList<String> listOfCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.spray_list);
        Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        dateTV = (TextView) findViewById(R.id.dateTitleTextView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            dateTV.setText(bundle.getString("sprayDate"));
        }


        final String date = dateTV.getText().toString();
        ListView sprayList = (ListView) findViewById(R.id.sprayListView);
        listOfCustomers = new ArrayList<>();
        Query query = firebase.child("serviceSchedule");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] values = dataSnapshot.getValue().toString().split("=");
                for (int i = 0; i < values.length; i++) {
                    if (values[i].contains(date)) {
                        listOfCustomers.add(values[i - 2].substring(1) + "-" + values[i + 2].substring(0, values[i + 2].indexOf(",")) + "-" +
                                values[i + 4].substring(0, values[i + 4].indexOf(",")));
                    }

                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfCustomers);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sprayList.setAdapter(adapter);
        Sprays sprays = new Sprays(0, listOfCustomers);
        sprays.setAppList(listOfCustomers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spray_sched, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Intent intent;
        Bundle b = new Bundle();
        if (id == R.id.assign) {
            b.putString("date", dateTV.getText().toString());
            b.putStringArrayList("list", listOfCustomers);
            intent = new Intent(this, AssignSpraysActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
        else if(id == R.id.map)
        {
            b.putStringArrayList("list", listOfCustomers);
            b.putBoolean("comesFromList", true);
            b.putString("date", dateTV.getText().toString());
            intent = new Intent(this, SpraysPlottedOnMap.class);
            intent.putExtras(b);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
