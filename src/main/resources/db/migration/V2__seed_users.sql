INSERT INTO app_user (id, name, email, phone) VALUES
    (1, 'Alice', 'alice@mail.com', '11111111'),
    (2, 'Bob', 'bob@mail.com', '22222222');

INSERT INTO user_subscribed_categories (user_id, subscribed_categories) VALUES
    (1, 'SPORTS'), (1, 'FINANCE'),
    (2, 'MOVIES');

INSERT INTO user_preferred_channels (user_id, preferred_channels) VALUES
    (1, 'EMAIL'), (1, 'SMS'),
    (2, 'PUSH_NOTIFICATION');