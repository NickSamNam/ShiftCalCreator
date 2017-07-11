package com.nicknam.shiftcalcreator;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Calendar calFrom, calTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView displayDateFrom = (TextView) findViewById(R.id.mainActivity_tv_dateFrom);
        final TextView displayDateTo = (TextView) findViewById(R.id.mainActivity_tv_dateTo);

        if (savedInstanceState == null) {
            calFrom = Calendar.getInstance();
            calTo = (Calendar) calFrom.clone();
            calTo.add(Calendar.YEAR, 1);
        } else {
            calFrom = (Calendar) savedInstanceState.getSerializable("calFrom");
            calTo = (Calendar) savedInstanceState.getSerializable("calTo");
        }

        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateFormat.setTimeZone(calFrom.getTimeZone());

        displayDateFrom.setText(dateFormat.format(calFrom.getTime()));
        displayDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calFrom.set(year, month, dayOfMonth);
                                displayDateFrom.setText(dateFormat.format(calFrom.getTime()));
                                if (calFrom.after(calTo)) {
                                    calTo = (Calendar) calFrom.clone();
                                    calTo.add(Calendar.YEAR, 1);
                                    displayDateTo.setText(dateFormat.format(calTo.getTime()));
                                }
                            }
                        },
                        calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        displayDateTo.setText(dateFormat.format(calTo.getTime()));
        displayDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calTemp = Calendar.getInstance();
                                calTemp.set(year, month, dayOfMonth);

                                if (calTemp.after(calFrom)) {
                                    calTo.set(year, month, dayOfMonth);
                                    displayDateTo.setText(dateFormat.format(calTo.getTime()));
                                } else
                                    Toast.makeText(MainActivity.this, R.string.errorToBeforeFrom, Toast.LENGTH_LONG).show();
                            }
                        },
                        calTo.get(Calendar.YEAR), calTo.get(Calendar.MONTH), calTo.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("calFrom", calFrom);
        outState.putSerializable("calTo", calTo);
    }
}