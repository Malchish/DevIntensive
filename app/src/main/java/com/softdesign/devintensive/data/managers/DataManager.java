package com.softdesign.devintensive.data.managers;

/**
 * Created by alena on 28.06.16.
 */
public class DataManager {
    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;

    public static DataManager getInstance() {
        if (INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }
    public PreferencesManager getPreferencesManager(){
        return mPreferencesManager;
    }
}
