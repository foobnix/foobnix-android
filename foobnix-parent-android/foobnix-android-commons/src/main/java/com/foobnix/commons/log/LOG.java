package com.foobnix.commons.log;

import android.util.Log;

/**
 * @author iivanenko
 * 
 */
public class LOG {
    public static boolean enableDebug = true;

    /**
     * Debug log
     * 
     * @param messages
     */
    public static void d(Object... messages) {
        if (enableDebug) {
            general("DEBUG", messages);
        }

    }

    /**
     * Test log
     * 
     * @param messages
     */
    public static void t(Object... messages) {
        if (enableDebug) {
            general("TEST", messages);
        }
    }

    /**
     * Broadcast log
     * 
     * @param messages
     */
    public static void br(Object... messages) {
        if (enableDebug) {
            general("BROADCAST", messages);
        }
    }

    /**
     * Error log
     * 
     * @param message
     * @param th
     */
    public static void e(String message, Throwable th) {
        Log.e("ERROR", message, th);
        th.printStackTrace();
    }

    public static void e(Throwable th) {
        e("ERROR", th);
    }

    private static void general(String tag, Object... messages) {
        StringBuilder result = new StringBuilder();
        for (Object msg : messages) {
            result.append(msg).append(" ");
        }
        Log.d(tag, result.toString());
    }

}
