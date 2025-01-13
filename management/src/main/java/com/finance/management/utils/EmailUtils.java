package com.finance.management.utils;

import com.finance.management.config.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;

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

    public static String getEmail(HttpServletRequest request){
        String bearer =  request.getHeader("Authorization");
        String jwtToken = bearer.substring(7);
        String email = JwtHelper.extractUsername(jwtToken);

        if(EmailUtils.isEmailDots(email)){
            email = EmailUtils.revertDotsBeforeAt(email, '.');
        }

        return email;
    }
}
