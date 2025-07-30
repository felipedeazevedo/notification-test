package com.felipe.notificationtest.controller;

import com.felipe.notificationtest.model.entity.NotificationLog;
import com.felipe.notificationtest.model.enums.Category;
import com.felipe.notificationtest.model.enums.Channel;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import com.felipe.notificationtest.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class NotificationLogController {

    private final NotificationLogRepository logRepository;

    @GetMapping
    public List<NotificationLog> getLogs(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Channel channel,
            @RequestParam(required = false) DeliveryStatus status
    ) {
        if (category != null && channel != null && status != null) {
            return logRepository.findAllByCategoryAndChannelAndStatusOrderByTimestampDesc(category, channel, status);
        }
        return logRepository.findAllByOrderByTimestampDesc();
    }
}
