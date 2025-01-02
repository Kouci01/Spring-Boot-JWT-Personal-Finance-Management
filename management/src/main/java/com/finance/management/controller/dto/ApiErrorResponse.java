package com.finance.management.controller.dto;

public record ApiErrorResponse(
                               int errorCode,
                               String description) {
}
