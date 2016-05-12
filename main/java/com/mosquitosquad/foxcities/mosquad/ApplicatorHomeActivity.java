package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Tyler Gotz on 5/1/2016.
 */
public class ApplicatorHomeActivity extends Activity
{
    private TextView nameTextView;
    private Button sprays, messages;
    private static final String FIREBASE_REF = "https://mosquito-squad.firebaseio.com/";
    private String licenseNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_applicator);
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase(FIREBASE_REF);
        nameTextView = (TextView) findViewById(R.id.techNameTV);
        sprays = (Button) findViewById(R.id.spraysBTN);
        messages = (Button) findViewById(R.id.techMessagesBTN);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            licenseNum = bundle.getString("licenseNum");
        }

        Query query = firebase.child("admin").child("applicators").child(licenseNum);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                nameTextView.setText(dataSnapshot.getValue().toString());
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

        sprays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ApplicatorSprayList.class);
                intent.putExtra("appName", nameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }
}

