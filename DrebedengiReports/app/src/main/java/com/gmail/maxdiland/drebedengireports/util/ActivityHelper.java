package com.gmail.maxdiland.drebedengireports.util;

import android.app.Activity;
import android.view.View;

/**
 * author Max Diland
 */
public final class ActivityHelper {
    private Activity activity;
    public ActivityHelper(Activity activity) {
        this.activity = activity;
    }

    public <V> V findViewById(int resourceId) {
        try {
            return (V) getViewById(resourceId);
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private View getViewById(int resourceId) {
        return activity.findViewById(resourceId);
    }

}
