package com.felipe.notificationtest.strategy;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.model.entity.User;
import com.felipe.notificationtest.model.enums.DeliveryStatus;

public interface NotificationStrategy {
    DeliveryStatus send(User user, MessageRequest message);
}
