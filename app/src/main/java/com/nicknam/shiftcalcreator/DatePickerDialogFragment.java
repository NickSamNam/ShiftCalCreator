package com.nicknam.shiftcalcreator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by snick on 13-7-2017.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public final static int CALENDAR_FROM = 0;
    public final static int CALENDAR_TO = 1;

    private Calendar calFrom, calTo;
    private OnDateSetListener calFromSetListener, calToSetListener;

    private int dpdYear, dpdMonth, dpdDayOfMonth, dpdTag;

    public DatePickerDialogFragment() {
        calFrom = Calendar.getInstance();
        calTo = (Calendar) calFrom.clone();
        calTo.add(Calendar.YEAR, 1);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, dpdYear, dpdMonth, dpdDayOfMonth);
        datePickerDialog.getDatePicker().setTag(dpdTag);
        return datePickerDialog;
    }

    public void setCalFromSetListener(OnDateSetListener calFromSetListener) {
        this.calFromSetListener = calFromSetListener;
    }

    public void setCalToSetListener(OnDateSetListener calToSetListener) {
        this.calToSetListener = calToSetListener;
    }

    public void setSelected(int calendar) {
        if (calendar == CALENDAR_FROM) {
            dpdYear = calFrom.get(Calendar.YEAR);
            dpdMonth = calFrom.get(Calendar.MONTH);
            dpdDayOfMonth = calFrom.get(Calendar.DAY_OF_MONTH);
            dpdTag = CALENDAR_FROM;
        } else if (calendar == CALENDAR_TO) {
            dpdYear = calTo.get(Calendar.YEAR);
            dpdMonth = calTo.get(Calendar.MONTH);
            dpdDayOfMonth = calTo.get(Calendar.DAY_OF_MONTH);
            dpdTag = CALENDAR_TO;
        } else
            throw new IllegalArgumentException("Invalid calendar");
    }

    public Calendar getCalendar(int calendar) {
        if (calendar == CALENDAR_FROM)
            return calFrom;
        else if (calendar == CALENDAR_TO)
            return calTo;
        else
            throw new IllegalArgumentException("Invalid calendar");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dpdTag == CALENDAR_FROM) {
            calFrom.set(year, month, dayOfMonth);
            calFromSetListener.onDateSet();
        } else if (dpdTag == CALENDAR_TO) {
            calTo.set(year, month, dayOfMonth);
            calToSetListener.onDateSet();
        }
    }

    public interface OnDateSetListener {
        void onDateSet();
    }
}
