package com.gmail.maxdiland.drebedengireports.db.entity;

import com.gmail.maxdiland.drebedengireports.db.util.annotation.Field;

/**
 * author Maksim Diland (yc14md1)
 */
public class FinancialOperation {

    @Field("sum")
    private int sum;
    @Field("currency")
    private String currency;
    @Field("operation_date")
    private String operationDate;
    @Field("place")
    private String placeName;
    @Field("target")
    private String targetName;
    @Field("comment")
    private String comment;

    public float getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        String pattern = "%d %s  %s  %s  %s  %s";
        return String.format(pattern, sum, currency, operationDate, placeName, targetName, comment);
    }
}
