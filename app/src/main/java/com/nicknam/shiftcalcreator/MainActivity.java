package com.nicknam.shiftcalcreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatePickerDialogFragment dpdf;

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
        dpdf = new DatePickerDialogFragment();

        dpdf.setCalFromSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet() {
                Calendar from = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                Calendar to = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO);
                displayDateFrom.setText(dateFormat.format(from.getTime()));
                if (from.after(to)) {
                    to.setTime(from.getTime());
                    to.add(Calendar.YEAR, 1);
                    displayDateTo.setText(dateFormat.format(to.getTime()));
                }
            }
        });

        dpdf.setCalToSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet() {
                Calendar from = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                Calendar to = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO);
                if (to.before(from)) {
                    to.setTime(from.getTime());
                    Toast.makeText(MainActivity.this, R.string.errorToBeforeFrom, Toast.LENGTH_LONG).show();
                }
                displayDateTo.setText(dateFormat.format(to.getTime()));
            }
        });

//        Set click action for dateFrom container
        contDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                dpdf.show(getFragmentManager(), "calFrom");
            }
        });

//        Set click action for dateTo container
        contDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setCalendar(DatePickerDialogFragment.CALENDAR_TO);
                dpdf.show(getFragmentManager(), "calTo");
            }
        });

//        Display date
        displayDateFrom.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).getTime()));
        displayDateTo.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).getTime()));

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