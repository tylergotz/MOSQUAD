package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by Tyler Gotz on 2/25/2016.
 */
public class ServicesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_services);
        String[] headers = {"Traditional Mosquito Control", "All Natural Mosquito Control", "Intensive Tick Treatment", "Special Event Spray"};
        String[] subs = {
                "Our traditional barrier spray, the most popular service, will decrease your yard's mosquito population by 85-90%! Our trained technicians treat key areas of your property, eliminating adult mosquitoes on contact and providing continuous protection, reapplying every 2-3 weeks",
                "Our all natural mosquito spray is made up of essential oils, eliminating adult mosquitoes on contact and repelling 70-75% of your property's mosquito population. As opposed to our traditional barrier protection, the all natural treatment must be applied every 14 days.",
                "For the most comprehensive tick control, our technicians utilize a combination of our traditional barrier spray and tick tubes. Our barrier spray will eliminate adult ticks on contact while tick tubes are placed in key areas on your property, twice a season, to eliminate ticks at their nymph stage.",
                "From outdoor weddings to backyard barbecues, our special event spray keeps your guests off the menu. Our trained technicians will spray the area before the event, creating an odorless barrier, eliminating mosquitoes."
        };
        GridView gridView = (GridView) findViewById(R.id.gridView1);
        TextViewAdapter textViewAdapter = new TextViewAdapter(this, headers, subs);
        gridView.setAdapter(textViewAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        Button rates = (Button) findViewById(R.id.rateButton);
        rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RatesActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_services, menu);
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
        else if(id == R.id.action_locations)
        {
            intent = new Intent(this, LocationActivity.class);
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



