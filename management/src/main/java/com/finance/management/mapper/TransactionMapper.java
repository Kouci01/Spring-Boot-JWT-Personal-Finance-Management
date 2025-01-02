package com.finance.management.mapper;

import com.finance.management.model.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // Optional if you use @MapperScan in MyBatisConfig
public interface TransactionMapper {
	void insertTransaction(Transaction transaction);
    List<Transaction> getTransactionsByCategory(Long categoryId);
}
