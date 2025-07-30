package com.felipe.notificationtest.dto;

public record NotificationSummaryResponse(
        int totalRecipients,
        int totalAttempts,
        int successCount,
        int failureCount
) {}