package com.gmail.maxdiland.drebedengireports.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.maxdiland.drebedengireports.parcelablecreator.ExpensesRequestParcelableCreator;
import com.gmail.maxdiland.drebedengireports.util.dateformat.DateTimeFormat;
import com.gmail.maxdiland.drebedengireports.util.sql.SqlComparisonOperator;
import com.gmail.maxdiland.drebedengireports.util.sql.SqlUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

/**
 * author Maksim Diland
 */
public class ExpensesRequest implements SqlWhereClauseBuildable, Parcelable {

    public static final Parcelable.Creator<ExpensesRequest> CREATOR =
            new ExpensesRequestParcelableCreator();
    private static final int SUM_COINS_MULTIPLIER = 100;

    private Integer sum;
    private SqlComparisonOperator sumClauseOperator;
    private Integer currencyId;
    private Integer targetId;
    private Integer placeId;
    private String dateFrom;
    private String dateTo;
    private String comment;

    @Override
    public String buildWhereClause() {
        return SqlUtil.buildCriteria(
                SqlUtil.buildAndClause(
                        buildSumClause(), buildCurrencyIdClause(), buildTargetIdClause(),
                        buildPlaceIdClause(), buildCommentClause()
                ),
                "operation_date",
                SqlUtil.buildAndClause(buildDateFromClause(), buildDateToClause()),
                null
        );
    }

    private String buildDateFromClause() {
        if (StringUtils.isEmpty(dateFrom)) {
            return "";
        }

        return SqlUtil.buildGreaterEqualsCondition(
                SqlUtil.buildFunctionStatement("datetime", "operation_date"),
                buildDateFunction(dateFrom)
        );
    }


    private String buildDateToClause() {
        if (StringUtils.isEmpty(dateTo)) {
            return "";
        }

        return SqlUtil.buildLessCondition(
                SqlUtil.buildFunctionStatement("datetime", "operation_date"),
                buildDateFunction(dateTo)
        );
    }

    private String buildDateFunction(String date) {
        try {
            String sqliteFormattedDate = DateTimeFormat.SQL_LITE_DETAILED.format(
                    DateTimeFormat.SHORT_DATE.parse(date));

            return SqlUtil.buildFunctionStatement(
                    "datetime", SqlUtil.wrapWithSingleQuotes(sqliteFormattedDate)
            );
        } catch (ParseException e) {
            // TODO handle Parse exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String buildSumClause() {
        return (sum != null) ? SqlUtil.buildClause("sum", sumClauseOperator, sum) : "";
    }

    private String buildCurrencyIdClause() {
        return currencyId != null ? SqlUtil.buildEqualsClause("currency_id", currencyId) : "";
    }

    private String buildTargetIdClause() {
        return targetId != null ? SqlUtil.buildEqualsClause("target_id", targetId) : "";
    }

    private String buildPlaceIdClause() {
        return placeId != null ? SqlUtil.buildEqualsClause("place_id", placeId) : "";
    }

    private String buildCommentClause() {
        return comment != null ? SqlUtil.buildLikeClauseContainingPhrase("comment", comment) : "";
    }

    public Integer getSum() {
        return sum;
    }

    public void setFloatSum(float sum) {
        this.sum = (int) (sum * SUM_COINS_MULTIPLIER);
    }

    public void setRawSum(int sum) {
        this.sum = sum;
    }

    public SqlComparisonOperator getSumClauseOperator() {
        return sumClauseOperator;
    }

    public void setSumClauseOperator(SqlComparisonOperator sumClauseOperator) {
        this.sumClauseOperator = sumClauseOperator;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sum);
        dest.writeInt(
                sumClauseOperator == null ?
                        ExpensesRequestParcelableCreator.ENUM_FIELD_NULL_VALUE :
                        sumClauseOperator.ordinal()
        );
        dest.writeValue(currencyId);
        dest.writeValue(targetId);
        dest.writeValue(placeId);
        dest.writeValue(dateFrom);
        dest.writeValue(dateTo);
        dest.writeValue(comment);
    }
}
