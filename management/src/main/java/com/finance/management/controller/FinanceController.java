package com.finance.management.controller;

import com.finance.management.model.Transaction;
import com.finance.management.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private final FinanceService financeService;

    @Autowired
    public FinanceController(FinanceService financeService){
        this.financeService = financeService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        financeService.addTransaction(transaction);
        return ResponseEntity.ok("Transaction added successfully");
    }

    @GetMapping("/transactions/{categoryId}")
    public List<Transaction> getTransactions(@PathVariable Long categoryId) {
        return financeService.getTransactionsByCategory(categoryId);
    }
}
