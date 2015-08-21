package com.gmail.maxdiland.drebedengireports.converter.dbentity;

/**
 * author Max Diland
 */
public interface EntityConverter<I, O> {
    O convert(I objectToConvert);
    O[] convert(I[] objectsToConvert);
}
