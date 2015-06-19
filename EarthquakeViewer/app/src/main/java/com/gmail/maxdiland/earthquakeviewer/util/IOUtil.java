package com.gmail.maxdiland.earthquakeviewer.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * author Max Diland
 */
public class IOUtil {

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
