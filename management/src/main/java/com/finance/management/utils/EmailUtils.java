package com.finance.management.utils;

import java.util.regex.Matcher;

public class EmailUtils {
    public static String revertDotsBeforeAt(String email, char replacement) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        String replacementStr = Matcher.quoteReplacement(String.valueOf(replacement));
        return email.replaceAll("\\$(?=[^@]+@)", replacementStr);
    }

    public static boolean isEmailDots(String email) {
        String regex = "^[^@]*\\$[^@]*@.*";
        boolean result = email.matches(regex);
        return result;
    }
}
