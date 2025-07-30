package com.felipe.notificationtest.repository;

import com.felipe.notificationtest.model.entity.NotificationLog;
import com.felipe.notificationtest.model.enums.Category;
import com.felipe.notificationtest.model.enums.Channel;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findAllByOrderByTimestampDesc();

    List<NotificationLog> findAllByCategoryAndChannelAndStatusOrderByTimestampDesc(
            Category category, Channel channel, DeliveryStatus status
    );
}