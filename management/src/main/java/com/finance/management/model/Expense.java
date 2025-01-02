package com.finance.management.model;

import java.util.Date;

import lombok.Data;

@Data
public class Expense {
	private Long id;
	private Long userId;
	private Long amount;
	private String description;
	private Date date;
}
