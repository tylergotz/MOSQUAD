package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Gotz on 4/10/2016.
 */
public class RatesActivity extends Activity {

    private CheckBox checkBoxA, checkBoxB, checkBoxC, tick;
    private TextView totalprice, tax, grandTotal, pickStartTV, dateTV, expdate;
    private Spinner propertySize, plan;
    private Button makePayment, datePicker;
    private EditText cardNumber, cvv, mm, yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        totalprice = (TextView) findViewById(R.id.priceTV);
        tax = (TextView) findViewById(R.id.taxesTV);
        grandTotal = (TextView) findViewById(R.id.grandTotalTV);
        checkBoxA = (CheckBox) findViewById(R.id.checkBoxA);
        checkBoxB = (CheckBox) findViewById(R.id.checkBoxB);
        checkBoxC = (CheckBox) findViewById(R.id.checkBoxC);
        tick = (CheckBox) findViewById(R.id.tickCB);
        propertySize = (Spinner) findViewById(R.id.propSizeSpinner);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Property Size:");
        spinnerArray.add("1/2 acre or less");
        spinnerArray.add("1/2 acre to 1 acre");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertySize.setAdapter(adapter);
        propertySize.setVisibility(View.INVISIBLE);
        plan = (Spinner) findViewById(R.id.payplanSpinner);
        plan.setVisibility(View.INVISIBLE);
        List<String> payplan = new ArrayList<>();
        payplan.add("Pick a plan:");
        payplan.add("Trial or Holiday Spray");
        payplan.add("6 weeks (All Nat)");
        payplan.add("9 weeks (Trad)");
        payplan.add("Full Season");
        ArrayAdapter<String> planadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payplan);
        planadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan.setAdapter(planadapter);
        makePayment = (Button) findViewById(R.id.paymentButton);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("comesFromRate", true);
                intent.putExtra("checkBoxA", checkBoxA.isChecked());
                intent.putExtra("checkBoxB", checkBoxB.isChecked());
                intent.putExtra("checkBoxC", checkBoxC.isChecked());
                intent.putExtra("tickCB", tick.isChecked());
                intent.putExtra("propertySize", propertySize.getSelectedItem().toString());
                intent.putExtra("plan", plan.getSelectedItem().toString());
                intent.putExtra("totalprice", totalprice.getText().toString());
                intent.putExtra("tax", tax.getText().toString());
                intent.putExtra("grandTotal", grandTotal.getText().toString());
                startActivity(intent);
            }
        });

        datePicker = (Button) findViewById(R.id.dateButton);
        datePicker.setVisibility(View.INVISIBLE);
        pickStartTV = (TextView) findViewById(R.id.pickStartTV);
        pickStartTV.setVisibility(View.INVISIBLE);
        dateTV = (TextView) findViewById(R.id.dateTV);
        dateTV.setVisibility(View.INVISIBLE);

        expdate = (TextView) findViewById(R.id.expDateTV);
        expdate.setVisibility(View.INVISIBLE);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cardNumber.setVisibility(View.INVISIBLE);
        cvv = (EditText) findViewById(R.id.cvvEditText);
        cvv.setVisibility(View.INVISIBLE);
        mm = (EditText) findViewById(R.id.mmET);
        mm.setVisibility(View.INVISIBLE);
        yy = (EditText) findViewById(R.id.yyET);
        yy.setVisibility(View.INVISIBLE);

    }


    public void onCheckboxClicked(View view) {

        switch(view.getId()) {

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
                        }
                        else if(selected.equals("Trial or Holiday Spray"))
                        {
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
                        }else if(selected.equals("Trial or Holiday Spray"))
                        {
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

}