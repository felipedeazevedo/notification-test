CREATE TABLE app_user (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

CREATE TABLE user_subscribed_categories (
    user_id BIGINT NOT NULL,
    subscribed_categories VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE user_preferred_channels (
     user_id BIGINT NOT NULL,
     preferred_channels VARCHAR(50),
     FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE notification_log (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT NOT NULL,
      message TEXT NOT NULL,
      channel VARCHAR(30) NOT NULL,
      category VARCHAR(30) NOT NULL,
      status VARCHAR(20) NOT NULL,
      timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      FOREIGN KEY (user_id) REFERENCES app_user(id)
);