package com.nicknam.shiftcalcreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatePickerDialogFragment dpdf;
    private ArrayList<Shift> shifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvDateFrom = (TextView) findViewById(R.id.mainActivity_tv_dateFrom);
        final TextView tvDateTo = (TextView) findViewById(R.id.mainActivity_tv_dateTo);
        final LinearLayout contDateFrom = (LinearLayout) findViewById(R.id.activityMain_cont_dateFrom);
        final LinearLayout contDateTo = (LinearLayout) findViewById(R.id.activityMain_cont_dateTo);
        final ImageButton btnRestart = (ImageButton) findViewById(R.id.toolbarSchedule_btn_restart);
        final RecyclerView rvShifts = (RecyclerView) findViewById(R.id.activityMain_rv_shifts);

//        Create date format
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());

//        Recover saved instance state
        if (savedInstanceState != null) {
            dpdf = (DatePickerDialogFragment) getFragmentManager().findFragmentByTag("datePicker");

            if (dpdf == null)
                dpdf = new DatePickerDialogFragment();

            dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).setTime((Date) savedInstanceState.getSerializable("from"));
            dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).setTime((Date) savedInstanceState.getSerializable("to"));

            shifts = (ArrayList<Shift>) savedInstanceState.getSerializable("shifts");
        } else {
            dpdf = new DatePickerDialogFragment();
            shifts = new ArrayList<>();
        }

//        Set listeners
        dpdf.setCalFromSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet() {
                Calendar from = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                Calendar to = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO);
                tvDateFrom.setText(dateFormat.format(from.getTime()));
                if (from.after(to)) {
                    to.setTime(from.getTime());
                    to.add(Calendar.YEAR, 1);
                    tvDateTo.setText(dateFormat.format(to.getTime()));
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
                tvDateTo.setText(dateFormat.format(to.getTime()));
            }
        });

//        Set click action for dateFrom container
        contDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setSelected(DatePickerDialogFragment.CALENDAR_FROM);
                dpdf.show(getFragmentManager(), "datePicker");
            }
        });

//        Set click action for dateTo container
        contDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setSelected(DatePickerDialogFragment.CALENDAR_TO);
                dpdf.show(getFragmentManager(), "datePicker");
            }
        });

//        Display date
        tvDateFrom.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).getTime()));
        tvDateTo.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).getTime()));

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

//        Setup List of shifts
        rvShifts.setLayoutManager(new LinearLayoutManager(this));
        rvShifts.setAdapter(new ShiftAdapter(shifts));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("from", dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).getTime());
        outState.putSerializable("to", dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).getTime());
        outState.putSerializable("shifts", shifts);
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