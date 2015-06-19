package com.gmail.maxdiland.earthquakeviewer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;


public class SettingsActivity extends Activity {

    public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
    public static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";

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

    public void onOkClick(View view) {
        savePreferences();
        setResult(RESULT_OK);
        finish();
    }

    public void onCancelClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void savePreferences() {
        int selectedFrequencyPosition = spUpdateFrequency.getSelectedItemPosition();
        int selectedMagnitudePosition = spMagnitude.getSelectedItemPosition();
        boolean isAutoUpdateChecked = chkbxAutoUpdate.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_AUTO_UPDATE, isAutoUpdateChecked);
        editor.putInt(PREF_UPDATE_FREQ_INDEX, selectedFrequencyPosition);
        editor.putInt(PREF_MIN_MAG_INDEX, selectedMagnitudePosition);
        editor.commit();
    }
}
