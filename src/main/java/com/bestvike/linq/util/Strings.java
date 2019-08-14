package com.bestvike.linq.util;

/**
 * Created by 许崇雷 on 2019-08-09.
 */
public final class Strings {
    public static final String Empty = "";

    private Strings() {
    }

    public static boolean isNullOrEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }
}
