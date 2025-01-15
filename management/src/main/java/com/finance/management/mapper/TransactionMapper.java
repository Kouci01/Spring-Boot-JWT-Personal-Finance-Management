package com.finance.management.mapper;

import com.finance.management.model.Summary;
import com.finance.management.model.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // Optional if you use @MapperScan in MyBatisConfig
public interface TransactionMapper {
	void insertTransaction(List<Transaction> transaction);
    void updateTransaction(Transaction transaction);
    List<Transaction> getTransactions(Transaction transaction);
    List<Summary> incomeExpenseSummary(Transaction transaction);
    List<Summary> yearlyTrends(Transaction transaction);
    List<Transaction> getTransactionGoalsByMonth(Transaction transaction);
}
