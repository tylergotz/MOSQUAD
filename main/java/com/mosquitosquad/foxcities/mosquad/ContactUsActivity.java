package com.mosquitosquad.foxcities.mosquad;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_contactus);

        final Context context = this;
        TextView phoneTV = (TextView) findViewById(R.id.phoneNumberTV);
        phoneTV.setPaintFlags(phoneTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();

            }
        });

        ImageButton emailBtn = (ImageButton) findViewById(R.id.emailButton);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        ImageButton twitBtn = (ImageButton) findViewById(R.id.twitterButton);
        twitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSocialMedia("https://twitter.com/MosquitoSquadFC");
            }
        });

        ImageButton fbBtn = (ImageButton) findViewById(R.id.facebookButton);
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSocialMedia("https://www.facebook.com/MosquitoSquadofthefoxcities");
            }
        });

        ImageButton gBtn = (ImageButton) findViewById(R.id.gButton);
        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSocialMedia("https://plus.google.com/118064180717687173220/posts");
            }
        });

        ImageButton youtubeBtn = (ImageButton) findViewById(R.id.youtubeButton);
        youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSocialMedia("https://www.youtube.com/channel/UCQz9Ay86FA_hvoWymXoxHeQ");
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultationActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

    }

    void goToSocialMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    void sendMail() {
        String recipient = "tygotz@yahoo.com";
        String subject = "Ask Mosquito Squad";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("plain/text");
        i.putExtra(Intent.EXTRA_EMAIL, recipient);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(i, "Choose an Email client :"));
    }

    private void call() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:9205584330"));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            activityException.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contactus, menu);
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
        if (id == R.id.action_home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_different) {
            intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.services) {
            intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_locations) {
            intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.login) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

