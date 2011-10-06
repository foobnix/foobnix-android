package com.foobnix.commons;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return false;
        }
        return true;
    }
}
