package com.gmail.maxdiland.drebedengireports.converter.dbentity;

import android.util.Log;

import com.gmail.maxdiland.drebedengireports.bo.FinancialOperationBO;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialOperation;
import com.gmail.maxdiland.drebedengireports.util.dateformat.DateTimeFormat;

import java.text.ParseException;
import java.util.Date;

/**
 * author Max Diland
 */
public class FinancialOperationConverter
        implements EntityConverter<FinancialOperation, FinancialOperationBO> {

    private static final String TAG = "Converter";


    @Override
    public FinancialOperationBO[] convert(FinancialOperation[] objectsToConvert) {
        FinancialOperationBO[] operationBOs = new FinancialOperationBO[objectsToConvert.length];

        for (int i = 0; i < objectsToConvert.length; i++) {
            operationBOs[i] = convert(objectsToConvert[i]);
        }
        return operationBOs;
    }

    @Override
    public FinancialOperationBO convert(FinancialOperation objectToConvert) {
        FinancialOperationBO convertedOperation = new FinancialOperationBO();

        populateSum(objectToConvert, convertedOperation);
        populateDate(objectToConvert, convertedOperation);

        convertedOperation.setCurrency(objectToConvert.getCurrency());
        convertedOperation.setPlaceName(objectToConvert.getPlaceName());
        convertedOperation.setTargetName(objectToConvert.getTargetName());
        convertedOperation.setComment(objectToConvert.getComment());

        return convertedOperation;
    }


    private void populateDate(FinancialOperation operation, FinancialOperationBO operationBO) {
        String operationDate = operation.getOperationDate();
        try {
            Date date = DateTimeFormat.DATE_FORMAT.parse(operationDate);
            operationBO.setOperationDate( date );

        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date: " + operationDate);
            throw new RuntimeException(e);
        }
    }

    private void populateSum(FinancialOperation operation, FinancialOperationBO operationBO) {
        operationBO.setSum(operation.getSum() / 100);
    }
}
