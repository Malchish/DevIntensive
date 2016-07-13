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

public class AuthActivity extends BaseActivity implements View.OnClickListener {


    private Button mSignIn;
    private TextView mRememberPassward;
    private EditText mLogin, mPassward;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


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
                loginSuccess();
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

    private void loginSuccess(){
        showSnackBar("Вход");
    }


}
