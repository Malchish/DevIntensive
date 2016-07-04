package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alena on 27.06.16.
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    private final static String[] USER_FIELDS = {ConstantManager.USER_MAIL_KEY, ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_BIO_KEY, ConstantManager.USER_PHONE_KEY, ConstantManager.USER_VK_KEY};

    public PreferencesManager(){
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }
    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }
    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<String>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "null"));
        return userFields;
    }
}
