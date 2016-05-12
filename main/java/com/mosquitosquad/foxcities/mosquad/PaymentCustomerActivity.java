package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/16/2016.
 */
public class PaymentCustomerActivity extends Activity
{
    private static final String PUBLISHABLE_KEY = "pk_test_YVj8HSfGFgdo92juXqKP7HzQ";
    private CheckBox checkBoxA, checkBoxB, checkBoxC, tick;
    private TextView totalprice, tax, grandTotal, date;
    private Spinner propertySize, plan;
    private Button makePayment, dateButton;
    private DatePickerDialog startDatePicker;
    private SimpleDateFormat dateFormat;
    private EditText cardNumber, CVC, mm, YYYY;
    private String customer;
    private int numSprays;
    private Sprays sprays;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_rates);
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        totalprice = (TextView) findViewById(R.id.priceTV);
        tax = (TextView) findViewById(R.id.taxesTV);
        grandTotal = (TextView) findViewById(R.id.grandTotalTV);
        checkBoxA = (CheckBox) findViewById(R.id.checkBoxA);
        checkBoxB = (CheckBox) findViewById(R.id.checkBoxB);
        checkBoxC = (CheckBox) findViewById(R.id.checkBoxC);
        tick = (CheckBox) findViewById(R.id.tickCB);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        CVC = (EditText) findViewById(R.id.cvvEditText);
        mm = (EditText) findViewById(R.id.mmET);
        YYYY = (EditText) findViewById(R.id.yyET);
        propertySize = (Spinner) findViewById(R.id.propSizeSpinner);
        final List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Property Size:");
        spinnerArray.add("1/2 acre or less");
        spinnerArray.add("1/2 acre to 1 acre");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertySize.setAdapter(adapter);
        propertySize.setVisibility(View.INVISIBLE);
        plan = (Spinner) findViewById(R.id.payplanSpinner);
        plan.setVisibility(View.INVISIBLE);
        final List<String> payplan = new ArrayList<>();
        payplan.add("Pick a plan:");
        payplan.add("Trial or Holiday Spray");
        payplan.add("6 weeks (All Nat)");
        payplan.add("9 weeks (Trad)");
        payplan.add("Full Season");
        ArrayAdapter<String> planadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payplan);
        planadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan.setAdapter(planadapter);
        sprays = new Sprays(numSprays, null);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            checkBoxA.setChecked(bundle.getBoolean("checkBoxA"));
            checkBoxB.setChecked(bundle.getBoolean("checkBoxB"));
            checkBoxC.setChecked(bundle.getBoolean("checkBoxC"));
            tick.setChecked(bundle.getBoolean("tickCB"));
            propertySize.setVisibility(View.VISIBLE);
            propertySize.setSelection(adapter.getPosition(bundle.getString("propertySize")));
            plan.setVisibility(View.VISIBLE);
            plan.setSelection(planadapter.getPosition(bundle.getString("plan")));
            totalprice.setText(bundle.getString("totalprice"));
            tax.setText(bundle.getString("tax"));
            grandTotal.setText(bundle.getString("grandTotal"));

        }

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundleName");
        customer = b.getString("author");

        makePayment = (Button) findViewById(R.id.paymentButton);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndChargeCard(grandTotal, cardNumber, CVC, mm, YYYY);
                Map<String, String> map = new HashMap<>();
                if (checkBoxA.isChecked()) {
                    map.put("sprayType", checkBoxA.getText().toString());
                } else if (checkBoxB.isChecked()) {
                    map.put("sprayType", checkBoxB.getText().toString());
                } else {
                    map.put("sprayType", checkBoxC.getText().toString());
                }
                map.put("propertySize", propertySize.getSelectedItem().toString());
                map.put("payPlan", plan.getSelectedItem().toString());
                map.put("startDate", date.getText().toString());
                if (plan.getSelectedItem().toString().equals("Full Season")) {
                    map.put("numberOfSprays", String.valueOf(sprays.getNumSprays()));
                } else if (plan.getSelectedItem().toString().equals("9 weeks (Trad)") || plan.getSelectedItem().equals("6 weeks (All Nat)")) {
                    map.put("numberOfSprays", String.valueOf(3));
                } else {
                    map.put("numberOfSprays", String.valueOf(1));
                }
                if (tick.isChecked()) {
                    map.put("tickTreatment", "yes");
                } else {
                    map.put("tickTreatment", "no");
                }

                firebase.child("serviceSchedule").child(customer).setValue(map);
            }
        });

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        date = (TextView) findViewById(R.id.dateTV);
        dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                startDatePicker = new DatePickerDialog(PaymentCustomerActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar startDate = Calendar.getInstance();
                        startDate.set(year, monthOfYear, dayOfMonth);
                        date.setText(dateFormat.format(startDate.getTime()));
                        if (plan.getSelectedItem().toString().equals("Full Season")) {
                            int curr = 0;
                            if (propertySize.getSelectedItem().toString().equals("1/2 acre or less") && checkBoxA.isChecked()) {
                                curr = 49;
                            } else if (propertySize.getSelectedItem().toString().equals("1/2 acre to 1 acre") && checkBoxA.isChecked()) {
                                curr = 69;
                            } else if (propertySize.getSelectedItem().toString().equals("1/2 acre or less") && checkBoxB.isChecked()) {
                                curr = 62;
                            } else if (propertySize.getSelectedItem().toString().equals("1/2 acre to 1 acre") && checkBoxB.isChecked()) {
                                curr = 86;
                            }
                            double taxes = 0, total = 0;
                            Calendar compareDate1 = Calendar.getInstance();
                            compareDate1.set(year, Calendar.MAY, 1);
                            Calendar compareDate2 = Calendar.getInstance();
                            compareDate2.set(year, Calendar.MAY, 21);
                            if (startDate.getTime().after(compareDate1.getTime()) && startDate.getTime().before(compareDate2.getTime())) {
                                numSprays = 8;
                                curr = curr * numSprays;

                            } else {
                                compareDate1.set(year, Calendar.MAY, 22);
                                compareDate2.set(year, Calendar.JUNE, 11);
                                if (startDate.getTime().after(compareDate1.getTime()) && startDate.getTime().before(compareDate2.getTime())) {
                                    numSprays = 7;
                                    curr = curr * numSprays;
                                } else {
                                    compareDate1.set(year, Calendar.JUNE, 12);
                                    compareDate2.set(year, Calendar.JULY, 2);
                                    if (startDate.getTime().after(compareDate1.getTime()) && startDate.getTime().before(compareDate2.getTime())) {
                                        numSprays = 6;
                                        curr = curr * numSprays;
                                    } else {
                                        compareDate1.set(year, Calendar.JULY, 3);
                                        compareDate2.set(year, Calendar.JULY, 23);
                                        if (startDate.getTime().after(compareDate1.getTime()) && startDate.getTime().before(compareDate2.getTime())) {
                                            numSprays = 5;
                                            curr = curr * numSprays;
                                        } else {
                                            compareDate1.set(year, Calendar.JULY, 24);
                                            compareDate2.set(year, Calendar.AUGUST, 13);
                                            if (startDate.getTime().after(compareDate1.getTime()) && startDate.getTime().before(compareDate2.getTime())) {
                                                numSprays = 4;
                                                curr = curr * numSprays;
                                            } else {
                                                numSprays = 3;
                                                curr = curr * numSprays;
                                            }
                                        }
                                    }


                                }
                            }
                            sprays.setNumSprays(numSprays);
                            totalprice.setText(String.valueOf(curr));
                            DecimalFormat df = new DecimalFormat("0.00");
                            taxes = curr * .05;
                            tax.setText(df.format(taxes));
                            total = curr + taxes;
                            grandTotal.setText(df.format(total));
                        }

                    }
                }, calendar.get(Calendar.YEAR), 4, 1);

                startDatePicker.show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {

            case R.id.checkBoxA:
                checkBoxB.setChecked(false);
                checkBoxC.setChecked(false);
                propertySize.setVisibility(View.VISIBLE);
                propertySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String text = propertySize.getSelectedItem().toString();
                        if (text.equals("1/2 acre to 1 acre")) {
                            totalprice.setText(String.valueOf(69));
                        } else {
                            totalprice.setText(String.valueOf(49));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                plan.setVisibility(View.VISIBLE);

                plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = plan.getSelectedItem().toString();
                        int currTotal = Integer.parseInt(totalprice.getText().toString());
                        double taxes;
                        if (selected.equals("9 weeks (Trad)")) {
                            if (currTotal == 392 || currTotal == 552) {
                                currTotal = (currTotal / 8) * 3;
                            } else {
                                currTotal = currTotal * 3;
                            }

                        } else if (selected.equals("Full Season")) {
                            if (currTotal == 147 || currTotal == 207) {
                                currTotal = currTotal * 3;
                            } else {
                                currTotal = currTotal * 8;
                            }
                        } else if (selected.equals("Trial or Holiday Spray")) {
                            if(currTotal == 392 || currTotal == 552)
                            {
                                currTotal = currTotal / 8;

                            }
                            else if(currTotal == 147 || currTotal == 207)
                            {
                                currTotal = currTotal / 3;
                            }
                        }
                        taxes = currTotal * .05;
                        DecimalFormat df = new DecimalFormat("0.00");
                        totalprice.setText(String.valueOf(currTotal));
                        tax.setText(df.format(taxes));
                        double total = taxes + currTotal;
                        grandTotal.setText(df.format(total));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                break;

            case R.id.checkBoxB:

                checkBoxC.setChecked(false);
                checkBoxA.setChecked(false);
                propertySize.setVisibility(View.VISIBLE);
                propertySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String text = propertySize.getSelectedItem().toString();

                        if (text.equals("1/2 acre to 1 acre")) {
                            totalprice.setText(String.valueOf(86));
                        } else {
                            totalprice.setText(String.valueOf(62));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                plan.setVisibility(View.VISIBLE);
                plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = plan.getSelectedItem().toString();
                        int currTotal = Integer.parseInt(totalprice.getText().toString());
                        double taxes;
                        if (selected.equals("6 weeks (All Nat)")) {
                            if (currTotal == 496 || currTotal == 688) {
                                currTotal = (currTotal / 8) * 3;
                            } else {
                                currTotal = currTotal * 3;
                            }
                        } else if (selected.equals("Full Season")) {
                            if (currTotal == 186 || currTotal == 258) {
                                currTotal = currTotal * 3;
                            } else {
                                currTotal = currTotal * 8;
                            }
                        } else if (selected.equals("Trial or Holiday Spray")) {
                            if(currTotal == 496 || currTotal == 688)
                            {
                                currTotal = currTotal / 8;

                            }
                            else if(currTotal == 186 || currTotal == 258)
                            {
                                currTotal = currTotal / 3;
                            }
                        }

                        totalprice.setText(String.valueOf(currTotal));
                        taxes = currTotal * .05;
                        DecimalFormat df = new DecimalFormat("0.00");
                        tax.setText(df.format(taxes));
                        double total = taxes + currTotal;
                        grandTotal.setText(df.format(total));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                break;

            case R.id.checkBoxC:

                checkBoxA.setChecked(false);
                checkBoxB.setChecked(false);
                propertySize.setVisibility(View.VISIBLE);
                propertySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String text = propertySize.getSelectedItem().toString();

                        if (text.equals("1/2 acre to 1 acre")) {
                            totalprice.setText(String.valueOf(150));
                        } else {
                            totalprice.setText(String.valueOf(100));
                        }
                        double taxes = Integer.parseInt(totalprice.getText().toString()) * .05;
                        DecimalFormat df = new DecimalFormat("0.00");
                        tax.setText(df.format(taxes));
                        double total = taxes + Integer.parseInt(totalprice.getText().toString());
                        grandTotal.setText(df.format(total));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                plan.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void saveAndChargeCard(final TextView grandTotal, EditText cardNumber, EditText CVC, EditText mm, EditText YYYY)
    {
        Card card = new Card(cardNumber.getText().toString(), Integer.valueOf(mm.getText().toString()), Integer.valueOf(YYYY.getText().toString()), CVC.getText().toString());
        if(card.validateCard())
        {
            new Stripe().createToken(card, PUBLISHABLE_KEY, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating Token",
                            Toast.LENGTH_LONG
                    ).show();
                }

                @Override
                public void onSuccess(Token token) {
                    try {
                        final Map<String, Object> chargeParams = new HashMap<String, Object>();
                        chargeParams.put("amount", Integer.valueOf(grandTotal.getText().toString().replace(".", "")));
                        chargeParams.put("currency", "usd");
                        chargeParams.put("source", token.getId());
                        chargeParams.put("description", "Mosquito Squad Service");
                        com.stripe.Stripe.apiKey = PUBLISHABLE_KEY;

                        new AsyncTask<Void, Void, Void>() {
                            Charge charge;
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    com.stripe.Stripe.apiKey = "sk_test_VmKyTVUWG2Q2aB6t6EdCh8cr";
                                    charge = Charge.create(chargeParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            protected void onPostExecute(Void result) {
                                Toast.makeText(getApplicationContext(), "Charge Successful",
                                        Toast.LENGTH_LONG).show();
                            }
                        }.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else if(!card.validateCard())
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid Card Number",
                    Toast.LENGTH_LONG
            ).show();
        }
        else if(!card.validateExpiryDate())
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid Expiration Date",
                    Toast.LENGTH_LONG
            ).show();
        }
        else if(!card.validateCVC())
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid CVC",
                    Toast.LENGTH_LONG
            ).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid Card Details",
                    Toast.LENGTH_LONG
            ).show();
        }
    }



}
