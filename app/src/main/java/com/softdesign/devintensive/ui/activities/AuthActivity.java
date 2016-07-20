package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.NetworkStatusCheker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {


    private Button mSignIn;
    private TextView mRememberPassward;
    private EditText mLogin, mPassward;
    private CoordinatorLayout mCoordinatorLayout;
    private DataManager mDatamanager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);



        mDatamanager = DataManager.getInstance();

        mUserDao = mDatamanager.getDaoSession().getUserDao();
        mRepositoryDao = mDatamanager.getDaoSession().getRepositoryDao();

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

    private void loginSuccess(UserModelRes userModelRes){
        mDatamanager.getPreferencesManager().saveAuthToken(userModelRes.getData().getToken());
        mDatamanager.getPreferencesManager().saveUserId(userModelRes.getData().getUser().getId());
        showSnackBar(userModelRes.getData().getToken());


        saveUserValues(userModelRes);
        saveUserFields(userModelRes);
        saveUserNames(userModelRes);
        saveUserPhotoAvatar(userModelRes);


        saveUserInDb();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);





    }
    private void singIn() {

        if (NetworkStatusCheker.isNetWorkAvailable(this)) {
            Call<UserModelRes> call = mDatamanager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassward.getText().toString()));

            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
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

    private void saveUserNames(UserModelRes userModel){
        String[] userName = {
                userModel.getData().getUser().getFirstName(),
                userModel.getData().getUser().getSecondName(),
                userModel.getData().getUser().getContacts().getEmail()
        };

        List<String> list = new ArrayList<>();
        for (int i = 0; i < userName.length; i++) {
            list.add(userName[i]);
        }
        mDatamanager.getPreferencesManager().saveUserName(list);
    }
    private void saveUserValues(UserModelRes userModel){
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects(),
        };
        mDatamanager.getPreferencesManager().saveUserProfileValues(userValues);
    }
    private void saveUserFields(UserModelRes userModel){
        String[] userFields = {
                userModel.getData().getUser().getContacts().getPhone(),
                userModel.getData().getUser().getContacts().getEmail(),
                userModel.getData().getUser().getContacts().getVk(),
                userModel.getData().getUser().getRepositories().getRepo().get(0).getGit(),
                userModel.getData().getUser().getPublicInfo().getBio()
        };

        List<String> list = new ArrayList<>();
        for (int i = 0; i < userFields.length; i++) {
            list.add(userFields[i]);
        }
        mDatamanager.getPreferencesManager().saveUserProfileData(list);
    }
    private void saveUserPhotoAvatar(UserModelRes userModel){
        Uri photo = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());
        Uri avatar = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());

        mDatamanager.getPreferencesManager().saveUserPhoto(photo);
        mDatamanager.getPreferencesManager().saveUserAvatar(avatar);
    }


    private void saveUserInDb(){
        Call<UserListRes> call = mDatamanager.getUserListFromNetwork();
        call.enqueue(new Callback<UserListRes>() {

            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200){
                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()){
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);

                    }else {
                        showSnackBar("Список пользователей не может быть получен");
                        Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody()));
                    }

               }catch (NullPointerException e){
                    e.printStackTrace();
                    showSnackBar("Something goes wrong");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                showSnackBar("Smth goes wrong");
            }
        });
    }
    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData){
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();

        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()){
            repositories.add(new Repository(repositoryRes, userId));
        }
        return repositories;
    }
}
