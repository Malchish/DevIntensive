package com.softdesign.devintensive.data.managers;

import android.content.ContentValues;
import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;



import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;

/**
 * Created by alena on 28.06.16.
 */
public class DataManager {


    private static DataManager INSTANCE = null;

    //private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;




    public DataManager(){
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);

        //this.mContext = DevIntensiveApplication.getContext();

    }

    public static DataManager getInstance() {
        if (INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }


    public PreferencesManager getPreferencesManager(){
        return mPreferencesManager;
    }


    /*public Context getContext(){
        return mContext;
    }
*/
    //region --Network--

    public Call<UserModelRes> loginUser (UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getUserList (){
        return mRestService.getUserList();
    }
}
