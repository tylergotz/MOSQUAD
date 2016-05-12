package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/26/2016.
 */
public class AdminCalendar extends AppCompatActivity {
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_calendar);
        Firebase.setAndroidContext(this);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        caldroidFragment = new CaldroidFragment();

        if(savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        else
        {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }
        final CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Intent intent = new Intent(getApplicationContext(), ListOfSpraysForDay.class);
                intent.putExtra("sprayDate", formatter.format(date));
                startActivity(intent);
            }
        };


        final Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);
        showDialogButton.setVisibility(View.INVISIBLE);

        caldroidFragment.setCaldroidListener(caldroidListener);

        Query query = firebase.child("serviceSchedule");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] values = dataSnapshot.getValue().toString().split("=");
                String date = "";
                int numSprays = 0;
                String sprayType = "";
                for (int i = 0; i < values.length; i++) {
                    if(values[i].equals("{startDate"))
                    {
                        date = values[i+1].substring(0, values[i+1].indexOf(","));
                        numSprays = Integer.parseInt(values[i+4].substring(0, values[i+4].indexOf(",")));
                        sprayType = values[i+5].substring(0, values[i+5].indexOf(","));
                    }

                }
                setCustomResourcesForDates(date, numSprays, sprayType);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
    }

    private void setCustomResourcesForDates(String dates, int numSprays, String sprayType)
    {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Map<Date, Drawable> map = new HashMap<>();
        if(sprayType.equals("Traditional"))
        {
            for(int i = 0; i < numSprays; i++)
            {
                try
                {
                    cal.setTime(df.parse(dates));
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }
                cal.add(Calendar.DATE, i * 21);
                map.put(cal.getTime(), new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            }
        }else if(sprayType.equals("All Natural"))
        {
            for(int i = 0; i < numSprays; i++)
            {
                try
                {
                    cal.setTime(df.parse(dates));
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }
                cal.add(Calendar.DATE, i * 14);
                map.put(cal.getTime(), new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            }
        }

        caldroidFragment.setBackgroundDrawableForDates(map);
        caldroidFragment.refreshView();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }
}


