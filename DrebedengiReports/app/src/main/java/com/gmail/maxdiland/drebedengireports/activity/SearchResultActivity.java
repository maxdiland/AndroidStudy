package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.adapter.FinancialOperationAdapter;
import com.gmail.maxdiland.drebedengireports.bo.FinancialOperationBO;
import com.gmail.maxdiland.drebedengireports.converter.dbentity.FinancialOperationConverter;
import com.gmail.maxdiland.drebedengireports.db.OperationDao;
import com.gmail.maxdiland.drebedengireports.request.ExpensesRequest;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends Activity {

    public static final String SEARCH_CRITERIA_KEY = "SEARCH_CRITERIA_KEY";
    public static final String CATEGORY_NAME_KEY = "CATEGORY_NAME_KEY";
    public static final int DIGITS_AFTER_COMMA = 2;
    private OperationDao operationDao;
    private FinancialOperationConverter operationConverter = new FinancialOperationConverter();

    private TextView tvNotFound;
    private ListView lvFoundOperations;
    private LinearLayout llSummary;
    private FinancialOperationBO[] operationsBO;
    private ExpensesRequest request;
    private String searchTargetCategoryName;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        resources = getResources();

        initUi();
        initDao();
        initFieldsFromIntent();
        getOperationsFromDb();
        updateUiWithData();
    }

    private void initFieldsFromIntent() {
        Intent intent = getIntent();
        request = intent.getParcelableExtra(SEARCH_CRITERIA_KEY);
        searchTargetCategoryName = intent.getStringExtra(CATEGORY_NAME_KEY);
        if (request == null) {
            throw new IllegalStateException("To start SearchResultActivity the instance " +
                    "of ExpensesRequest should be passed through Intent"
            );
        }
    }

    private void initUi() {
        tvNotFound = getViewById(R.id.tvNotFound);
        lvFoundOperations = getViewById(R.id.lvFoundOperations);
        llSummary = getViewById(R.id.llSummary);
    }

    private void initDao() {
        String dbPath = getIntent().getStringExtra(SearchFormActivity.DATABASE_PATH_KEY);
        operationDao = new OperationDao(new File(dbPath));
    }

    private void updateUiWithData() {
        if (operationsBO.length != 0) {
            tvNotFound.setVisibility(View.GONE);
            FinancialOperationAdapter adapter = new FinancialOperationAdapter(this, operationsBO);
            lvFoundOperations.setAdapter(adapter);
            lvFoundOperations.setOnItemLongClickListener(new OperationLongClickListener());
            lvFoundOperations.setVisibility(View.VISIBLE);
            fillSummary();
            llSummary.setVisibility(View.VISIBLE);
        }
    }

    private void getOperationsFromDb() {
        operationsBO = operationConverter.convert( operationDao.findOperations(request) );
    }

    private void fillSummary() {
        Map<String, BigDecimal> sums = calculateSums();

        TextView tvByCategory = (TextView) llSummary.findViewById(R.id.tvByCategory);
        LinearLayout llSumsForCategory =
                (LinearLayout) llSummary.findViewById(R.id.llSumsForCategory);

        llSumsForCategory.removeAllViews();

        String totalByCategoryPattern = resources.getString(R.string.totalByCategory);
        tvByCategory.setText(String.format(totalByCategoryPattern,  searchTargetCategoryName));
        for (Map.Entry<String, BigDecimal> entry : sums.entrySet()) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.END);
            BigDecimal sum = entry.getValue();
            sum = sum.setScale(DIGITS_AFTER_COMMA, RoundingMode.HALF_UP);
            textView.setText(String.valueOf(sum.floatValue()) + " " + entry.getKey());
            llSumsForCategory.addView(textView);
        }
    }

    private Map<String, BigDecimal> calculateSums() {
        Map<String, BigDecimal> sums = new HashMap<>();
        for (FinancialOperationBO operation : operationsBO) {
            if (operation.isIgnored()) {
                continue;
            }

            String currency = operation.getCurrency();
            BigDecimal operationSum = new BigDecimal(operation.getSum());
            BigDecimal calculatedSum = sums.get(currency);
            if (calculatedSum == null) {
                sums.put(currency, operationSum);
            } else {
                calculatedSum = calculatedSum.add(operationSum);
                sums.put(currency, calculatedSum);
            }
        }
        return sums;
    }

    private <V> V getViewById(int resourceId) {
        try {
            return (V) findViewById(resourceId);
        } catch (ClassCastException ex) {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        operationDao.close();
    }

    private class OperationLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            FinancialOperationAdapter adapter = (FinancialOperationAdapter) parent.getAdapter();
            FinancialOperationBO operation = adapter.getItem(position);

            if (operation.isIgnored()) {
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                int color = getResources().getColor(R.color.ignoredOperationListItem);
                view.setBackgroundColor(color);
            }

            operation.setIgnored( !operation.isIgnored() );
            fillSummary();
            return true;
        }
    }
}
