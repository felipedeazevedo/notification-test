package com.felipe.notificationtest.service;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.dto.NotificationSummaryResponse;
import com.felipe.notificationtest.model.entity.NotificationLog;
import com.felipe.notificationtest.model.entity.User;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import com.felipe.notificationtest.repository.NotificationLogRepository;
import com.felipe.notificationtest.repository.UserRepository;
import com.felipe.notificationtest.strategy.NotificationStrategy;
import com.felipe.notificationtest.strategy.NotificationStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationStrategyFactory strategyFactory;
    private final NotificationLogRepository logRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationSummaryResponse sendToSubscribers(MessageRequest request) {
        List<User> recipients = userRepository.findAll().stream()
                .filter(user -> user.getSubscribedCategories().contains(request.getCategory()))
                .toList();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        recipients.parallelStream().forEach(user -> user.getPreferredChannels().forEach(channel -> {
            NotificationStrategy strategy = strategyFactory.getStrategy(channel);
            DeliveryStatus status;

            try {
                status = strategy.send(user, request);
            } catch (Exception e) {
                status = DeliveryStatus.FAILURE;
            }

            if (status == DeliveryStatus.SUCCESS) {
                successCount.incrementAndGet();
            } else {
                failureCount.incrementAndGet();
            }

            NotificationLog log = NotificationLog.builder()
                    .user(user)
                    .message(request.getMessage())
                    .category(request.getCategory())
                    .channel(channel)
                    .status(status)
                    .timestamp(LocalDateTime.now())
                    .build();

            logRepository.save(log);
        }));

        return new NotificationSummaryResponse(
                recipients.size(),
                successCount.get() + failureCount.get(),
                successCount.get(),
                failureCount.get()
        );
    }
}
