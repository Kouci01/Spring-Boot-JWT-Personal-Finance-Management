package com.finance.management.service.impl;

import com.finance.management.mapper.TransactionMapper;
import com.finance.management.model.Goal;
import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;
import com.finance.management.service.FinanceService;
import com.finance.management.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final TransactionMapper transactionMapper;
    private final GoalService goalService;

    @Autowired
    public FinanceServiceImpl(TransactionMapper transactionMapper, GoalService goalService) {
        this.transactionMapper = transactionMapper;
        this.goalService = goalService;
    }

    @Transactional
    @Override
    public void addTransaction(List<Transaction> transaction) {
        try {
            transactionMapper.insertTransaction(transaction);
            Goal query = new Goal();
            query.setUserId(transaction.get(0).getUserId());
            query.setStatus("In Progress");
            query = goalService.findCurrentGoals(query).get(0);
            goalService.updateGoal(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public void updateTransaction(Transaction transaction) {

        try {
            Goal query = new Goal();
            query.setUserId(transaction.getUserId());
            query.setStatus("In Progress");
            query = goalService.findCurrentGoals(query).get(0);

            transaction.setGoalId(query.getGoalId());
            transactionMapper.updateTransaction(transaction);
            Transaction getTransaction = transactionMapper.getTransactions(transaction).get(0);
            query.setDateRequest(getTransaction.getDate().toString());
            goalService.updateGoal(query);
        }catch (Exception e){
            e.printStackTrace();
        }
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
