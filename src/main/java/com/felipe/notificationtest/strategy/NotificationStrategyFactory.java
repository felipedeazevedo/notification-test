package com.felipe.notificationtest.strategy;

import com.felipe.notificationtest.model.enums.Channel;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class NotificationStrategyFactory {

    private final Map<Channel, NotificationStrategy> strategyMap = new EnumMap<>(Channel.class);

    public NotificationStrategyFactory(
            EmailNotificationStrategy email,
            SmsNotificationStrategy sms,
            PushNotificationStrategy push
    ) {
        strategyMap.put(Channel.EMAIL, email);
        strategyMap.put(Channel.SMS, sms);
        strategyMap.put(Channel.PUSH_NOTIFICATION, push);
    }

    public NotificationStrategy getStrategy(Channel channel) {
        NotificationStrategy strategy = strategyMap.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + channel);
        }
        return strategy;
    }
}
