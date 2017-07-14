package com.nicknam.shiftcalcreator;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by snick on 13-7-2017.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Shift> shifts;

    public ShiftAdapter(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    @Override
    public ShiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShiftHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ShiftHolder holder, int position) {
        final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        timeFormat.setTimeZone(Calendar.getInstance().getTimeZone());

        Shift s = shifts.get(position);
        holder.name.setText(s.getName());
        if (!s.isDayOff()) {
            holder.repetition.setText(s.getRepetition() + "x");
            holder.timeStart.setText(timeFormat.format(s.getTimeStart().getTime()));
            holder.timeEnd.setText(timeFormat.format(s.getTimeEnd().getTime()));
        }
        else {
            holder.centre.setText(R.string.dayOff);
            holder.repetition.setVisibility(View.INVISIBLE);
            holder.timeStart.setVisibility(View.INVISIBLE);
            holder.timeEnd.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition)
            for (int i = fromPosition; i < toPosition; i++)
                Collections.swap(shifts, i, i+1);
        else
            for (int i = fromPosition; i > toPosition; i--)
                Collections.swap(shifts, i, i-1);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        shifts.remove(position);
        notifyItemRemoved(position);
    }

    public static class ShiftHolder extends RecyclerView.ViewHolder {
        public TextView name, repetition, timeStart, timeEnd, centre;

        public ShiftHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.itemShift_tv_name);
            repetition = (TextView) itemView.findViewById(R.id.itemShift_tv_repetition);
            timeStart = (TextView) itemView.findViewById(R.id.itemShift_tv_timeStart);
            timeEnd = (TextView) itemView.findViewById(R.id.itemShift_tv_timeEnd);
            centre = (TextView) itemView.findViewById(R.id.itemShift_tv_centre);
        }
    }
}