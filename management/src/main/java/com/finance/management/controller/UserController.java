package com.finance.management.controller;

import com.finance.management.config.JwtHelper;
import com.finance.management.controller.dto.LoginRequestDTO;
import com.finance.management.controller.dto.LoginResponse;
import com.finance.management.controller.dto.SignUpRequestDTO;
import com.finance.management.model.User;
import com.finance.management.service.UserService;
import com.finance.management.utils.EmailUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequestDTO requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = JwtHelper.generateToken(request.email());
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7200);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(new LoginResponse(request.email(), token));
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginRes(String error, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(error.equals("true")){
            return ResponseEntity.badRequest().body("Login Error");
        }else{
            if (session != null) {
                String jwtToken = (String) session.getAttribute("jwt");
                String email = JwtHelper.extractUsername(jwtToken);
                if(EmailUtils.isEmailDots(email)){
                    email = EmailUtils.revertDotsBeforeAt(email, '.');
                }
                String name = email.split("@")[0];

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                userService.signUpGoogle(user);

                return ResponseEntity.ok(jwtToken);
            }
            return ResponseEntity.ok("Login Success But Token Error");
        }
    }


}
