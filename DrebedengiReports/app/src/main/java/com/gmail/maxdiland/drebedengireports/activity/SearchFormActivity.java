package com.gmail.maxdiland.drebedengireports.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.db.DrebedengiDao;
import com.gmail.maxdiland.drebedengireports.entity.Currency;

import java.io.File;

public class SearchFormActivity extends ActionBarActivity {
    public static final String DATABASE_PATH_KEY = "DATABASE_PATH_KEY";



    private DrebedengiDao drebedengiDao;
    private EditText etSum;
    private Spinner sCurrency;
    private Spinner sMoneyPlace;
    private Spinner sExpensesCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);

        initUi();

        drebedengiDao = new DrebedengiDao(
                new File(getIntent().getStringExtra(DATABASE_PATH_KEY))
        );

        fillUiWithDbData();
    }

    private void initUi() {
        etSum = getViewById(R.id.etSum);
        sCurrency = getViewById(R.id.sCurrency);
        sMoneyPlace = getViewById(R.id.sMoneyPlace);
        sExpensesCategory = getViewById(R.id.sExpensesCategory);
    }


    private void fillUiWithDbData() {
        fillCurrenciesSpinner();

    }

    private void fillCurrenciesSpinner() {
        Currency[] currencies = drebedengiDao.getCurrencies();
        ArrayAdapter<Currency> currencyAdapter = createBaseSpinnerArrayAdapter(currencies);
        sCurrency.setAdapter(currencyAdapter);
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
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }


}
