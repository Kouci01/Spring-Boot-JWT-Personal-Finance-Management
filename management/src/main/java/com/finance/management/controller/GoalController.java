package com.finance.management.controller;

import com.finance.management.mapper.UserMapper;
import com.finance.management.model.Goal;
import com.finance.management.model.User;
import com.finance.management.service.GoalService;
import com.finance.management.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance")
public class GoalController {
    private final GoalService goalService;
    private final UserMapper userMapper;

    public GoalController(GoalService goalService, UserMapper userMapper){
        this.goalService = goalService;
        this.userMapper = userMapper;
    }

    @PostMapping("/goals")
    public ResponseEntity<?> createGoal(@RequestBody Goal goals,
                                             HttpServletRequest request) {
        String email = EmailUtils.getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()){
            goals.setUserId(user.get().getId());
            goals.setCurrentAmount((double) 0);
//            Find if there are active goals
            Goal query = new Goal();
            query.setUserId(goals.getUserId());
            query.setStatus("In Progress");
            List<Goal> findActiveGoals = goalService.findCurrentGoals(query);
            if(findActiveGoals.isEmpty()){
                goalService.createGoal(goals);
                return ResponseEntity.ok("Goals created successfully");
            }else{
                return ResponseEntity.badRequest().body("There are current goals that still active");
            }
        }else{
            return ResponseEntity.badRequest().body("Internal Error");
        }
    }

    @PutMapping("/goals")
    public ResponseEntity<?> updateGoal(Goal goal, HttpServletRequest request){
        String email = EmailUtils.getEmail(request);
        Optional<User> user = userMapper.findByEmail(email);
        if(user.isPresent()){
            goal.setUserId(user.get().getId());
            goalService.updateGoal(goal);
            return ResponseEntity.ok("Goals updated successfully");
        }else{
            return ResponseEntity.badRequest().body("Internal Error");
        }
    }

    @GetMapping("/goals")
    public ResponseEntity<?> findCurrentGoals(Goal goal,
                                             HttpServletRequest request) {
        String email = EmailUtils.getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()){
            goal.setUserId(user.get().getId());
            return ResponseEntity.ok(goalService.findCurrentGoals(goal));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

    @GetMapping("/goals/deadline")
    public ResponseEntity<?> getDeadline(HttpServletRequest request){
        String email = EmailUtils.getEmail(request);
        Optional<User> user= userMapper.findByEmail(email);
        if(user.isPresent()){
            Goal query = new Goal();
            query.setUserId(user.get().getId());
            return ResponseEntity.ok(goalService.getGoalNearDeadline(query));
        }
        return ResponseEntity.badRequest().body("Token authorization is expired");
    }

}
