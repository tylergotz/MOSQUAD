package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    String url = "http://foxcities.mosquitosquad.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        new HTMLParse().execute();
    }

    private class HTMLParse extends AsyncTask<Void, Void, Void>
    {
        String content;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("img[alt=INC. 5000 Mosquito Squad]");
                content = element.attr("src");

                InputStream input = new java.net.URL(content).openStream();
                bitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
         return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_locations)
        {
            intent = new Intent(this, LocationActivity.class);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
