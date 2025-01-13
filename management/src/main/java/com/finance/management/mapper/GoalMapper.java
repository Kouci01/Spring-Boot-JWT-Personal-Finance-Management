package com.finance.management.mapper;

import com.finance.management.model.Goal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalMapper {
    void createGoal(Goal goals);
    List<Goal> findCurrentGoals(Goal goal);
    void updateGoal(Goal goal);
    Goal getGoalNearDeadline(Goal goal);
}
