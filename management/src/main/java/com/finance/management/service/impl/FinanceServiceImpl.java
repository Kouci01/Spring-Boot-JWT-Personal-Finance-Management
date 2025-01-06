package com.finance.management.service.impl;

import com.finance.management.mapper.TransactionMapper;
import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;
import com.finance.management.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final TransactionMapper transactionMapper;

    @Autowired
    public FinanceServiceImpl(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Override
    public void addTransaction(List<Transaction> transaction) {
        transactionMapper.insertTransaction(transaction);
    }

    @Override
    public List<Transaction> getTransactions(Transaction transaction) {
        return transactionMapper.getTransactions(transaction);
    }

    @Override
    public List<Summary> incomeExpenseSummary(Transaction transaction) {
        return transactionMapper.incomeExpenseSummary(transaction);
    }

    @Override
    public List<Summary> yearlyTrends(Transaction transaction) {
        return transactionMapper.yearlyTrends(transaction);
    }
}
