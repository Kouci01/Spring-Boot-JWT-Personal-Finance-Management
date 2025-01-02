package com.finance.management.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.finance.management.model.Expense;

@Mapper
public interface ExpenseMapper {
	void insertExpense(Expense expense);
    List<Expense> findExpensesByUserId(Long userId);
}
