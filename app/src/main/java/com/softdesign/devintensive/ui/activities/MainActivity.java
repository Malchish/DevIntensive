package com.softdesign.devintensive.ui.activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import android.graphics.Color;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.TextValidator;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    protected EditText mEditText;
    protected int mColorMode;
    private ImageView mImageViewmg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode = 0;
    private FloatingActionButton mFloat;
    private EditText mUserMail, mUserPhome, mUserGit, mUserBio, mUserVk;
    private List<EditText> mUserInfo;
    private ImageHelper mImageHelper;
    private Bitmap icon;
    private RelativeLayout mPlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private AppBarLayout mAppBarLayout;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private ImageView mPhotoView, mCallPic, mEmailPic, mGithubPic, mVkPic;
    private boolean isValid = false;




    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mPlaceholder = (RelativeLayout) findViewById(R.id.place_holder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar_layout);
        mPhotoView = (ImageView)findViewById(R.id.user_photo_img);
        mEmailPic = (ImageView)findViewById(R.id.email_pic);
        mGithubPic = (ImageView)findViewById(R.id.github_pic);
        mVkPic = (ImageView)findViewById(R.id.vk_pic);


        mFloat = (FloatingActionButton) findViewById(R.id.floating_button);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserPhome = (EditText) findViewById(R.id.phone_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.about_me_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mCallPic = (ImageView)findViewById(R.id.call_img);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhome);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserBio);
        mUserInfo.add(mUserVk);

        mFloat.setOnClickListener(this);
        mPlaceholder.setOnClickListener(this);
        mCallPic.setOnClickListener(this);
        mEmailPic.setOnClickListener(this);
        mGithubPic.setOnClickListener(this);
        mVkPic.setOnClickListener(this);

        final List<String> test = mDataManager.getPreferencesManager().loadUserProfileData();


        setupToolbar();
        setupDrawer();
        loadUserValue();

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.arnold)
                .into(mPhotoView);

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }


        icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.arni);
        mImageHelper = new ImageHelper();
        icon = mImageHelper.getRoundedCornerBitmap(icon, icon.getHeight());
        setutAvatar();




  /*валидация полей EditText*/


        mUserPhome.addTextChangedListener(new TextValidator(mUserPhome) {
            @Override public void validate(TextView textView, String text) {

                text = mUserPhome.getText().toString();
                if (text.length()>10 && text.length()<21){
                    isValid = true;
                }else{
                    isValid = false;
                    mUserPhome.setError("Invalid phone number");
                }
            }
        });

        mUserMail.addTextChangedListener(new TextValidator(mUserMail) {
            @Override public void validate(TextView textView, String text) {

                text = mUserMail.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]{3,30}+@[a-z]{2,30}+\\.+[a-z]{2,4}";
                if (text.matches(emailPattern) && text.length()>0){
                    isValid = true;
                }else{
                    isValid = false;
                    mUserMail.setError("Invalid e-mail");
                }
            }
        });
        mUserVk.addTextChangedListener(new TextValidator(mUserVk) {
            @Override public void validate(TextView textView, String text) {

                text = mUserVk.getText().toString();
                if (text.startsWith("https://")){

                    text = text.replace("https://", "");
                }
                if (text.startsWith("vk.com")){
                    isValid = true;
                }else{
                    isValid = false;
                    mUserVk.setError("vk account is invalid");
                }

            }
        });
        mUserGit.addTextChangedListener(new TextValidator(mUserGit) {
            @Override public void validate(TextView textView, String text) {

                text = mUserGit.getText().toString();
                if (text.startsWith("https://")){

                    text = text.replace("https://", "");
                }
                if (text.startsWith("github.com")){
                    isValid = true;
                }else{
                    isValid = false;
                    mUserGit.setError("github account is invalid");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button:

                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.place_holder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_img:
                makeCall();
                break;
            case R.id.email_pic:
                sendEmail();
                break;
            case R.id.github_pic:
                openGithub();
                break;
            case R.id.vk_pic:
                openVk();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

    }

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void setutAvatar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        ImageView mI = (ImageView) header.findViewById(R.id.avatar_img);
        mI.setImageBitmap(icon);
    }

    public void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null){

                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {

                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }
    }



    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFloat.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);


                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            }

        } else {
            mFloat.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserValue();
                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
            }

        }
        String formattedNumber = PhoneNumberUtils.formatNumber(mUserPhome.getText().toString());
        mUserPhome.setText(formattedNumber);
    }

    private void loadUserValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }

    private void saveUserValue() {
        List<String> userData = new ArrayList<String>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    @Override
    public void onBackPressed() {
        if (this.mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            this.mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.choose_photo)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        }else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать разрешение", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do smth
            }
        }

        if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
            // do smth else
        }
    }

    private void hideProfilePlaceholder() {
        mPlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mPlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }



        @Override
        protected Dialog onCreateDialog ( int id){
            switch (id) {
                case ConstantManager.LOAD_PROFILE_PHOTO:
                    String[] selectItem = {getString(R.string.load_from_gallery), getString(R.string.make_photo), getString(R.string.cancel)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.user_title_change));
                    builder.setItems(selectItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choiceItem) {
                            switch (choiceItem) {
                                case 0:
                                    loadPhotoFromGallery();
                                    //showSnackBar("Загрузить из галереи");
                                    break;
                                case 1:
                                    loadPhotoFromCamera();
                                    //showSnackBar("Загрузить из камеры");
                                    break;
                                case 2:
                                    dialog.cancel();
                                    //showSnackBar("Отмена");
                                    break;
                            }
                        }
                    });
                    return builder.create();
                default:
                    return null;
            }

        }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return image;
    }
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mPhotoView);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }
    public void openApplicationSettings(){
        Intent appSetingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package: " + getPackageName()));
        startActivityForResult(appSetingsIntent, ConstantManager.REQUEST_SETTING_CODE);
    }

    public void makeCall(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Uri tellNumber = Uri.parse("tel:" + mUserPhome.getText());
            Intent makeCallIntent = new Intent(Intent.ACTION_CALL, tellNumber);
            startActivity(makeCallIntent);

        }else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.CALL_PHONE},
                    ConstantManager.CALL_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать разрешение", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();

        }


    }
    /*вызов невных интентов для перехода в другие приложения при нажатии на иконку справа*/

    public void sendEmail(){
        Uri emailUri = Uri.parse("mailto:" + mUserMail.getText());
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
        startActivity(mailIntent);
    }
    public void openGithub(){
        Uri githubUri = Uri.parse("http://" + mUserGit.getText());
        Intent githubIntent = new Intent(Intent.ACTION_VIEW, githubUri);
        startActivity(githubIntent);
    }
    public void openVk(){
        Uri vkUri = Uri.parse("http://" + mUserVk.getText());
        Intent vkIntent = new Intent(Intent.ACTION_VIEW, vkUri);
        startActivity(vkIntent);
    }





    }

