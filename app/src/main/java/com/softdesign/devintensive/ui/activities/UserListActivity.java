package com.softdesign.devintensive.ui.activities;


import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;

import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UserAdapter;
import com.softdesign.devintensive.utils.ConstantManager;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;



public class UserListActivity extends AppCompatActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private DataManager mDataManager;
    private UserAdapter mUserAdapter;
    private List<User> mUser;
    private List<User> mUserSearch;
    private static Response<UserListRes> mUserListResCall;
    private static int sPositionItemUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView)findViewById(R.id.user_list);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        setUpToolBar();
        setUpDrawer();
        loadUsersFromDb();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setUpToolBar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setUpDrawer() {

        //TODO: реализовать переход в другую активность при клике по элементу меню в NavigationDrawer
    }
    private void loadUsersFromDb() {

        mUser = mDataManager.getUserListFromDb();


        if (mUser.size() == 0) {
            showSnackBar("Список пользователей не может быть загружен");
        } else {

            mUserAdapter = new UserAdapter(mUser, new UserAdapter.CustomClickListener() {
                @Override
                public void onUserItemClickListener(int position) {

                    UserDTO userDTO = new UserDTO(mUser.get(position));
                    Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                    profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                    startActivity(profileIntent);
                }
            });
            mRecyclerView.setAdapter(mUserAdapter);
        }
    }

        /*mUser = mDataManager.getUserListFromDb();


        if (mUser.size() == 0){
            showSnackBar("Список пользователей не может быть загружен");
        }else {

            mUserAdapter = new UserAdapter(mUser, new UserAdapter.CustomClickListener() {
                @Override
                public void onUserItemClickListener(int position) {

                    UserDTO userDTO = new UserDTO(mUser.get(position));
                    Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                    profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                    startActivity(profileIntent);
                }
            });
        }

    }*/
    private void loadUsersSearch(final List<User> users) {
        mUserAdapter = new UserAdapter(users, new UserAdapter.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {



                sPositionItemUser = position;
                UserDTO userProfile = new UserDTO(users.get(position));
                Intent userIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                userIntent.putExtra(ConstantManager.PARCELABLE_KEY, userProfile);
                startActivity(userIntent);
                }
        });
        mRecyclerView.setAdapter(mUserAdapter);
        if (sPositionItemUser != 0){
           mRecyclerView.scrollToPosition(sPositionItemUser);
            sPositionItemUser = 0;
            }
        }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }*/
    /*@Override
    public boolean onQueryTextSubmit(String s) {
        List<User> listOfUsers = (List<User>) mUserListResCall.body().getData();
        mUserSearch = new ArrayList<>();
        for (User user : listOfUsers) {
            if (user.getFullName().toLowerCase().contains(s.toLowerCase())) {
                mUserSearch.add(user);
            }
        }
        loadUsersSearch(mUserSearch);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }*/
}
