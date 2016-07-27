package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class SplashScreenActivity extends AppCompatActivity {

    public static Bus bus;

        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.splash);

            /* инициализация bus */
            bus = BusProvider.getInstance();
            bus.register(this);

            /* Выводим сообщение, пока идет загрузка данных */
            Toast.makeText(SplashScreenActivity.this, "Your data are loading...", Toast.LENGTH_SHORT).show();
        }


    /* Слушатель событий */
    @Subscribe
    public void reactTheEvent(String s) {
        Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
        SplashScreenActivity.this.startActivity(mainIntent);
        SplashScreenActivity.this.finish();
    }
}

