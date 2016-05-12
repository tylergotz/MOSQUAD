package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Gotz on 3/22/2016.
 */
public class AdminAddAppActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_app_action);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com");
        final Context context = this;
        final EditText fName = (EditText) findViewById(R.id.firstnameeditText);
        final EditText lname = (EditText) findViewById(R.id.lastnameeditText);
        final EditText licenseNum = (EditText) findViewById(R.id.licenseNumEditText);
        final EditText password = (EditText) findViewById(R.id.passwordEditTxt);

        Button button = (Button) findViewById(R.id.addAppButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.createUser(licenseNum.getText().toString() + "@test.com", password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("fullName", fName.getText().toString() + " " + lname.getText().toString());
                        firebase.child("admin").child("applicators").child(licenseNum.getText().toString()).setValue(map);
                        Intent intent = new Intent(context, AdminAppListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                    }
                });

            }
        });


    }
}