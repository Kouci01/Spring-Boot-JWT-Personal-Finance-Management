package com.finance.management.service.impl;

import com.finance.management.mapper.GoalMapper;
import com.finance.management.mapper.TransactionMapper;
import com.finance.management.model.Goal;
import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;
import com.finance.management.service.GoalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {
    private final GoalMapper goalMapper;
    private final TransactionMapper transactionMapper;

    public GoalServiceImpl(GoalMapper goalMapper, TransactionMapper transactionMapper){
        this.goalMapper = goalMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public void createGoal(Goal goals) {
        goalMapper.createGoal(goals);
    }

    @Override
    public List<Goal> findCurrentGoals(Goal goal) {
        return goalMapper.findCurrentGoals(goal);
    }

    @Override
    public void updateGoal(Goal goal) {
//        Get Income
        Transaction query = new Transaction();
        query.setStartDate(String.valueOf(goal.getStartDate()));
        query.setUserId(goal.getUserId());
        query.setEndDate(String.valueOf(goal.getEndDate()));
        query.setGoalId(goal.getGoalId());
        List<Summary> summaries =  transactionMapper.incomeExpenseSummary(query);
//        Separate Income and Expenses and Minus them
        Double income = summaries.stream().filter(item-> item.getCategoryName().equals("Income")).mapToDouble(Summary::getTotal).sum();
        Double expenses = summaries.stream().filter(item-> !item.getCategoryName().equals("Income")).mapToDouble(Summary::getTotal).sum();

        goal.setCurrentAmount(income - expenses);
        if(goal.getCurrentAmount() >= goal.getTargetAmount()){
            goal.setStatus("Completed");
        }
        goalMapper.updateGoal(goal);
    }

    @Override
    public Goal getGoalNearDeadline(Goal goal) {
        return goalMapper.getGoalNearDeadline(goal);
    }
}
