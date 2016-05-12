package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 5/2/2016.
 */
public class ApplicatorSprayList extends Activity
{
    private static final String FIREBASE_REF = "https://mosquito-squad.firebaseio.com/";
    private ListView listView;
    private ArrayList<String> spraysList;
    private String name, truck;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicator_spray_list);
        Firebase.setAndroidContext(this);
        listView = (ListView) findViewById(R.id.spraysListView);
        spraysList = new ArrayList<>();
        Firebase firebase = new Firebase(FIREBASE_REF);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            name = bundle.getString("appName");
        }
        Query query = firebase.child("assignedSprays");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] values = dataSnapshot.getValue().toString().split("=");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        SpraysListAdapter spraysListAdapter = new SpraysListAdapter(this, spraysList);
        listView.setAdapter(spraysListAdapter);

        button = (Button) findViewById(R.id.routeBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putStringArrayList("sprays", spraysList);
                b.putString("truck", truck);
                Intent intent = new Intent(ApplicatorSprayList.this, RouteSprays.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }

    public class SpraysListAdapter extends ArrayAdapter<String> {
        public SpraysListAdapter(Context context, List<String> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String s = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spray_list_row, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.nameTextView);
            // Populate the data into the template view using the data object
            tvName.setText(s);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
