package com.foobnix.commons;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return false;
        }
        return true;
    }

    public static String getStringIfEmpty(String result, String value) {
        if(isEmpty(result)){
            return value;
        }
        return result;
    }
}
