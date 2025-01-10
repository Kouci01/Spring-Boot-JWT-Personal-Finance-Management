package com.finance.management.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SessionAttributes("jwt")
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        if(isValidEmail(email)){
//            Contain . before @
            email = replaceDotsBeforeAt(email, '$');
        }

        // Generate JWT token
        String jwtToken = JwtHelper.generateToken(email);

        // Add token to response
        response.addHeader("Authorization", "Bearer " + jwtToken);

        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7200);
        response.addCookie(jwtCookie);

        HttpSession session = request.getSession(true); // Create a session if it doesn't exist
        session.setAttribute("jwt", jwtToken);

        response.sendRedirect("/auth/login?error=false");

    }

    private boolean isValidEmail(String email) {
        String regex = "^[^@]*\\.[^@]*@.*$";
        return Pattern.matches(regex, email);
    }

    private String replaceDotsBeforeAt(String email, char replacement) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        String replacementStr = Matcher.quoteReplacement(String.valueOf(replacement));

        return email.replaceAll("\\.(?=[^@]+@)", replacementStr);
    }
}
