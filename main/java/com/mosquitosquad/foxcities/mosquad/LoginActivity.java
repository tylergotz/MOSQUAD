package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Gotz on 2/29/2016.
 */
public class LoginActivity extends AppCompatActivity
{
    String url = "http://foxcities.mosquitosquad.com/";
    @Override
    protected void onCreate(final Bundle savedInstance) {
        super.onCreate(savedInstance);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com");
        new getDread().execute();
        final EditText emailET = (EditText) findViewById(R.id.emailEditText);
        final EditText passwordET = (EditText) findViewById(R.id.passwordEditText);
        final Context context = this;
        Button loginBtn = (Button) findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.authWithPassword(emailET.getText().toString(), passwordET.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent;
                        String user = emailET.getText().toString();
                        String[] userParts = user.split("@");
                        if (userParts[0].equals("tygotz")) {
                            intent = new Intent(context, AdminHomeActivity.class);
                            startActivity(intent);
                        } else if(userParts[1].equals("test.com"))
                        {
                            intent = new Intent(context, ApplicatorHomeActivity.class);
                            intent.putExtra("licenseNum", userParts[0]);
                            startActivity(intent);
                        }
                        else
                        {
                            Boolean comesFromRate = null;
                            Bundle bundle = getIntent().getExtras();
                            if (bundle != null)
                            {
                                comesFromRate = bundle.getBoolean("comesFromRate");
                                if(comesFromRate == true)
                                {
                                    Intent toPay = new Intent(context, PaymentCustomerActivity.class);
                                    toPay.putExtra("checkBoxA", bundle.getBoolean("checkBoxA"));
                                    toPay.putExtra("checkBoxB", bundle.getBoolean("checkBoxB"));
                                    toPay.putExtra("checkBoxC", bundle.getBoolean("checkBoxC"));
                                    toPay.putExtra("tickCB", bundle.getBoolean("tickCB"));
                                    toPay.putExtra("propertySize", bundle.getString("propertySize"));
                                    toPay.putExtra("plan", bundle.getString("plan"));
                                    toPay.putExtra("totalprice", bundle.getString("totalprice"));
                                    toPay.putExtra("tax", bundle.getString("tax"));
                                    toPay.putExtra("grandTotal", bundle.getString("grandTotal"));
                                    startActivity(toPay);
                                }

                            }
                            else
                            {
                                intent = new Intent(context, CustomerHomeScreen.class);
                                intent.putExtra("uid", authData.getUid());
                                intent.putExtra("email", emailET.getText().toString());
                                startActivity(intent);
                            }

                        }


                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });


            }
        });

        TextView register = (TextView) findViewById(R.id.registerTextView);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, RegisterActivity.class);
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

    private class getDread extends AsyncTask<Void, Void, Void>
    {
        String content;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("img[class=dread]");
                content = element.attr("src");

                InputStream input = new java.net.URL(content).openStream();
                bitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ImageView imageView = (ImageView) findViewById(R.id.dreadImageView);
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if (id == R.id.action_home)
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_contactus)
        {
            intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_different)
        {
            intent = new Intent(this, DifferencesActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.services)
        {
            intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_locations)
        {
            intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
            return true;
        }else if(id == android.R.id.home)
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
