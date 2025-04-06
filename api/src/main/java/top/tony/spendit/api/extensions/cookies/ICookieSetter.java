/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.extensions.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface ICookieSetter {
    Cookie set(String value);

    Cookie clean();

    String get(HttpServletRequest request);
}
