package com.gmail.maxdiland.drebedengireports.util;

import android.content.Context;
import android.widget.Toast;

/**
 * author Max Diland
 */
public final class Toaster {
    private Toaster() {}

    public static void makeShort(Context context, String message) {
        make(context, message, false);
    }

    public static void makeLong(Context context, String message) {
        make(context, message, true);
    }

    private static void make(Context context, String message, boolean isLong) {
        int duration = isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, message, duration).show();
    }

}
