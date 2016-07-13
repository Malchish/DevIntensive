package com.softdesign.devintensive.data.network.res;

import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alena on 13.07.16.
 */
public interface UserModelRes {

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);
}
