package com.ljj.tcc.core.utils;

/**
 * Created by liangjinjing on 2/14/16.
 */
public class ByteUtils {

    public static byte[] longToBytes(long num) {
        return String.valueOf(num).getBytes();
    }

    public static long bytesToLong(byte[] bytes) {
        return Long.valueOf(new String(bytes));
    }

    public static byte[] intToBytes(int num) {
        return String.valueOf(num).getBytes();
    }

    public static int bytesToInt(byte[] bytes) {
        return Integer.valueOf(new String(bytes));
    }

}
