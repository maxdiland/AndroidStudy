package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.bo.FinancialTargetBO;
import com.gmail.maxdiland.drebedengireports.converter.dbentity.FinancialTargetConverter;
import com.gmail.maxdiland.drebedengireports.db.DrebedengiEntityDao;
import com.gmail.maxdiland.drebedengireports.db.entity.Currency;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialTarget;
import com.gmail.maxdiland.drebedengireports.request.ExpensesRequest;
import com.gmail.maxdiland.drebedengireports.util.dateformat.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchFormActivity extends Activity {
    private static final int DIALOG_FROM_DATE_PICKER = 1;
    private static final int DIALOG_TILL_DATE_PICKER = 2;

    public static final String DATABASE_PATH_KEY = "DATABASE_PATH_KEY";
    private static final int EMPTY_ENTRY_ID = -1;
    public static final String EMPTY_SPINNER_ENTRY = "";

    private DrebedengiEntityDao drebedengiEntityDao;
    private EditText etSum;
    private Spinner sCurrency;
    private Spinner sMoneyPlace;
    private Spinner sExpensesCategory;
    private EditText etFromDate;
    private EditText etTillDate;
    private EditText etComment;

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
        etFromDate = getViewById(R.id.etFromDate);
        etTillDate = getViewById(R.id.etToDate);
        etComment = getViewById(R.id.etComment);
    }

    private void fillUiWithDbData() {
        fillCurrenciesSpinner();
        fillExpensesSpinner();
        fillMoneyPlaceSpinner();
    }

    private void fillCurrenciesSpinner() {
        Currency[] currencies = addEmptyEntryToTheBeginning(
                new Currency(EMPTY_ENTRY_ID, EMPTY_SPINNER_ENTRY),
                drebedengiEntityDao.getCurrencies()
        );
        ArrayAdapter<Currency> adapter = createBaseSpinnerArrayAdapter(currencies);
        sCurrency.setAdapter(adapter);
    }

    private void fillMoneyPlaceSpinner() {
        FinancialTarget[] financialTargets = addEmptyEntryToTheBeginning(
                new FinancialTarget(EMPTY_ENTRY_ID, EMPTY_SPINNER_ENTRY, 0, null),
                drebedengiEntityDao.getMoneyPlaceCategories()
        );
        ArrayAdapter<FinancialTarget> adapter = createBaseSpinnerArrayAdapter(financialTargets);
        sMoneyPlace.setAdapter(adapter);
    }

    private void fillExpensesSpinner() {
//        FinancialTarget[] financialTargets = addEmptyEntryToTheBeginning(
//                new FinancialTarget(EMPTY_ENTRY_ID, EMPTY_SPINNER_ENTRY, 0),
//                drebedengiEntityDao.getExpenseCategories()
//        );
        FinancialTarget[] financialTargets = drebedengiEntityDao.getExpenseCategories();
        FinancialTargetConverter financialTargetConverter = new FinancialTargetConverter();
        FinancialTargetBO[] parentFinancialTargets =
                financialTargetConverter.convert(financialTargets);
        ArrayAdapter<FinancialTargetBO> adapter = createBaseSpinnerArrayAdapter(parentFinancialTargets);
        sExpensesCategory.setAdapter(adapter);
    }

    private <T> T[] addEmptyEntryToTheBeginning(T object, T[] array) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        Class<?> componentType = array.getClass().getComponentType();
        list.add(0, object); //TODO Investigate ability ho have non-selected (empty) entry in a spinner.
        T[] resultArray = (T[]) Array.newInstance(componentType, list.size());
        return list.toArray(resultArray);
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
        ExpensesRequest expensesRequest = new ExpensesRequest();
        String sum = etSum.getText().toString();
        if (StringUtils.isNotEmpty(sum)) {
            expensesRequest.setSum( Float.parseFloat(sum) ); // TODO catch potential exception
        }

        int currencyId = ((Currency) sCurrency.getSelectedItem()).getId();
        if (isNotEmptySpinnerEntry(currencyId)) {
            expensesRequest.setCurrencyId( currencyId );
        }

        int moneyPlaceId = ((FinancialTarget) sMoneyPlace.getSelectedItem()).getId();
        if (isNotEmptySpinnerEntry(moneyPlaceId)) {
            expensesRequest.setPlaceId(moneyPlaceId);
        }

        FinancialTargetBO expenseCategory = (FinancialTargetBO) sExpensesCategory.getSelectedItem();
        int expenseCategoryId = expenseCategory.getId();
        if (isNotEmptySpinnerEntry(expenseCategoryId)) {
            expensesRequest.setTargetId(expenseCategoryId);
        }

        String fromDate = etFromDate.getText().toString();
        if (StringUtils.isNotEmpty(fromDate)) {
            expensesRequest.setDateFrom(fromDate);
        }

        String toDate = etTillDate.getText().toString();
        if (StringUtils.isNotEmpty(toDate)) {
            expensesRequest.setDateTo(toDate);
        }

        String comment = etComment.getText().toString();
        if (StringUtils.isNotEmpty(comment)) {
            expensesRequest.setComment(comment);
        }

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.SEARCH_CRITERIA_KEY, expensesRequest);
        intent.putExtra(SearchResultActivity.CATEGORY_NAME_KEY, expenseCategory.getName());
        intent.putExtra(DATABASE_PATH_KEY, getIntent().getStringExtra(DATABASE_PATH_KEY));
        startActivity(intent);
    }

    private boolean isNotEmptySpinnerEntry(int currencyId) {
        return currencyId > EMPTY_ENTRY_ID;
    }

    @Override
    protected Dialog onCreateDialog(int id) { //TODO migrate to non-deprecated DialogFragment
        Calendar currentDate = Calendar.getInstance();

        if (DIALOG_FROM_DATE_PICKER == id) {
            return new DatePickerDialog(
                    this, new OnDatePickedListener(etFromDate), currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)
            );
        } else if (DIALOG_TILL_DATE_PICKER == id) {
            return new DatePickerDialog(
                    this, new OnDatePickedListener(etTillDate), currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)
            );
        }
        return super.onCreateDialog(id);
    }

    public void pickFromDate(View view) {
        showDialog(DIALOG_FROM_DATE_PICKER);
    }

    public void pickTillDate(View view) {
        showDialog(DIALOG_TILL_DATE_PICKER);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drebedengiEntityDao.close();
    }
}

class OnDatePickedListener implements DatePickerDialog.OnDateSetListener {
    private TextView targetView;

    OnDatePickedListener(TextView targetView) {
        this.targetView = targetView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        Date date = calendar.getTime();

        targetView.setText(DateTimeFormat.SHORT_DATE.format(date));
    }
}