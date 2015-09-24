package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.adapter.ExtendedArrayAdapter;
import com.gmail.maxdiland.drebedengireports.db.DrebedengiEntityDao;
import com.gmail.maxdiland.drebedengireports.db.entity.Currency;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialTarget;
import com.gmail.maxdiland.drebedengireports.exception.validation.SearchFormValidationException;
import com.gmail.maxdiland.drebedengireports.request.ExpensesRequest;
import com.gmail.maxdiland.drebedengireports.util.Toaster;
import com.gmail.maxdiland.drebedengireports.util.dateformat.DateTimeFormat;
import com.gmail.maxdiland.drebedengireports.util.sql.SqlComparisonOperator;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.gmail.maxdiland.drebedengireports.util.sql.SqlComparisonOperator.*;

public class SearchFormActivity extends Activity {
    private static final int DIALOG_FROM_DATE_PICKER = 1;
    private static final int DIALOG_TILL_DATE_PICKER = 2;

    private static final int EMPTY_ENTRY_ID = -1;
    public static final String EMPTY_SPINNER_ENTRY = "";

    private Resources resources;
    private String sumModeAny;
    private String sumModeLess;
    private String sumModeEqual;
    private String sumModeGreater;

    private DrebedengiEntityDao drebedengiEntityDao;
    private Spinner sSumMode;
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

