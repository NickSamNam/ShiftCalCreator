package com.nicknam.shiftcalcreator;

/**
 * Created by snick on 14-7-2017.
 */

interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
