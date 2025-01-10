package com.finance.management.controller;

import com.finance.management.config.JwtHelper;
import com.finance.management.mapper.UserMapper;
import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;
import com.finance.management.model.User;
import com.finance.management.service.FinanceService;
import com.finance.management.service.UserService;
import com.finance.management.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private final FinanceService financeService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public FinanceController(FinanceService financeService, UserMapper userMapper, UserService userService){
        this.financeService = financeService;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransactions(@RequestBody List<Transaction> transactions,
                                             HttpServletRequest request) {
        String email = getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()){
            transactions.forEach(transaction -> transaction.setUserId(user.get().getId()));
            financeService.addTransaction(transactions);
            return ResponseEntity.ok("Transaction added successfully");
        }else{
            return ResponseEntity.badRequest().body("Internal Error");
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity<?> updateTransaction(Transaction transaction){
        financeService.updateTransaction(transaction);
        return ResponseEntity.ok("Transaction have been updated");
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(Transaction transaction,
                                             HttpServletRequest request) {
        String email = getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            transaction.setUserId(user.get().getId());
            return ResponseEntity.ok(financeService.getTransactions(transaction));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

    @GetMapping("/transactions/summary")
    public ResponseEntity<?> getSummary(Transaction transaction,
                                        HttpServletRequest request){
        String email = getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            transaction.setUserId(user.get().getId());
            return ResponseEntity.ok(financeService.incomeExpenseSummary(transaction));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

    @GetMapping("/transactions/yearly")
    public ResponseEntity<?> getYearly(Transaction transaction,
                                       HttpServletRequest request){
        String email = getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            transaction.setUserId(user.get().getId());
            return ResponseEntity.ok(financeService.yearlyTrends(transaction));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

    private String getEmail(HttpServletRequest request){
        String bearer =  request.getHeader("Authorization");
        String jwtToken = bearer.substring(7);
        String email = JwtHelper.extractUsername(jwtToken);

        if(EmailUtils.isEmailDots(email)){
            email = EmailUtils.revertDotsBeforeAt(email, '.');
        }

        return email;
    }
}
