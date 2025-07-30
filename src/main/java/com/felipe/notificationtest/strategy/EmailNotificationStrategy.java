package com.felipe.notificationtest.strategy;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.model.entity.User;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public DeliveryStatus send(User user, MessageRequest request) {
        System.out.println("Trying to send email to " + user.getEmail());
        // Simulate failure
        if (true) {
            throw new RuntimeException("Simulated email send failure");
        }
        return DeliveryStatus.SUCCESS;
    }

    @Recover
    public DeliveryStatus recover(Exception ex, User user, MessageRequest request) {
        System.out.println("All retries failed for user " + user.getEmail());
        return DeliveryStatus.FAILURE;
    }
}
