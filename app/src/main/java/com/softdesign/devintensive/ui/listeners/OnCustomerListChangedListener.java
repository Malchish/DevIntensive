package com.softdesign.devintensive.ui.listeners;

import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

/**
 * Created by alena on 30.07.16.
 */
public interface OnCustomerListChangedListener {
    void onNoteListChanged(List<User> customers);
}
