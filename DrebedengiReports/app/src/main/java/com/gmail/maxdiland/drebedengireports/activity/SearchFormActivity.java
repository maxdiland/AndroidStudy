package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.db.DrebedengiEntityDao;
import com.gmail.maxdiland.drebedengireports.entity.Currency;
import com.gmail.maxdiland.drebedengireports.entity.FinancialTarget;

import java.io.File;
import java.text.SimpleDateFormat;

public class SearchFormActivity extends Activity {
    public static final String DATABASE_PATH_KEY = "DATABASE_PATH_KEY";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private DrebedengiEntityDao drebedengiEntityDao;
    private EditText etSum;
    private Spinner sCurrency;
    private Spinner sMoneyPlace;
    private Spinner sExpensesCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);

        initUi();
        initDao();
        fillUiWithDbData();
    }

    private void initDao() {
        File dbFile = new File(getIntent().getStringExtra(DATABASE_PATH_KEY));
        drebedengiEntityDao = new DrebedengiEntityDao(dbFile);
    }

    private void initUi() {
        etSum = getViewById(R.id.etSum);
        sCurrency = getViewById(R.id.sCurrency);
        sMoneyPlace = getViewById(R.id.sMoneyPlace);
        sExpensesCategory = getViewById(R.id.sExpensesCategory);
    }

    private void fillUiWithDbData() {
        fillCurrenciesSpinner();
        fillExpensesSpinner();
        fillMoneyPlaceSpinner();
    }

    private void fillCurrenciesSpinner() {
        Currency[] currencies = drebedengiEntityDao.getCurrencies();
        ArrayAdapter<Currency> adapter = createBaseSpinnerArrayAdapter(currencies);
        sCurrency.setAdapter(adapter);
    }

    private void fillExpensesSpinner() {
        FinancialTarget[] financialTargets = drebedengiEntityDao.getExpenseCategories();
        ArrayAdapter<FinancialTarget> adapter = createBaseSpinnerArrayAdapter(financialTargets);
        sExpensesCategory.setAdapter(adapter);
    }

    private void fillMoneyPlaceSpinner() {
        FinancialTarget[] financialTargets = drebedengiEntityDao.getMoneyPlaceCategories();
        ArrayAdapter<FinancialTarget> adapter = createBaseSpinnerArrayAdapter(financialTargets);
        sMoneyPlace.setAdapter(adapter);
    }

    private <V> V getViewById(int resourceId) {
        try {
            return (V) findViewById(resourceId);
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private <T> ArrayAdapter<T> createBaseSpinnerArrayAdapter(T[] data) {
        ArrayAdapter<T> arrayAdapter =
                new ArrayAdapter<T>(this, android.R.layout.simple_spinner_item, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }


    public void findRecords(View view) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        drebedengiEntityDao.close();
    }
}
