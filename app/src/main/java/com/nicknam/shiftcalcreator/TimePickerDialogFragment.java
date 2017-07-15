package com.nicknam.shiftcalcreator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by snick on 15-7-2017.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public final static int TIME_FROM = 0;
    public final static int TIME_TO = 1;

    private Calendar timeFrom, timeTo;
    private OnTimeSetListener timeFromSetListener, timeToSetListener;

    private int tpdHour, tpdMinute, tpdTag;

    public TimePickerDialogFragment() {
        timeFrom = Calendar.getInstance();
        timeFrom.set(0, 0, 0, 9, 0, 0);
        timeTo = Calendar.getInstance();
        timeTo.set(0, 0, 0, 17, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, tpdHour, tpdMinute, DateFormat.is24HourFormat(getActivity()));
    }

    public void setTimeFromSetListener(OnTimeSetListener timeFromSetListener) {
        this.timeFromSetListener = timeFromSetListener;
    }

    public void setTimeToSetListener(OnTimeSetListener timeToSetListener) {
        this.timeToSetListener = timeToSetListener;
    }

    public void setSelected(int time) {
        if (time == TIME_FROM) {
            tpdHour = timeFrom.get(Calendar.HOUR_OF_DAY);
            tpdMinute = timeFrom.get(Calendar.MINUTE);
            tpdTag = TIME_FROM;
        } else if (time == TIME_TO) {
            tpdHour = timeTo.get(Calendar.HOUR_OF_DAY);
            tpdMinute = timeTo.get(Calendar.MINUTE);
            tpdTag = TIME_TO;
        } else
            throw new IllegalArgumentException("Invalid time");
    }

    public Calendar getTime(int time) {
        if (time == TIME_FROM)
            return timeFrom;
        else if (time == TIME_TO)
            return timeTo;
        else
            throw new IllegalArgumentException("Invalid time");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (tpdTag == TIME_FROM) {
            timeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeFrom.set(Calendar.MINUTE, minute);
            timeFromSetListener.onTimeSet();
        } else if (tpdTag == TIME_TO) {
            timeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeTo.set(Calendar.MINUTE, minute);
            timeToSetListener.onTimeSet();
        }
    }

    public interface OnTimeSetListener {
        void onTimeSet();
    }
}
