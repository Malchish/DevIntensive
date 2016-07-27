package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.managers.BusProvider;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.greenrobot.greendao.database.Database;

/**
 * Created by alena on 27.06.16.
 */
public class DevIntensiveApplication extends Application {


    public static SharedPreferences sSharedPreferences;
    private static Context mContext;
    private static DaoSession sDaoSession;
    public static Bus bus;


    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mContext = this;
        mContext = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);

        bus = BusProvider.getInstance();

    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return mContext;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
