package com.felipe.notificationtest.strategy;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.model.entity.User;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public DeliveryStatus send(User user, MessageRequest message) {
        System.out.printf("Sending SMS to %s: %s%n", user.getPhone(), message.getMessage());
        return DeliveryStatus.SUCCESS;
    }

    @Recover
    public DeliveryStatus recover(Exception ex, User user, MessageRequest request) {
        System.out.println("All retries failed for user " + user.getEmail());
        return DeliveryStatus.FAILURE;
    }
}
