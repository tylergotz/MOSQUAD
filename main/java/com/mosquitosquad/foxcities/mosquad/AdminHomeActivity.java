package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Tyler Gotz on 3/22/2016.
 */
public class AdminHomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedIntance)
    {
        super.onCreate(savedIntance);
        setContentView(R.layout.admin_home);
        final Context context = this;

        Button appsbtn = (Button) findViewById(R.id.appBtn);
        appsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminAppListActivity.class);
                startActivity(intent);
            }
        });

        Button custBtn = (Button) findViewById(R.id.custBtn);
        custBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminCustListActivity.class);
                startActivity(intent);
            }
        });

        Button messagesBtn = (Button) findViewById(R.id.adminMessagesBtn);
        messagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminMessagesListActivity.class);
                startActivity(intent);
            }
        });

        Button scheduleBtn = (Button) findViewById(R.id.adminCalendarBtn);
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminCalendar.class);
                startActivity(intent);
            }
        });
    }
}
