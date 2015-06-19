package com.gmail.maxdiland.earthquakeviewer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


public class EarthQuakeActivity extends Activity {

    private static final int SHOW_PREFERENCES = 1;

    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    private static final int MENU_UPDATE = Menu.FIRST + 2;

    private int minimumMagnitude;
    private int updateFrequency;
    private boolean isAutoUpdateChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateStateFromPreferences();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
        return true;

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == MENU_PREFERENCES) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SHOW_PREFERENCES);
            return true;
        }

        return false;
    }

    private void updateStateFromPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
            getApplicationContext()
        );

        int minMagIndex = preferences.getInt(SettingsActivity.PREF_MIN_MAG_INDEX, 0);
        int freqIndex = preferences.getInt(SettingsActivity.PREF_UPDATE_FREQ_INDEX, 0);

        Resources resources = getResources();
        minimumMagnitude =
                Integer.valueOf(resources.getStringArray(R.array.magnitude)[minMagIndex]);
        updateFrequency =
                Integer.valueOf(resources.getStringArray(R.array.update_freq_values)[freqIndex]);
        isAutoUpdateChecked = preferences.getBoolean(SettingsActivity.PREF_AUTO_UPDATE, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES) {
            if (resultCode == Activity.RESULT_OK) {
                updateStateFromPreferences();
                FragmentManager fragmentManager = getFragmentManager();
                final EarthQuakeListFragment listFragment =
                        (EarthQuakeListFragment) fragmentManager.findFragmentById(R.id.fragment);

                new Thread() {
                    @Override
                    public void run() {
                        listFragment.refreshEarthquakes();
                    }
                }.start();
            }
        }
    }

    public int getMinimumMagnitude() {
        return minimumMagnitude;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public boolean isAutoUpdateChecked() {
        return isAutoUpdateChecked;
    }
}
