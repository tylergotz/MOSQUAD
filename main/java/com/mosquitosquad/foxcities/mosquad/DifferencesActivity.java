package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Tyler Gotz on 2/24/2016.
 */
public class DifferencesActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_differences);
        ImageView iv = (ImageView) findViewById(R.id.familyIV);
        TextView textView = (TextView) findViewById(R.id.differentContentTV);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_different, menu);
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
        else if(id == R.id.services)
        {
            intent = new Intent(this, ServicesActivity.class);
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
