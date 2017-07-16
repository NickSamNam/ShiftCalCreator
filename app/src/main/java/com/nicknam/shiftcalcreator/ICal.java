package com.nicknam.shiftcalcreator;

import android.content.Context;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by snick on 16-7-2017.
 */

public class ICal {
    private final Calendar cal;
    private final Context context;
    private OnExportResultListener onExportResultListener;

    public ICal(Context context, String name) {
        cal = new Calendar();
        this. context = context;

        cal.getProperties().add(new ProdId("-//" + context.getString(R.string.com_name) + "//" + context.getString(R.string.app_name) + " " + context.getString(R.string.app_version) + "//" + Locale.getDefault().getLanguage().toUpperCase()));
        cal.getProperties().add(Version.VERSION_2_0);
        cal.getProperties().add(CalScale.GREGORIAN);
        cal.getProperties().add(new XProperty("X-WR-CALNAME", name));
    }

    // TODO: 16-7-2017 Fix event creation
    public void addEvents(java.util.Calendar to, List<Shift> shifts) {
        for (Shift shift : shifts)
            if (!shift.isDayOff())
                for (int i = 0; i < shift.getRepetition(); i++) {
                    Shift shiftCopy = new Shift(shift);
                    shiftCopy.getTimeStart().add(java.util.Calendar.DAY_OF_MONTH, i);
                    shiftCopy.getTimeEnd().add(java.util.Calendar.DAY_OF_MONTH, i);
                    addEvent(shiftCopy);
                }
    }

    // TODO: 16-7-2017 Fix event creation
    private void addEvent(Shift shift) {
        final VEvent event = new VEvent(new net.fortuna.ical4j.model.Date(shift.getTimeStart()), new net.fortuna.ical4j.model.Date(shift.getTimeEnd()), shift.getName());
        UidGenerator uid;
        try {
            uid = new UidGenerator(String.valueOf(android.os.Process.myPid()));
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        event.getProperties().add(uid.generateUid());
        cal.getComponents().add(event);
    }

    public void setOnExportResultListener(OnExportResultListener onExportResultListener) {
        this.onExportResultListener = onExportResultListener;
    }

    public void exportToCache() {
        File folder = new File(context.getCacheDir(), "cals");
        folder.mkdir();
        File file = new File(folder, "cal.ics");
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            CalendarOutputter calOut = new CalendarOutputter();
            calOut.output(cal, fileOut);
        } catch (IOException e) {
            if (onExportResultListener != null)
                onExportResultListener.onFailed(e);
            return;
        } catch (ValidationException e) {
            e.printStackTrace();
            if (onExportResultListener != null)
                onExportResultListener.onFailed(e);
            return;
        } finally {
            if (fileOut != null)
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if (onExportResultListener != null) {
            if (file.exists() && !file.isDirectory())
                onExportResultListener.onSaved(file);
            else
                onExportResultListener.onFailed(null);
        }
    }

    public interface OnExportResultListener {
        void onSaved(File file);
        void onFailed(Exception e);
    }
}
