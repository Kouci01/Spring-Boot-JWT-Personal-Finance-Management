package com.finance.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.finance.management.config.NonZeroAndNonNullFilter;

import java.util.Date;

public class Goal {
    private Long goalId;
    private Long userId;
    private String goalName;
    private Double targetAmount;
    private Double currentAmount;
    private String startDate;
    private String endDate;
    private String status;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NonZeroAndNonNullFilter.class)
    private double amountLeftToAchieve;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NonZeroAndNonNullFilter.class)
    private float progress_percentage;
    private Date createdAt;
    private Date updatedAt;

    private String dateRequest;

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmountLeftToAchieve() {
        return amountLeftToAchieve;
    }

    public void setAmountLeftToAchieve(double amountLeftToAchieve) {
        this.amountLeftToAchieve = amountLeftToAchieve;
    }

    public float getProgress_percentage() {
        return progress_percentage;
    }

    public void setProgress_percentage(float progress_percentage) {
        this.progress_percentage = progress_percentage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }
}
