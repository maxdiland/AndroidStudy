package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gmail.maxdiland.drebedengireports.R;


public class SplashScreenActivity extends Activity {
    private NextActivityStarterTask nextActivityStarterTask = new NextActivityStarterTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        nextActivityStarterTask.execute();
    }

    @Override
    public void onBackPressed() {
        nextActivityStarterTask.cancel(true);
        finish();
    }


    private class NextActivityStarterTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.splashScreenDelay));
            } catch (InterruptedException e) {
                return null;
            }
            Intent intent = new Intent(SplashScreenActivity.this, ChooseDbActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            return null;
        }
    }
}