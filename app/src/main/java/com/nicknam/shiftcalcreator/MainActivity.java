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
    private DatePickerDialogFragment datePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView displayDateFrom = (TextView) findViewById(R.id.mainActivity_tv_dateFrom);
        final TextView displayDateTo = (TextView) findViewById(R.id.mainActivity_tv_dateTo);
        final LinearLayout contDateFrom = (LinearLayout) findViewById(R.id.activityMain_cont_dateFrom);
        final LinearLayout contDateTo = (LinearLayout) findViewById(R.id.activityMain_cont_dateTo);
        final ImageButton btnRestart = (ImageButton) findViewById(R.id.toolbarSchedule_btn_restart);

//        Create date format
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());

//        Create DatePickerDialog
        datePickerDialogFragment = new DatePickerDialogFragment();

        datePickerDialogFragment.setCalFromSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(Calendar calFrom, Calendar calTo) {
                displayDateFrom.setText(dateFormat.format(calFrom.getTime()));
                if (calFrom.after(calTo)) {
                    calTo = (Calendar) calFrom.clone();
                    calTo.add(Calendar.YEAR, 1);
                    displayDateTo.setText(dateFormat.format(calTo.getTime()));
                }
            }
        });

        datePickerDialogFragment.setCalToSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(Calendar calFrom, Calendar calTo) {
                if (calTo.after(calFrom)) {
                    displayDateTo.setText(dateFormat.format(calTo.getTime()));
                } else {
                    calTo.set(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(MainActivity.this, R.string.errorToBeforeFrom, Toast.LENGTH_LONG).show();
                }
            }
        });

//        Set click action for dateFrom container
        contDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogFragment.setCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                datePickerDialogFragment.show(getFragmentManager(), "calFrom");
            }
        });

//        Set click action for dateTo container
        contDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogFragment.setCalendar(DatePickerDialogFragment.CALENDAR_TO);
                datePickerDialogFragment.show(getFragmentManager(), "calTo");
            }
        });

//        Display date
        displayDateFrom.setText(dateFormat.format(datePickerDialogFragment.getDate(DatePickerDialogFragment.CALENDAR_FROM)));
        displayDateTo.setText(dateFormat.format(datePickerDialogFragment.getDate(DatePickerDialogFragment.CALENDAR_TO)));

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