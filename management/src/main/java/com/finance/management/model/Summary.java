package com.finance.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.finance.management.config.NonZeroAndNonNullFilter;

public class Summary {
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String categoryName;
    private double total;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NonZeroAndNonNullFilter.class)
    private int year;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NonZeroAndNonNullFilter.class)
    private int month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
