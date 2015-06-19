package com.gmail.maxdiland.earthquakeviewer.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author Max Diland
 */
public class HttpUtil {

    public static InputStream getInputStreamFromResource(String resource) {
        try {
            URL url = new URL(resource);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

            if (HttpURLConnection.HTTP_OK == httpConnection.getResponseCode()) {
                return httpConnection.getInputStream();
            } else {
                throw new RuntimeException("Connection is unsuccessful");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBodyBytesFromResource(String resource) {
        BufferedInputStream is = null;
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream(1024);
        try {
            is = new BufferedInputStream( getInputStreamFromResource(resource) );
            int readByte;
            while ( (readByte = is.read()) != -1 ) {
                resultStream.write(readByte);
            }
            return resultStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeStream(is);
        }
    }
}
