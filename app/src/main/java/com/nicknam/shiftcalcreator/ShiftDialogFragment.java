package com.nicknam.shiftcalcreator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by snick on 14-7-2017.
 */

public class ShiftDialogFragment extends DialogFragment {
    int width, height;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shift_dialog, container, false);
    }
}
