package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusCheker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {


    private Button mSignIn;
    private TextView mRememberPassward;
    private EditText mLogin, mPassward;
    private CoordinatorLayout mCoordinatorLayout;
    private DataManager mDatamanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        mDatamanager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mRememberPassward = (TextView)findViewById(R.id.remember_txt);
        mLogin = (EditText)findViewById(R.id.email_enter_et);
        mPassward = (EditText)findViewById(R.id.password_et);
        mSignIn = (Button)findViewById(R.id.login_btn);


        mSignIn.setOnClickListener(this);
        mRememberPassward.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_btn:
                singIn();
                break;
            case R.id.remember_txt:
                rememberPassward();
                break;
        }
    }

    private void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassward(){
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(Response<UserModelRes> response){
        mDatamanager.getPreferencesManager().saveAuthToken(response.body().getData().getToken());
        mDatamanager.getPreferencesManager().getUserId(response.body().getData().getUser().getId());
        showSnackBar(response.body().getData().getToken());

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }
    private void singIn() {

        if (NetworkStatusCheker.isNetWorkAvailable(this)) {
            Call<UserModelRes> call = mDatamanager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassward.getText().toString()));

            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response);
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Что-то не так...");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    //TODO: обработать ошибки ретрофита
                }
            });
        }else{
            showSnackBar("Сеть на дынный момент не доступна, попробуйте позже");
        }
    }

}
