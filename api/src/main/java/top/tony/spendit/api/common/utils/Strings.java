/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.common.utils;


import org.apache.commons.text.StringEscapeUtils;

import java.util.Base64;

public class Strings {
    private Strings() {}

    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String escape(String str) {
        // escape so that JSON can use
        return StringEscapeUtils.escapeJson(str);
    }

    public static String unescape(String str) {
        return StringEscapeUtils.unescapeJson(str);
    }

    public static String toBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String fromBase64(String str) {
        return new String(Base64.getDecoder().decode(str));
    }
}
