package com.nicknam.shiftcalcreator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
    private boolean isDayOff = false;
    private OnShiftCreatedListener onShiftCreatedListener;
    private TimePickerDialogFragment tpdf;
    private SingleToast toast = new SingleToast();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_shift_dialog, container, false);
        final View root = v.findViewById(R.id.fragmentShiftDialog_root);
        final EditText etName = (EditText) v.findViewById(R.id.fragmentShiftDialog_et_name);
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

//        Use arguments or restore saved instance state or create new
            Bundle args = getArguments();
        if (args != null) {
            Shift shiftTemp = (Shift) args.getSerializable("shift");
            if (shiftTemp != null)
                shift = new Shift(shiftTemp);
            tpdf = new TimePickerDialogFragment();
            btnAdd.setText(R.string.save);
            isDayOff = args.getBoolean("isDayOff");
        } else if (savedInstanceState != null) {
            shift = (Shift) savedInstanceState.getSerializable("shift");
            tpdf = (TimePickerDialogFragment) getFragmentManager().findFragmentByTag("timePicker");
        }

        if (shift == null) {
            shift = new Shift();
            shift.setDayOff(isDayOff);
        }
        if (tpdf == null) {
            tpdf = new TimePickerDialogFragment();
        }
        tpdf.getTime(TimePickerDialogFragment.TIME_FROM).setTime(shift.getTimeStart().getTime());
        tpdf.getTime(TimePickerDialogFragment.TIME_TO).setTime(shift.getTimeEnd().getTime());

//        Day off or shift
        int visibility;
        if (shift.isDayOff())
            visibility = View.INVISIBLE;
        else
            visibility = View.VISIBLE;

        contTimeFrom.setVisibility(visibility);
        contTimeTo.setVisibility(visibility);
        etName.setVisibility(visibility);

//        Set time listeners
        tpdf.setTimeFromSetListener(new TimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet() {
                Calendar from = tpdf.getTime(TimePickerDialogFragment.TIME_FROM);
                shift.setTimeStart(from);
                tvTimeFrom.setText(timeFormat.format(from.getTime()));
            }
        });

        tpdf.setTimeToSetListener(new TimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet() {
                Calendar to = tpdf.getTime(TimePickerDialogFragment.TIME_TO);
                shift.setTimeEnd(to);
                tvTimeTo.setText(timeFormat.format(to.getTime()));
            }
        });


//        Time from click listener
        contTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpdf.setSelected(TimePickerDialogFragment.TIME_FROM);
                tpdf.show(getFragmentManager(), "timePicker");
            }
        });

//        Time to click listener
        contTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpdf.setSelected(TimePickerDialogFragment.TIME_TO);
                tpdf.show(getFragmentManager(), "timePicker");
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
                if (shift.isDayOff() || !shift.getName().equals("")) {
                    if (onShiftCreatedListener != null)
                        onShiftCreatedListener.onShiftCreated(shift);
                    dismiss();
                } else
                    toast.showText(getActivity(), R.string.errorNoShiftName, Toast.LENGTH_LONG);
            }
        });

        //        Lose focus on done
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    root.requestFocus();
                    if (getDialog().getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

//        Fill in view with shift data
        etName.setText(shift.getName());
        tvNRep.setText(shift.getRepetition() + "x");
        tvTimeFrom.setText(timeFormat.format(shift.getTimeStart().getTime()));
        tvTimeTo.setText(timeFormat.format(shift.getTimeEnd().getTime()));
        if (shift.getRepetition() <= 1)
            btnRepDown.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (shift.isDayOff()) {
            getDialog().findViewById(R.id.fragmentShiftDialog_root).getLayoutParams().height = (int) getResources().getDimension(R.dimen.dayOff_dialog_height);
            getDialog().findViewById(R.id.fragmentShiftDialog_root).getLayoutParams().width = (int) getResources().getDimension(R.dimen.dayOff_dialog_width);
        } else {
            getDialog().findViewById(R.id.fragmentShiftDialog_root).getLayoutParams().height = (int) getResources().getDimension(R.dimen.shift_dialog_height);
            getDialog().findViewById(R.id.fragmentShiftDialog_root).getLayoutParams().width = (int) getResources().getDimension(R.dimen.shift_dialog_width);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("shift", shift);
    }

    @Override
    public void onPause() {
        super.onPause();
        toast.cancel();
    }

    public void setOnShiftCreatedListener(OnShiftCreatedListener onShiftCreatedListener) {
        this.onShiftCreatedListener = onShiftCreatedListener;
    }

    public interface OnShiftCreatedListener {
        void onShiftCreated(Shift shift);
    }
}
