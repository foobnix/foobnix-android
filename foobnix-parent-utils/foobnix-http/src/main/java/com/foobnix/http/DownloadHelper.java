package com.foobnix.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadHelper {

    public static void downloadUrlToFile(String fromUrl, File to, ProgressListener progressListener) throws IOException {
        if (fromUrl == null || fromUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("FromUrl is empty");
        }
        if (to == null) {
            throw new IllegalArgumentException("Download to is empty");
        }

        URL url = new URL(fromUrl);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET");
        connect.setDoOutput(true);
        connect.connect();

        FileOutputStream toStream = new FileOutputStream(to);
        InputStream fromStream = connect.getInputStream();

        if (fromStream == null) {
            return;
        }

        byte[] buffer = new byte[1024];
        int lenght = 0;
        int size = connect.getContentLength();
        int current = 0;

        while ((lenght = fromStream.read(buffer)) > 0) {
            toStream.write(buffer, 0, lenght);
            current += lenght;
            // persent
            // current * 100 / size
            if (progressListener != null) {
                progressListener.contentSize(size);
                progressListener.transferred(current);
            }
        }
        toStream.close();
        fromStream.close();

        return;
    }
}
