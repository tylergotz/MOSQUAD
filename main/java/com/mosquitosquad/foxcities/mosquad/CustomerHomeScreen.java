package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 3/14/2016.
 */
public class CustomerHomeScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.customer_homescreen);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        Firebase ref = new Firebase("https://mosquito-squad.firebaseio.com/");
        final String uid;
        Bundle bundle = getIntent().getExtras();
        final TextView textView = (TextView) findViewById(R.id.welcomeTextView);
        if(bundle != null)
        {
            uid = bundle.getString("uid");
            final String email = bundle.getString("email");
            final Query query = ref.orderByChild("users/clients/" + uid);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue().toString();
                    String[] values = value.split("=");
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].substring(values[i].indexOf(" ") + 1).equals("fullName") && textView.getText().toString().isEmpty()
                                && email.equals(values[i + 7].substring(0, values[i + 7].indexOf("}")))) {
                            String name = values[i + 1].replace('}', ' ').replace(',', ' ').replaceAll("[0-9]", "");
                            textView.setText("Welcome: " + name.substring(0, name.length() - 14));
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

            Button button = (Button) findViewById(R.id.messagesBTN);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CustMessageActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("author", textView.getText().toString().substring(9));
                    startActivity(intent);
                }
            });




        }

        List<String> serviceOption = new ArrayList<>();
        serviceOption.add("Schedule a Service");
        serviceOption.add("Services Schedule");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, serviceOption);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        final ListView listView = (ListView) findViewById(R.id.serviceOptionListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (listView.getItemAtPosition(position).equals("Schedule a Service")) {
                    intent = new Intent(getApplicationContext(), PaymentCustomerActivity.class);
                    Bundle b = new Bundle();
                    b.putString("author", textView.getText().toString().substring(9));
                    intent.putExtra("bundleName", b);
                    startActivity(intent);
                } else if (listView.getItemAtPosition(position).equals("Services Schedule")) {
                    intent = new Intent(getApplicationContext(), CustomerCalendar.class);
                    Bundle b = new Bundle();
                    b.putString("author", textView.getText().toString().substring(9));
                    intent.putExtra("bundleName", b);
                    startActivity(intent);
                }
            }
        });

    }
}
