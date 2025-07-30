package com.felipe.notificationtest.controller;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.dto.NotificationSummaryResponse;
import com.felipe.notificationtest.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationSummaryResponse> sendNotification(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(notificationService.sendToSubscribers(request));
    }
}
