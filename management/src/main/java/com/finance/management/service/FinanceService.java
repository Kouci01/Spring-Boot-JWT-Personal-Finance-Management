package com.finance.management.service;

import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;

import java.util.List;

public interface FinanceService {
    void addTransaction(List<Transaction> transaction);
    void updateTransaction(Transaction transaction);
    List<Transaction> getTransactions(Transaction transaction);
    List<Summary> incomeExpenseSummary(Transaction transaction);
    List<Summary> yearlyTrends(Transaction transaction);
}
