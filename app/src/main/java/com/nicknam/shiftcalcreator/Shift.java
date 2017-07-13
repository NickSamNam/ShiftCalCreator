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

    public Shift(String name, int repetition, Calendar timeStart, Calendar timeEnd) {
        this.name = name;
        this.repetition = repetition;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getName() {
        return name;
    }

    public int getRepetition() {
        return repetition;
    }

    public Calendar getTimeStart() {
        return timeStart;
    }

    public Calendar getTimeEnd() {
        return timeEnd;
    }
}
