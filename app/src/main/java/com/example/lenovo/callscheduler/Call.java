package com.example.lenovo.callscheduler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Call extends AppCompatActivity {
    Calendar calendar;
    EditText numberText;
    Button callButton;
    Button pickContact;
    TextView dateTimeView;
    Button dateTimeButton;
    Button setDate;
    Button setTime;
    SimpleDateFormat simpleDateFormat;
   // TimePicker timePicker;

    static final int PICK_CONTACT=1;
    String cNumber;
     String currentDateTimeString ;
     StringBuffer strBufTime = new StringBuffer();
     StringBuffer strBufDate = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);

        callButton=(Button)findViewById(R.id.call);
        numberText=(EditText)findViewById(R.id.number);
        pickContact=(Button)findViewById(R.id.pickContact);
        dateTimeButton=findViewById(R.id.dateTimeButton);
        dateTimeView=findViewById(R.id.dateTimeTextView);
        //timePicker=findViewById(R.id.timePicker1);
        setDate=findViewById(R.id.datePickerDialogButton);
        setTime=findViewById(R.id.timePickerDialogButton);


        //Call on tton ClickBu
        callButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                final String number=numberText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+number));
                    startActivity(intent);

            }
        });

        //Pick a contact from contact list
        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        dateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
               // dateTimeView.setText(currentDateTimeString);


                //For 24 hr format use the following code
                calendar=Calendar.getInstance();
                simpleDateFormat=new SimpleDateFormat("HH:mm");
                currentDateTimeString=simpleDateFormat.format(calendar.getTime());


                //For 12 hr format use the following code

                //Date d=new Date();
                //SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
                //currentDateTimeString = sdf.format(d);
                dateTimeView.setText(currentDateTimeString);
            }
        });









        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        //strBufDate.append("You select date is ");
                        strBufDate.append(year);
                        strBufDate.append("-");
                        strBufDate.append(month+1);
                        strBufDate.append("-");
                        strBufDate.append(dayOfMonth);

                        TextView datePickerValueTextView = (TextView)findViewById(R.id.dateTimeTextView);
                        datePickerValueTextView.setText(strBufDate.toString());
                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);

                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(Call.this, onDateSetListener, year, month, day);

                // Set dialog icon and title.
               // datePickerDialog.setIcon(R.drawable.if_snowman);
                datePickerDialog.setTitle("Please select date.");

                // Popup the dialog.
                datePickerDialog.show();
            }
        });



// Create and show a TimePickerDialog when click button.
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        TextView timePickerValueTextView = findViewById(R.id.dateTimeTextView);
                        //strBufTime.append("You select time is ");
                        strBufTime.append(hour);
                        strBufTime.append(":");
                        strBufTime.append(minute);


                        timePickerValueTextView.setText(strBufTime.toString());
                    }
                };


                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Call.this, onTimeSetListener, hour, minute, is24Hour);

                //timePickerDialog.setIcon(R.drawable.if_snowman);
                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });





















    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                        numberText.setText( cNumber);
                    }
                }
                break;
        }

    }
}
