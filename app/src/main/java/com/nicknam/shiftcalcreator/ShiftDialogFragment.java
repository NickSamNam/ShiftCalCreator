package com.nicknam.shiftcalcreator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by snick on 14-7-2017.
 */

public class ShiftDialogFragment extends DialogFragment {
    private Shift shift;
    private OnShiftCreatedListener onShiftCreatedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_shift_dialog, container, false);
        final EditText etName = (EditText) v.findViewById(R.id.fragmentShiftDialog_et_name);
        final CheckBox chkDayOff = (CheckBox) v.findViewById(R.id.fragmentShiftDialog_chkB_dayOff);
        final TextView tvNRep = (TextView) v.findViewById(R.id.fragmentShiftDialog_tv_nRep);
        final ImageButton btnRepUp = (ImageButton) v.findViewById(R.id.fragmentShiftDialog_btn_repUp);
        final ImageButton btnRepDown = (ImageButton) v.findViewById(R.id.fragmentShiftDialog_btn_repDown);
        final LinearLayout contTimeFrom = (LinearLayout) v.findViewById(R.id.fragmentShiftDialog_cont_timeFrom);
        final LinearLayout contTimeTo = (LinearLayout) v.findViewById(R.id.fragmentShiftDialog_cont_timeTo);
        final TextView tvTimeFrom = (TextView) v.findViewById(R.id.fragmentShiftDialog_tv_timeFrom);
        final TextView tvTimeTo = (TextView) v.findViewById(R.id.fragmentShiftDialog_tv_timeTo);
        final Button btnCancel = (Button) v.findViewById(R.id.fragmentShiftDialog_btn_cancel);
        final Button btnAdd = (Button) v.findViewById(R.id.fragmentShiftDialog_btn_add);

        final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        timeFormat.setTimeZone(Calendar.getInstance().getTimeZone());

//        Restore saved instance state
        if (savedInstanceState != null) {
            shift = (Shift) savedInstanceState.getSerializable("shift");
        } else {
            shift = new Shift();
        }

//        Day off check listener
        chkDayOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shift.setDayOff(isChecked);

                int visibility;
                if (isChecked)
                    visibility = View.INVISIBLE;
                else
                    visibility = View.VISIBLE;

                v.findViewById(R.id.fragmentShiftDialog_repetitionLabel).setVisibility(visibility);
                tvNRep.setVisibility(visibility);
                btnRepUp.setVisibility(visibility);
                btnRepDown.setVisibility(visibility);
                contTimeFrom.setVisibility(visibility);
                contTimeTo.setVisibility(visibility);
            }
        });

//        Time from click listener
        contTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO: 15-7-2017 Handle click
            }
        });

//        Time to click listener
        contTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO: 15-7-2017 Handle click
            }
        });

//        Repetition up click listener
        btnRepUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shift.getRepetition() == 1)
                    btnRepDown.setVisibility(View.VISIBLE);
                shift.setRepetition(shift.getRepetition()+1);
                tvNRep.setText(shift.getRepetition() + "x");
            }
        });

//        Repetition down click listener
        btnRepDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift.setRepetition(shift.getRepetition()-1);
                if (shift.getRepetition() <= 1)
                    btnRepDown.setVisibility(View.INVISIBLE);
                tvNRep.setText(shift.getRepetition() + "x");
            }
        });

//        Button cancel click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        Button add click listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift.setName(etName.getText().toString());
                if (shift.getName() != null && !shift.getName().equals("")) {
                    if (onShiftCreatedListener != null)
                        onShiftCreatedListener.onShiftCreated(shift);
                    dismiss();
                } else
                    Toast.makeText(getActivity(), R.string.errorNoShiftName, Toast.LENGTH_LONG).show();
            }
        });

//        Fill in view with shift data
        etName.setText(shift.getName());
        chkDayOff.setChecked(shift.isDayOff());
        tvNRep.setText(shift.getRepetition() + "x");
        tvTimeFrom.setText(timeFormat.format(shift.getTimeStart().getTime()));
        tvTimeTo.setText(timeFormat.format(shift.getTimeEnd().getTime()));
        if (shift.getRepetition() <= 1)
            btnRepDown.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("shift", shift);
    }

    public void setOnShiftCreatedListener(OnShiftCreatedListener onShiftCreatedListener) {
        this.onShiftCreatedListener = onShiftCreatedListener;
    }

    public interface OnShiftCreatedListener {
        void onShiftCreated(Shift shift);
    }
}
