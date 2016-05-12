package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 2/23/2016.
 */
public class LocationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.locations);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        List<String> locationArray = new ArrayList<>();
        locationArray.add("Appleton");
        locationArray.add("Green Bay");
        locationArray.add("De Pere");
        locationArray.add("Grand Chute");
        locationArray.add("Neenah");
        locationArray.add("Menasha");
        locationArray.add("Kimberly");
        locationArray.add("Kaukuana");
        locationArray.add("Combined Locks");
        locationArray.add("Greenville");
        locationArray.add("Hortonville");
        locationArray.add("Door County");
        locationArray.add("Oshkosh");
        locationArray.add("Surronding areas");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.locationListView);
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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
        if (id == R.id.action_home)
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_contactus)
        {
            intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_different)
        {
            intent = new Intent(this, DifferencesActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.services)
        {
            intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.login)
        {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }else if(id == android.R.id.home)
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
