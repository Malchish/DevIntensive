package com.softdesign.devintensive.ui.adapters;

/**
 * Created by alena on 28.07.16.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