        init();
        initUi();
        fillUiWithStaticData();
        initDao();
        fillUiWithDbData();
    }

    private void init() {
        resources = getResources();
        sumModeAny = resources.getString(R.string.sumModeAny);
        sumModeLess = resources.getString(R.string.sumModeLess);
        sumModeEqual = resources.getString(R.string.sumModeEqual);
        sumModeGreater = resources.getString(R.string.sumModeGreater);
    }

    private void initDao() {
        File dbFile = new File(
                getIntent().getStringExtra(ChooseDbActivity.EXTRA_DB_KEY)
        );
        drebedengiEntityDao = new DrebedengiEntityDao(dbFile);
    }

    private void initUi() {
        sSumMode = getViewById(R.id.sSumMode);
        etSum = getViewById(R.id.etSum);
        sCurrency = getViewById(R.id.sCurrency);
        sMoneyPlace = getViewById(R.id.sMoneyPlace);
        sExpensesCategory = getViewById(R.id.sExpensesCategory);
        etFromDate = getViewById(R.id.etFromDate);
        etTillDate = getViewById(R.id.etToDate);
        etComment = getViewById(R.id.etComment);
    }

    private void fillUiWithStaticData() {
        setupSumModeSpinner();
    }

    private void setupSumModeSpinner() {
        String[] staticData = {sumModeAny, sumModeLess, sumModeEqual, sumModeGreater};
        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, staticData);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSumMode.setAdapter(stringArrayAdapter);
        sSumMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isSumEnabled = !(parent.getSelectedItem().equals(sumModeAny));
                etSum.setEnabled(isSumEnabled);
                sCurrency.setEnabled(isSumEnabled);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fillUiWithDbData() {
        fillCurrenciesSpinner();
        fillExpensesSpinner();
        fillMoneyPlaceSpinner();
    }

    private void fillCurrenciesSpinner() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(EMPTY_ENTRY_ID, EMPTY_SPINNER_ENTRY));
        currencies.addAll(Arrays.asList(drebedengiEntityDao.getCurrencies()));
        ArrayAdapter<Currency> adapter = createBaseSpinnerArrayAdapter(currencies);
        sCurrency.setAdapter(adapter);
    }

    private void fillMoneyPlaceSpinner() {
        List<FinancialTarget> financialTargets = new ArrayList<>();
        financialTargets.add(new FinancialTarget(EMPTY_ENTRY_ID, EMPTY_SPINNER_ENTRY, 0, null));
        financialTargets.addAll(Arrays.asList(drebedengiEntityDao.getMoneyPlaceCategories()));
        ArrayAdapter<FinancialTarget> adapter = createBaseSpinnerArrayAdapter(financialTargets);
        sMoneyPlace.setAdapter(adapter);
    }

    private void fillExpensesSpinner() {
        sExpensesCategory.setAdapter(new ExpensesSpinner(
                Arrays.asList(drebedengiEntityDao.getSortedExpenseCategories())
        ));
    }

    private <V> V getViewById(int resourceId) {
        try {
            return (V) findViewById(resourceId);
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private <T> ArrayAdapter<T> createBaseSpinnerArrayAdapter(List<T> data) {
        ArrayAdapter<T> arrayAdapter =
                new ExtendedArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    public void findRecords(View view) {
        ExpensesRequest expensesRequest = generateExpensesRequest();
        try {
            validateInput(expensesRequest);
        } catch (SearchFormValidationException e) {
            Toaster.makeShort(this, e.getMessage());
            return;
        }

        String requestedCategoryName =
                ((FinancialTarget) sExpensesCategory.getSelectedItem()).getName();
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.SEARCH_CRITERIA_KEY, expensesRequest);
        intent.putExtra(SearchResultActivity.CATEGORY_NAME_KEY, requestedCategoryName);
        intent.putExtra(
                ChooseDbActivity.EXTRA_DB_KEY,
                getIntent().getStringExtra(ChooseDbActivity.EXTRA_DB_KEY)
        );
        startActivity(intent);
    }

    private ExpensesRequest generateExpensesRequest() {
        ExpensesRequest expensesRequest = new ExpensesRequest();

        // In Db expenses are stored with negative sums.
        // So sum will be multiplied by -1
        // and comparison operator will be reversed
        String sumMode = (String) this.sSumMode.getSelectedItem();
        if (sumModeAny.equals(sumMode)) {
            expensesRequest.setSumClauseOperator(null);
        } else if (sumModeLess.equals(sumMode)) {
            expensesRequest.setSumClauseOperator(GREATER_THAN);
        } else if (sumModeEqual.equals(sumMode)) {
            expensesRequest.setSumClauseOperator(EQUAL);
        } else if (sumModeGreater.equals(sumMode)) {
            expensesRequest.setSumClauseOperator(LESS_THAN);
        }

        String sum = etSum.getText().toString();
        if (StringUtils.isNotEmpty(sum)) {
            try {
                expensesRequest.setFloatSum( -1 * Float.parseFloat(sum) );
            } catch (NumberFormatException e) {
                // Just catch without setting the sum.
                // Further validation will ask user to enter valid number
            }
        }

        int currencyId = ((Currency) sCurrency.getSelectedItem()).getId();
        if (isNotEmptySpinnerEntry(currencyId)) {
            expensesRequest.setCurrencyId( currencyId );
        }

        int moneyPlaceId = ((FinancialTarget) sMoneyPlace.getSelectedItem()).getId();
        if (isNotEmptySpinnerEntry(moneyPlaceId)) {
            expensesRequest.setPlaceId(moneyPlaceId);
        }

        FinancialTarget expenseCategory = (FinancialTarget) sExpensesCategory.getSelectedItem();
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
        return expensesRequest;
    }

    private void validateInput(ExpensesRequest request) throws SearchFormValidationException {
        SqlComparisonOperator operator = request.getSumClauseOperator();
        if (LESS_THAN == operator || EQUAL == operator || GREATER_THAN == operator) {
            if (request.getSum() == null) {
                throw new SearchFormValidationException("Please enter positive number for sum");
            }
        }
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

    private class ExpensesSpinner extends ExtendedArrayAdapter<FinancialTarget> {
        private ExpensesSpinner(List<FinancialTarget> expenseCategories) {
            super(SearchFormActivity.this, android.R.layout.simple_spinner_item, expenseCategories);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        protected boolean propagateDataOnDropDownView(int position, View view) {
            FinancialTarget item = getItem(position);
            Resources res = getResources();
            String displayText = item.isRoot() ?
                    res.getString(R.string.spinner_spacer_zero) :
                    res.getString(R.string.spinner_spacer);
            displayText += item.getName();
            ((CheckedTextView) view).setText(displayText);
            return true;
        }
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