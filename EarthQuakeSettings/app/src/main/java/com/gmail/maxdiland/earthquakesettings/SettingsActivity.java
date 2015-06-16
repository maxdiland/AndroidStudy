package com.gmail.maxdiland.earthquakesettings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;


public class SettingsActivity extends Activity {

    private static final String USER_PREFERENCE = "USER_PREFERENCE";
    private static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    private static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
    private static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";

    private Spinner spMagnitude;
    private Spinner spUpdateFrequency;
    private CheckBox chkbxAutoUpdate;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();

        Context context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        updateUiFromPreferences();
    }

    private void initUI() {
        spMagnitude = (Spinner) findViewById(R.id.spQuakeMag);
        spUpdateFrequency = (Spinner) findViewById(R.id.spUpdateFreq);
        chkbxAutoUpdate = (CheckBox) findViewById(R.id.chkbxAutoUpdate);

        populateSpinners();
    }

    private void populateSpinners() {
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(
                this, R.array.update_freq_options, android.R.layout.simple_spinner_item
        );
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUpdateFrequency.setAdapter(frequencyAdapter);

        ArrayAdapter<CharSequence> magnitudeAdapter = ArrayAdapter.createFromResource(
                this, R.array.magnitude_options, android.R.layout.simple_spinner_item
        );
        magnitudeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMagnitude.setAdapter(magnitudeAdapter);
    }

    private void updateUiFromPreferences() {
        boolean isAutoUpdateEnabled = preferences.getBoolean(PREF_AUTO_UPDATE, false);
        int updateFrequencyIndex = preferences.getInt(PREF_UPDATE_FREQ_INDEX, 2);
        int magnitudeIndex = preferences.getInt(PREF_MIN_MAG_INDEX, 0);

        spUpdateFrequency.setSelection(updateFrequencyIndex);
        spMagnitude.setSelection(magnitudeIndex);
        chkbxAutoUpdate.setChecked(isAutoUpdateEnabled);
    }


}
