package com.nicknam.shiftcalcreator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by snick on 13-7-2017.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftHolder> {
    private ArrayList<Shift> shifts;
    private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public ShiftAdapter(ArrayList<Shift> shifts) {
        this.shifts = shifts;
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
    }

    @Override
    public ShiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShiftHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false));
    }

    @Override
    public void onBindViewHolder(ShiftHolder holder, int position) {
        Shift s = shifts.get(position);
        holder.name.setText(s.getName());
        holder.repetition.setText(String.valueOf(s.getRepetition()));
        holder.timeStart.setText(dateFormat.format(s.getTimeStart().getTime()));
        holder.timeEnd.setText(dateFormat.format(s.getTimeEnd().getTime()));
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public static class ShiftHolder extends RecyclerView.ViewHolder {
        public TextView name, repetition, timeStart, timeEnd;

        public ShiftHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.itemShift_tv_name);
            repetition = (TextView) itemView.findViewById(R.id.itemShift_tv_repetition);
            timeStart = (TextView) itemView.findViewById(R.id.itemShift_tv_timeStart);
            timeEnd = (TextView) itemView.findViewById(R.id.itemShift_tv_timeEnd);
        }
    }
}