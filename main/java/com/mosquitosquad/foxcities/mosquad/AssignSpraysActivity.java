package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tyler Gotz on 4/26/2016.
 */
public class AssignSpraysActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener
{
    private TextView text1,text2,text3,text4,text5,text6, driverOfTruck;
    String date;
    ArrayList<String> listOfSprays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] driverArray = {"Truck 1", "Truck 2", "Truck 3"};
        Firebase.setAndroidContext(this);
        setContentView(R.layout.action_assign_sprays);
        text1 = (TextView) findViewById(R.id.name1);
        text1.setOnTouchListener(this);
        text2 = (TextView) findViewById(R.id.name2);
        text2.setOnTouchListener(this);
        text3 = (TextView) findViewById(R.id.name3);
        text3.setOnTouchListener(this);
        text4 = (TextView) findViewById(R.id.name4);
        text4.setOnTouchListener(this);
        text5 = (TextView) findViewById(R.id.name5);
        text5.setOnTouchListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            date = bundle.getString("date");
            listOfSprays = bundle.getStringArrayList("list");
        }
        final LinearLayout linear = (LinearLayout) findViewById(R.id.drivers);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(450,150);
        lp.setMargins(100, 100, 10, 100);

        final List<TextView> list = new ArrayList<>();

        for(int i = 0; i < driverArray.length; i++)
        {
            driverOfTruck = new TextView(this);
            driverOfTruck.setText(driverArray[i]);
            driverOfTruck.setTextSize(22);
            driverOfTruck.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            driverOfTruck.setBackgroundColor(Color.WHITE);
            driverOfTruck.setLayoutParams(lp);
            driverOfTruck.setId(i);
            list.add(driverOfTruck);
            linear.addView(driverOfTruck);
            driverOfTruck.setOnDragListener(this);
        }


        final Button assign = new Button(this);
        assign.setText("Next");
        assign.setTextSize(16);
        assign.setBackgroundColor(Color.BLUE);
        linear.addView(assign, lp);

        Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        Query query = firebase.child("admin").child("applicators");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue().toString();

                final String[] values = value.split("=");
                for (int i = 0; i < values.length; i++) //gets name
                {
                    if (values[i].equals("{fullName")) {
                        String name = values[i + 1].replace('}', ' ').replace(',', ' ').replaceAll("[0-9]", "");
                        if(text1.getText().toString().equals(""))
                        {
                           text1.setText(name);
                        }
                        else if(text2.getText().toString().equals(""))
                        {
                            text2.setText(name);
                        }
                        else if(text3.getText().toString().equals(""))
                        {
                            text3.setText(name);
                        }
                        else if(text4.getText().toString().equals(""))
                        {
                            text4.setText(name);
                        }
                        else if(text4.getText().toString().equals(""))
                        {
                            text2.setText(name);
                        }

                    }
                }


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


        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] driver = new String[list.size()];
                for(int i = 0; i < list.size(); i++)
                {
                    driver[i] = list.get(i).getText().toString();
                }

                Bundle b = new Bundle();
                b.putStringArrayList("list", listOfSprays);
                b.putStringArray("drivers", driver);
                b.putString("date", date);
                Intent intent = new Intent(getApplicationContext(), AssignToDriver.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction()==DragEvent.ACTION_DROP)
        {
            //handle the dragged view being dropped over a target view
            TextView dropped = (TextView)event.getLocalState();
            TextView dropTarget = (TextView) v;
            //stop displaying the view where it was before it was dragged
            dropped.setVisibility(View.INVISIBLE);

            //if an item has already been dropped here, there will be different string
            String text=dropTarget.getText().toString();
            //if there is already an item here, set it back visible in its original place
            if (text.equals(text1.getText().toString()))
            {
                text1.setVisibility(View.VISIBLE);
            } else if(text.equals(text2.getText().toString()))
            {
                text2.setVisibility(View.VISIBLE);

            }else if(text.equals(text3.getText().toString()))
            {
                text3.setVisibility(View.VISIBLE);

            }else if(text.equals(text4.getText().toString()))
            {
                text4.setVisibility(View.VISIBLE);

            }else if(text.equals(text5.getText().toString()))
            {
                text5.setVisibility(View.VISIBLE);

            }

            //update the text and color in the target view to reflect the data being dropped
            dropTarget.setText(dropped.getText());
            dropTarget.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }


        return true;
    }

    //When text1 or text2 or text3 gets clicked or touched then this method will be called
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
        else return false;
    }
}


