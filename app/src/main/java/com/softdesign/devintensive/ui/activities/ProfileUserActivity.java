package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.List;

public class ProfileUserActivity extends BaseActivity {


    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private EditText mUserBio;
    private TextView mUserRating, mUserCodeLines, mUserProgects;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private ListView mRepoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mProfileImage = (ImageView)findViewById(R.id.user_photo_img);
        mUserBio = (EditText) findViewById(R.id.about_me_et);
        mUserRating = (TextView) findViewById(R.id.user_info_rating_tv);
        mUserCodeLines = (TextView) findViewById(R.id.user_info_code_lines_tv);
        mUserProgects = (TextView) findViewById(R.id.user_info_project_tv);
        mRepoList = (ListView) findViewById(R.id.repositories_list);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);

        setUpToolbar();
        initProfileData();
    }
    private void setUpToolbar(){

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initProfileData(){
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();

        RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoList.setAdapter(repositoriesAdapter);

        mRepoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(mCoordinatorLayout, "Репозиторий " + repositories.get(position), Snackbar.LENGTH_LONG).show();
            }
        });

        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProgects.setText(userDTO.getProjects());

        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .error(R.drawable.login_bg)
                .placeholder(R.drawable.login_bg)
                .into(mProfileImage);
    }


}
