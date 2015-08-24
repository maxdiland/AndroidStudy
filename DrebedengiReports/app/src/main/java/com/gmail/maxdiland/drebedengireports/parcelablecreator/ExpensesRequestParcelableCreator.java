package com.gmail.maxdiland.drebedengireports.parcelablecreator;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.maxdiland.drebedengireports.request.ExpensesRequest;
import com.gmail.maxdiland.drebedengireports.util.sql.SqlComparisonOperator;

/**
 * author Max Diland
 */
public class ExpensesRequestParcelableCreator implements Parcelable.Creator<ExpensesRequest> {
    @Override
    public ExpensesRequest createFromParcel(Parcel source) {
        ExpensesRequest expensesRequest = new ExpensesRequest();
        ClassLoader integerClassLoader = Integer.class.getClassLoader();
        ClassLoader stringClassLoader = String.class.getClassLoader();
        Integer sum = (Integer) source.readValue(integerClassLoader);
        if (sum != null) {
            expensesRequest.setRawSum(sum);
        }
        int ordinal = source.readInt();
        expensesRequest.setSumClauseOperator(SqlComparisonOperator.values()[ordinal]);

        expensesRequest.setCurrencyId( (Integer) source.readValue(integerClassLoader) );
        expensesRequest.setTargetId( (Integer) source.readValue(integerClassLoader) );
        expensesRequest.setPlaceId( (Integer) source.readValue(integerClassLoader) );

        expensesRequest.setDateFrom((String) source.readValue(stringClassLoader));
        expensesRequest.setDateTo((String) source.readValue(stringClassLoader));
        expensesRequest.setComment((String) source.readValue(stringClassLoader));

        return expensesRequest;
    }

    @Override
    public ExpensesRequest[] newArray(int size) {
        return new ExpensesRequest[size];
    }
}
