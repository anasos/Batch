package com.vgf.dbs.process.DBS_process.util;

public class FormatUtil {


    public static String formatPhoneNumber(String digits) {
        if (digits == null || digits.length() != 10) {
            return digits;
        }
        return String.format("%s %s %s %s %s",
                digits.substring(0, 2),
                digits.substring(2, 4),
                digits.substring(4, 6),
                digits.substring(6, 8),
                digits.substring(8, 10)
        );
    }

}
