package com.sjtu.chenzhongpu.cantonese;

import java.io.IOException;
import java.io.UnsupportedEncodingException;



/**
 * Created by chenzhongpu on 9/18/16.
 */
public class Utils {

    public static String getBig5FromString(String query) throws UnsupportedEncodingException{
        byte[] bytes = query.getBytes("Big5");
        if (bytes.length != 2) {
            throw new UnsupportedEncodingException();
        } else {
            char[] hexArray = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for ( int j = 0; j < bytes.length; j++ ) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
    }

    public static WordBean getWordBeanFromBig5(String big5) throws IOException {

        return null;

    }
}

