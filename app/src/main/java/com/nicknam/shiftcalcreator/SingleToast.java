package com.nicknam.shiftcalcreator;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by snick on 15-7-2017.
 */

public class SingleToast {
    private Toast toast;

    public void showText(Context context, CharSequence text, int duration) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void showText(Context context, @StringRes int resId, int duration) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, resId, duration);
        toast.show();
    }

    public void cancel() {
        if (toast != null)
            toast.cancel();
    }
}
