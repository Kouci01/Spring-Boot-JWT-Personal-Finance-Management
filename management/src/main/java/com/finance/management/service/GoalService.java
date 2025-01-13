package com.finance.management.service;

import com.finance.management.model.Goal;

import java.util.List;

public interface GoalService {
    void createGoal(Goal goals);
    List<Goal> findCurrentGoals(Goal goal);
    void updateGoal(Goal goal);
    Goal getGoalNearDeadline(Goal goal);
}
