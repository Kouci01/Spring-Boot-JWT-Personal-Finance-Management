package com.finance.management.config;

public class NonZeroAndNonNullFilter {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return true; // Exclude null
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() == 0.0; // Exclude 0
        }
        return false; // Include non-null, non-zero values
    }
}
