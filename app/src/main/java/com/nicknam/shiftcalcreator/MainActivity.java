package com.nicknam.shiftcalcreator;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Calendar calFrom, calTo;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView displayDateFrom = (TextView) findViewById(R.id.mainActivity_tv_dateFrom);
        final TextView displayDateTo = (TextView) findViewById(R.id.mainActivity_tv_dateTo);
        final LinearLayout contDateFrom = (LinearLayout) findViewById(R.id.activityMain_cont_dateFrom);
        final LinearLayout contDateTo = (LinearLayout) findViewById(R.id.activityMain_cont_dateTo);
        final ImageButton btnRestart = (ImageButton) findViewById(R.id.toolbarSchedule_btn_restart);

        String calTagOnStart = null;

        if (savedInstanceState == null) {
            calFrom = Calendar.getInstance();
            calTo = (Calendar) calFrom.clone();
            calTo.add(Calendar.YEAR, 1);
        } else {
            calFrom = (Calendar) savedInstanceState.getSerializable("calFrom");
            calTo = (Calendar) savedInstanceState.getSerializable("calTo");
            calTagOnStart = savedInstanceState.getString("calTag");
        }

        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateFormat.setTimeZone(calFrom.getTimeZone());

        displayDateFrom.setText(dateFormat.format(calFrom.getTime()));
        displayDateTo.setText(dateFormat.format(calTo.getTime()));

        datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (view.getTag().equals("from")) {
                            calFrom.set(year, month, dayOfMonth);
                            displayDateFrom.setText(dateFormat.format(calFrom.getTime()));
                            if (calFrom.after(calTo)) {
                                calTo = (Calendar) calFrom.clone();
                                calTo.add(Calendar.YEAR, 1);
                                displayDateTo.setText(dateFormat.format(calTo.getTime()));
                            }
                        } else if (view.getTag().equals("to")) {
                            Calendar calTemp = Calendar.getInstance();
                            calTemp.set(year, month, dayOfMonth);

                            if (calTemp.after(calFrom)) {
                                calTo.set(year, month, dayOfMonth);
                                displayDateTo.setText(dateFormat.format(calTo.getTime()));
                            } else
                                Toast.makeText(MainActivity.this, R.string.errorToBeforeFrom, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                0,
                0,
                0
        );


        contDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.getDatePicker().setTag("from");
                datePickerDialog.getDatePicker().init(
                        calFrom.get(Calendar.YEAR),
                        calFrom.get(Calendar.MONTH),
                        calFrom.get(Calendar.DAY_OF_MONTH),
                        null
                );
                datePickerDialog.show();
            }
        });

        contDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.getDatePicker().setTag("to");
                datePickerDialog.getDatePicker().init(
                        calTo.get(Calendar.YEAR),
                        calTo.get(Calendar.MONTH),
                        calTo.get(Calendar.DAY_OF_MONTH),
                        null
                );
                datePickerDialog.show();
            }
        });

//        Auto open DatePickerDialog if needed
        if (calTagOnStart != null && !datePickerDialog.isShowing()) {
            if (calTagOnStart.equals("from"))
                contDateFrom.callOnClick();
            else if (calTagOnStart.equals("to"))
                contDateTo.callOnClick();
        }

//        Restart button
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.restart)
                        .setMessage(R.string.restartMessage)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = MainActivity.this.getIntent();
                                MainActivity.this.finish();
                                startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datePickerDialog.dismiss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("calFrom", calFrom);
        outState.putSerializable("calTo", calTo);
        if (datePickerDialog != null && datePickerDialog.isShowing()) {
            outState.putString("calTag", (String) datePickerDialog.getDatePicker().getTag());
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.exit)
                .setMessage(R.string.exitMessage)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.super.onBackPressed();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}