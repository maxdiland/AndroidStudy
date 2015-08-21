package com.gmail.maxdiland.drebedengireports.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.maxdiland.drebedengireports.R;
import com.gmail.maxdiland.drebedengireports.bo.FinancialOperationBO;
import com.gmail.maxdiland.drebedengireports.util.dateformat.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author Max Diland
 */
public class FinancialOperationAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<FinancialOperationBO> data;
    private Resources resources;

    public FinancialOperationAdapter(Context context, FinancialOperationBO[] data) {
        this(context, new ArrayList<>(Arrays.asList(data)));
    }

    public FinancialOperationAdapter(Context context, List<FinancialOperationBO> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        resources = context.getResources();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FinancialOperationBO getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.financial_operation_list_item, parent, false);
        }

        FinancialOperationBO operation = getItem(position);

        TextView tvSum = (TextView) convertView.findViewById(R.id.tvSum);
        TextView tvPlace = (TextView) convertView.findViewById(R.id.tvPlace);
        TextView tvTarget = (TextView) convertView.findViewById(R.id.tvTarget);
        TextView tvCurrency = (TextView) convertView.findViewById(R.id.tvCurrency);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);

        tvSum.setText(String.valueOf(operation.getSum()));
        tvPlace.setText(operation.getPlaceName());
        tvTarget.setText(operation.getTargetName());
        tvCurrency.setText(operation.getCurrency());
        tvDate.setText(DateTimeFormat.SHORT_DATE_TIME.format(operation.getOperationDate()));
        tvComment.setText(operation.getComment());

        if (operation.isIgnored()) {
            int color = resources.getColor(R.color.ignoredOperationListItem);
            convertView.setBackgroundColor(color);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }
}