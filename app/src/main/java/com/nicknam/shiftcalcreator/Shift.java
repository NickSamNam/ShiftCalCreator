package com.nicknam.shiftcalcreator;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by snick on 13-7-2017.
 */

public class Shift implements Serializable {
    private String name;
    private int repetition;
    private Calendar timeStart, timeEnd;
    private boolean dayOff;

    public Shift() {
        name = "";
        repetition = 1;
        timeStart = Calendar.getInstance();
        timeStart.set(0, 0, 0, 9, 0, 0);
        timeStart.set(Calendar.MILLISECOND, 0);
        timeEnd = Calendar.getInstance();
        timeEnd.set(0, 0, 0, 17, 0, 0);
        timeEnd.set(Calendar.MILLISECOND, 0);
        dayOff = false;
    }

    public Shift(String name, int repetition, Calendar timeStart, Calendar timeEnd) {
        this.name = name;
        this.repetition = repetition;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        dayOff = false;
    }

    public Shift(Shift shift) {
        this.name = shift.getName();
        this.repetition = shift.getRepetition();
        this.timeStart = (Calendar) shift.getTimeStart().clone();
        this.timeEnd = (Calendar) shift.getTimeEnd().clone();
        this.dayOff = shift.isDayOff();
    }

    public String getName() {
        return name;
    }

    public int getRepetition() {
        return repetition;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Calendar getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Calendar timeStart) {
        this.timeStart = timeStart;
    }

    public Calendar getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Calendar timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "name='" + name + '\'' +
                ", repetition=" + repetition +
                ", timeStart=" + timeStart.get(Calendar.DAY_OF_MONTH) + "-" + timeStart.get(Calendar.MONTH) + "-" + timeStart.get(Calendar.YEAR) + " " + timeStart.get(Calendar.HOUR_OF_DAY) + ":" + timeStart.get(Calendar.MINUTE) +
                ", timeEnd=" + timeEnd.get(Calendar.DAY_OF_MONTH) + "-" + timeEnd.get(Calendar.MONTH) + "-" + timeEnd.get(Calendar.YEAR) + " " + timeEnd.get(Calendar.HOUR_OF_DAY) + ":" + timeEnd.get(Calendar.MINUTE) +
                ", dayOff=" + dayOff +
                '}';
    }
}
