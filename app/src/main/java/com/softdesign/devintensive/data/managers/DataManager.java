package com.softdesign.devintensive.data.managers;

import android.content.ContentValues;
import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevIntensiveApplication;



import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by alena on 28.06.16.
 */
public class DataManager {

    private Picasso mPicasso;
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    private DaoSession mDaoSession;


    public DataManager(){
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mContext = DevIntensiveApplication.getContext();
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevIntensiveApplication.getDaoSession();
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

    public Picasso getPicasso() {
        return mPicasso;
    }

    public Context getContext(){
        return mContext;
    }
    //region --Network--

    public Call<UserModelRes> loginUser (UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getUserListFromNetwork (){
        return mRestService.getUserList();
    }

    public List<User> getUserListFromDb(){
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        }catch (Exception e){
            e.printStackTrace();
        }


        return userList;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
