package com.finance.management.service;

import com.finance.management.model.Transaction;

import java.util.List;

public interface FinanceService {
    void addTransaction(Transaction transaction);
    List<Transaction> getTransactions(Transaction transaction);
}
