package com.finance.management.controller;

import com.finance.management.mapper.UserMapper;
import com.finance.management.model.Goal;
import com.finance.management.model.Transaction;
import com.finance.management.model.User;
import com.finance.management.service.FinanceService;
import com.finance.management.service.GoalService;
import com.finance.management.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private final FinanceService financeService;
    private final UserMapper userMapper;
    private final GoalService goalService;

    @Autowired
    public FinanceController(FinanceService financeService, UserMapper userMapper, GoalService goalService){
        this.financeService = financeService;
        this.userMapper = userMapper;
        this.goalService = goalService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransactions(@RequestBody List<Transaction> transactions,
                                             HttpServletRequest request) {
        String email = EmailUtils.getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()){
            Goal query = new Goal();
            query.setUserId(user.get().getId());
            query.setStatus("In Progress");
            List<Goal> findActiveGoals = goalService.findCurrentGoals(query);
            if(findActiveGoals.isEmpty()){
                return ResponseEntity.ok("There are currently no active goals set, please create it first");
            }else{
                transactions.forEach(transaction -> transaction.setUserId(user.get().getId()));
                financeService.addTransaction(transactions);
                return ResponseEntity.ok("Transaction have been added");
            }
        }else{
            return ResponseEntity.badRequest().body("Internal Error");
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity<?> updateTransaction(@RequestBody Transaction transaction, HttpServletRequest request){
        String email = EmailUtils.getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()) {
            Goal query = new Goal();
            query.setUserId(user.get().getId());
            query.setStatus("In Progress");
            List<Goal> findActiveGoals = goalService.findCurrentGoals(query);
            transaction.setUserId(user.get().getId());
            if(findActiveGoals.isEmpty()){
                return ResponseEntity.ok("There are currently no active goals set, please create it first");
            }

            Transaction searchQuery = new Transaction();
            searchQuery.setId(transaction.getId());
            searchQuery.setUserId(user.get().getId());
            searchQuery.setGoalId(findActiveGoals.get(0).getGoalId());
            List<Transaction> checkTransactions = financeService.getTransactions(searchQuery);
            if(checkTransactions.isEmpty()){
                return ResponseEntity.ok("This goals transaction already completed, can't be updated");
            }
            else{
                financeService.updateTransaction(transaction);
                return ResponseEntity.ok("Transaction have been updated");
            }
        }
        return ResponseEntity.badRequest().body("Internal Error");
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(Transaction transaction,
                                             HttpServletRequest request) {
        String email = EmailUtils.getEmail(request);
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
        String email = EmailUtils.getEmail(request);
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
        String email = EmailUtils.getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            transaction.setUserId(user.get().getId());
            return ResponseEntity.ok(financeService.yearlyTrends(transaction));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

    @GetMapping("/transactions/goals")
    public ResponseEntity<?> transactionGoals(Transaction transaction, HttpServletRequest request){
        String email = EmailUtils.getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            transaction.setUserId(user.get().getId());
            return ResponseEntity.ok(financeService.getTransactionGoalsByMonth(transaction));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }
}
