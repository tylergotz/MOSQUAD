package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tyler Gotz on 3/15/2016.
 */
public class RegisterActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.action_register);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        final EditText fname = (EditText) findViewById(R.id.firstnameEditText);
        final EditText lname = (EditText) findViewById(R.id.lastnameEditText);
        final EditText streetAddress = (EditText) findViewById(R.id.addressLine1EditText);
        final EditText addressLine2 = (EditText) findViewById(R.id.adressLine2EditText);
        final EditText city = (EditText) findViewById(R.id.cityEditText);
        final EditText state = (EditText) findViewById(R.id.stateEditText);
        final EditText zip = (EditText) findViewById(R.id.zipCodeEditText);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneEditText);
        final EditText email = (EditText) findViewById(R.id.registeremailEditText);
        final EditText password = (EditText) findViewById(R.id.registerpasswordEditText);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        final Context context = this;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Button done = (Button) findViewById(R.id.signUpBtn);
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals(" ")) {
                    firebase.createUser(email.getText().toString(), confirmPassword.getText().toString(), new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            alertDialogBuilder.setMessage("Registration Successful").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebase.authWithPassword(email.getText().toString(), confirmPassword.getText().toString(), new Firebase.AuthResultHandler() {
                                        @Override
                                        public void onAuthenticated(AuthData authData) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("fullName", fname.getText().toString() + " " + lname.getText().toString());
                                            map.put("streetAddress", streetAddress.getText().toString());
                                            map.put("addressLine2", addressLine2.getText().toString());
                                            map.put("city", city.getText().toString());
                                            map.put("state", state.getText().toString());
                                            map.put("zipCode", zip.getText().toString());
                                            map.put("phoneNumber", phoneNumber.getText().toString());
                                            map.put("emailAddress", email.getText().toString());
                                            firebase.child("users").child("clients").child(authData.getUid()).setValue(map);
                                            Bundle bundle = getIntent().getExtras();
                                            if(bundle != null)
                                            {
                                                Boolean userIsAdmin = bundle.getBoolean("userIsAdmin");
                                                if(userIsAdmin == true)
                                                {
                                                    firebase.unauth();
                                                    Intent intent = new Intent(context, AdminCustListActivity.class);
                                                    startActivity(intent);
                                                }

                                            }

                                            Intent intent = new Intent(context, CustomerHomeScreen.class);
                                            intent.putExtra("uid", authData.getUid());
                                            intent.putExtra("email", email.getText().toString());
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onAuthenticationError(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            });
                            AlertDialog dialog = alertDialogBuilder.create();
                            dialog.show();


                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {

                        }
                    });


                }
        }

    });
}
}
