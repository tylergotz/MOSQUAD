package com.mosquitosquad.foxcities.mosquad;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tyler Gotz on 3/1/2016.
 */
public class ConsultationActivity extends Activity
{
    TextView textViewname, textViewaddress, textViewemail, textViewPhone, textViewHear, textViewInterested;
    CheckBox mosquito, ticks;
    EditText editTextFName, editTextLName, editTextEmail, editText, editText2, editText3, editText4, editText5, editText6;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_consult);

        textViewname = (TextView) findViewById(R.id.textView22);
        textViewaddress = (TextView) findViewById(R.id.textView24);
        textViewemail = (TextView) findViewById(R.id.textView23);
        textViewPhone = (TextView) findViewById(R.id.textView25);
        textViewHear = (TextView) findViewById(R.id.textView26);
        textViewInterested = (TextView) findViewById(R.id.textView21);

        mosquito = (CheckBox) findViewById(R.id.checkBox);
        ticks = (CheckBox) findViewById(R.id.checkBox2);

        editTextFName = (EditText) findViewById(R.id.editTextFName);
        editTextLName = (EditText) findViewById(R.id.editTextLName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select One:");
        spinnerArray.add("Print Ad");
        spinnerArray.add("Online Ad");
        spinnerArray.add("TV Spot");
        spinnerArray.add("Radio Spot");
        spinnerArray.add("Yard Sign");
        spinnerArray.add("Truck");
        spinnerArray.add("Door Hanger");
        spinnerArray.add("Postcard/Mailer");
        spinnerArray.add("Friend");
        spinnerArray.add("Google/Search");
        spinnerArray.add("Mobile App");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        final Context context = this;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Button sendBtn = (Button) findViewById(R.id.buttonSendRequest);
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                alertDialogBuilder.setTitle("Mosquito Squad");

                if(!mosquito.isChecked() && !ticks.isChecked())
                {
                    alertDialogBuilder.setMessage("Please select what interests you.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            }

                    );
                }
                else if(editTextFName.getText().toString().isEmpty() || editTextLName.getText().toString().isEmpty())
                {
                    alertDialogBuilder.setMessage("Please enter name.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            }

                    );
                }
                else if(editText.getText().toString().isEmpty() || editText3.getText().toString().isEmpty() || editText4.getText().toString().isEmpty() || editText5.getText().toString().isEmpty())
                {
                    alertDialogBuilder.setMessage("Address contents missing").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            }

                    );
                }
                else if(editTextEmail.getText().toString().isEmpty() || editText6.getText().toString().isEmpty())
                {
                    alertDialogBuilder.setMessage("Phone number or email is missing").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            }

                    );
                }
                else if(spinner.getSelectedItem().toString().equals("Select One:"))
                {
                    alertDialogBuilder.setMessage("Please select how you heard about us.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            }

                    );
                }
                else
                {
                    final String p1;

                    if(mosquito.isChecked())
                    {
                        p1 = textViewHear.toString() + mosquito.toString();
                    }
                    else if(ticks.isChecked())
                    {
                        p1 = textViewHear.toString() + ticks.toString();
                    }
                    else
                    {
                        p1 = textViewHear.toString() + mosquito.toString() + "and " + ticks.toString();
                    }

                    final String p2 = textViewname.toString() + editTextFName.getText().toString() + editTextLName.getText().toString();
                    final String p3;
                    if(editText2.getText().toString().isEmpty())
                    {
                        p3 = textViewaddress.toString() + editText.getText().toString() + editText3.getText().toString() + editText4.getText().toString() + editText5.getText().toString();
                    }
                    else
                    {
                        p3 = textViewaddress.toString() + editText.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString() + editText5.getText().toString();

                    }

                    final String p4 = textViewPhone.toString() + editText6.getText().toString();
                    final String p5 = textViewemail.toString() + editTextEmail.getText().toString();
                    final String p6 = textViewHear.toString() + spinner.getSelectedItem().toString();
                    alertDialogBuilder.setMessage("Somebody will get back to you as soon as possible.").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Mail m = new Mail("tyler.k.gotz@gmail.com", "Jetfavre04$");

                                    String[] toArr = {"tyler.k.gotz@gmail.com"};
                                    m.set_to(toArr);
                                    m.set_from("tygotz@yahoo.com");
                                    m.set_subject("Free Consultation");
                                    m.setBody("A customer wants free consultation");

                                    try {
                                        m.addAttachment(createPDF(p1,p2,p3,p4,p5,p6).toString());
                                        m.send();
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                    );
                }
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

    }



    private Document createPDF(String s1, String s2, String s3, String s4, String s5, String s6) throws FileNotFoundException, DocumentException
    {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "pdfDemos");
        if(!pdfFolder.exists())
        {
            pdfFolder.mkdir();
        }


        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document doc = new Document();

        PdfWriter.getInstance(doc, output);

        doc.open();

        doc.add(new Paragraph(s1));
        doc.add(new Paragraph(s2));
        doc.add(new Paragraph(s3));
        doc.add(new Paragraph(s4));
        doc.add(new Paragraph(s5));
        doc.add(new Paragraph(s6));


        doc.close();

        return doc;
    }



}
