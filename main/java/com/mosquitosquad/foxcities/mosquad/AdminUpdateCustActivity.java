package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Gotz on 3/31/2016.
 */
public class AdminUpdateCustActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.update_cust_info);
        final EditText fname = (EditText) findViewById(R.id.custFName);
        final EditText lname = (EditText) findViewById(R.id.custLName);
        final EditText address = (EditText) findViewById(R.id.custStreetAddress);
        final EditText address2 = (EditText) findViewById(R.id.custAddress2);
        final EditText city = (EditText) findViewById(R.id.custCity);
        final EditText state = (EditText) findViewById(R.id.custState);
        final EditText zip = (EditText) findViewById(R.id.custZip);
        final EditText phone = (EditText) findViewById(R.id.custPhone);
        final EditText email = (EditText) findViewById(R.id.custEmail);
        final Button update = (Button) findViewById(R.id.updateBtn);
        final Button delete = (Button) findViewById(R.id.deleteBtn);
        final Intent intent = new Intent(getApplicationContext(), AdminCustListActivity.class);

        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com");
        Query query = firebase.child("users").child("clients");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue().toString();
                final String[] values = value.split("=");
                Bundle bundle = getIntent().getExtras();
                if(bundle != null) {
                    final String name = bundle.getString("name");
                    String[] fullName = name.split(" ");
                    fname.setText(fullName[0]);
                    lname.setText(fullName[1]);

                    for(int i = 0; i < values.length; i++)
                    {
                        if (values[i].substring(7).equals("fullName")) {
                            String names = values[i + 1].substring(0, values[i + 1].length() - 15);
                            if(names.equals(name))
                            {
                                address.setText(values[i+2].substring(0, values[i+2].length() - 6));
                                address2.setText(values[i+4].substring(0, values[i+4].length() - 7));
                                city.setText(values[i+3].substring(0, values[i+3].length() - 14));
                                state.setText(values[i+5].substring(0, values[i+5].length() - 13));
                                zip.setText(values[i].substring(0, values[i].length() - 10));
                                phone.setText(values[i+6].substring(0, values[i+6].length() - 14));
                                email.setText(values[i + 7].substring(0, values[i + 7].indexOf("}")));

                                //values[i-2].substring(values[i-2].indexOf("}")+2)); user id


                            }
                        }
                    }

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> updateInfo = new HashMap<String, String>();
                            updateInfo.put("fullName", fname.getText().toString() + " " + lname.getText().toString());
                            updateInfo.put("streetAddress", address.getText().toString());
                            updateInfo.put("addressLine2", address2.getText().toString());
                            updateInfo.put("city", city.getText().toString());
                            updateInfo.put("state", state.getText().toString());
                            updateInfo.put("zipCode", zip.getText().toString());
                            updateInfo.put("phoneNumber", phone.getText().toString());
                            updateInfo.put("emailAddress", email.getText().toString());
                            for(int j = 0; j < values.length; j++) {
                                if (values[j].substring(7).equals("fullName")) {
                                    String names = values[j + 1].substring(0, values[j + 1].length() - 15);
                                    if (names.equals(name)) {
                                        firebase.child("users").child("clients").child(values[j-2].substring(values[j-2].indexOf(" ")+1).replace('{', ' ').replaceAll(" ", "")).setValue(updateInfo);
                                    }
                                }
                            }
                            startActivity(intent);
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> deleteInfo = new HashMap<String, String>();
                            deleteInfo.put("fullName", "");
                            deleteInfo.put("streetAddress", "");
                            deleteInfo.put("addressLine2", "");
                            deleteInfo.put("city", "");
                            deleteInfo.put("state", "");
                            deleteInfo.put("zipCode", "");
                            deleteInfo.put("phoneNumber", "");
                            deleteInfo.put("emailAddress", "");
                            for (int j = 0; j < values.length; j++) {
                                if (values[j].substring(7).equals("fullName")) {
                                    String names = values[j + 1].substring(0, values[j + 1].length() - 15);
                                    if (names.equals(name)) {
                                        firebase.child("users").child("clients").child(values[j - 2].substring(values[j - 2].indexOf(" ")+1).replace('{', ' ').replaceAll(" ", "")).setValue(deleteInfo);
                                    }
                                }
                            }

                            startActivity(intent);
                        }


                    });
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
    }
}
