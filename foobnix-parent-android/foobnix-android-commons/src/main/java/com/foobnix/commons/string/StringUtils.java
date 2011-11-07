package com.foobnix.commons.string;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getStringIfEmpty(String result, String value) {
        if(isEmpty(result)){
            return value;
        }
        return result;
    }
}
