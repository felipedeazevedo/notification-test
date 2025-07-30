package com.felipe.notificationtest.service;

import com.felipe.notificationtest.dto.MessageRequest;
import com.felipe.notificationtest.dto.NotificationSummaryResponse;
import com.felipe.notificationtest.model.entity.NotificationLog;
import com.felipe.notificationtest.model.entity.User;
import com.felipe.notificationtest.model.enums.Category;
import com.felipe.notificationtest.model.enums.Channel;
import com.felipe.notificationtest.model.enums.DeliveryStatus;
import com.felipe.notificationtest.repository.NotificationLogRepository;
import com.felipe.notificationtest.repository.UserRepository;
import com.felipe.notificationtest.strategy.NotificationStrategy;
import com.felipe.notificationtest.strategy.NotificationStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @Mock
    private NotificationStrategyFactory strategyFactory;

    @Mock
    private NotificationLogRepository logRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationStrategy strategy;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private User buildUser(Long id, List<Category> categories, List<Channel> channels) {
        User user = new User();
        user.setId(id);
        user.setName("User " + id);
        user.setEmail("user" + id + "@mail.com");
        user.setSubscribedCategories(categories);
        user.setPreferredChannels(channels);
        return user;
    }

    private MessageRequest buildMessageRequest(Category category) {
        MessageRequest request = new MessageRequest();
        request.setCategory(category);
        request.setMessage("Test message");
        return request;
    }

    @Test
    void shouldSendToMatchingUsersAndCountSuccessAndFailures() {
        User user1 = buildUser(1L, List.of(Category.SPORTS), List.of(Channel.EMAIL, Channel.SMS));
        User user2 = buildUser(2L, List.of(Category.SPORTS), List.of(Channel.SMS));

        MessageRequest request = buildMessageRequest(Category.SPORTS);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(strategy);
        when(strategyFactory.getStrategy(Channel.SMS)).thenReturn(strategy);
        when(strategy.send(any(), eq(request)))
                .thenReturn(DeliveryStatus.SUCCESS)
                .thenThrow(new RuntimeException());

        NotificationSummaryResponse response = notificationService.sendToSubscribers(request);

        assertThat(response.totalRecipients()).isEqualTo(2);
        assertThat(response.totalAttempts()).isEqualTo(3);
        assertThat(response.successCount()).isEqualTo(1);
        assertThat(response.failureCount()).isEqualTo(2);

        verify(logRepository, times(3)).save(any(NotificationLog.class));
    }

    @Test
    void shouldReturnZeroCountsWhenNoUsersMatchCategory() {
        User user = buildUser(1L, List.of(Category.FINANCE), List.of(Channel.SMS));
        when(userRepository.findAll()).thenReturn(List.of(user));

        MessageRequest request = buildMessageRequest(Category.SPORTS);

        NotificationSummaryResponse response = notificationService.sendToSubscribers(request);

        assertThat(response.totalRecipients()).isZero();
        assertThat(response.totalAttempts()).isZero();
        assertThat(response.successCount()).isZero();
        assertThat(response.failureCount()).isZero();

        verifyNoInteractions(strategyFactory);
        verifyNoInteractions(logRepository);
    }

    @Test
    void shouldHandleStrategyReturningFailureStatus() {
        User user = buildUser(1L, List.of(Category.SPORTS), List.of(Channel.EMAIL));
        MessageRequest request = buildMessageRequest(Category.SPORTS);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(strategy);
        when(strategy.send(user, request)).thenReturn(DeliveryStatus.FAILURE);

        NotificationSummaryResponse response = notificationService.sendToSubscribers(request);

        assertThat(response.totalRecipients()).isEqualTo(1);
        assertThat(response.totalAttempts()).isEqualTo(1);
        assertThat(response.successCount()).isZero();
        assertThat(response.failureCount()).isEqualTo(1);

        verify(logRepository).save(any(NotificationLog.class));
    }

    @Test
    void shouldSaveNotificationLogWithCorrectFields() {
        User user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@mail.com")
                .phone("123456789")
                .subscribedCategories(List.of(Category.SPORTS))
                .preferredChannels(List.of(Channel.EMAIL))
                .build();

        MessageRequest request = new MessageRequest(Category.SPORTS, "message");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(strategy);
        when(strategy.send(user, request)).thenReturn(DeliveryStatus.SUCCESS);

        notificationService.sendToSubscribers(request);

        ArgumentCaptor<NotificationLog> logCaptor = ArgumentCaptor.forClass(NotificationLog.class);
        verify(logRepository).save(logCaptor.capture());

        NotificationLog log = logCaptor.getValue();
        assertThat(log.getUser()).isEqualTo(user);
        assertThat(log.getMessage()).isEqualTo("message");
        assertThat(log.getCategory()).isEqualTo(Category.SPORTS);
        assertThat(log.getChannel()).isEqualTo(Channel.EMAIL);
        assertThat(log.getStatus()).isEqualTo(DeliveryStatus.SUCCESS);
        assertThat(log.getTimestamp()).isNotNull();
    }
}
