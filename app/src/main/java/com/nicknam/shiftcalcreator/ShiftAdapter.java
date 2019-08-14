package com.nicknam.shiftcalcreator;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by snick on 13-7-2017.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftHolder> implements ItemTouchHelperAdapter {
    private final ArrayList<Shift> shifts;
    private OnItemClickListener onItemClickListener;
    private DateFormat timeFormat;

    public ShiftAdapter(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ShiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        timeFormat = android.text.format.DateFormat.getTimeFormat(parent.getContext());
        return new ShiftHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ShiftHolder holder, final int position) {
        Shift s = shifts.get(position);
        holder.name.setText(s.getName());
        holder.repetition.setText(s.getRepetition() + "x");
        if (!s.isDayOff()) {
            holder.name.setVisibility(View.VISIBLE);
            holder.timeStart.setText(timeFormat.format(s.getTimeStart().getTime()));
            holder.timeEnd.setText(timeFormat.format(s.getTimeEnd().getTime()));
            holder.timeStart.setVisibility(View.VISIBLE);
            holder.dash.setVisibility(View.VISIBLE);
            holder.timeEnd.setVisibility(View.VISIBLE);
            holder.dayOff.setVisibility(View.INVISIBLE);
        }
        else {
            holder.name.setVisibility(View.INVISIBLE);
            holder.timeStart.setVisibility(View.INVISIBLE);
            holder.dash.setVisibility(View.INVISIBLE);
            holder.timeEnd.setVisibility(View.INVISIBLE);
            holder.dayOff.setVisibility(View.VISIBLE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ShiftHolder extends RecyclerView.ViewHolder {
        private final View root;
        private final TextView name;
        private final TextView repetition;
        private final TextView timeStart;
        private final TextView timeEnd;
        private final TextView dash;
        private final TextView dayOff;

        private ShiftHolder(View itemView) {
            super(itemView);
            root = itemView;
            name = (TextView) itemView.findViewById(R.id.itemShift_tv_name);
            repetition = (TextView) itemView.findViewById(R.id.itemShift_tv_repetition);
            timeStart = (TextView) itemView.findViewById(R.id.itemShift_tv_timeStart);
            timeEnd = (TextView) itemView.findViewById(R.id.itemShift_tv_timeEnd);
            dash = (TextView) itemView.findViewById(R.id.itemShift_tv_dash);
            dayOff = (TextView) itemView.findViewById(R.id.itemShift_tv_dayOff);
        }
    }
}