package com.finance.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.management.model.User;
import com.finance.management.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SessionAttributes("jwt")
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = email.split("@")[0];

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userService.signUpGoogle(user);

        if(isValidEmail(email)){
//            Contain . before @
            email = replaceDotsBeforeAt(email, '$');
        }

        // Generate JWT token
        String jwtToken = JwtHelper.generateToken(email);


        // Add token to response
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("token", jwtToken);
//
//        // Set response headers and content type
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_OK);

        response.sendRedirect("http://localhost:63342/management/static/index.html?token=" + jwtToken);

//        // Write JSON to response
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(response.getWriter(), responseBody);


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
