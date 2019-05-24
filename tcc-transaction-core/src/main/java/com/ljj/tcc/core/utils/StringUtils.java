package com.ljj.tcc.core.utils;

/**
 * Created by liangjinjing on 11/11/15.
 */
public class StringUtils {

    public static boolean isNotEmpty(String value) {

        if(value == null) {
            return false;
        }

        if(value.equals("")) {
            return false;
        }

        return true;
    }
}
